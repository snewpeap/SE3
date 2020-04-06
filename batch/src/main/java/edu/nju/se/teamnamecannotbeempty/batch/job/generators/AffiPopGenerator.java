package edu.nju.se.teamnamecannotbeempty.batch.job.generators;

import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;
import edu.nju.se.teamnamecannotbeempty.data.repository.AffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.AffiPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.PaperPopDao;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.Future;

@Component
public class AffiPopGenerator {
    private final AffiliationDao affiliationDao;
    private final AffiPopDao affiPopDao;
    private final PaperPopDao paperPopDao;

    @Autowired
    public AffiPopGenerator(AffiliationDao affiliationDao, AffiPopDao affiPopDao, PaperPopDao paperPopDao) {
        this.affiliationDao = affiliationDao;
        this.affiPopDao = affiPopDao;
        this.paperPopDao = paperPopDao;
    }

    @Async
    public Future<?> generateAffiPop() {
        affiliationDao.getAll().forEach(this::generatePop);
        LoggerFactory.getLogger(getClass()).info("Done generate affiliation popularity");
        return new AsyncResult<>(null);
    }

    void generatePop(Affiliation affi) {
        Affiliation actual = affi.getActual();
        Optional<Affiliation.Popularity> result = affiPopDao.findByAffiliation_Id(actual.getId());
        Affiliation.Popularity pop = result.orElse(new Affiliation.Popularity(actual, 0.0));
        if (!actual.getName().equals("NA")) {
            Double sum = paperPopDao.getPopSumByAffiId(actual.getId());
            sum = sum == null ? 0.0 : sum;
            pop.setPopularity(pop.getPopularity() + sum);
        }
        affiPopDao.save(pop);
    }
}
