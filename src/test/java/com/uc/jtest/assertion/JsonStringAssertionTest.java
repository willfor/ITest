package com.uc.jtest.assertion;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.uc.jtest.file.FileOperateUtil;

public class JsonStringAssertionTest {
    /**
     * 断言异常
     * */
    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Test
    public void testJsonStringIsNull(){
        exception.expect(UnsupportedOperationException.class);
        JsonAssertion.JsonAssert(null);
    }
    
    @Test
    public void testDataIsNull(){
        String jsonString = "{\"data\":[null,null],\"id\":100}";
        JsonAssertion.JsonAssert(jsonString).assertEquals(null, "data[0]").assertEquals(null, "data[1]");
    }
    
    @Test
    public void testKeyNotExist(){
        String jsonString = "{\"data\":{\"totalCount\":2,\"content\":[{\"id\":6501,\"resourceType\":0,\"packageName\":\"com.lanrenzhoumo.weekend\",\"name\":\"QQ\",\"categoryId\":10001,\"categoryName\":\"ͨѶ�罻\",\"versionName\":\"1.0.1\",\"versionCode\":11,\"size\":7557,\"downloadUrl\":\"http://android-apps.25pp.com/fs01/2014/11/11/2_5b5561e81dde429559b8eb30024e3cce.apk?yingid\u003dpp_client\u0026packageid\u003d500000088\u0026md5\u003d22e0aa974022bef1dd9333333d2bc214\u0026minSDK\u003d9\u0026size\u003d7735069\",\"iconUrl\":\"http://android-artworks.25pp.com/fs01/2014/11/11/2_ea6f3690ea6c8becf4cf4a342a7aeb73.png\",\"downloads\":551,\"versionId\":500000088,\"commentCount\":0,\"score\":0.0,\"safeStatus\":0,\"hotLevel\":0,\"editorRecommend\":\"�й�������Ӱ�����ʹ����Ե���������Ů���֡������ˣ�������̳��������\",\"vDownloads\":0,\"searchCount\":0,\"yrank\":0,\"dbyrank\":0,\"risingrate\":0.0,\"isSignificant\":0},{\"id\":6502,\"resourceType\":0,\"packageName\":\"com.lanrenzhoumo.weekend\",\"name\":\"������ĩ\",\"categoryId\":10001,\"categoryName\":\"ͨѶ�罻\",\"versionName\":\"1.0.1\",\"versionCode\":11,\"size\":7557,\"downloadUrl\":\"http://android-apps.25pp.com/fs01/2014/11/11/2_5b5561e81dde429559b8eb30024e3cce.apk?yingid\u003dpp_client\u0026packageid\u003d500000088\u0026md5\u003d22e0aa974022bef1dd9333333d2bc214\u0026minSDK\u003d9\u0026size\u003d7735069\",\"iconUrl\":\"http://android-artworks.25pp.com/fs01/2014/11/11/2_ea6f3690ea6c8becf4cf4a342a7aeb73.png\",\"downloads\":552,\"versionId\":500000088,\"commentCount\":0,\"score\":0.0,\"safeStatus\":0,\"hotLevel\":0,\"editorRecommend\":\"�й�������Ӱ�����ʹ����Ե���������Ů���֡������ˣ�������̳��������\",\"vDownloads\":0,\"searchCount\":0,\"yrank\":0,\"dbyrank\":0,\"risingrate\":0.0,\"isSignificant\":0}]},\"id\":\"100\",\"state\":{\"code\":2000000,\"msg\":\"Ok\",\"tips\":\"\"}}";
        JsonAssertion.JsonAssert(jsonString).assertKeyNotExist("data.content[0].test");
    }
   
