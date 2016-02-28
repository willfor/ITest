package com.uc.jtest.table;

import java.util.HashMap;
import java.util.Map;

import com.uc.jtest.table.template.TableGroup;
import com.uc.jtest.table.template.TableRelation;
import com.uc.jtest.table.template.TableTemplateHandler;

public class TableGroupInsertor {
    private TableGroup tableGroup;
    private Map<String, Object> mainTableColumnNameAndValue = new HashMap<String, Object>();
    private Map<String, Object> tableColumnNameAndValue = new HashMap<String, Object>();
    

    public static void main(String[] args) {
        tableGroup("app").mainTableValue("id", "[300~303]").mainTableValue("app_catid", "[1~3]").insert(3);
    }

    public static TableGroupInsertor tableGroup(String groupName) {
        return new TableGroupInsertor(groupName);
    }

    public TableGroupInsertor(String groupName) {
        this.tableGroup = TableTemplateHandler.getInstance().getTableGroups().get(groupName);
        if (this.tableGroup == null) {
            throw new UnsupportedOperationException("tableGroup:" + groupName
                    + " not exist,pls check whether configured in template!");
        }

    }
    
    public TableGroupInsertor tableValue(String key, Object value) {
        tableColumnNameAndValue.put(key, value);
        return this;
    }

    public TableGroupInsertor mainTableValue(String key, Object value) {
        mainTableColumnNameAndValue.put(key, value);
        return this;
    }

    public TableGroupInsertor insert(int count) {
        TableInsertor mainTableInsertor = TableInsertor
                .table(tableGroup.getMainTableName());
        for (String columnName : mainTableColumnNameAndValue.keySet()) {
            mainTableInsertor.value(columnName, mainTableColumnNameAndValue.get(columnName));
        }
        mainTableInsertor.insert(count);
        for (TableRelation relation : tableGroup.getTableRelations()) {
            TableInsertor relateTableInsertor =TableInsertor.table(relation.getTableName()); 
            Object specifiedColumnValue = mainTableColumnNameAndValue.get(relation.getReferenceKey());
            if (specifiedColumnValue != null) {
                relateTableInsertor
                        .value(relation.getFk(), specifiedColumnValue).insert(count);
            }
        }
        return this;
    }
    // tableGroup("app").table("android_app").value("id","1-3").value("app_catid","1-3").insert(3);

}
