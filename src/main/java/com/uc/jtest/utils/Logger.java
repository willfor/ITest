package com.uc.jtest.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.uc.jtest.utils.DateUtil;


public class Logger {
    
    public static final String LINE_SEPERATOR = System.getProperty("line.separator");
    
    private java.util.logging.Logger logger;
    
    private ConsoleHandler handler;
    
    private Level defLevel;

    public Logger(java.util.logging.Logger logger) {
        //don't use JDK provided handlers
        logger.setUseParentHandlers(false);
        System.setProperty("eclipselink.logging.logger", Logger.class.toString());
        Formatter formatter = new JTestFormatter();
        handler = new JTestConsoleHandler();
        handler.setFormatter(formatter);
        //set level
        if (defLevel == null) {
            defLevel = LoggerFactory.getDefaultLevel();
        }
        if (defLevel == null) {
            handler.setLevel(Level.INFO);
            logger.setLevel(Level.INFO);
        } else {
            handler.setLevel(defLevel);
            logger.setLevel(defLevel);
        }
        logger.addHandler(handler);
        this.logger = logger;
    }
    
    public void debug(String message) {
        logger.fine(wrapper(message, 0));
    }
    
    public void info(String message) {
        logger.info(wrapper(message, 0));
    }
    
    public void warn(String message) {
        logger.warning(wrapper(message, 0));
    }
    
    public void error(String message) {
        logger.severe(wrapper(message, 0));
    }
    
    public void error(String message, int depth) {
        logger.severe(wrapper(message, depth));
    }
    
    public void error(String message, Throwable cause) {
        final Writer detail = new StringWriter(); 
        final PrintWriter printWriter = new PrintWriter(detail); 
        cause.printStackTrace(printWriter); 
        logger.severe(wrapper(message + LINE_SEPERATOR + detail.toString(), 0));
    }
    
    public void error(String message, Throwable cause, int depth) {
        final Writer detail = new StringWriter(); 
        final PrintWriter printWriter = new PrintWriter(detail); 
        cause.printStackTrace(printWriter); 
        logger.severe(wrapper(message + LINE_SEPERATOR + detail.toString(), depth));
    }
    
    private String wrapper(String message, int depth) {
        if (message == null) {
            return null;
        }
        final int def = 4 + depth;
        StringBuffer sb = new StringBuffer(message).append(LINE_SEPERATOR);
        String callerInfo = referCaller(def);
        if (callerInfo != null && !callerInfo.isEmpty()) {
            sb.append("[").append(callerInfo).append("]").append(LINE_SEPERATOR);
        }
        return sb.toString();
    }
    
    private String referCaller(int depth) {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] eles = Thread.currentThread().getStackTrace();
        if (eles.length > depth + 1) {
            StackTraceElement ele = eles[depth];
//            sb.append("class:" + ele.getClassName() + ", ");
//            sb.append("method:" + ele.getMethodName() + ", ");
//            sb.append("line:" + ele.getLineNumber());
        }
        return sb.toString();
    }
    
    public class JTestFormatter extends Formatter {

        private String levelToString(Level level) {
            if (level.equals(Level.SEVERE)) {
                return "ERROR";
            }
            if (level.equals(Level.WARNING)) {
                return "WARN";
            }
            if (level.equals(Level.FINE)) {
                return "DEBUG";
            }
            return level.toString();
        }
        
        @Override
        public String format(LogRecord record) {
            StringBuffer sb = new StringBuffer();
            sb.append("[")
              .append(DateUtil.formatCurrentDate("yyyy-MM-dd hh:mm:ss"))
              .append("]")
              .append(levelToString(record.getLevel()))
              .append(" ~ ")
              .append(record.getMessage());
            return sb.toString();
        }
        
    }
    
    public class JTestConsoleHandler extends ConsoleHandler {
        public JTestConsoleHandler() {
            super();
            setOutputStream(System.out);
        }
    }
    
}