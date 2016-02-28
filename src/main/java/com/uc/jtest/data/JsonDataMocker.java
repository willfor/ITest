package com.uc.jtest.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonDataMocker {

    private JsonObject rootJsonObject;
    public static final String DEFAULT_ID = "100";

    public static JsonDataMocker jsonMocker() {
        return new JsonDataMocker();
    }

    public JsonDataMocker addToRoot(String key, Object value) {
        rootJsonObject = addToObject(key, value, rootJsonObject);
        return this;
    }

    private JsonDataMocker() {
        rootJsonObject = new JsonObject();
    }

    public JsonDataMocker id(String id) {
        rootJsonObject.add("id", new JsonPrimitive(id));
        return this;
    }

    public JsonDataMocker state(int code, String msg) {
        JsonObject statObject = (rootJsonObject.get("state") == null) ? new JsonObject()
                : rootJsonObject.get("state").getAsJsonObject();
        statObject.add("code", new JsonPrimitive(code));
        statObject.add("msg", new JsonPrimitive(msg));
        rootJsonObject.add("state", statObject);
        return this;
    }

    public JsonDataMocker addListDataToData(String key, String value) {
        JsonElement dataJe = rootJsonObject.get("data");
        rootJsonObject.add("data", addListData(key, value, dataJe));
        return this;
    }

    public JsonDataMocker addListPrimitiveDataToData(String key, String... values) {
        JsonElement dataJe = rootJsonObject.get("data");
        rootJsonObject.add("data", addListPrimitiveData(key, dataJe, values));
        return this;
    }
    
    public JsonDataMocker addToData(String parentKey,String key,String value){
        JsonElement dataJe = rootJsonObject.get(parentKey);
        rootJsonObject.add(parentKey, addToObject(key, value, dataJe));
        return this;
    }

    public JsonDataMocker addToData(String key, String value) {
        JsonElement dataJe = rootJsonObject.get("data");
        rootJsonObject.add("data", addToObject(key, value, dataJe));
        return this;
    }

    public String getResponseString() {
        return rootJsonObject.toString();
    }
    
    public static  JsonObject addListData(String key, String value, JsonElement je) {
        JsonObject jo = null;
        if (je != null) {
            jo = je.getAsJsonObject();
        } else {
            jo = new JsonObject();
        }
        String[] keys = key.split("\\" + ".");
        int tempIndex = -1;
        String parentKey = keys[0];
        String tempKey = parentKey;
        if (parentKey.contains("[") && parentKey.contains("]")) {
            int start = parentKey.indexOf("[");
            int end = parentKey.indexOf("]");
            tempKey = parentKey.substring(0, start);
            tempIndex = Integer.valueOf(parentKey.substring(start + 1, end));
        }
        JsonElement currentElement = jo.get(tempKey);
        if (currentElement == null) {
            if (tempIndex > -1) {
                currentElement = new JsonArray();
            } else {
                currentElement = new JsonObject();
            }
        }
        if(currentElement.isJsonArray()){
            if (currentElement.getAsJsonArray()!= null && currentElement.getAsJsonArray().size()>tempIndex) {
                currentElement.getAsJsonArray().get(tempIndex).getAsJsonObject()
                        .add(keys[1], new JsonPrimitive(value));
            }else{
                JsonElement leafObject = new JsonObject();
                leafObject.getAsJsonObject().add(keys[1], new JsonPrimitive(value));
                currentElement.getAsJsonArray().add(leafObject);
            }
        }else{
            currentElement.getAsJsonObject().add(keys[1], new JsonPrimitive(value));
        }
        jo.add(tempKey, currentElement);
        return jo;
    }
    
    public static  JsonObject addListPrimitiveData(String key, JsonElement je, String... values) {
        JsonObject jo = null;
        if (je != null) {
            jo = je.getAsJsonObject();
        } else {
            jo = new JsonObject();
        }
        JsonArray ja = new JsonArray();
        for (String value : values) {
            JsonPrimitive jev = new JsonPrimitive(value);
            ja.add(jev);
        }
        jo.add(key, ja);
        return jo;
    }
    
    public static JsonObject addToObject(String key, Object value, JsonElement je) {
        JsonObject jo = null;
        if (je != null) {
            jo = je.getAsJsonObject();
        } else {
            jo = new JsonObject();
        }
        jo.addProperty(key, String.valueOf(value));
        return jo;
    }


}
