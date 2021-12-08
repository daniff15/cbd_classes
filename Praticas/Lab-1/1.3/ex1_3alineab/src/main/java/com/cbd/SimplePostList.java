package com.cbd;

import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class SimplePostList {

    private Jedis jedis;
    public static String USERS = "usersList";

    public SimplePostList() {
        this.jedis = new Jedis("localhost");
    }

    public void saveUser(String username) {
        jedis.rpush(USERS, username);
    }

    public List<String> getUser() {
        return jedis.lrange(USERS, 0, -1);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }

}