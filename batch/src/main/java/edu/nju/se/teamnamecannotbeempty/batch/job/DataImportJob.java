package edu.nju.se.teamnamecannotbeempty.batch.job;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import edu.nju.se.teamnamecannotbeempty.api.IDataImportJob;
import edu.nju.se.teamnamecannotbeempty.batch.job.worker.*;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.FromCSV;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.domain.Ref;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class DataImportJob implements IDataImportJob {
    private final FromCSV fromCSV;
    private final Attacher attacher;
    private final BatchGenerator batchGenerator;

    private static Logger logger = LoggerFactory.getLogger(DataImportJob.class);

    @Autowired
    public DataImportJob(FromCSV fromCSV, Attacher attacher, BatchGenerator batchGenerator) {
        this.fromCSV = fromCSV;
        this.attacher = attacher;
        this.batchGenerator = batchGenerator;
    }

    @Override
    public long trigger() {
        logger.info("Triggered");
        long total = 0;

        String name = "/datasource/ase13_15_16_17_19.csv";
        InputStream ase_csv = getClass().getResourceAsStream(name);
        InputStream ase_json = getClass().getResourceAsStream("/datasource/ase_res.json");
        total += readFile(name, ase_csv, ase_json);

        name = "/datasource/icse15_16_17_18_19.csv";
        InputStream icse_csv = getClass().getResourceAsStream(name);
        InputStream icse_json = getClass().getResourceAsStream("/datasource/icse_res.json");
        total += readFile(name, icse_csv, icse_json);

        batchGenerator.trigger_init(total);
        return total;
    }

    private long readFile(String name, InputStream csv, InputStream json) {
        logger.info("Start import papers from " + name);
        List<Paper> papers = fromCSV.convert(csv);
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
        private final PaperDao paperDao;

        @Autowired
        public Attacher(PaperDao paperDao) {
            this.paperDao = paperDao;
        }

        /*
        小知识：标记了@Async的方法需要在别的类内调用才能生效，猜想是给类做了个代理
         */
        @Async
        void attachAndSave(List<Paper> papers, InputStream jsonFile, String name) {
            try {
                paperDao.saveAll(attachJsonInfo(papers, jsonFile));
                logger.info("Done Saving data from " + name);
            } catch (Exception e) {
                logger.error("Error Saving papers.");
            } finally {
                try {
                    jsonFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private List<Paper> attachJsonInfo(List<Paper> papers, InputStream jsonFile) {
            HashMap<Long, Paper> paperHashMap = new HashMap<>(papers.size());
            papers.forEach(paper -> paperHashMap.put(paper.getId(), paper));

            BufferedReader reader = new BufferedReader(new InputStreamReader(jsonFile, StandardCharsets.UTF_8));
            String line;
            while (true) {
                try {
                    if (!StringUtils.isBlank(line = reader.readLine())) {
                        JSONObject object = JSONUtil.parseObj(line);
                        Long id = Long.parseLong(object.getStr("pdf_link").split("=")[1]);
                        Paper paper = paperHashMap.get(id);
                        if (paper != null) {
                            JSONArray refs = object.getJSONArray("ref");
                            if (!refs.isEmpty()) {
                                refs.jsonIter().forEach(
                                        jsonObject -> {
                                            String title = jsonObject.getStr("title");
                                            if (!StringUtils.isBlank(title)) {
                                                String link = jsonObject.getStr("link");
                                                Ref ref = new Ref(title);
                                                if (!StringUtils.isEmpty(link)) {
                                                    Long refereeId = Long.parseLong(
                                                            link.substring(link.lastIndexOf('/') + 1));
                                                    ref.setReferee(paperHashMap.get(refereeId));
                                                }
                                                paper.addRef(ref);
                                            }
                                        }
                                );

                            }
                        }
                        continue;
                    }
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                jsonFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info("Done attach json");
            return new ArrayList<>(paperHashMap.values());
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
        private final RefWorker refWorker;

        @Autowired
        public BatchGenerator(AuthorPopWorker authorPopWorker, AffiPopWorker affiPopWorker, TermPopWorker termPopWorker,
                              AuthorDupWorker authorDupWorker, AffiDupWorker affiDupWorker, PaperPopWorker paperPopWorker,
                              RefWorker refWorker) {
            this.authorPopWorker = authorPopWorker;
            this.affiPopWorker = affiPopWorker;
            this.termPopWorker = termPopWorker;
            this.authorDupWorker = authorDupWorker;
            this.affiDupWorker = affiDupWorker;
            this.paperPopWorker = paperPopWorker;
            this.refWorker = refWorker;
        }

        @Async
        void trigger_init(long total) {
            long startTime = System.currentTimeMillis();
            final long DEADLINE = 1000 * 60 * 5;
            while (paperPopWorker.count() != total) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    logger.warn(e.getMessage());
                }
                if (System.currentTimeMillis() - startTime > DEADLINE) {
                    logger.error("Data Import time exceed " + DEADLINE / 1000 + " second, and current count is " + paperPopWorker.count() + ". Abort it");
                    return;
                }
            }
            logger.info("Done import papers. Start generating paper popularity...");
            paperPopWorker.generatePaperPop();
            logger.info("Done generate paper popularity");
//            refWorker.generate();
            Future<?> authorFuture = authorPopWorker.generateAuthorPop();
            Future<?> affiFuture = affiPopWorker.generateAffiPop();
            termPopWorker.generateTermPop();
            affiDupWorker.generateAffiDup(affiFuture);
            authorDupWorker.generateAuthorDup(authorFuture);
        }
    }
}
