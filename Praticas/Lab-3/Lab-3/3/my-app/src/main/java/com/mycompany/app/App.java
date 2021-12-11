package com.mycompany.app;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;

public class App 
{
    private static Session session;

    public static void main( String[] args )
    {
        Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        Session session = cluster.connect("cbd_lab3_2");

        //ADDING

        session.execute("INSERT INTO user (email, name, registration_date, username) VALUES ('manu@gmail.com', 'Emanuel', '2021-12-11 16:26:28.030000+0000', 'Manu');");
        System.out.println("Inserted user Manu");
        ResultSet result = session.execute("SELECT * FROM user;");
        System.out.println(result.all());
        System.out.println();
    

        //UPDATING

        session.execute("UPDATE user SET name='Manuel' WHERE email='manu@gmail.com';");
        System.out.println("Updated user Manu");
        result = session.execute("SELECT * FROM user;");
        System.out.println(result.all());
        System.out.println();

        //DELETING

        session.execute("DELETE FROM User WHERE email='manu@gmail.com';");
        System.out.println("Deleted user Manu");
        result = session.execute("SELECT * FROM user;");
        System.out.println(result.all());
        System.out.println();

        //Alinea B
        System.out.println("Last 3 comments introducted:");
        result = session.execute("SELECT * FROM comentario_video LIMIT 3;");
        System.out.println(result.all());

        System.out.println();
        System.out.println("Tag list from a certain video:");
        result = session.execute("SELECT tags FROM video_video WHERE id = 30864b60-562c-11ec-a36b-d9fa4527049e;");
        System.out.println(result.all());

        System.out.println();
        System.out.println("Last 5 events from certain video by a user:");
        result = session.execute("SELECT * FROM video_author WHERE author_email='raquel@gmail.com' and upload_timestamp<'2021-12-09 00:06:18.154000+0000';");
        System.out.println(result.all());

        System.out.println();
        System.out.println("All followers from certain video:");
        result = session.execute("SELECT * FROM video_follower WHERE video_id=30705260-562c-11ec-a36b-d9fa4527049e;");
        System.out.println(result.all());

        session.close();
        System.exit(1);
    }
}