    @Test
    public void testTwoArrays() {
        String complicateIndex = "{\"data\":{\"totalCount\":1,\"content\":[{\"id\":1,\"name\":\"名字\",\"totalCount\":4,\"content\":[{\"id\":4,\"name\":\"anky\",\"iconUrl\":\"http://s1.app.uc.cn/asset/bq/1432_1421462980968/icon.png\",\"resourceType\":12,\"type\":1},{\"id\":3,\"name\":\"anky\",\"iconUrl\":\"http://s1.app.uc.cn/asset/bq/1432_1421462980968/icon.png\",\"resourceType\":12,\"type\":1},{\"id\":1,\"name\":\"anky\",\"iconUrl\":\"http://s1.app.uc.cn/asset/bq/1431_1421462980968/icon.png\",\"resourceType\":12,\"type\":1}],\"order\":1}],\"resourceType\":12},\"id\":\"100\",\"state\":{\"code\":2000000,\"msg\":\"Ok\",\"tips\":\"\"}}";
        JsonAssertion.JsonAssert(complicateIndex).assertEquals(3, "data.content[0].content[1].id");
    }

    @Test
    public void testOneArrayOnePrimitive() {
        String jsonString = "{\"params\":[{\"userToken\":\"token1\",\"uuid\":\"id1\"},{\"userToken\":\"token2\",\"uuid\":\"id2\"}],\"key\":\"test\"}";
        JsonAssertion.JsonAssert(jsonString).assertEquals("token1", "params[0].userToken");
        JsonAssertion.JsonAssert(jsonString).assertEquals("token2", "params[1].userToken");
        JsonAssertion.JsonAssert(jsonString).assertEquals("test", "key");
    }
    
    @Test
    public void testEqualsWithFile(){
        String filePath = "test//resources//data//com//uc//jtest//assertion//JsonStringAssertionTest//testCompareContentInFile_expected.txt";
        String srcJsonString = "{\"data\":{\"nextOffset\":21,\"content\":[{\"id\":1,\"name\":\"推荐集-专题\",\"adType\":0,\"imageUrl\":\"http://img.25pp.com/uploadfile/poster/images/2014/1220/20141220052152636.png\",\"listorder\":0,\"positionNo\":0,\"resourceType\":13},{\"id\":2,\"name\":\"推荐集-专题\",\"adType\":1,\"imageUrl\":\"http://img.25pp.com/uploadfile/poster/images/2014/1220/20141220052152636.png\",\"listorder\":0,\"positionNo\":0,\"resourceType\":13},{\"id\":3,\"name\":\"推荐集-专题\",\"adType\":4,\"imageUrl\":\"http://img.25pp.com/uploadfile/poster/images/2014/1220/20141220052152636.png\",\"listorder\":0,\"positionNo\":0,\"resourceType\":13},{\"id\":4,\"name\":\"推荐集-专题\",\"adType\":14,\"imageUrl\":\"http://img.25pp.com/uploadfile/poster/images/2014/1220/20141220052152636.png\",\"listorder\":0,\"positionNo\":0,\"resourceType\":13},{\"id\":5,\"name\":\"推荐集-专题\",\"adType\":10,\"imageUrl\":\"http://img.25pp.com/uploadfile/poster/images/2014/1220/20141220052152636.png\",\"listorder\":0,\"positionNo\":0,\"resourceType\":13},{\"id\":6,\"name\":\"推荐集-专题\",\"adType\":12,\"imageUrl\":\"http://img.25pp.com/uploadfile/poster/images/2014/1220/20141220052152636.png\",\"listorder\":0,\"positionNo\":0,\"resourceType\":13},{\"id\":7,\"name\":\"推荐集-专题\",\"adType\":11,\"imageUrl\":\"http://img.25pp.com/uploadfile/poster/images/2014/1220/20141220052152636.png\",\"listorder\":0,\"positionNo\":0,\"resourceType\":13},{\"id\":8,\"name\":\"推荐集-专题\",\"adType\":16,\"imageUrl\":\"http://img.25pp.com/uploadfile/poster/images/2014/1220/20141220052152636.png\",\"listorder\":0,\"positionNo\":0,\"resourceType\":13},{\"id\":9,\"name\":\"推荐集-专题\",\"adType\":15,\"imageUrl\":\"http://img.25pp.com/uploadfile/poster/images/2014/1220/20141220052152636.png\",\"listorder\":0,\"positionNo\":0,\"resourceType\":13}]},\"id\":\"100\",\"state\":{\"code\":2000000,\"msg\":\"Ok\",\"tips\":\"\"}}";
        Assert.assertTrue(FileOperateUtil.deleteIfExist(filePath));
        Assert.assertFalse(FileOperateUtil.isExist(filePath));
        try{
            JsonAssertion.JsonAssert(srcJsonString).assertEqualsWithFile();
        }catch(AssertionError e){
            Assert.assertTrue(true);
        }
        //Assert.assertTrue(FileOperateUtil.isExist(filePath));
        //JsonAssertion.JsonAssert(srcJsonString).assertEqualsWithFile();
    }

