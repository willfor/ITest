package com.uc.jtest.utils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.StringUtils;

import com.uc.jtest.config.JTestConstant;
import com.uc.jtest.file.FileOperateUtil;
import com.uc.jtest.table.TableCleaner;

public class TableCleanerTest {
    
    @Test
    public void testA(){
        Assert.assertEquals(FileOperateUtil.readFile(JTestConstant.TEMPLATE_TABLE_FILE_NAME),StringUtils.arrayToCommaDelimitedString(TableCleaner.resetTableNames("*")));
        Assert.assertEquals("sync_app_check_safe_00,sync_app_check_safe_01,sync_app_check_safe_02,sync_app_check_safe_03,sync_app_check_safe_04,sync_app_check_safe_05,sync_app_check_safe_06,sync_app_check_safe_07",StringUtils.arrayToCommaDelimitedString(TableCleaner.resetTableNames("sync_app_check_safe")));
        Assert.assertEquals("android_app",StringUtils.arrayToCommaDelimitedString(TableCleaner.resetTableNames("android_app")));
        Assert.assertEquals("android_app,android_app_version",StringUtils.arrayToCommaDelimitedString(TableCleaner.resetTableNames("android_app","android_app_version")));
    }
    
    

}
