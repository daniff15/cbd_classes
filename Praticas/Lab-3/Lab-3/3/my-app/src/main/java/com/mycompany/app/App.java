package com.mycompany.app;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class App 
{
    private static Session session;

    public static void main( String[] args )
    {
        try {
            Cluster cluster = Cluster.builder().addContactPoints("localhost").build();

            session = cluster.connect("cbd_lab3_2");

            //Insert a user
            //insertUser("novoUser", "Novo User", "novo@gmail.com", "2021-12-09 10:38:28.030000+0000");

        }
        catch(Exception e){
            System.err.println("Error connecting to Cassandra database: " + e);
        }
    }


    public static void insertUser(String username, String name, String email, String timestamp){
        try {
            StringBuilder sb = new StringBuilder("INSERT INTO ")
                    .append("user").append("(email, name, registration_date, username) ")
                    .append("VALUES (").append(email).append(", '" + name + "'").append(", '" + timestamp + "'").append(", '" + username + "'");
            String query = sb.toString();
            session.execute(query);
            System.out.println("User insertado com sucesso!");
        }catch (Exception e){
            System.err.println("Error inserting into Cassandra database: " + e);
        }
    }
}
