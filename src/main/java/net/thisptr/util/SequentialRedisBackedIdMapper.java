package net.thisptr.util;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import redis.clients.jedis.Jedis;

public class SequentialRedisBackedIdMapper implements IdMapper<String> {
	private static final String REVKEY_SUFFIX = ":rev";

	private ConcurrentHashMap<String, Integer> cache = new ConcurrentHashMap<>();
	private Jedis jedis;
	private String key;
	private String revkey;

	public SequentialRedisBackedIdMapper(final Jedis jedis, final String key) {
		this.jedis = jedis;
		this.key = key;
		this.revkey = key + REVKEY_SUFFIX;
	}

	@Override
	public int size() {
		return jedis.hlen(key).intValue();
	}

	@Override
	public String reverse(int id) {
		return jedis.hget(revkey, String.valueOf(id));
	}

	@Override
	public int get(String obj) {
		final Integer id = cache.get(obj);
		if (id != null)
			return id;
		final String id_ = jedis.hget(key, obj);
		if (id_ != null)
			return Integer.parseInt(id_);
		return -1;
	}

	@Override
	public int map(String obj) {
		final Integer id = cache.get(obj);
		if (id != null)
			return id;
		final Long id_ = (Long) jedis.eval(""
				+ "local id = redis.call('hget', KEYS[1], KEYS[2]) "
				+ "if (id) then "
				+ "  return tonumber(id) "
				+ "else "
				+ "  local id = redis.call('hlen', KEYS[1]) "
				+ "  redis.call('hset', KEYS[1], KEYS[2], id) "
				+ "  redis.call('hset', KEYS[1] .. '" + REVKEY_SUFFIX + "', id, KEYS[2]) "
				+ "  return id "
				+ "end"
				+ "", Arrays.asList(key, obj), Arrays.<String>asList());
		cache.put(obj, id_.intValue());
		return id_.intValue();
	}

	@Override
	public Set<String> keySet() {
		return jedis.hkeys(key);
	}
}
