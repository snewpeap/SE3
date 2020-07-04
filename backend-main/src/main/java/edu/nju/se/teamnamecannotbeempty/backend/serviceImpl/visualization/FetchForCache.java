package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization;

import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.TermPopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FetchForCache {

    private final TermPopDao termPopDao;
    private final PaperDao paperDao;

    @Autowired
    public FetchForCache(TermPopDao termPopDao, PaperDao paperDao) {
        this.termPopDao = termPopDao;
        this.paperDao = paperDao;
    }

    /**
     * 代理dao层通过论文id获取TermPop的方法，加上缓存
     */
    @Cacheable(value = "getTermPopByPaperID", key = "#p0")
    public List<Term.Popularity> getTermPopByPaperID(long id){
        return termPopDao.getTermPopByPaperID(id);
    }

    /**
     * 代理dao层通过作者id获取Paper的方法，加上缓存
     */
    @Cacheable(value = "getAllPapersByAuthor", key = "#p0")
    public List<Paper> getAllPapersByAuthor(long id){
        return paperDao.findByAuthorId(id);
    }

    /**
     * 代理dao层通过作者id获取Paper的方法，加上缓存
     */
    @Cacheable(value = "getAllPapersByAffi", key = "#p0")
    public List<Paper> getAllPapersByAffi(long id){
        return paperDao.findByAffiId(id);
    }

    /**
     * 代理dao层通过论文id获取Paper的方法，加上缓存
     */
    @Cacheable(value = "getAllPapersByConference", key = "#p0")
    public List<Paper> getAllPapersByConference(long id){
        return paperDao.findByConferenceID(id);
    }
}
