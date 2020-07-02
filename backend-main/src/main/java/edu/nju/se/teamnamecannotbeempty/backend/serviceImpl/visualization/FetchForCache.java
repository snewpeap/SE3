package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization;

import edu.nju.se.teamnamecannotbeempty.data.domain.Term;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.TermPopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FetchForCache {

    private final TermPopDao termPopDao;

    @Autowired
    public FetchForCache(TermPopDao termPopDao) {
        this.termPopDao = termPopDao;
    }

    /**
     * 代理dao层通过论文id获取TermPop的方法，加上缓存
     * @param id
     * @return
     */
    @Cacheable(value = "getTermPopByPaperID", key = "#p0")
    public List<Term.Popularity> getTermPopByPaperID(Long id){
        return termPopDao.getTermPopByPaperID(id);
    }
}
