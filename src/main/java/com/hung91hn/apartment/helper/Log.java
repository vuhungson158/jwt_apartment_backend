package com.hung91hn.apartment.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Log {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void i(String s) {
        logger.info(s);
    }
}
