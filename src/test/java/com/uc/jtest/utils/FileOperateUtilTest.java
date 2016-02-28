package com.uc.jtest.utils;

import org.junit.Test;

import com.uc.jtest.file.FileOperateUtil;

public class FileOperateUtilTest {
	
	@Test
	public void testA(){
		FileOperateUtil.writeToFileIfNotExist("test//resources//response//generated//apitest//" + "ddddee.txt" , "{\"data\":{\"nextOffset\":21,\"content\":[{\"id\":5535,\"name\":\"每周精选213\",\"adType\":16,\"tpData\":\"6009|662,1|系统,商城,2|系统,商城,3|系统,商城\",\"imageUrl\":\"http://img.25pp.com/uploadfile/poster/images/2014/1220/20141220052152636.png\",\"listorder\":10,\"positionNo\":10,\"resourceType\":13}]},\"id\":\"100\",\"state\":{\"code\":2000000,\"msg\":\"Ok\",\"tips\":\"\"}}");
	}
	
	@Test
	public void testB(){
	    //FileOperateUtil.createFilePath("test//resources//response//citest//apitest//resource//app//CheckPiratedTest//testSignatureIsNotSameAndLegal_generated.properties");
	}
	
	@Test
	public void testMakeDir(){
	    FileOperateUtil.createDir("test//resources//response//citest//apitest//resource");
	}
	

}
