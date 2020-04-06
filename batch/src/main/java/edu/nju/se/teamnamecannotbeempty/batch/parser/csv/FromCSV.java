package edu.nju.se.teamnamecannotbeempty.batch.parser.csv;

import com.opencsv.bean.CsvToBeanBuilder;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters.ToAffiliation;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters.ToAuthor;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters.ToTerm;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.repository.AffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.AuthorDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.TermDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class FromCSV {
    private final AuthorDao authorDao;
    private final AffiliationDao affiliationDao;
    private final TermDao termDao;

    private static Logger logger = LoggerFactory.getLogger(FromCSV.class);

    @Autowired
    public FromCSV(AuthorDao authorDao, AffiliationDao affiliationDao, TermDao termDao) {
        this.authorDao = authorDao;
        this.affiliationDao = affiliationDao;
        this.termDao = termDao;
    }

    public List<Paper> convert(InputStream in) {
        ToAuthor.clearSave();   ToAffiliation.clearSave();  ToTerm.clearSave();

        List<PaperDelegation> delegations = new CsvToBeanBuilder<PaperDelegation>(
                new InputStreamReader(in, StandardCharsets.UTF_8)
        ).withType(PaperDelegation.class).withSkipLines(1).build().parse();

        authorDao.saveAll(ToAuthor.getSaveList());
        affiliationDao.saveAll(ToAffiliation.getSaveList());
        termDao.saveAll(ToTerm.getSaveList());

        List<Paper> papers = new ArrayList<>(delegations.size());
        for (PaperDelegation delegation : delegations) {
            Paper paper = delegation.toPaper();
            if (paper != null && paper.getAa() != null)
                papers.add(paper);
        }
        logger.info("Done convert to paper POs");
        return papers;
    }
}
