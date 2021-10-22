package com.cbd;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class App {

    private Jedis jedis;
    private String users;
    private String usersFollowing;
    private String userInfo;
    private String userMessage;

    public App() {
        this.jedis = new Jedis("localhost");
        this.users = "userSet";
        this.userInfo = "userInfo:";
        this.usersFollowing = "userFollowing:";
    }

    public static void main(String[] args) {

        App appinha = new App();

        String userName = appinha.newUser();

        Scanner sc = new Scanner(System.in);

        int choice;

        while (true) {
            System.out.println("Select an option...");
            System.out.print(
                    "1. Follow User\n2. Unfollow User\n3. Get Information\n4. Send Message\n5. Read From Following User\n6. Read My Messages\n7. Quit\nOption:");

            choice = sc.nextInt();

            switch (choice) {

            case 1:
                // Follow user
                appinha.follow(userName);
                break;

            case 2:
                // Unfollow user
                break;

            case 3:
                // Get Info from user
                break;

            case 4:
                // Send Message
                break;

            case 5:
                // Read Messages from following
                break;
            case 6:
                // Read own Messages
                break;

            case 7:
                // Quit
                System.out.println("Exiting Program...");
                System.exit(0);
                break;
            default:
                System.out.println("This is not a valid Menu Option! Please Select Another");
                break;
            }

        }

    }

    public String newUser() {
        Scanner input = new Scanner(System.in);
        System.out.print("\nInsert username: ");
        String userName = input.nextLine();

        if (!this.jedis.sismember(this.users, userName)) {
            Map<String, String> info = new HashMap<String, String>();
            System.out.print("First and Last name: ");
            info.put("name", input.nextLine());
            System.out.print("Age: ");
            info.put("age", input.nextLine());
            System.out.print("Gender: ");
            info.put("gender", input.nextLine());
            System.out.print("Favorite message system: ");
            info.put("message", input.nextLine());

            this.jedis.sadd(this.users, userName); 
            this.jedis.hmset(this.userInfo + userName, info);

        }

        else {
            System.out.println("Welcome Back " + userName);
        }

        return userName;

    }

    public void follow(String userName) {
        Scanner inputScanner = new Scanner(System.in);

        //Gonna give a set of users that aren't followed by userName - key in jedis -> userFollowing:username
        //This will have the own user so we have to remove him
        Set<String> usersSet = this.jedis.sdiff(this.users, this.usersFollowing + userName);
        usersSet.remove(userName);

        if (usersSet.size() == 0) {
            System.err.println("There are no users to be followed!");
        }
        else {
            System.out.println("Users available to follow: ");
            Map<Integer, String> userNameMap = new HashMap<>();
            int count = 0;
            for (String string : usersSet) {
                System.out.println(++count + ": " + string);
                userNameMap.put(count, string);
            }
            int number = inputScanner.nextInt();
            
            this.jedis.sadd(this.usersFollowing + userName, userNameMap.get(number));

            System.out.print(userNameMap.get(number) + " followed!\n");
        }

    }
}
