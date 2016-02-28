package com.uc.jtest.table.template;

public enum TableTemplateNode {
    table, 
    column, 
    name, 
    ddlClass, 
    shardColumn,
    modebase, 
    defaultValue, 
    type, 
    random, 
    uniqueNumber,
    uniqueSequenceNumber,
    tableGroup,
    mainTable,
    gTable,
    fk,
    referencekey,
    autoIncrement,
    defaultMethod,
    nickName
    ;

    public static boolean isTable(String nodeName) {
        return resolveNodeName(nodeName) == table;
    }

    public static boolean isColumn(String nodeName) {
        return resolveNodeName(nodeName) == column;
    }
    
    public static boolean isTableGroup(String nodeName) {
        return resolveNodeName(nodeName) == tableGroup;
    }
    
    public static boolean isGTable(String nodeName) {
        return resolveNodeName(nodeName) == gTable;
    }

    public static TableTemplateNode resolveNodeName(String nodeName) {
        for (TableTemplateNode node : TableTemplateNode.values()) {
            if (nodeName.toLowerCase().equals(node.name().toLowerCase())) {
                return node;
            }
        }
        return null;
    }
}
