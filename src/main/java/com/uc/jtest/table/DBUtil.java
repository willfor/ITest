package com.uc.jtest.table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import jws.Jws;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;

import com.uc.jtest.config.ConfigLoader;
import com.uc.jtest.config.JTestConstant;
import com.uc.jtest.table.template.DBType;
import com.uc.jtest.table.template.TableColumnInfo;
import com.uc.jtest.table.template.TableInfo;
import com.uc.jtest.utils.JTestCollectionUtil;
import com.uc.jtest.utils.PrintUtil;

public class DBUtil {
    private static String DEFAULT_CONNECTION_STR;
    
    static {
        if(StringUtils.isNotEmpty(ConfigLoader.getProperty("JDBC_URL"))){
            DEFAULT_CONNECTION_STR = ConfigLoader.getProperty("JDBC_URL");
            PrintUtil.print("jdbc connection str 从文件：" + JTestConstant.GLOBAL_FILE_NAME + " 中加载",Level.INFO);
        }else{
            DEFAULT_CONNECTION_STR = "jdbc:mysql://"
                  + Jws.configuration.getProperty("p1.dbbase.host") + "/"
                  + Jws.configuration.getProperty("p1.dbbase.database") + "?user="
                  + Jws.configuration.getProperty("p1.dbbase.user") + "&password="
                  + Jws.configuration.getProperty("p1.dbbase.pass")
                  + "&useUnicode=true&characterEncoding=utf-8";
            PrintUtil.print("jdbc connection str 从JWS配置文件 中加载",Level.INFO);
        }
    }



    private static Connection cnn = null;

    private static DBUtil dbUtil = new DBUtil();

    private DBUtil() {

    }

    public static DBUtil getInstance() {
        setConnection();
        return dbUtil;
    }

