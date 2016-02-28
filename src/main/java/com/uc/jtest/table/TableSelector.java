package com.uc.jtest.table;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.uc.jtest.table.template.TableInfo;
import com.uc.jtest.table.template.TableTemplateHandler;
import com.uc.jtest.utils.JTestStringUtils;
import com.uc.jtest.utils.PrintUtil;

/*
 * 从数据库中获取数据,index从0开始
 * 
 * tableName：表明
 * condition：查询条件
 * 
 * 获取某个字段的值get(String column)
 * 获取某一条的值：getColumnAndValue(int index) 
 * 
 * */
public class TableSelector {

    private TableInfo tableInfo;
    private String selectSql;
    private int index = 0;
	Map<Integer, Map<String, String>> indexAndColumnAndValue = new HashMap<Integer, Map<String, String>>();

	public Map<String, String> getColumnAndValue(int index) {
        return getIndexAndColumnAndValue().get(index+1);
    }

    public String get(String column) {
        return getSelectedRecordColumnAndValue().get(column);
    }

    public TableSelector condition(String condition) {
        selectSql = "select * from " + tableInfo.getTableName();
        if (condition!=null) {
            selectSql += " where " + condition;
        }
        return this;
    }
    
    

    public Map<Integer, Map<String, String>> getIndexAndColumnAndValue() {
        if (indexAndColumnAndValue.isEmpty()) {
            try {
                indexAndColumnAndValue = DBUtil.getInstance().selectResult(selectSql, tableInfo);
            } catch (Exception e) {
                PrintUtil.print("exception throws when query from db!" + e.getMessage(),Level.SEVERE);
            }
        }
        return indexAndColumnAndValue;
    }

    public TableSelector assertTotalCount(int expectedCount) {
        assertEquals(expectedCount, getIndexAndColumnAndValue().keySet().size());
        return this;
    }

    public TableSelector index(int index) {
        this.index = index;
        return this;
    }
    
   

    public TableSelector assertSame(String column, Object expectedValue) {
        String dbValue = get(column);
        PrintUtil.print("columnName:"+ column + " dbValue:" + dbValue + " expectedValue:" + expectedValue,Level.INFO);
        assertEquals(dbValue, String.valueOf(expectedValue));
        return this;
    }

    public Map<String, String> getSelectedRecordColumnAndValue() {
        Map<String, String> selectedRecordColumnAndValue = getColumnAndValue(index);
        if (selectedRecordColumnAndValue == null) {
            throw new UnsupportedOperationException("index已经超过查询结果的总数量，请检查！" + index);
        }
        return selectedRecordColumnAndValue;
    }

    public void queryAndConstructResult() {
        if (indexAndColumnAndValue.isEmpty()) {
            try {
                indexAndColumnAndValue = DBUtil.getInstance().selectResult(selectSql, tableInfo);
            } catch (Exception e) {
                PrintUtil.print("exception throws when query from db!" + e.getMessage(),Level.SEVERE);
            }
        }
    }

    public static TableSelector select(String tableName) {
        TableInfo tableInfo = TableTemplateHandler.getInstance().getTableInfo(tableName);
        return new TableSelector(tableInfo, null);
    }

    public static TableSelector select(String tableName, String sql) {
        TableInfo tableInfo = TableTemplateHandler.getInstance().getTableInfo(tableName);
        return new TableSelector(tableInfo, sql);
    }

    public TableSelector(TableInfo tableInfo, String sql) {
        this.tableInfo = tableInfo;
        if (JTestStringUtils.isEmpty(sql)) {
            sql = "select * from " + tableInfo.getTableName();
        }
        this.selectSql = sql;
        // queryAndConstructResult();
    }

    public TableInfo getTableInfo() {
        return tableInfo;
    }

    public void setTableInfo(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

}
