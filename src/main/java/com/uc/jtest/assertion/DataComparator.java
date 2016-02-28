package com.uc.jtest.assertion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.junit.Assert;

import com.uc.jtest.table.TableSelector;
import com.uc.jtest.table.template.TableColumnInfo;
import com.uc.jtest.table.template.TableInfo;
import com.uc.jtest.utils.PrintUtil;

public class DataComparator {

	private Map<String, List<TableInfo>> tableNameAndTableInfos;

	public DataComparator(Map<String, List<TableInfo>> tableNameAndTableInfos) {
		this.tableNameAndTableInfos = tableNameAndTableInfos;
	}

	public DataComparator() {
		super();
	}

	public void compareInsertedAndDBData(String tableName) {
		List<TableInfo> preparedData = tableNameAndTableInfos.get(tableName);
		Map<Integer, Map<String, String>> dbData = TableSelector.select(
				tableName).getIndexAndColumnAndValue();
		Assert.assertEquals(preparedData.size(), dbData.size());
		for (int i = 1; i <= dbData.size(); i++) {
			Map<String, String> dbDataColumnValue = dbData.get(i);
			Map<String, TableColumnInfo> preparedColumnDataAndValue = preparedData
					.get(i - 1).getColumnNameAndColumnDetailMap();
			for (String columnName : dbDataColumnValue.keySet()) {
				TableColumnInfo preparedColumnData = preparedColumnDataAndValue
						.get(columnName);
				Assert.assertFalse(preparedColumnData == null);
				if (!preparedColumnData.isAutoIncrement()) {
					Assert.assertEquals(preparedColumnData.getValue(),
							dbDataColumnValue.get(columnName));
				}
			}
		}
	}

	public static void compareData(String sourceJsonString,
			Map<String, Object> expectedkeyAndValue) {
		for (String key : expectedkeyAndValue.keySet()) {
			JsonAssertion.JsonAssert(sourceJsonString).assertEquals(
					expectedkeyAndValue.get(key), key);
		}
	}

	public static void mappingInsertAndResponse(
			Map<String, Object> insertedData, String s) {
		String[] c = s.split(",");
		for (int i = 0; i < c.length; i++) {
			String[] keyValue = c[i].split(":");
			if (insertedData.get(keyValue[0]) != null) {
				insertedData.put(keyValue[1], insertedData.get(keyValue[0]));
				insertedData.remove(keyValue[0]);
			}
		}

	}

	public Map<String, Object> getInsertedData(String tableName, int index,
			String... columns) {
		List<TableInfo> tableInfos = tableNameAndTableInfos.get(tableName);
		TableInfo tableInfo = tableInfos.get(index);
		Map<String, Object> keyValue = new HashMap<String, Object>();
		Map<String, TableColumnInfo> columnNameAndColumnInfo = tableInfo
				.getColumnNameAndColumnDetailMap();
		if (columns == null || columns.length == 0) {
			PrintUtil.print("没有显式声明列，将返回表 " + tableName + " 中所有列的数据",Level.FINE);
			for (String column : columnNameAndColumnInfo.keySet()) {
				keyValue.put(column, columnNameAndColumnInfo.get(column)
						.getValue());
			}
		} else {
			for (String column : columns) {
				keyValue.put(column, columnNameAndColumnInfo.get(column)
						.getValue());
			}
		}
		return keyValue;
	}

	public Map<String, Object> getInsertedData(String tableName, String idName,
			Object id, String... columns) {
		List<TableInfo> tableInfos = tableNameAndTableInfos.get(tableName);
		TableInfo tableInfo = null;
		for (TableInfo tempTableInfo : tableInfos) {
			Map<String, TableColumnInfo> columnNameAndColumnInfo = tempTableInfo
					.getColumnNameAndColumnDetailMap();
			for (String column : columns) {
				TableColumnInfo tableColumn = columnNameAndColumnInfo
						.get(column);
				if (idName.equals(tableColumn.getName())
						&& String.valueOf(id).equals(tableColumn.getValue())) {
					tableInfo = tempTableInfo;
				}
			}
		}
		Map<String, Object> keyValue = new HashMap<String, Object>();
		Map<String, TableColumnInfo> columnNameAndColumnInfo = tableInfo
				.getColumnNameAndColumnDetailMap();
		if (columns == null || columns.length == 0) {
			PrintUtil.print("没有显式声明列，将返回表 " + tableName + " 中所有列的数据",Level.INFO);
			for (String column : columnNameAndColumnInfo.keySet()) {
				keyValue.put(column, columnNameAndColumnInfo.get(column)
						.getValue());
			}
		} else {
			for (String column : columns) {
				keyValue.put(column, columnNameAndColumnInfo.get(column)
						.getValue());
			}
		}
		return keyValue;
	}

	public static Map<String, Object> getSelectedData(String tableName,
			String condition, String... columns) {
		return getSelectedData(tableName, condition, 0, columns);
	}

	public static Map<String, Object> getSelectedData(String tableName,
			String condition, int index, String... columns) {
		Map<String, Object> specifiedColumnAndValue = new HashMap<String, Object>();
		Map<String, String> columnNameAndValue = TableSelector
				.select(tableName).condition(condition)
				.getColumnAndValue(index);
		if (columnNameAndValue == null || columnNameAndValue.size() == 0) {
			PrintUtil.print("没有符合条件的数据，请检查数据库表" + tableName + "！",Level.WARNING);
			return specifiedColumnAndValue;
		}

		if (columns == null || columns.length == 0) {
			PrintUtil.print("没有声明特定的列，将返回数据库中表" + tableName + "的所有列的值！",Level.FINE);
			for (String columnName : columnNameAndValue.keySet()) {
				specifiedColumnAndValue.put(columnName,
						columnNameAndValue.get(columnName));
			}
		} else {
			for (String column : columns) {
				if (columnNameAndValue.get(column) == null) {
					PrintUtil.print("column:" + column + " is null！",Level.WARNING);
				}
				specifiedColumnAndValue.put(column,
						columnNameAndValue.get(column));
			}
		}
		return specifiedColumnAndValue;
	}

}
