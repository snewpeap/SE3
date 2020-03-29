package edu.nju.se.teamnamecannotbeempty.batch;

import edu.nju.teannamecannotbeempty.api.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String test(String string) {
        logger.info("> " + string);
        return "Receive " + string;
    }
}
