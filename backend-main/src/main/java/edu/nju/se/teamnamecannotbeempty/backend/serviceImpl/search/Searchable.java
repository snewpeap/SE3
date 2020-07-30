package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search;

import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Searchable {
    private final PaperDao paperDao;
    private static boolean ok = false;
    private static boolean indexing = false;
    private static boolean pass = false;
    private static Long num = null;

    @Autowired
    public Searchable(PaperDao paperDao) {
        this.paperDao = paperDao;
    }

    /**
     * 获得能否开始搜索标志
     *
     * @return 能否开始搜索
     */
    public boolean isOk() {
        if (!ok)
            ok = importOK();
        return (ok && !indexing) || pass;
    }

    /**
     * 跳过导入阶段，可以直接搜索
     */
    public void pass() {
        pass = true;
    }

    /**
     * 设置导入条目总数
     *
     * @param l 导入条目总数
     */
    public void setNum(long l) {
        if (num == null) num = l;
    }

    boolean importOK() {
        return (num != null) && (num == paperDao.count());
    }

    void startIndexing() {
        indexing = true;
        ok = false;
    }

    void endIndexing() {
        indexing = false;
        ok = true;
    }
}
