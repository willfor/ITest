package com.uc.jtest.assertion;

import java.util.List;
import java.util.logging.Level;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.uc.jtest.utils.PrintUtil;


public class GsonExclusionStrategy implements ExclusionStrategy {

	private List<String> fieldList;
	
	public GsonExclusionStrategy(List<String> fieldList) {
		this.fieldList = fieldList;
	}
	
	public boolean shouldSkipField(FieldAttributes f) {
		boolean isSkippedField = fieldList.contains(f.getName());
		if(isSkippedField){
			PrintUtil.print(f.getName(),Level.INFO);
		}
		return isSkippedField;
	}

	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

}
