package com.uc.jtest.table;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import jws.dal.common.HashUtils;

import com.uc.jtest.utils.PrintUtil;

public class ShardUtil {
	public static String getShard(int tableModebase, String tableName,
			Object columnValue) {
		String columnValueStr = String.valueOf(columnValue);
		int index = 0;
		try {
			index = (Integer.valueOf(columnValueStr).intValue())
					% tableModebase;
		} catch (Exception e) {
			index = getTableNumFromHashKey(columnValueStr, tableModebase);
		}
		tableName += getFormattedTableName(index);
		return tableName;
	}

    private static String getFormattedTableName(int index) {
        return "_0" + index;
    }
	
    public static List<String> getAllTableNames(int tableModeBase, String tableName) {
        List<String> tableNames = new ArrayList<String>();
        for (int i = 0; i < tableModeBase; i++) {
            tableNames.add(tableName + getFormattedTableName(i));
        }
        return tableNames;
    }

	/**
	 * 
	 * 获取准确分表.
	 * 
	 * @param key
	 * @return
	 */
	private static int getTableNumFromHashKey(String key, int modeBase) {
		int index = 0;
		try {
			long value = HashUtils.md5(key);
			index = (int) (value % modeBase);
		} catch (Exception e) {
			PrintUtil.print("exception throws when getTableNumFromHashKey"
					+ e.getMessage(),Level.SEVERE);
		}
		return index;
	}
}
