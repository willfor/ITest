package com.uc.jtest.utils;

import java.util.ArrayList;
import java.util.List;

public class JTestCollectionUtil {

	public static List<String> getList(String... values) {
		List<String> ignoreList = new ArrayList<String>();
		for (String str : values) {
			ignoreList.add(str.trim());
		}
		return ignoreList;
	}
	
	public static boolean isEmpty(List<String> values){
		return values ==null || values.size()==0;
	}
	
    public static List<byte[]> getByteList(byte[]... bodies) {
        List<byte[]> responses = new ArrayList<byte[]>();
        for (byte[] body : bodies) {
            responses.add(body);
        }
        return responses;
    }

}