    @Test
    public void testThreeLayerStructure() {
        String jsonString = "{\"appTest\":{\"second\":{\"test\":\"bbbddd\",\"name\":\"nameaaa\"}}}";
        JsonAssertion.JsonAssert(jsonString).assertEquals("bbbddd", "appTest.second.test");
        JsonAssertion.JsonAssert(jsonString).assertEquals("nameaaa", "appTest.second.name");
    }
    
    @Test
    public void testNotLeafNodeContent(){
        String jsonString = "{\"appTest\":{\"second\":{\"test\":\"bbbddd\",\"name\":\"nameaaa\"}}}";
        JsonAssertion.JsonAssert(jsonString).assertEquals("{\"test\":\"bbbddd\",\"name\":\"nameaaa\"}", "appTest.second");
    }
    
    @Test
    public void testGetMultipleTimes(){
       String jsonString =  "{\"data\":{\"totalCount\":2,\"content\":[{\"id\":6501,\"resourceType\":0,\"packageName\":\"com.lanrenzhoumo.weekend\",\"name\":\"QQ\",\"categoryId\":10001,\"categoryName\":\"ͨѶ�罻\",\"versionName\":\"1.0.1\",\"versionCode\":11,\"size\":7557,\"downloadUrl\":\"http://android-apps.25pp.com/fs01/2014/11/11/2_5b5561e81dde429559b8eb30024e3cce.apk?yingid\u003dpp_client\u0026packageid\u003d500000088\u0026md5\u003d22e0aa974022bef1dd9333333d2bc214\u0026minSDK\u003d9\u0026size\u003d7735069\",\"iconUrl\":\"http://android-artworks.25pp.com/fs01/2014/11/11/2_ea6f3690ea6c8becf4cf4a342a7aeb73.png\",\"downloads\":551,\"versionId\":500000088,\"commentCount\":0,\"score\":0.0,\"safeStatus\":0,\"hotLevel\":0,\"editorRecommend\":\"�й�������Ӱ�����ʹ����Ե���������Ů���֡������ˣ�������̳��������\",\"vDownloads\":0,\"searchCount\":0,\"yrank\":0,\"dbyrank\":0,\"risingrate\":0.0,\"isSignificant\":0},{\"id\":6502,\"resourceType\":0,\"packageName\":\"com.lanrenzhoumo.weekend\",\"name\":\"������ĩ\",\"categoryId\":10001,\"categoryName\":\"ͨѶ�罻\",\"versionName\":\"1.0.1\",\"versionCode\":11,\"size\":7557,\"downloadUrl\":\"http://android-apps.25pp.com/fs01/2014/11/11/2_5b5561e81dde429559b8eb30024e3cce.apk?yingid\u003dpp_client\u0026packageid\u003d500000088\u0026md5\u003d22e0aa974022bef1dd9333333d2bc214\u0026minSDK\u003d9\u0026size\u003d7735069\",\"iconUrl\":\"http://android-artworks.25pp.com/fs01/2014/11/11/2_ea6f3690ea6c8becf4cf4a342a7aeb73.png\",\"downloads\":552,\"versionId\":500000088,\"commentCount\":0,\"score\":0.0,\"safeStatus\":0,\"hotLevel\":0,\"editorRecommend\":\"�й�������Ӱ�����ʹ����Ե���������Ů���֡������ˣ�������̳��������\",\"vDownloads\":0,\"searchCount\":0,\"yrank\":0,\"dbyrank\":0,\"risingrate\":0.0,\"isSignificant\":0}]},\"id\":\"100\",\"state\":{\"code\":2000000,\"msg\":\"Ok\",\"tips\":\"\"}}";
       JsonAssertion.JsonAssert(jsonString).assertEquals(2, "data.totalCount");
       JsonAssertion.JsonAssert(jsonString).assertEquals(6501, "data.content[0].id");
       JsonAssertion.JsonAssert(jsonString).assertEquals(6502, "data.content[1].id");
    }
    
