package com.uc.jtest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import jws.mvc.Http;

import org.junit.AfterClass;

import com.uc.jtest.assertion.DataComparator;
import com.uc.jtest.assertion.JsonAssertion;
import com.uc.jtest.cache.MemCacheUtil;
import com.uc.jtest.listener.TableInfoChangeListener;
import com.uc.jtest.table.DBUtil;
import com.uc.jtest.table.TableGroupInsertor;
import com.uc.jtest.table.TableInsertor;
import com.uc.jtest.table.template.TableInfo;
import com.uc.jtest.utils.PrintUtil;

public abstract class JTestBase extends JTestBaseWithRequest {

    public static final String UUID = "uuid";

    /*
     * 每个方法准备的数据放在内存中
     */
    protected Map<String, List<TableInfo>> tableNameAndTableInfos;

    /*
     * 根据表名获取准备的数据的某些列的值，如果多条，默认取第一条
     */
    public Map<String, Object> getInsertedData(String tableName, String... columns) {
        DataComparator compartor = new DataComparator(tableNameAndTableInfos);
        return compartor.getInsertedData(tableName, 0, columns);
    }

    /*
     * 根据表名获取准备的数据的某一行某些列的值
     */
    public Map<String, Object> getInsertedData(String tableName, int index, String... columns) {
        DataComparator compartor = new DataComparator(tableNameAndTableInfos);
        return compartor.getInsertedData(tableName, index, columns);
    }

    /*
     * 根据表名和表的Id获取准备的数据的某些列的值
     */
    public Map<String, Object> getInsertedDataById(String tableName, String idName, Object id,
            String... columns) {
        DataComparator compartor = new DataComparator(tableNameAndTableInfos);
        return compartor.getInsertedData(tableName, idName, id, columns);
    }

    /*
     * 
     * 根据表名，查询条件获取声明的列的值，如果多条，默认取第一条
     * */
    public Map<String, Object> getSelectedData(String tableName, String condition,
            String... columns) {
        return DataComparator.getSelectedData(tableName, condition, columns);
    }

    /*
     * 根据表名，查询条件获取声明的列的值
     * 
     * */
    public Map<String, Object> getSelectedData(String tableName, String condition, int index,
            String... columns) {
        return DataComparator.getSelectedData(tableName, condition, index, columns);
    }

    /*
     * 比较Map和JsonString
     */
    public void compareData(String sourceJsonString, Map<String, Object> expectedkeyAndValue) {
        DataComparator.compareData(sourceJsonString, expectedkeyAndValue);
    }
    
	public void expectPreparedDataAndDBDataSame(String tableName) {
		DataComparator compartor = new DataComparator(tableNameAndTableInfos);
		compartor.compareInsertedAndDBData(tableName);
	}

    /*
     * 
     * 映射关系，JsonString和DB列的
     */
    public void mappingSourceAndExpect(Map<String, Object> insertedData, String mapping) {
        DataComparator.mappingInsertAndResponse(insertedData, mapping);
    }

    /**
     * 子类如果需要在方法之前做一些清理动作，需要重载这个方法
     * 
     * */
    public void init() { 
        tableNameAndTableInfos = new HashMap<String, List<TableInfo>>();
        TableInfoChangeListener.getInstance().setTableNameAndTableInfos(tableNameAndTableInfos);
        MemCacheUtil.flushAll();
        PrintUtil.print("JTestBase.init将清除数据,清除缓存--(如果想清除模板配置的所有表的数据，可以调用 TableCleaner.deleteAll(\"*\") 或者 TableCleaner.truncate(\"*\"))",Level.INFO);
    }

    public abstract String getUrl();

    public String getConstructedUrl() {
        return "/api/" + this.getUrl();
    }

    public String post(String request) {
    	String content = this.post(getConstructedUrl(), request);
    	PrintUtil.print(content,Level.INFO);
        return content;
    }
    

    public TableInsertor table(String tableName) {
        return TableInsertor.table(tableName);
    }
    
    public TableGroupInsertor group(String groupName) {
        return TableGroupInsertor.tableGroup(groupName);
    }

    public JsonAssertion JsonAssert(String jsonString) {
        return JsonAssertion.JsonAssert(jsonString);
    }

    public String getDataElement(String key) {
        return getJsonKey("data", key);
    }

    public String getJsonKey(String... keys) {
        return JsonAssertion.getJsonKey(keys);
    }

    @AfterClass
    public static void tearDown() {
        DBUtil.getInstance().closeConnection();
    }

    public static final String JSON_CONTENTTYPE = "application/json; charset=utf-8";

    public String post(String url, String requestJsonStr) {
        return getContent(POST(url, JSON_CONTENTTYPE, requestJsonStr));
    }
    
	public String post(Http.Request request,Object url, String contenttype, String body) {
		return getContent(POST(request, url, contenttype, body));
	}
	
    

}
