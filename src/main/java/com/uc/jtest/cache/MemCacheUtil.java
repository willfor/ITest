package com.uc.jtest.cache;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import jws.cache.MemcachedImpl;
import jws.dal.cache.ConfigManager;
import jws.dal.common.McConfig;
import net.spy.memcached.MemcachedClient;
import jws.dal.cache.SerializeBase;
import jws.dal.cache.SerializeResult;
import jws.dal.common.Entity;
import jws.dal.common.LCache;
import jws.dal.manager.CacheManager;
import jws.dal.manager.EntityManager;

public class MemCacheUtil extends SerializeBase {

    public MemcachedClient getMemcachedClient(String prefix) {
        McConfig config = ConfigManager.getInstance().getConfig(prefix);
        return config.getClient().getClient();
    }

    public MemcachedClient getMemcachedClientByClientName(String clientName) {
        return ConfigManager.getInstance().getClients().get(clientName)
                .getClient();
    }

    /***
     * 获取某个KEY的值
     *
     * @param key
     * @return
     * @throws Exception
     */
    public Object get(String key, String prefix) throws Exception {
        Object obj = getMemcachedClient(prefix).get(key);
        return obj;
    }

    /***
     * 设置某个KEY的值，对value的值需要进行序列化操作
     *
     * @param key
     * @param value
     * @param expiration
     *            过期时间
     */
    public void set(String key, Object value, int expiration, String prefix) {
        getMemcachedClient(prefix).set(key, expiration, value);
    }

    /***
     * 删除某个KEY的缓存
     *
     * @param key
     */
    public void delete(String key, String prefix) {
        getMemcachedClient(prefix).delete(key);
    }

    /***
     * 获取cache的状态，返回需要的状态字段，如get_hits，即为get的成功命中次数
     *
     * @param stat
     * @return
     * @throws Exception
     */
    public int getStatusByKeyword(String stat, String prefix)
            throws Exception {
        Map<SocketAddress, Map<String, String>> map = getMemcachedClient(prefix)
                .getStats();
        Iterator<Entry<SocketAddress, Map<String, String>>> iterator = map
                .entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<SocketAddress, Map<String, String>> entry = iterator.next();
            // SocketAddress address = entry.getKey();
            Map<String, String> status = entry.getValue();
            Iterator<Entry<String, String>> statusIterator = status.entrySet()
                    .iterator();
            while (statusIterator.hasNext()) {
                Entry<String, String> innerentry = statusIterator.next();
                // Logger.info("key: %s, value: %s", innerentry.getKey(),
                // innerentry.getValue());
                if (innerentry.getKey().equals(stat)) {
                    int hit = Integer.parseInt(innerentry.getValue());
                    return hit;
                }
            }
        }
        return 0;
    }

    public int get_hits(String prefix) throws Exception {
        return getStatusByKeyword("get_hits", prefix);
    }

    public int cmd_get(String prefix) throws Exception {
        return getStatusByKeyword("cmd_get", prefix);
    }

    public int cmd_set(String prefix) throws Exception {
        return getStatusByKeyword("cmd_set", prefix);
    }

    public int curr_items(String prefix) throws Exception {
        return getStatusByKeyword("curr_items", prefix);
    }

    /**
     * clear all data
     *
     * @param prefix
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public boolean flushAllByPrefix(String prefix) throws InterruptedException,
            ExecutionException {
        return getMemcachedClient(prefix).flush().get();
    }
    
    /**
     * flush by client name
     * @param clientName
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public boolean flush(String clientName) throws InterruptedException,
        ExecutionException {
        return getMemcachedClientByClientName(clientName).flush().get();
    }

    /***
     * 序列化RowCache数据
     *
     * @param cl
     * @param obj
     * @return
     */
    public byte[] serializeObject(Class<?> cl, Object obj) {
        Entity entity = EntityManager.getInstance().getEntity(cl);
        return serialize(entity, obj);
    }

    /***
     * 反序列化RowCache数据
     *
     * @param cl
     * @param buff
     * @return
     */
    public SerializeResult<Object> deSerializeObject(Class<?> cl, byte[] buff) {
        Entity entity = EntityManager.getInstance().getEntity(cl);
        return deSerialize(entity, buff);
    }

    /***
     * 序列化ListCache数据
     *
     * @param list_name
     * @param tList
     * @return
     */
    public <T> byte[] serializeList(String list_name, List<T> tList) {
        LCache listcache = CacheManager.getInstance().getListcache(list_name);
        return serialize(listcache, tList);

    }

    /***
     * 反序列化ListCache数据
     *
     * @param list_name
     * @param buff
     * @return
     */
    public <T> SerializeResult<T> deSerializeList(String list_name,
            byte[] buff) {
        LCache listcache = CacheManager.getInstance().getListcache(list_name);
        return deSerialize(listcache, buff);
    }

    /***
     * 序列化count数据
     *
     * @param count
     * @param list_name
     * @return
     */
    public byte[] SerializeCount(int count, String list_name) {
        LCache listcache = CacheManager.getInstance().getListcache(list_name);
        return serialize(count, listcache);
    }
    
    /***
     * 反序列化count数据
     *
     * @param list_name
     * @return
     */
    public SerializeResult<Object> deSerializeCacheCount(String list_name, byte[] buff) {
        LCache listcache = CacheManager.getInstance().getListcache(list_name);
        return deSerializeCount(listcache, buff);
    }

    public <T> List<T> getListCache(String key, String listCacheName,
            String prefix) {
        Object objs = getMemcachedClient(prefix).get(key);
        if (objs == null) {
            return null;
        }
        SerializeResult<T> result = deSerializeList(listCacheName,
                (byte[]) objs);
        List<T> list = result.entityObjs;
        return list;
    }

    public <T> boolean setListCache(List<T> list, String key,
            String listCacheName, String prefix) {
        byte[] value = serializeList(listCacheName, list);
        return jws.dal.Cache.set(key, value, prefix);
    }
    
    public Object getObjectCache(String key, String prefix, Class<?> cls) {
        Object objs = getMemcachedClient(prefix).get(key);
        if (objs == null) {
            return null;
        }
        SerializeResult<?> result = deSerializeObject(cls, (byte[]) objs);
        return result.entityObj;
    }

    public <T> boolean setObjectCache(T t, String key, String prefix) {
        jws.dal.Cache.delete(key, prefix);
        return jws.dal.Cache.set(key, serializeObject(t.getClass(), t), prefix);
    }

    public boolean setCacheCount(int count, String key, String listCacheName,
            String prefix) {
        byte[] value = SerializeCount(count, listCacheName);
        return jws.dal.Cache.set(key, value, prefix);
    }
    
    public void deleteCache(String key, String prefix) {
        jws.dal.Cache.delete(key, prefix);
    }
    
    public Integer getCacheCount(String listCacheName, String key, String prefix) {
        Object objs = getMemcachedClient(prefix).get(key);
        if (objs == null) {
            return null;
        }
        SerializeResult<Object> result = deSerializeCacheCount(listCacheName,
                (byte[]) objs);
        return result.count;
    }


	public static void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {
			System.out.println("exception throws when sleep" + e.getMessage());
		}
	}

	public static void flushAll(String key) {
		McConfig config = ConfigManager.getInstance().getConfig(key);
		MemcachedClient client = config.getClient().getClient();
		client.flush();
	}

	public static void flushAll() {
		try {
			MemcachedImpl.getInstance().clear();
		} catch (Exception e) {
			System.out.println("test");
		}
	}

}
