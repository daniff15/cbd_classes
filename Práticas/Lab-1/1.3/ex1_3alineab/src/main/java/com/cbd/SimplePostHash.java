package com.cbd;

import redis.clients.jedis.Jedis;
import java.util.Map;
import java.util.Set;

class SimplePostHash {
    private Jedis jedis;
    public static String USERS = "usersHash";

    public SimplePostHash() {
        this.jedis = new Jedis("localhost");
    }

    public void saveUser(Map<String, String> m) {
        jedis.hmset(USERS, m);
    }

    public Map<String, String> getUser() {
        return jedis.hgetAll(USERS);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }
}