package com.uc.jtest.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.uc.jtest.table.template.TableInfo;

public class TableInfoChangeListener {
    public static TableInfoChangeListener listener;

    private TableInfoChangeListener() {

    }

    public static TableInfoChangeListener getInstance() {
        if (listener == null) {
            listener = new TableInfoChangeListener();
        }
        return listener;
    }

    private Map<String, List<TableInfo>> tableNameAndTableInfos;

    public Map<String, List<TableInfo>> getTableNameAndTableInfos() {
        return tableNameAndTableInfos;
    }

    public void setTableNameAndTableInfos(Map<String, List<TableInfo>> tableNameAndTableInfos) {
        this.tableNameAndTableInfos = tableNameAndTableInfos;
    }

    public boolean updateTableInfo(String tableName, TableInfo tableInfo) {
        boolean isFirstInsert = false;
        if(tableNameAndTableInfos == null){
            tableNameAndTableInfos = new HashMap<String, List<TableInfo>>();
        }
        if (tableNameAndTableInfos.get(tableName) == null) {
            isFirstInsert = true;
            tableNameAndTableInfos.put(tableName, new ArrayList<TableInfo>());
            //PrintUtil.print("first add to map "+ tableName);
        }
        tableNameAndTableInfos.get(tableName).add(tableInfo);
        //PrintUtil.print("table info added to map "+ tableName);
        return isFirstInsert;
    }

}
