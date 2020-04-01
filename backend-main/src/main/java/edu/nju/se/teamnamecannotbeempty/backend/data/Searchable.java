package edu.nju.se.teamnamecannotbeempty.backend.data;

import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Searchable {
    private final PaperDao paperDao;
    private static boolean ok = false;
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
            ok = (num != null) && (num == paperDao.count());
        return ok;
    }

    /**
     * 设置导入条目总数
     *
     * @param l 导入条目总数
     */
    public void setNum(long l) {
        if (num == null) num = l;
    }
}
