package edu.nju.se.teamnamecannotbeempty.backend.data;

import com.opencsv.bean.CsvToBeanBuilder;
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
        logger.info("Start Parsing");
        List<PaperDelegation> delegations = new CsvToBeanBuilder<PaperDelegation>(
                new InputStreamReader(in, StandardCharsets.UTF_8)
        ).withType(PaperDelegation.class).withSkipLines(1).build().parse();
        logger.info("Done parsing");
        List<Paper> papers = new ArrayList<>();
        for (PaperDelegation delegation : delegations) {
            Paper paper = delegation.toPaper();
            if (paper != null && paper.getAa() != null)
                papers.add(delegation.toPaper());
        }
        return papers;
    }

}
