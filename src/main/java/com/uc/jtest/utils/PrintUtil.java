package com.uc.jtest.utils;

import java.util.logging.Level;


public class PrintUtil {
    public static final Logger logger = LoggerFactory
            .getLogger(PrintUtil.class);
	

    public static void print(String message, Level level) {
        if (level.equals(Level.SEVERE)) {
            logger.error(message);
        }
        if (level.equals(Level.WARNING)) {
            logger.warn(message);
        }
        if (level.equals(Level.FINE) || level.equals(Level.INFO)) {
            logger.info(message);
        }
    }

}
