package com.uc.jtest.cache;

public interface ICacheOperation {

	void set(String key, Object value, int expiration, String prefix) throws Exception;

	void delete(String key, String prefix) throws Exception;

	Object get(String key, String prefix) throws Exception;

	Object get(String key, String prefix, Class<?> cls) throws Exception;

	void flush(String string) throws Exception;

}
