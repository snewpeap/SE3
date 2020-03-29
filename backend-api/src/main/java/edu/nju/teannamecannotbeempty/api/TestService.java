package edu.nju.teannamecannotbeempty.api;

import javax.jws.WebService;

@WebService
public interface TestService {
    String test(String string);
}
