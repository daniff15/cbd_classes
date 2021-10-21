package com.cbd;

import java.util.Set;
import redis.clients.jedis.Jedis;

public class PostSetCSV {
    private Jedis jedis;
    
    public static String USERS = "names";

    public PostSetCSV() {
        this.jedis = new Jedis("localhost");
    }

    public void saveUser( String username,int score) {
        jedis.zadd(USERS, score, username);
    }

    public Set<String> getUser(String piece) {
        return jedis.zrevrange(USERS, 0 ,-1);
    }

    public Set<String> getAllKeys() {
        return jedis.keys("*");
    }

}
