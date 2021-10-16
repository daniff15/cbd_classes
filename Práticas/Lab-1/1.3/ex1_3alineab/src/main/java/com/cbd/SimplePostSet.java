package com.cbd;

import java.util.Set;
import redis.clients.jedis.Jedis;

public class SimplePostSet {
    private Jedis jedis;
    public static String USERS = "usersSet";

    public SimplePostSet() {
        this.jedis = new Jedis("localhost");
    }

    public void saveUser(String username) {
        jedis.sadd(USERS, username);
    }

    public Set<String> getUser() {
        return jedis.smembers(USERS);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }
}