    @Test
    public void testAssertSize(){
       String jsonString =  "{\"data\":{\"totalCount\":2,\"content\":[{\"id\":6501,\"resourceType\":0,\"packageName\":\"com.lanrenzhoumo.weekend\",\"name\":\"QQ\",\"categoryId\":10001,\"categoryName\":\"ͨѶ�罻\",\"versionName\":\"1.0.1\",\"versionCode\":11,\"size\":7557,\"downloadUrl\":\"http://android-apps.25pp.com/fs01/2014/11/11/2_5b5561e81dde429559b8eb30024e3cce.apk?yingid\u003dpp_client\u0026packageid\u003d500000088\u0026md5\u003d22e0aa974022bef1dd9333333d2bc214\u0026minSDK\u003d9\u0026size\u003d7735069\",\"iconUrl\":\"http://android-artworks.25pp.com/fs01/2014/11/11/2_ea6f3690ea6c8becf4cf4a342a7aeb73.png\",\"downloads\":551,\"versionId\":500000088,\"commentCount\":0,\"score\":0.0,\"safeStatus\":0,\"hotLevel\":0,\"editorRecommend\":\"�й�������Ӱ�����ʹ����Ե���������Ů���֡������ˣ�������̳��������\",\"vDownloads\":0,\"searchCount\":0,\"yrank\":0,\"dbyrank\":0,\"risingrate\":0.0,\"isSignificant\":0},{\"id\":6502,\"resourceType\":0,\"packageName\":\"com.lanrenzhoumo.weekend\",\"name\":\"������ĩ\",\"categoryId\":10001,\"categoryName\":\"ͨѶ�罻\",\"versionName\":\"1.0.1\",\"versionCode\":11,\"size\":7557,\"downloadUrl\":\"http://android-apps.25pp.com/fs01/2014/11/11/2_5b5561e81dde429559b8eb30024e3cce.apk?yingid\u003dpp_client\u0026packageid\u003d500000088\u0026md5\u003d22e0aa974022bef1dd9333333d2bc214\u0026minSDK\u003d9\u0026size\u003d7735069\",\"iconUrl\":\"http://android-artworks.25pp.com/fs01/2014/11/11/2_ea6f3690ea6c8becf4cf4a342a7aeb73.png\",\"downloads\":552,\"versionId\":500000088,\"commentCount\":0,\"score\":0.0,\"safeStatus\":0,\"hotLevel\":0,\"editorRecommend\":\"�й�������Ӱ�����ʹ����Ե���������Ů���֡������ˣ�������̳��������\",\"vDownloads\":0,\"searchCount\":0,\"yrank\":0,\"dbyrank\":0,\"risingrate\":0.0,\"isSignificant\":0}]},\"id\":\"100\",\"state\":{\"code\":2000000,\"msg\":\"Ok\",\"tips\":\"\"}}";
       JsonAssertion.JsonAssert(jsonString).assertSize(2, "data.content");
    }
    
    
   
    

  
    
