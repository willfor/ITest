package com.uc.jtest.table;

import static com.uc.jtest.config.JTestConstant.TEMPLATE_TABLE_FILE_NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.junit.Assert;
import org.springframework.util.StringUtils;

import com.uc.jtest.file.FileOperateUtil;
import com.uc.jtest.table.template.TableInfo;
import com.uc.jtest.table.template.TableTemplateHandler;
import com.uc.jtest.utils.PrintUtil;

public class TableCleaner {
    
    public void delete(String tableName, String columnName, String value){
        DBUtil.getInstance().delete(tableName, columnName, value);
    }

    public static boolean deleteAll(String... tableNames) {
        return DBUtil.getInstance().deleteAll(resetTableNames(tableNames));
    }

    public static boolean truncateAll(String... tableNames) {
        return DBUtil.getInstance().truncate(resetTableNames(tableNames));
    }
    
    public static boolean truncateAllByOriginTableName(String... tableNames) {
        return DBUtil.getInstance().truncate(tableNames);
    }

    public static String[] resetTableNames(String... tableNames) {
        Assert.assertFalse(tableNames ==null || tableNames.length ==0);
        if ("*".equals(tableNames[0])) {
            return FileOperateUtil.readFile(TEMPLATE_TABLE_FILE_NAME).split(",");
        }
        List<String> actualTableNames = new ArrayList<String>();
        for (String tableName : tableNames) {
            TableInfo tableInfo = TableTemplateHandler.getInstance().getTableInfo(tableName);
            List<String> alltableNames = tableInfo.getAllTableNames();
            if(alltableNames.size()>1){
                PrintUtil.print("将清除分库表："+ tableName + " 对应的所有分表的数据" + StringUtils.arrayToCommaDelimitedString((String[]) alltableNames.toArray(new String[alltableNames.size()])),Level.INFO);
            }
            actualTableNames.addAll(alltableNames);
        }
        return (String[]) actualTableNames.toArray(new String[actualTableNames.size()]);
    }

}