    public boolean closeConnection() {
        try {
            if (cnn != null) {
                cnn.close();
                PrintUtil.print("DB Connection Closed!",Level.INFO);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static Connection setConnection() {
        try {
            if (cnn == null || cnn.isClosed()) {
                cnn = DriverManager.getConnection(DEFAULT_CONNECTION_STR);
                PrintUtil.print("DB Connection Done!:" + DEFAULT_CONNECTION_STR,Level.INFO);
            }
        } catch (Exception e) {
            PrintUtil.print(e.getMessage(),Level.SEVERE);
        }
        return cnn;
    }

    protected void delete(String tableName, String columnName, String value) {
        this.execute("delete from " + tableName + " where " + columnName + " = " + value);
    }

    protected boolean truncate(String... tableNames) {
        boolean allSuccess = true;
        List<String> sqls = new ArrayList<String>();
        for (String tableName : tableNames) {
            sqls.add("truncate table " + tableName);
        }
        allSuccess = batchExecute(sqls);
        return allSuccess;
    }


    protected boolean deleteAll(String... tableNames) {
        boolean allSuccess = true;
        List<String> sqls = new ArrayList<String>();
        for (String tableName : tableNames) {
            sqls.add("delete from " + tableName);
        }
        allSuccess = batchExecute(sqls);
        return allSuccess;
    }

    public boolean execute(String sql) {
        boolean isSuccess = true;
        try {
            isSuccess = executeSql(sql);
            // PrintUtil.print("execute sql:" + sql + " successfully");
        } catch (Exception e) {
            isSuccess = false;
            PrintUtil.print("exception throws when execute sql :" + sql + " " + e.getMessage(),Level.SEVERE);
            
        }
        Assert.assertTrue(isSuccess);
        return isSuccess;
    }
    
    private boolean batchExecute(List<String> sqls) {
        long t1 = System.currentTimeMillis();
        boolean isSuccess = true;
        Statement stmt = null;
        try {
            stmt = cnn.createStatement();
            for (String sql : sqls) {
                stmt.addBatch(sql);
            }
            stmt.executeBatch();
        } catch (Exception e) {
            isSuccess = false;
            PrintUtil.print("批量执行SQL出错，请检查表名是否正确 :" + sqls + " " + e.getMessage(),Level.SEVERE);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                isSuccess = false;
                PrintUtil.print("exception throws when close Statement :" + sqls + " "
                        + e.getMessage(),Level.SEVERE);
            }
        }
        long t2 = System.currentTimeMillis();
        if (t2 - t1 > 100) {
            PrintUtil.print("批量执行SQL:" + sqls + " 共花费时间：" + (t2 - t1),Level.INFO);
        }
        Assert.assertTrue(isSuccess);
        return isSuccess;
    }

    private boolean executeSql(String sql) throws Exception {
        boolean isSuccess = true;
        PreparedStatement stmt = null;
        try {
            stmt = cnn.prepareStatement(sql);
            stmt.execute(sql);
        } catch (Exception e) {
            isSuccess = false;
            PrintUtil.print("exception throws when execute sql :" + sql + " " + e.getMessage(),Level.SEVERE);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return isSuccess;
    }

    public Map<Integer, Map<String, String>> selectResult(String sql, TableInfo tableInfo)
            throws Exception {
        PreparedStatement stmt = null;
        Map<Integer, Map<String, String>> indexAndColumnAndValue = new HashMap<Integer, Map<String, String>>();
        boolean isSuccess = true;
        try {
            stmt = cnn.prepareStatement(sql);
            ResultSet result = stmt.executeQuery(sql);
            int i = 0;
            while (result.next()) {
                i++;
                Map<String, String> columnValueMap = new HashMap<String, String>();
                for (TableColumnInfo column : tableInfo.getTableColumns()) {
                    try {
                        String value = "";
                        value = getStringTypeValue(
                                result.getString(column.getName()));

                        columnValueMap.put(column.getName(), value);
                    } catch (Exception e) {
                        PrintUtil.print("exception throws when query from db!" + e.getMessage(),
                                Level.SEVERE);
                        isSuccess = false;
                        continue;
                    }
                }
                indexAndColumnAndValue.put(i, columnValueMap);
            }
        } catch (Exception e) {
            isSuccess = false;
            PrintUtil.print("exception throws when query from db!" + e.getMessage(), Level.SEVERE);

        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        Assert.assertTrue(isSuccess);
        return indexAndColumnAndValue;
    }

    private String getStringTypeValue(Object value) {
        if (value != null) {
            // PrintUtil.print(columnName + "'s value :" + value);
            return String.valueOf(value);
        }
        return null;
    }
    
    
    public List<Map<String, String>> selectResult(String sql, Map<String, TableInfo> tableInfos,List<String> expectedColumns) {
        PreparedStatement stmt = null;
        List<Map<String, String>> columnValues = new ArrayList<Map<String, String>>();
        try {
            stmt = cnn.prepareStatement(sql);
            ResultSet result = stmt.executeQuery(sql);
            String currentColumn = "";
            String currentTable = "";
            while (result.next()) {
                Map<String,String> columnValueMap = new HashMap<String,String>(); 
                columnValues.add(columnValueMap);
                for(TableInfo tableInfo : tableInfos.values()){
                for (TableColumnInfo column : tableInfo.getTableColumns()) {
                    if(columnValueMap.get(column.getName()) !=null){
                        continue;
                    }
                    
                    if(!JTestCollectionUtil.isEmpty(expectedColumns) && !expectedColumns.contains(column.getNickName())){
                        continue;
                    }
                        try {
                            String value = "";
                            value = getStringTypeValue(result.getString(column.getNickName()));
                            columnValueMap.put(column.getNickName(), value);
                        } catch (Exception e) {
                            PrintUtil.print("exception throws when query from db!" + e.getMessage()
                                    + " actualtable:" + currentTable + " actualColumn:"
                                    + currentColumn, Level.SEVERE);
                            continue;
                        }
                }
                }
            }
        } catch (Exception e) {
            PrintUtil.print("exception throws when query from db!" + e.getMessage(),Level.SEVERE);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                PrintUtil.print("exception throws when close stream!" + e.getStackTrace(),
                        Level.SEVERE);
            }
        }
        closeConnection();
        return columnValues;
    }
}
