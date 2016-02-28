package com.uc.jtest.utils;

import java.util.HashMap;
import java.util.Map;

public class MapConstructor {
    private Map<String, String> mapper = new HashMap<String, String>();

    public static MapConstructor newMapConstructor() {
        return new MapConstructor();
    }

    public MapConstructor put(String key, String value) {
        mapper.put(key, value);
        return this;
    }

    public Map<String, String> get() {
        return mapper;
    }

}