    @Test
    public void testAssertEquals(){
    	String jsonString = "{\"data\":{\"nextOffset\":-1,\"content\":[{\"id\":301,\"resourceType\":1,\"packageName\":\"com.lanrenzhoumo.weekend\",\"name\":\"UC\",\"categoryId\":6002,\"categoryName\":\"ͨѶ�罻\",\"versionName\":\"1.0.1\",\"versionCode\":11,\"size\":7557,\"downloadUrl\":\"http://android-apps.25pp.com/fs01/2014/11/11/2_5b5561e81dde429559b8eb30024e3cce.apk?yingid\u003dpp_client\u0026packageid\u003d500000088\u0026md5\u003d22e0aa974022bef1dd9333333d2bc214\u0026minSDK\u003d9\u0026size\u003d7735069\",\"iconUrl\":\"http://android-artworks.25pp.com/fs01/2014/11/11/2_ea6f3690ea6c8becf4cf4a342a7aeb73.png\",\"downloads\":331,\"updateTime\":1423021863000,\"versionId\":500000088,\"hotLevel\":0,\"editorRecommend\":\"�й�������Ӱ�����ʹ����Ե���������Ů���֡������ˣ�������̳��������\",\"appSource\":0,\"searchCount\":0,\"yrank\":0,\"dbyrank\":0,\"risingrate\":0.0,\"isSignificant\":0},{\"id\":300,\"resourceType\":1,\"packageName\":\"com.lanrenzhoumo.weekend\",\"name\":\"PP Assistant\",\"categoryId\":6001,\"categoryName\":\"ͨѶ�罻\",\"versionName\":\"1.0.1\",\"versionCode\":11,\"size\":7557,\"downloadUrl\":\"http://android-apps.25pp.com/fs01/2014/11/11/2_5b5561e81dde429559b8eb30024e3cce.apk?yingid\u003dpp_client\u0026packageid\u003d500000088\u0026md5\u003d22e0aa974022bef1dd9333333d2bc214\u0026minSDK\u003d9\u0026size\u003d7735069\",\"iconUrl\":\"http://android-artworks.25pp.com/fs01/2014/11/11/2_ea6f3690ea6c8becf4cf4a342a7aeb73.png\",\"downloads\":330,\"updateTime\":1423021862000,\"versionId\":500000088,\"hotLevel\":0,\"editorRecommend\":\"�й�������Ӱ�����ʹ����Ե���������Ů���֡������ˣ�������̳��������\",\"appSource\":0,\"searchCount\":0,\"yrank\":0,\"dbyrank\":0,\"risingrate\":0.0,\"isSignificant\":0}]},\"id\":\"100\",\"state\":{\"code\":2000000,\"msg\":\"Ok\",\"tips\":\"\"}}";
    	JsonAssertion.JsonAssert(jsonString).assertEquals("data.nextOffset","-1")
        .assertEquals(1,"data.content[0].resourceType");
    }
    
    
    
    @Test
    public void testAssertEqualsException(){
    	String jsonString = "{\"data\":{\"nextOffset\":-1,\"content\":[{\"id\":301,\"resourceType\":1,\"packageName\":\"com.lanrenzhoumo.weekend\",\"name\":\"UC\",\"categoryId\":6002,\"categoryName\":\"ͨѶ�罻\",\"versionName\":\"1.0.1\",\"versionCode\":11,\"size\":7557,\"downloadUrl\":\"http://android-apps.25pp.com/fs01/2014/11/11/2_5b5561e81dde429559b8eb30024e3cce.apk?yingid\u003dpp_client\u0026packageid\u003d500000088\u0026md5\u003d22e0aa974022bef1dd9333333d2bc214\u0026minSDK\u003d9\u0026size\u003d7735069\",\"iconUrl\":\"http://android-artworks.25pp.com/fs01/2014/11/11/2_ea6f3690ea6c8becf4cf4a342a7aeb73.png\",\"downloads\":331,\"updateTime\":1423021863000,\"versionId\":500000088,\"hotLevel\":0,\"editorRecommend\":\"�й�������Ӱ�����ʹ����Ե���������Ů���֡������ˣ�������̳��������\",\"appSource\":0,\"searchCount\":0,\"yrank\":0,\"dbyrank\":0,\"risingrate\":0.0,\"isSignificant\":0},{\"id\":300,\"resourceType\":1,\"packageName\":\"com.lanrenzhoumo.weekend\",\"name\":\"PP Assistant\",\"categoryId\":6001,\"categoryName\":\"ͨѶ�罻\",\"versionName\":\"1.0.1\",\"versionCode\":11,\"size\":7557,\"downloadUrl\":\"http://android-apps.25pp.com/fs01/2014/11/11/2_5b5561e81dde429559b8eb30024e3cce.apk?yingid\u003dpp_client\u0026packageid\u003d500000088\u0026md5\u003d22e0aa974022bef1dd9333333d2bc214\u0026minSDK\u003d9\u0026size\u003d7735069\",\"iconUrl\":\"http://android-artworks.25pp.com/fs01/2014/11/11/2_ea6f3690ea6c8becf4cf4a342a7aeb73.png\",\"downloads\":330,\"updateTime\":1423021862000,\"versionId\":500000088,\"hotLevel\":0,\"editorRecommend\":\"�й�������Ӱ�����ʹ����Ե���������Ů���֡������ˣ�������̳��������\",\"appSource\":0,\"searchCount\":0,\"yrank\":0,\"dbyrank\":0,\"risingrate\":0.0,\"isSignificant\":0}]},\"id\":\"100\",\"state\":{\"code\":2000000,\"msg\":\"Ok\",\"tips\":\"\"}}";
    	//JsonAssertion.JsonAssert(jsonString).assertEquals("data.s","-1");
    }
   
    
    
