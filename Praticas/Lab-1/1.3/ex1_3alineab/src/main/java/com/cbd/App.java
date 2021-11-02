package com.cbd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {

        // Set some users
        // Set
        System.out.println("SET");
        SimplePostSet board = new SimplePostSet();
        String[] users = { "Ana", "Pedro", "Maria", "Luis" };
        for (String user : users)
            board.saveUser(user);

        board.getAllKeys().stream().forEach(System.out::println);
        board.getUser().stream().forEach(System.out::println);

        // List
        System.out.println("LIST");
        List<String> usersList = new ArrayList<>();

        for (String string : users) {
            usersList.add(string);
        }

        SimplePostList boardList = new SimplePostList();
        for (String string : usersList) {
            boardList.saveUser(string);
        }

        boardList.getAllKeys().stream().forEach(System.out::println);
        boardList.getUser().stream().forEach(System.out::println);

        // Hash
        System.out.println("HASH");
        SimplePostHash boardHash = new SimplePostHash();
        
        Map<String, String> mapinha = new HashMap<>();
        mapinha.put("1", "Ana");
        mapinha.put("2", "Pedro");
        mapinha.put("3", "Maria");
        mapinha.put("4", "Luis");

        boardHash.saveUser(mapinha);

        Map<String, String> m = boardHash.getUser();
        for (String key : m.keySet()) {
            System.out.println(m.get(key));
        }
    }
}
