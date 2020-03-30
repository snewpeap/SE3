package edu.nju.se.teamnamecannotbeempty.api;

import javax.jws.WebService;

@WebService
public interface TestService {
    String test(String string);
}
