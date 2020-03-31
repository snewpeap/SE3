package edu.nju.se.teamnamecannotbeempty.batch.job;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import edu.nju.se.teamnamecannotbeempty.api.IDataImportJob;
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

@Service
public class DataImportJob implements IDataImportJob {
    private final FromCSV fromCSV;
    private final Attacher attacher;

    private static Logger logger = LoggerFactory.getLogger(DataImportJob.class);

    @Autowired
    public DataImportJob(FromCSV fromCSV, Attacher attacher) {
        this.fromCSV = fromCSV;
        this.attacher = attacher;
    }

    @Override
    public long trigger() {
        long total = 0;

        String name = "/datasource/ase13_15_16_17_19.csv";
        InputStream ase_csv = getClass().getResourceAsStream(name);
        InputStream ase_json = getClass().getResourceAsStream("/datasource/ase_res.json");
        total += readFile(name, ase_csv, ase_json);

        name = "/datasource/icse15_16_17_18_19.csv";
        InputStream icse_csv = getClass().getResourceAsStream(name);
        InputStream icse_json = getClass().getResourceAsStream("/datasource/icse_res.json");
        total += readFile(name, icse_csv, icse_json);
        return total;
    }

    private long readFile(String name, InputStream csv, InputStream json) {
        logger.info("Start parsing " + name );
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
        private HashMap<Long, Paper> paperHashMap;

        @Autowired
        public Attacher(PaperDao paperDao) {
            this.paperDao = paperDao;
        }

        /*
        小知识：标记了@Async的方法需要在别的类内调用才能生效
         */
        @Async
        void attachAndSave(List<Paper> papers, InputStream jsonFile, String name) {
            paperDao.saveAll(attachJsonInfo(papers, jsonFile));
            logger.info("Done Saving data from " + name);
        }

        private List<Paper> attachJsonInfo(List<Paper> papers, InputStream jsonFile) {
            logger.info("Start attach json");
            paperHashMap = new HashMap<>(papers.size());
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
                                                Ref ref = new Ref();
                                                ref.setRefTitle(title);
                                                if (!StringUtils.isEmpty(link)) {
                                                    Long refereeId = Long.parseLong(
                                                            link.substring(link.lastIndexOf('/') + 1));
                                                    Paper referee = paperHashMap.get(refereeId);
                                                    ref.setReferee(referee);
                                                    if (referee != null)
                                                        paperDao.save(referee);
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
}
