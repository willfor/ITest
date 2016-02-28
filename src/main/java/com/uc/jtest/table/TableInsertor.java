package com.uc.jtest.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.lang.StringEscapeUtils;

import com.uc.jtest.listener.TableInfoChangeListener;
import com.uc.jtest.table.template.DBType;
import com.uc.jtest.table.template.TableColumnInfo;
import com.uc.jtest.table.template.TableInfo;
import com.uc.jtest.table.template.TableTemplateHandler;
import com.uc.jtest.utils.JTestStringUtils;
import com.uc.jtest.utils.PrintUtil;
import com.uc.jtest.utils.RandomUtil;

public class TableInsertor {
    private String tableName;
    private boolean needInsert = true;
    private boolean needTruncate = true;
    private Map<String, Object> columnNameAndValue = new HashMap<String, Object>();
    private List<TableInfo> tableInfos = new ArrayList<TableInfo>();
    private List<String> insertSqls;

    public List<String> getInsertSqls() {
        return insertSqls;
    }

    private void addToInsertSqls(String insertSql) {
        if (insertSqls == null) {
            insertSqls = new ArrayList<String>();
        }
        insertSqls.add(insertSql);
    }

    private TableInsertor(String tableName) {
        this.tableName = tableName;
    }

    public static TableInsertor table(String tableName) {
        return new TableInsertor(tableName);
    }

    public static TableInsertor table(String tableName, boolean needInsert) {
        return new TableInsertor(tableName, needInsert);
    }
    

    private TableInsertor(String tableName, boolean needInsert) {
        this.tableName = tableName;
        this.needInsert = needInsert;
    }

    public TableInsertor value(String columnName, Object columnValue) {
        columnNameAndValue.put(columnName, columnValue);
        return this;
    }
    
    public TableInsertor needTruncate(boolean needTruncate){
        this.needTruncate = needTruncate;
        return this;
    }

    /**
     * 默认插入1条数据 <br>
     * 
     * @author:zhoulz <br>
     * @date: 2015年1月19日 下午2:37:30
     */
    public TableInsertor insert() {
        this.insert(1);
        return this;
    }

    /*
     * 
     * 组装列的值 计算分表的名 拼装SQL 插入到数据库
     */
    public TableInsertor insert(int count) {
        for (int i = 0; i < count; i++) {
            TableInfo tableInfo = TableTemplateHandler.getInstance().getTableInfo(this.tableName)
                    .getCopy();
            setValueToColumns(i, tableInfo);
            String tableName = getCaculatedTableName(tableInfo);
            tableInfos.add(tableInfo);
            boolean isNeedTruncate = TableInfoChangeListener.getInstance().updateTableInfo(
                    tableName, tableInfo);
            if (needTruncate && isNeedTruncate) {
                PrintUtil.print("will delete all table data before first insert:" + tableName,Level.INFO);
                TableCleaner.truncateAllByOriginTableName(tableName);
            }

            StringBuffer sql = new StringBuffer();
            sql.append("insert into ").append(tableName).append(" ( ");
            int j = 0;
            for (TableColumnInfo column : tableInfo.getTableColumns()) {
                // 如果列是自增的，并且没有显式声明，列名则不会加到Insert语句里面
                if (column.isAutoIncrement() && columnNameAndValue.get(column.getName()) == null) {
                    continue;
                }
                if (j > 0) {
                    sql.append(",");
                }
                sql.append(column.getName());
                j++;
            }
            sql.append(")").append(" values( ");
            int k = 0;
            for (TableColumnInfo column : tableInfo.getTableColumns()) {
                // 如果列是自增的，并且没有显式声明，列的值则不会加到Insert语句里面
                if (column.isAutoIncrement() && columnNameAndValue.get(column.getName()) == null) {
                    continue;
                }
                if (k > 0) {
                    sql.append(",");
                }
                try {
                    sql.append(getFormatedColumnNameForDBInsert(tableInfo
                            .getColumnNameAndColumnDetailMap().get(column.getName())));
                } catch (Exception e) {
                    PrintUtil.print("throws Exception when fetch value for column,  tableInfo:"
                            + tableInfo.toString() + "columnInfo:" + column.toString(),
                            Level.SEVERE);
                }
                k++;
            }
            sql.append(")");
            if (needInsert) {
                DBUtil.getInstance().execute(sql.toString());
                PrintUtil.print("sql :" + sql.toString() + "执行成功！",Level.INFO);
            }else{
                //PrintUtil.print("收到的指令不会执行Insert", Level.INFO);
            }
            this.addToInsertSqls(sql.toString());
        }
        return this;
    }

    /*
     * 
     * 分表的处理
     */
    private String getCaculatedTableName(TableInfo tableInfo) {
        String tableName = tableInfo.getTableName();
        if (tableInfo.getModebase() > 0) {
            for (TableColumnInfo column : tableInfo.getTableColumns()) {
                if (column.getName().equals(tableInfo.getShardColumn())) {
                    tableName = ShardUtil.getShard(tableInfo.getModebase(), tableName,
                            column.getValue());
                    break;
                }
            }
        }
        return tableName;
    }

    /*
     * 
     * 处理列值有SQL转义字符的
     */
    private String getFormatedColumnNameForDBInsert(TableColumnInfo column) {
        if (column.getValue() == null) {
            return null;
        }

        String result = String.valueOf(column.getValue());
        result = StringEscapeUtils.escapeSql(result);
        if (JTestStringUtils.isEmpty(column.getType())
                || DBType.isStringRelatedType(column.getType())
                || DBType.isDateTime(column.getType())) {
            return "'" + result + "'";
        }

        return result;
    }

    /*
     * 设置特殊值，例如唯一顺序，随机等等 如果显式声明的列在模板里面不存在，将允许插入
     */
    private void setValueToColumns(int i, TableInfo tableInfo) {
        for (TableColumnInfo column : tableInfo.getTableColumns()) {
            column.setToExpectedValue(i);
        }
        for (String columnName : columnNameAndValue.keySet()) {
            String columnValue = String.valueOf(columnNameAndValue.get(columnName));
            columnValue = RandomUtil.getRangeValueBySequenceWithStartAndEndFlag(columnValue, i);
            setColumnValue(tableInfo, columnName, columnValue);
        }
    }

    /*
     * 显式声明的列的值，映射到TableInfo里面
     */
    private void setColumnValue(TableInfo tableInfo, String columnName, Object columnValue) {
        boolean isInsert = true;
        for (TableColumnInfo column : tableInfo.getTableColumns()) {
            if (column.getName().equals(columnName)) {
                column.setValue(columnValue);
                tableInfo.refreshColumnNameAndValueMap(column);
                isInsert = false;
                break;
            }
        }
        if (isInsert) {
            TableColumnInfo column = new TableColumnInfo(columnName, columnValue);
            tableInfo.addToTableColumns(column);
            tableInfo.refreshColumnNameAndValueMap(column);
        }
    }

    public List<TableInfo> getTableInfos() {
        return tableInfos;
    }

}
