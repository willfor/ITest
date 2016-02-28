package com.uc.jtest.table.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.uc.jtest.table.ShardUtil;
import com.uc.jtest.utils.JTestCollectionUtil;

public class TableInfo {
    private String tableName;
    private String ddlClass;
    private String shardColumn;
    private int modebase=0;
    

    private List<TableColumnInfo> tableColumns;
    private Map<String, TableColumnInfo> columnNameAndColumnDetailMap;
    
    public List<String> getAllTableNames() {
        if (modebase > 0 && StringUtils.isNotEmpty(shardColumn)) {
            return ShardUtil.getAllTableNames(modebase, tableName);
        } else {
            return JTestCollectionUtil.getList(this.tableName);
        }
    }

    public int getModebase() {
        return modebase;
    }

    public void setModebase(int modebase) {
        this.modebase = modebase;
    }

    @Override
    public String toString() {
        return "tableName:" + tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDdlClass() {
        return ddlClass;
    }

    public void setDdlClass(String ddlClass) {
        this.ddlClass = ddlClass;
    }

    public String getShardColumn() {
        return shardColumn;
    }

    public void setShardColumn(String shardColumn) {
        this.shardColumn = shardColumn;
    }

    public List<TableColumnInfo> getTableColumns() {
        return tableColumns;
    }

    public void addToTableColumns(TableColumnInfo column) {
        if (tableColumns == null) {
            tableColumns = new ArrayList<TableColumnInfo>();
        }
        tableColumns.add(column);
        
    }

	public void refreshColumnNameAndValueMap(TableColumnInfo column) {
		//PrintUtil.print("will refresh tableColumnMap:" + column.toString());
		getColumnNameAndColumnDetailMap().put(column.getName(), column);
	}

	public void setTableColumns(List<TableColumnInfo> tableColumns) {
        this.tableColumns = tableColumns;
    }

	public Map<String, TableColumnInfo> getColumnNameAndColumnDetailMap() {
		if (columnNameAndColumnDetailMap == null) {
			//PrintUtil.print("construct table column info map.....");
			columnNameAndColumnDetailMap = new HashMap<String, TableColumnInfo>();
			for (TableColumnInfo column : tableColumns) {
				columnNameAndColumnDetailMap.put(column.getName(), column);
			}
		}
		return columnNameAndColumnDetailMap;
	}

    public void setColumnNameAndColumnDetailMap(
            Map<String, TableColumnInfo> columnNameAndColumnDetailMap) {
        this.columnNameAndColumnDetailMap = columnNameAndColumnDetailMap;
    }
    
    public TableInfo getCopy(){
    	TableInfo tableInfo = new TableInfo();
    	tableInfo.setTableName(this.tableName);
    	tableInfo.setModebase(this.modebase);
    	tableInfo.setShardColumn(this.shardColumn);
    	for(TableColumnInfo column : this.getTableColumns()){
    		TableColumnInfo newColumn = new TableColumnInfo();
    		newColumn.setAutoIncrement(column.isAutoIncrement());
    		newColumn.setDefaultMethod(column.getDefaultMethod());
    		newColumn.setDefaultValue(column.getDefaultValue());
    		newColumn.setName(column.getName());
    		newColumn.setRandomValue(column.getRandomValue());
    		newColumn.setType(column.getType());
    		newColumn.setUniqueNumber(column.isUniqueNumber());
    		newColumn.setUniqueSequenceNumber(column.getUniqueSequenceNumber());
    		newColumn.setValue(column.getValue());
    		tableInfo.addToTableColumns(newColumn);
    	}
    	return tableInfo;
    	
    }

}
