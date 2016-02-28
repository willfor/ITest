package com.uc.jtest.table.template;

import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/*
 * 
 * all mysql db types 

 * 只有对Char 和Varchar 和Text进行了特殊处理
 * */
public enum DBType {

	Int, TinyInt, SmallInt, Mediumint, BigInt, Char, Varchar, Float, Double, Mediumtext,Text, DateTime, Blob;

	public static boolean isInt(String type) {
		return resolveDBType(type) == Int || resolveDBType(type) == TinyInt
				|| resolveDBType(type) == SmallInt
				|| resolveDBType(type) == Mediumint;
	}

	public static boolean isBigInt(String type) {
		return resolveDBType(type) == BigInt;
	}

	public static boolean isFloat(String type) {
		return resolveDBType(type) == Float;
	}

	public static boolean isDouble(String type) {
		return resolveDBType(type) == Double;
	}

	public static boolean isDateTime(String type) {
		return resolveDBType(type) == DateTime;
	}

	public static boolean isText(String type) {
		return Text == resolveDBType(type);
	}

	public static DBType resolveDBType(String type) {
		String actualType = type;
		if (type.contains("(")) {
			int index = type.indexOf("(");
			actualType = type.substring(0, index);
		}
		for (DBType dbType : DBType.values()) {
			if (actualType.toLowerCase().equals(dbType.name().toLowerCase())) {
				return dbType;
			}
		}
		return null;
	}

	public static boolean isStringRelatedType(String type) {
		return resolveDBType(type) == Char || resolveDBType(type) == Varchar
				|| resolveDBType(type) == Text || resolveDBType(type) == Mediumtext;
	}

	public static void main(String[] args) {
	    String content = "{\"status\":\"200004\",\"message\":\"Success\",\"data\":{\"service_ticket\":\"st580147052d4ae9b7dca9d938dc33da\",\"uid\":\"173772677\",\"nickname\":\"sandbox_zhangcl\"}";
        JsonParser parse = new JsonParser();
        JsonObject json = parse.parse(content).getAsJsonObject();
	}
	
	

}
