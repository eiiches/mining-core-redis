package net.thisptr.util;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;

import redis.clients.jedis.Jedis;


public class SequentialRedisBackedIdMapperTest {
	@Test
	@Ignore
	public void test() {
		final String key = "test:" + UUID.randomUUID().toString();
		final Jedis jedis = new Jedis("localhost", 6379);
		final SequentialRedisBackedIdMapper mapper = new SequentialRedisBackedIdMapper(jedis, key);
		assertEquals(0, mapper.map("aa"));
		assertEquals(1, mapper.map("bb"));
		assertEquals(2, mapper.map("cc"));

		assertEquals(0, mapper.map("aa"));
		assertEquals(1, mapper.map("bb"));
		assertEquals(2, mapper.map("cc"));
		assertEquals(0, mapper.map("aa"));
		assertEquals(1, mapper.map("bb"));
		assertEquals(2, mapper.map("cc"));
	}
}
