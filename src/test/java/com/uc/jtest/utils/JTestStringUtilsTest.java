package com.uc.jtest.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
public class JTestStringUtilsTest {
	
	@Test
	public void test(){
		assertEquals("CheckPiratedTest_testSignatureIsNotSameAndLegal.properties",JTestStringUtils.constructResponsePropertiesFileName("citest.apitest.resource.app.CheckPiratedTest.testSignatureIsNotSameAndLegal(CheckPiratedTest.java:93)"));
	}
	
	
	

}
