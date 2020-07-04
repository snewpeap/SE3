package edu.nju.se.teamnamecannotbeempty.batch.job;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import edu.nju.se.teamnamecannotbeempty.api.IDataImportJob;
import edu.nju.se.teamnamecannotbeempty.batch.job.worker.*;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.FromCSV;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.domain.Ref;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.JDBCType;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Future;

@Service
public class DataImportJob implements IDataImportJob {
    private final FromCSV fromCSV;
    private final Attacher attacher;
    private final BatchGenerator batchGenerator;

    private static final Logger logger = LoggerFactory.getLogger(DataImportJob.class);

    @Autowired
    public DataImportJob(FromCSV fromCSV, Attacher attacher, BatchGenerator batchGenerator) {
        this.fromCSV = fromCSV;
        this.attacher = attacher;
        this.batchGenerator = batchGenerator;
    }

    @Override
    public long trigger() {
        logger.info("Triggered import job");
        long total = 0;

        try {
            InputStream ase_csv = getClass().getResourceAsStream("/datasource/ase13_15_16_17_19.csv");
            InputStream ase_complete = getClass().getResourceAsStream("/datasource/ase_complete_ref.csv");
            InputStream ase_json = getClass().getResourceAsStream("/datasource/ase_ref.json");

            InputStream icse_csv = getClass().getResourceAsStream("/datasource/icse15_16_17_18_19.csv");
            InputStream icse_complete = getClass().getResourceAsStream("/datasource/icse_complete_ref.csv");
            InputStream icse_json = getClass().getResourceAsStream("/datasource/icse_ref.json");

            String name = "all together";
            total += readFile(
                    name,
                    mergeCSV(
                            mergeCSV(ase_csv, ase_complete),
                            mergeCSV(icse_csv, icse_complete)
                    ),
                    new SequenceInputStream(ase_json, icse_json)
            );

            batchGenerator.trigger_init(total);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    private InputStream mergeCSV(InputStream first, InputStream later) throws IOException {
        //需要把later的表头先读掉
        int c;
        do {
            c = later.read();
        } while (c != '\n');

        return new SequenceInputStream(first, later);
    }

    private long readFile(String name, InputStream csv, InputStream json) {
        logger.info("Start import papers from " + name);
        Collection<Paper> papers = fromCSV.convert(csv);
        long size = papers.size();
        try {
            csv.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        attacher.attachAndSave(papers, json, name);
        return size;
    }

    @Component
    static class Attacher {
        private final JdbcTemplate jdbcTemplate;

        @Autowired
        public Attacher(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        /*
        小知识：标记了@Async的方法需要在别的类内调用才能生效，猜想是给类做了个代理
         */
        @Async
        void attachAndSave(Collection<Paper> papers, InputStream jsonFile, String name) {
            try {
                jdbcTemplate.batchUpdate(
                        "insert papers(" +
                                "id, citation, document_identifier, " +
                                "doi, end_page, funding_info, " +
                                "ieee_id, isbn, issn, " +
                                "license, pdf_link, publisher, " +
                                "reference, start_page, abstract, " +
                                "title, volume, year, conference_id) " +
                                "VALUES (0,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        attachRefs(papers, jsonFile),
                        500,
                        (ps, paper) -> {
                            ps.setObject(1, paper.getCitation(), JDBCType.INTEGER);
                            ps.setString(2, paper.getDocument_identifier());
                            ps.setString(3, paper.getDoi());
                            ps.setObject(4, paper.getEnd_page(), JDBCType.INTEGER);
                            ps.setString(5, paper.getFunding_info());
                            ps.setObject(6, paper.getIeeeId(), JDBCType.BIGINT);
                            ps.setString(7, paper.getIsbn());
                            ps.setString(8, paper.getIssn());
                            ps.setString(9, paper.getLicense());
                            ps.setString(10, paper.getPdf_link());
                            ps.setString(11, paper.getPublisher());
                            ps.setObject(12, paper.getReference(), JDBCType.INTEGER);
                            ps.setObject(13, paper.getStart_page(), JDBCType.INTEGER);
                            ps.setString(14, paper.getSummary());
                            ps.setString(15, paper.getTitle());
                            ps.setObject(16, paper.getVolume(), JDBCType.INTEGER);
                            ps.setObject(17, paper.getYear(), JDBCType.INTEGER);
                            Long conferenceId = paper.getConference() == null ? null : paper.getConference().getId();
                            ps.setObject(18, conferenceId, JDBCType.BIGINT);
                        }
                );
                logger.info("Done saving data from " + name);
            } catch (Exception e) {
                logger.error("Fatal saving papers.");
                e.printStackTrace();
            } finally {
                try {
                    jsonFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private Collection<Paper> attachRefs(Collection<Paper> papers, InputStream jsonFile) {
            HashMap<Long, Paper> paperHashMap = new HashMap<>(papers.size());
            HashMap<String, Paper> doiMap = new HashMap<>(papers.size());
            papers.forEach(paper -> {
                if (paper.getIeeeId() != null) {
                    paperHashMap.put(paper.getIeeeId(), paper);
                } else if (!StringUtils.isBlank(paper.getDoi())) {
                    doiMap.put(paper.getDoi(), paper);
                }
            });

            BufferedReader reader = new BufferedReader(new InputStreamReader(jsonFile, StandardCharsets.UTF_8));
            String line;
            try {
                while (!StringUtils.isBlank(line = reader.readLine())) {
                    JSONObject object = JSONUtil.parseObj(line);
                    Long id = Long.parseLong(object.getStr("pdf_link").split("=")[1]);
                    Paper paper = paperHashMap.get(id);
                    if (paper != null) {
                        for (JSONObject jsonObject : object.getJSONArray("ref").jsonIter()) {
                            String title = jsonObject.getStr("title");
                            if (!StringUtils.isBlank(title)) {
                                String link = jsonObject.getStr("link");
                                String doi = jsonObject.getStr("doi");
                                Ref ref = new Ref(title);
                                if (!StringUtils.isBlank(link)) {
                                    Long refereeIeeeId = Long.parseLong(
                                            link.substring(link.lastIndexOf('/') + 1));
                                    ref.setReferee(paperHashMap.get(refereeIeeeId));
                                } else if (!StringUtils.isBlank(doi)) {
                                    ref.setReferee(doiMap.get(doi));
                                }
                                paper.addRef(ref);
                            }
                        }
                    }
                }
                jsonFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            logger.info("Done attach json");
            return papers;
        }
    }

    @Component
    static class BatchGenerator {
        private final AuthorPopWorker authorPopWorker;
        private final AffiPopWorker affiPopWorker;
        private final TermPopWorker termPopWorker;
        private final PaperPopWorker paperPopWorker;
        private final AuthorDupWorker authorDupWorker;
        private final AffiDupWorker affiDupWorker;

        @Autowired
        public BatchGenerator(AuthorPopWorker authorPopWorker, AffiPopWorker affiPopWorker, TermPopWorker termPopWorker,
                              AuthorDupWorker authorDupWorker, AffiDupWorker affiDupWorker, PaperPopWorker paperPopWorker) {
            this.authorPopWorker = authorPopWorker;
            this.affiPopWorker = affiPopWorker;
            this.termPopWorker = termPopWorker;
            this.authorDupWorker = authorDupWorker;
            this.affiDupWorker = affiDupWorker;
            this.paperPopWorker = paperPopWorker;
        }

        @Async
        public void trigger_init(long total) {
            long startTime = System.currentTimeMillis();
            final long DEADLINE = 1000 * 60 * 10;
            logger.info("Triggered, expect deadline is " + new Date(startTime + DEADLINE).toString());
            long count;
            while ((count = paperPopWorker.count()) != total) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    logger.warn(e.getMessage());
                }
                if (System.currentTimeMillis() - startTime > DEADLINE) {
                    logger.error("Data import time exceed " + DEADLINE / 1000 + " second, and current count is " + count + ". Abort it");
                    return;
                }
            }
            logger.info("Done import papers. Start generating paper popularity...");
            System.gc();
            paperPopWorker.generatePaperPop();
            logger.info("Done generate paper popularity");
            Future<?> authorFuture = authorPopWorker.generateAuthorPop();
            Future<?> affiFuture = affiPopWorker.generateAffiPop();
            termPopWorker.generateTermPop();
            affiDupWorker.generateAffiDup(affiFuture);
            authorDupWorker.generateAuthorDup(authorFuture);
        }
    }
}
