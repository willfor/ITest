package com.uc.jtest.utils;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.uc.jtest.assertion.DataComparator;
import com.uc.jtest.assertion.JsonAssertion;
import com.uc.jtest.table.TableInsertor;

public class TableDataInsertorTest {
    
  

    @Test
	public void test() {
		List<String> insertSqls = TableInsertor.table("android_app",false).value("id", "[1~5]").insert(5).getInsertSqls();
		for(int i=0;i<insertSqls.size();i++){
		    System.out.println(insertSqls.get(i));
		}
	}


    @Ignore
	public void test2() {
		TableInsertor.table("android_lingsheng").value("id", "[1~2]")
				.value("catid", 1).value("status", "[1,0]")
				.value("updatetime", "[1,3]").insert(2);
		Map<String, Object> selectedData = DataComparator.getSelectedData(
				"android_lingsheng", "status=1", "id", "title", "updatetime",
				"download_url");
		DataComparator.mappingInsertAndResponse(selectedData,
				"title:name,download_url:url,updatetime:updateTime");
		selectedData.put("url",
				"http://android-rings.25pp.com/" + selectedData.get("url"));
		String jsonString = "{\"data\":{\"totalCount\":1,\"content\":[{\"id\":1,\"name\":\"铃声title\",\"url\":\"http://android-rings.25pp.com/uploadfile/callring/1.mp3\",\"size\":1024,\"updateTime\":1,\"downloads\":5414229,\"duration\":10}]},\"id\":\"100\",\"state\":{\"code\":2000000,\"msg\":\"Ok\",\"tips\":\"\"}}";
		JsonAssertion.JsonAssert(jsonString).assertEquals("data.content[0]",
				selectedData);
	}
	
	@Ignore
	public void test1(){
		//排查为什么会多出两列
		//下面的将会抛出异常：package_id的写法应该为 [1,2,3]但是写法为1,2,3将会导致拼出来的列比实际的列要多
		TableInsertor.table("sync_app_check_safe").value("apk_md5", "[22e0aa974022bef1dd9333333d2bc000,22e0aa974022bef1dd9333333d2bc001,22e0aa974022bef1dd9333333d2bc002]")
        .value("package_id","1,2,3").value("status", "[1,1,1]").value("level", "[1,2,3]").value("virus_type","[0,1,2]").insert(3);
	}
	
	@Ignore
    public void testTrue(){
        Assert.assertTrue(true);
    }
}
