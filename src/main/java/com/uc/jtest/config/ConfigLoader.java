package com.uc.jtest.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Level;

import com.uc.jtest.file.FileOperateUtil;
import com.uc.jtest.utils.PrintUtil;

public class ConfigLoader {
    static Properties properties = null;

    static {
        loadProperties(FileOperateUtil.getCurrentProjectPath(false)+JTestConstant.GLOBAL_FILE_NAME);
    }

    private static Properties loadProperties(String fileName) {
        properties = new Properties();
        int i = 2;
        while (i > 0) {
            try {
                properties.load(new InputStreamReader(new FileInputStream(new File(fileName)),
                        "UTF-8"));
                return properties;
            } catch (Exception e) {
                i--;
                PrintUtil.print(
                        "File IOExcepiton. Please check the file : " + fileName + e.getMessage(),
                        Level.SEVERE);
                fileName = FileOperateUtil.getCurrentProjectPath(true)
                        + JTestConstant.GLOBAL_FILE_NAME;
            }
        }
        return properties;
    }

    public static Properties getProperties() {
        if (null == properties) {
            PrintUtil.print("global.properties 不存在,请检查！" + JTestConstant.GLOBAL_FILE_NAME,Level.SEVERE);
        }
        return properties;
    }

    public static String getProperty(String key) {
        return getProperties().getProperty(key);
    }

}