    @Test
    public void testAssertEmpty(){
    	String jsonString = "{\"data\":{\"nextOffset\":-1,\"content\":[]},\"id\":\"100\",\"state\":{\"code\":2000000,\"msg\":\"Ok\",\"tips\":\"\"}}";
    	JsonAssertion.JsonAssert(jsonString).assertEquals("data.content","[]");
    }
    
    
    @Test
    public void testAssertEqualsWithFileContent(){
    	String jsonString = "{\"data\":{\"nextOffset\":21,\"content\":[{\"id\":5535,\"adType\":16,\"name\":\"ÿ�ܾ�ѡ213\",\"tpData\":\"6009|662,1|ϵͳ,�̳�,2|ϵͳ,�̳�,3|ϵͳ,�̳�\",\"imageUrl\":\"http://img.25pp.com/uploadfile/poster/images/2014/1220/20141220052152636.png\",\"listorder\":10,\"positionNo\":10,\"resourceType\":13}]},\"id\":\"100\",\"state\":{\"code\":2000000,\"msg\":\"Ok\",\"tips\":\"\"}}";
    	//FileOperateUtil.writeToFileIfNotExist(JsonAssertion.DATA_PATH + JTestStringUtils.getExpectedFileName(Thread.currentThread().getStackTrace()[1].toString()), jsonString);
    	//JsonAssertion.JsonAssert(jsonString).assertEqualsWithFile();
    }
    
    @Ignore
    public void testE(){
		String json1 = FileOperateUtil.readFile("test//resources//response//generated//ListWithoutAdsTest_nextOffSetTest.properties");
		String json2 = FileOperateUtil.readFile("test//resources//response//expected//ListWithoutAdsTest_nextOffSetTest.properties");
		System.out.println(json1);
    	System.out.println(json2);
		//JsonAssertion.JsonAssert(json1)
    	//.assertEqualsWithExpectedJsonStringInFile(json1,json2,JTestCollectionUtil.getList("updateTime"));
    	
    }
    
    
	
	

    @Ignore
    public void testAssertEqualsWithFileContentA() {
        String jsonString = "{\"data\":{\"nextOffset\":21,\"content\":[{\"id\":5535,\"adType\":16,\"name\":\"ÿ�ܾ�ѡ213\",\"tpData\":\"6009|662,1|ϵͳ,�̳�,2|ϵͳ,�̳�,3|ϵͳ,�̳�\",\"imageUrl\":\"http://img.25pp.com/uploadfile/poster/images/2014/1220/20141220052152636.png\",\"listorder\":10,\"positionNo\":10,\"resourceType\":13}]},\"id\":\"100\",\"state\":{\"code\":2000000,\"msg\":\"Ok\",\"tips\":\"\"}}";
    } 
            
        
        
	
	
	
	
}
