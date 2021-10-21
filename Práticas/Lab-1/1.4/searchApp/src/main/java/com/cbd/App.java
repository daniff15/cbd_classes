package com.cbd;

import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        File file = new File("./src/main/java/com/cbd/names.txt");

        SimplePostSet postSet = new SimplePostSet();
        Scanner scNames = null;
        
        try {
            scNames = new Scanner(file);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found!");
            System.exit(0);
        }
        while (scNames.hasNextLine()) {
            String name = scNames.nextLine();
            postSet.saveUser(name);
        }

        while (true) {
            System.out.print("Search for ('Enter' for quit): ");
            String name = sc.nextLine();
            if (name.equals("")) {
                System.out.println("Quiting...");
                System.exit(0);
            }

            Set<String> setinho = postSet.getUser(name);

            for (String s : setinho) {
                System.out.println(s);
            }

        }
    }
}
