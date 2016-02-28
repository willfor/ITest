package com.uc.jtest.table.template;

import java.util.ArrayList;
import java.util.List;

public class TableGroup {
    private String groupName;
    private String mainTableName;
    private List<TableRelation> tableRelations;

    public void addToTableRelations(TableRelation relation) {
        if (tableRelations == null) {
            tableRelations = new ArrayList<TableRelation>();
        }
        tableRelations.add(relation);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMainTableName() {
        return mainTableName;
    }

    public void setMainTableName(String mainTableName) {
        this.mainTableName = mainTableName;
    }

    public List<TableRelation> getTableRelations() {
        return tableRelations;
    }

    public void setTableRelations(List<TableRelation> tableRelations) {
        this.tableRelations = tableRelations;
    }

}
