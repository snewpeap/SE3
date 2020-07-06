package edu.nju.se.teamnamecannotbeempty.batch.job.worker;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.domain.Ref;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Component
public class Attacher {
    private final PaperDao paperDao;
    private static final List<Ref> refs = new LinkedList<>();
    public static final Logger logger = LoggerFactory.getLogger(Attacher.class);

    @Autowired
    public Attacher(PaperDao paperDao) {
        this.paperDao = paperDao;
    }

    static List<Ref> getRefs() {
        return refs;
    }

    /*
    小知识：标记了@Async的方法需要在别的类内调用才能生效，猜想是给类做了个代理
     */
    @Async
    public void attachAndSave(Collection<Paper> papers, InputStream jsonFile, String name) {
        try {
            paperDao.saveAll(attachRefs(papers, jsonFile));
            paperDao.flush();
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
                            refs.add(ref);
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
