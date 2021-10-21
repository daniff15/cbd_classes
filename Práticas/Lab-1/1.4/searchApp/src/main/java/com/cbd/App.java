package com.cbd;

import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        File file = new File("./src/main/java/com/cbd/names.txt");

        SimplePostSet postSet = new SimplePostSet();
        Scanner scNames = null;

        // Aline a
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
                break;
            }

            Set<String> setinho = postSet.getUser(name);

            for (String s : setinho) {
                System.out.println(s);
            }

        }

        // Alinea b

        PostSetCSV postCSV = new PostSetCSV();

        Scanner scCSV = new Scanner(System.in);

        try {
            FileReader fr = new FileReader("./src/main/java/com/cbd/nomes-pt-2021.csv");
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                postCSV.saveUser(data[0], Integer.parseInt(data[1]));
            }
            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        while (true) {
            System.out.print("Search for ('Enter') for quit): ");
            String name = scCSV.nextLine();

            if (name.equals(""))
                break;

            Set<String> setinhoCSV = postCSV.getUser(name);
            for (String string : setinhoCSV)
                if (string.toLowerCase().matches(name + "(.*)"))
                    System.out.println(string);
        }
        sc.close();

    }
}
