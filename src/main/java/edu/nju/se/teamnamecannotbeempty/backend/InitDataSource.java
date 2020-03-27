package edu.nju.se.teamnamecannotbeempty.backend;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.NeedParseCSV;
import edu.nju.se.teamnamecannotbeempty.backend.dao.PaperDao;
import edu.nju.se.teamnamecannotbeempty.backend.data.FromCSVOpenCSVImpl;
import edu.nju.se.teamnamecannotbeempty.backend.po.Paper;
import edu.nju.se.teamnamecannotbeempty.backend.po.Ref;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//TODO 转用Batch方式
//TODO 定时任务
@Component
public class InitDataSource implements ApplicationListener<ContextRefreshedEvent> {
    private final FromCSVOpenCSVImpl fromCSV;
    private final PaperDao paperDao;

    private static Logger logger = LoggerFactory.getLogger(InitDataSource.class);

    @Autowired
    public InitDataSource(FromCSVOpenCSVImpl fromCSV, PaperDao paperDao) {
        this.fromCSV = fromCSV;
        this.paperDao = paperDao;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null && AppContextProvider.getBean(NeedParseCSV.class).isNeed()) {
            String name = "/datasource/ase13_15_16_17_19.csv";
            InputStream ase_csv = getClass().getResourceAsStream(name);
            InputStream ase_json = getClass().getResourceAsStream("/datasource/ase_res.json");
            readFile(name, ase_csv, ase_json);

            name = "/datasource/icse15_16_17_18_19.csv";
            InputStream icse_csv = getClass().getResourceAsStream(name);
            InputStream icse_json = getClass().getResourceAsStream("/datasource/icse_res.json");
            readFile(name, icse_csv, icse_json);
        }
    }

    private void readFile(String name, InputStream csv, InputStream json) {
        paperDao.saveAll(attachJsonInfo(fromCSV.convert(csv), json));
        try {
            csv.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            json.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Done Saving data from " + name);
    }

    private List<Paper> attachJsonInfo(List<Paper> papers, InputStream jsonFile) {
        logger.info("Start attach json");
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
        logger.info("Done attach json");
        return new ArrayList<>(paperHashMap.values());
    }
}
