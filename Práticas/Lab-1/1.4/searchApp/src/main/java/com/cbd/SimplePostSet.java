package com.cbd;

import java.util.Set;
import redis.clients.jedis.Jedis;

public class SimplePostSet {
    private Jedis jedis;
    public static String USERS = "namesSet";

    public SimplePostSet() {
        this.jedis = new Jedis("localhost");
    }

    public void saveUser(String username) {
        jedis.zadd(USERS, 0, username);
    }

    public Set<String> getUser(String piece) {
        return jedis.zrangeByLex(USERS, "[" + piece, "[" + piece + (char)0xFF);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }
}
