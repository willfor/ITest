package com.uc.jtest.assertion;

import static com.uc.jtest.config.JTestConstant.DATA_PATH;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.junit.Assert;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.uc.jtest.file.FileOperateUtil;
import com.uc.jtest.utils.JTestStringUtils;
import com.uc.jtest.utils.PrintUtil;

public class JsonAssertion {

    public static final String SPLITTER = "-";
    public static final String NEW_SPLITTER = ".";

    private String jsonString;
   

    /**
     * 预期结果存储在文件当中进行断言
     * 
     * @param actualJsonString
    
     * @param expectedJsonStr 要断言
    
     * @param excludes 要排除不断言列表
    
     * @return
    
     */
    public JsonAssertion assertEqualsWithExpectedJsonStringInFile(String actualJsonString,
            String expectedJsonStr, List<String> excludes) {
        JsonParser parser = new JsonParser();
        JsonElement actEle = parser.parse(actualJsonString);
        JsonElement expEle = parser.parse(expectedJsonStr);
        if (!actEle.equals(expEle)) {
            Assert.assertEquals(expEle.toString(), actEle.toString());
        }
        return this;
    }
    
    /**
     * 需求：
     * 1.把JsonString写入到文件中
     * 2.保证文件不存在时，断言失败
     * 捕获JUnit的AssertionError的原因:如果出现了断言失败，JUnit将会抛AssertionError，如果不捕获，将永远不会走到写文件那一步。
     * 
     * */
    public JsonAssertion assertEqualsWithFile() {
        String expectedFilePath = DATA_PATH
                + JTestStringUtils.getExpectedFileName(Thread.currentThread().getStackTrace()[2]
                        .toString());
        AssertionError actualError = null;
        try {
            assertEqualsWithExpectedJsonStringInFile(jsonString,
                    FileOperateUtil.readFile(expectedFilePath), null);
        } catch (AssertionError e) {
            actualError = e;
        }
        FileOperateUtil.writeToFileIfNotExist(expectedFilePath, jsonString);
        if (actualError != null) {
            throw actualError;
        }
        return this;
    }

    
   

    public JsonAssertion assertEqualsWithFile(List<String> excludes) {
        String expectedFilePath = DATA_PATH
                + JTestStringUtils.getExpectedFileName(Thread.currentThread().getStackTrace()[2]
                        .toString());
        AssertionError actualError = null;
        try {
            assertEqualsWithExpectedJsonStringInFile(jsonString, expectedFilePath, excludes);
        } catch (AssertionError e) {
            actualError = e;
        }
        FileOperateUtil.writeToFileIfNotExist(expectedFilePath, jsonString);
        if (actualError != null) {
            throw actualError;
        }
        return this;
    }

   

    public JsonAssertion assertEqualsWithFile(String fileName, List<String> excludes) {
        String expectedFilePath = DATA_PATH  + fileName;
        AssertionError actualError = null;
        try{
        assertEqualsWithExpectedJsonStringInFile(jsonString,
                FileOperateUtil.readFile(expectedFilePath),
                excludes);
        }catch(AssertionError e){
            actualError = e;
        }
        FileOperateUtil.writeToFileIfNotExist(expectedFilePath, jsonString);
        if (actualError != null) {
            throw actualError;
        }
        return this;
    }
    

    public JsonAssertion assertEqualsWithInFile(String srcJsonString, List<String> excludes) {
        String expectedFilePath = DATA_PATH
                + JTestStringUtils.getExpectedFileName(Thread
                        .currentThread().getStackTrace()[2].toString());
        AssertionError actualError = null;
        try{
         assertEqualsWithExpectedJsonStringInFile(
                srcJsonString,
                FileOperateUtil.readFile(expectedFilePath), excludes);
        }catch(AssertionError e){
            actualError = e;
        }
        FileOperateUtil.writeToFileIfNotExist(expectedFilePath, jsonString);
        if (actualError != null) {
            throw actualError;
        }
        return this;
    }
    

    /**
     * 预期结果为一个Map进行断言
     * 
     * @param key
     * @param expectedkeyAndValue
     * 
     * @return
     */
    public JsonAssertion assertEquals(String key, Map<String, Object> expectedkeyAndValue) {
        DataComparator.compareData(getValue(jsonString, key), expectedkeyAndValue);
        return this;
    }

