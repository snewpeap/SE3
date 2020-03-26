package edu.nju.se.teamnamecannotbeempty.backend.data;

import com.opencsv.bean.CsvToBeanBuilder;
import edu.nju.se.teamnamecannotbeempty.backend.AppContextProvider;
import edu.nju.se.teamnamecannotbeempty.backend.dao.AffiliationDao;
import edu.nju.se.teamnamecannotbeempty.backend.dao.AuthorDao;
import edu.nju.se.teamnamecannotbeempty.backend.dao.TermDao;
import edu.nju.se.teamnamecannotbeempty.backend.data.converters.ToAffiliation;
import edu.nju.se.teamnamecannotbeempty.backend.data.converters.ToAuthor;
import edu.nju.se.teamnamecannotbeempty.backend.data.converters.ToTerm;
import edu.nju.se.teamnamecannotbeempty.backend.po.Paper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class FromCSVOpenCSVImpl {
    private static Logger logger = LoggerFactory.getLogger(FromCSVOpenCSVImpl.class);

    public List<Paper> convert(InputStream in) {
        logger.info("Start parsing");
        List<PaperDelegation> delegations = new CsvToBeanBuilder<PaperDelegation>(
                new InputStreamReader(in, StandardCharsets.UTF_8)
        ).withType(PaperDelegation.class).withSkipLines(1).build().parse();

        logger.info("Start saving non-paper");
        AppContextProvider.getBean(AuthorDao.class).saveAll(ToAuthor.getSaveList());
        AppContextProvider.getBean(AffiliationDao.class).saveAll(ToAffiliation.getSaveList());
        AppContextProvider.getBean(TermDao.class).saveAll(ToTerm.getSaveList());

        logger.info("Start convert to paper POs");
        List<Paper> papers = new ArrayList<>(delegations.size());
        for (PaperDelegation delegation : delegations) {
            Paper paper = delegation.toPaper();
            if (paper != null && paper.getAa() != null)
                papers.add(delegation.toPaper());
        }
        return papers;
    }

}
