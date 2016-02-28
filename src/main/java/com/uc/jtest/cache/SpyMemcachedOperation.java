package com.uc.jtest.cache;

import java.util.List;

import com.uc.jtest.utils.Logger;
import com.uc.jtest.utils.LoggerFactory;


public class SpyMemcachedOperation implements ICacheOperation {

	private MemCacheUtil clientUtils;
	
	public static final Logger logger = LoggerFactory.getLogger(SpyMemcachedOperation.class);
	
	public SpyMemcachedOperation() {
		clientUtils = new MemCacheUtil();
	}
	
	public void delete(String key, String prefix) {
		logger.info("delete: key = " + key + " , prefix:" + prefix);
		clientUtils.delete(prefix + key, prefix);
	}

	public Object get(String key, String prefix) throws Exception {
		Object result = clientUtils.get(prefix + key, prefix);
		logger.info("get: key = " + key + " , prefix:" + prefix + ", result :" + result);
		return result;
	}

	public void set(String key, Object value, int expiration, String prefix)
			throws Exception {
		logger.info("set: key = " + key + " , prefix:" + prefix + ", expiration :" + expiration);
		clientUtils.set(prefix + key, value, expiration, prefix);
	}

	public Object get(String key, String prefix, Class<?> cls) throws Exception {
		logger.info("get: key = " + key + " , prefix:" + prefix + ", cls :" + cls);
		return clientUtils.get(prefix + key, prefix);
	}

	public <T> void setListCache(List<T> list, String listCacheName, String key, String prefix) {
		logger.info("setListCache: key = " + key + " , prefix:" + prefix + ", listCacheName :" + listCacheName);
		clientUtils.setListCache(list, prefix + key, listCacheName, prefix);
	}
	
	public <T> List<T> getListCache(String listCacheName, String key, String prefix) {
		logger.info("getListCache: key = " + key + " , prefix:" + prefix + ", listCacheName :" + listCacheName);
		return clientUtils.getListCache(prefix + key, listCacheName, prefix);
	}

	public void setRowCache(Object newOb, String key, String prefix) {
		logger.info("setRowCache: key = " + key + " , prefix:" + prefix + ", newOb :" + newOb);
		clientUtils.setObjectCache(newOb, prefix + key, prefix);
	}
	
	public Object getRowCache(String key, String prefix, Class<?> cls) {
		logger.info("getRowCache: key = " + key + " , prefix:" + prefix + ", cls :" + cls);
		return clientUtils.getObjectCache(prefix + key, prefix, cls);
	}
	
//	public void deleteCache(String key, String prefix) {
//		logger.info("deleteCache: key = " + key + " , prefix:" + prefix );
//		clientUtils.deleteCache(prefix + key, prefix);
//	}

	public void flush(String name) throws Exception {
		boolean result = clientUtils.flush(name);
		logger.info("flush : name = " + name + ", result : " + result);
	}
	
	public void setCacheCount(int count, String listCacheName, String key, String prefix) {
		clientUtils.setCacheCount(count, prefix + key, listCacheName, prefix);
	}
	
	public int getCacheCount(String listCacheName, String key, String prefix) {
		return clientUtils.getCacheCount(listCacheName, prefix + key, prefix);
	}
	
}