    /*
     * 断言Json中不包含指定的Key
     */
    public JsonAssertion assertKeyNotExist(String key) {
        Assert.assertNull(getElement(jsonString, key));
        return this;
    }

    /*
     * 断言为空字符
     */
    public JsonAssertion assertEmpty(String key) {
        return assertEquals("", getValue(jsonString, key));
    }

    /*
     * 断言是否相等
     * @param key
     * @param expectedValue
     * @return
     * 跟Junit的规范保持一致，第一个参数为期望的结果，接二个参数为想要比较的Key
     * 
     * 
     * 兼容把Key和ExpectedValue写反的情况
     */
    public JsonAssertion assertEquals(Object expectedValue, String key) {
        String value = "";
        String tempExpectedValue = String.valueOf(expectedValue);
        try {
            try {
                value = getValue(this.jsonString, key);
            } catch (Exception e) {
                value = getValue(this.jsonString, String.valueOf(expectedValue));
                tempExpectedValue = key;
            }
        } catch (Exception e) {
            PrintUtil.print("Exception throws when expect key [" + key + " ] 's value was:["
                    + expectedValue + "] or key was [" + expectedValue + "] value was [" + key
                    + "]",Level.SEVERE);
        }
        Assert.assertEquals(tempExpectedValue, value);
        return this;
    }

    /**
     * 断言是否包含
     * @param key
     * @param expectedValue
     * @return
     */
    public JsonAssertion assertContains(String key, Object expectedValue) {
        Assert.assertTrue((getValue(jsonString, key)).contains(String.valueOf(expectedValue)));
        return this;
    }

    /**
     * 断言列表的长度
     * @param expectedSize
     * @param key
     * @return
     */
    public JsonAssertion assertSize(int expectedSize, String key) {
        Assert.assertEquals(expectedSize, getContentSize(jsonString, key));
        return this;
    }

    /*
     * 可以支持下面的格式：具体使用方法示例可以参考类JsonStringAssertionTest appTest.second.test params[0].userToken data.content[0].content[1].id
     */
    public static String getValue(String jsonString, String key) {
        JsonElement element = getElement(jsonString, key);
        if (element == null) {
            throw new UnsupportedOperationException("key :" + key + " 不存在！");
        }
        if (element.isJsonPrimitive()) {
            return element.getAsString();
        } else if (element.isJsonObject() || element.isJsonArray() || element.isJsonNull()) { // 兼容JsonNull的情况，modified by wanglh
            return element.toString();
        }

        return null;
    }

    private static JsonElement getElement(String jsonString, String key) {
        String[] keys = key.split("-");
        if (key.contains(".")) {
            keys = key.split("\\" + ".");
        }
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonString);
        
        for (int i = 0; i < keys.length; i++) {
            if (i + 1 > keys.length) {
                break;
            }
            String currentKey = keys[i];
            int tempIndex = -1;
            String tempKey = currentKey;
            if (currentKey.contains("[") && currentKey.contains("]")) {
                int start = currentKey.indexOf("[");
                int end = currentKey.indexOf("]");
                tempKey = currentKey.substring(0, start);
                tempIndex = Integer.valueOf(currentKey.substring(start + 1, end));
            }
            element = element.getAsJsonObject().get(tempKey);
            if (tempIndex > -1) {
                element = element.getAsJsonArray().get(tempIndex);
                if (element.isJsonObject()) {
                    element = element.getAsJsonObject();
                }
            }
        }
        return element;
    }

    public static JsonAssertion JsonAssert(String jsonString) {
        if(JTestStringUtils.isEmpty(jsonString)){
            throw new UnsupportedOperationException("JsonString 不能为空");
        }
        return new JsonAssertion(jsonString);
    }

    private JsonAssertion(String jsonString) {
        this.jsonString = jsonString;
    }

    public static String getJsonKey(String... keys) {
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append(NEW_SPLITTER);
        }
        return sb.toString();
    }

    private static int getContentSize(String jsonString, String key) {
        Object object = getElement(jsonString, key);
        JsonElement element = (JsonElement) object;
        if (element.isJsonArray()) {
            return element.getAsJsonArray().size();
        }
        return 1;
    }

}
