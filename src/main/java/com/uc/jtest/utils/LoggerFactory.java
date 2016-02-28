package com.uc.jtest.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.uc.jtest.config.ConfigLoader;
import com.uc.jtest.config.JTestConstant;

public class LoggerFactory {
    
    public static com.uc.jtest.utils.Logger getLogger(Class<?> cls) {
        return new com.uc.jtest.utils.Logger(java.util.logging.Logger.getLogger(cls.getName()));
    }
    
    public static Level getDefaultLevel() {
        String level = ConfigLoader.getProperty(JTestConstant.LOGGER_LEVEL);
        if (level == null) {
            return null;
        }
        level = level.toUpperCase();
        Map<String, Level> levMap = new HashMap<String, Level>();
        levMap.put("DEBUG", Level.FINE);
        levMap.put("INFO", Level.INFO);
        levMap.put("WARN", Level.WARNING);
        levMap.put("ERROR", Level.SEVERE);
        if (!levMap.keySet().contains(level)) {
            return null;
        }
        return levMap.get(level);
    }

}
