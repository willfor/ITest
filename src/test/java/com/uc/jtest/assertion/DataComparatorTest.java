package com.uc.jtest.assertion;

import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.uc.jtest.listener.TableInfoChangeListener;
import com.uc.jtest.table.TableCleaner;
import com.uc.jtest.table.TableInsertor;

public class DataComparatorTest {

    @Ignore
	public void test() {
		String jsonString = "{\"id\":2,\"name\":\"铃声title\",\"url\":\"http://android-rings.25pp.com/uploadfile/callring/1.mp3\",\"size\":1024,\"updateTime\":1,\"downloads\":40,\"duration\":11}";
		TableInsertor.table("android_lingsheng").value("id", 2)
				.value("title", "铃声title").insert();
		DataComparator compartor = new DataComparator(TableInfoChangeListener
				.getInstance().getTableNameAndTableInfos());
		Map<String, Object> insertedData = compartor.getInsertedData(
				"android_lingsheng", 0, "id", "title");
		DataComparator.mappingInsertAndResponse(insertedData,
				"title:name,url:aaa");
		DataComparator.compareData(jsonString, insertedData);
		Map<String, Object> selectedData = DataComparator.getSelectedData(
				"android_lingsheng", null, 0, "id", "title");
		DataComparator.mappingInsertAndResponse(selectedData,
				"title:name,url:aaa");
		DataComparator.compareData(jsonString, selectedData);
		TableCleaner.deleteAll("android_lingsheng");
	}

    @Test
	public void testA() {
		Map<String, Object> s = DataComparator.getSelectedData("android_app", "id=113");
		System.out.println(s);
	}
	
	@Test
	public void testB(){
	    TableInsertor.table("android_app_collection").value("user_id", "1111")
        .value("data_id", "11").value("res_type", "2")
        .value("account_type", 1).insert();
	    DataComparator compartor = new DataComparator(TableInfoChangeListener
                .getInstance().getTableNameAndTableInfos());
        compartor.compareInsertedAndDBData("android_app_collection");
	}
	
	@Test
	public void testTrue(){
	    Assert.assertTrue(true);
	}

}
