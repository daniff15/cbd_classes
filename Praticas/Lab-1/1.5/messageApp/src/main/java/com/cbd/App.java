package com.cbd;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        this.userMessage = "userMessage:";
    }

    public static void main(String[] args) {

        App appinha = new App();

        String userName = appinha.newUser();

        Scanner sc = new Scanner(System.in);

        int choice;

        while (true) {
            System.out.println("\nSelect an option...");
            System.out.print(
                    "1. Follow User\n2. Unfollow User\n3. Get Information From Users\n4. Send Message\n5. Read Messages From Users\n6. Quit\nOption:");

            choice = sc.nextInt();
            
            System.out.println();

            switch (choice) {

            case 1:
                // Follow user
                appinha.follow(userName);
                break;

            case 2:
                // Unfollow user
                appinha.unfollow(userName);
                break;

            case 3:
                // Get Info from user
                appinha.getInfo(userName);
                break;

            case 4:
                // Send Message
                appinha.sendMsg(userName);
                break;

            case 5:
                // Read Messages from following
                appinha.readMsg(userName);
                break;
            case 6:
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
            info.put("Name", input.nextLine());
            System.out.print("Age: ");
            info.put("Age", input.nextLine());
            System.out.print("Gender: ");
            info.put("Gender", input.nextLine());
            System.out.print("Favorite message system: ");
            info.put("FavMessage", input.nextLine());

            this.jedis.sadd(this.users, userName);
            this.jedis.hmset(this.userInfo + userName, info);

        }

        else {
            System.out.println("\nWelcome Back " + userName);
        }

        return userName;

    }

    public void follow(String userName) {
        Scanner sc = new Scanner(System.in);

        // Gonna give a set of users that aren't followed by userName - key in jedis ->
        // userFollowing:username
        // This will have the own user so we have to remove him
        Set<String> usersSet = this.jedis.sdiff(this.users, this.usersFollowing + userName);
        usersSet.remove(userName);

        if (usersSet.size() == 0) {
            System.err.println("There are no users to be followed!");
        } else {
            System.out.println("Users available to follow: ");
            Map<Integer, String> userNameMap = new HashMap<>();
            int count = 0;
            for (String string : usersSet) {
                System.out.println(++count + ": " + string);
                userNameMap.put(count, string);
            }
            System.out.print("Follow user number: ");
            int number = sc.nextInt();

            this.jedis.sadd(this.usersFollowing + userName, userNameMap.get(number));

            System.out.print(userNameMap.get(number) + " followed!\n");
        }

    }

    public void unfollow(String userName) {
        Scanner sc = new Scanner(System.in);

        Set<String> usersFollowing = this.jedis.smembers(this.usersFollowing + userName);

        if (usersFollowing.size() == 0) {
            System.err.println("There are no users to unfollow!");
        } else {
            System.out.println("Users available to follow: ");
            Map<Integer, String> usersFollowingMap = new HashMap<>();
            int count = 0;
            for (String string : usersFollowing) {
                System.out.println(++count + ": " + string);
                usersFollowingMap.put(count, string);
            }
            System.out.print("Unfollow user number: ");
            int number = sc.nextInt();

            this.jedis.srem(this.usersFollowing + userName, usersFollowingMap.get(number));

            System.out.print(usersFollowingMap.get(number) + " unfollowed!\n");

        }
    }

    public void getInfo(String userName) {
        System.out.println("NOTE: Can only see information from users that you follow!");
        Scanner sc = new Scanner(System.in);

        //See if the user that we want to send the message is followed by us (the user)
        Set<String> usersFollowing = this.jedis.smembers(this.usersFollowing + userName);
        Map<Integer, String> userNameMap = new HashMap<>();
        int count = 0;
        System.out.println("List of users being followed!");
        for (String string : usersFollowing) {
            System.out.println(++count + " - " + string);
            userNameMap.put(count, string);
        }

        // put the user itself so he can read his own messages
        System.out.println(userNameMap.size() + 1 + " - " + userName);
        userNameMap.put(userNameMap.size() + 1, userName);

        System.out.println(userNameMap.size() + 1 + " - Back");

        System.out.println();
        System.out.print("See info from user: ");
        int infoFromUser = sc.nextInt();

        if (infoFromUser == userNameMap.size() + 1) {
            return;
        }

        Map<String, String> usersInfo = this.jedis.hgetAll(this.userInfo + userNameMap.get(infoFromUser));

        if (usersInfo.size() == 0) {
            System.out.println("User not found!");
        } else {
            for (String key : usersInfo.keySet()) {
                System.out.println(key + ": " + usersInfo.get(key));
            }
        }

    }

    public void sendMsg(String userName) {
        Scanner sc = new Scanner(System.in);

        System.out.println("1 - Send Message\n2 - Back");
        System.out.println("Option: ");
        int option = sc.nextInt();

        //best way to store messages are list cause of order of arrival
        Scanner msgSc = new Scanner(System.in);
        if (option == 1) {
            System.out.print("Message: ");
            this.jedis.lpush(this.userMessage + userName, msgSc.nextLine());
        }
        else{
            return;
        }

    }


    public void readMsg(String userName) {
        System.out.println("NOTE: Can only see messages from users that you follow or your own!");
        Scanner sc = new Scanner(System.in);

        //See if the user that we want to send the message is followed by us (the user)
        Set<String> usersFollowing = this.jedis.smembers(this.usersFollowing + userName);

        //Put in a map so only have acess to following users
        Map<Integer, String> userNameMap = new HashMap<>();
        int count = 0;
        System.out.println("List of users being followed!");
        for (String string : usersFollowing) {
            System.out.println(++count + " - " + string);
            userNameMap.put(count, string);
        }

        // put the user itself so he can read his own messages
        System.out.println(userNameMap.size() + 1 + " - " + userName);
        userNameMap.put(userNameMap.size() + 1, userName);


        System.out.println();
        System.out.print("Read messages from user number: ");
        int userToReadMsg = sc.nextInt();

        List<String> userMessages = this.jedis.lrange(this.userMessage + userNameMap.get(userToReadMsg), 0, -1);

        if (userMessages.size() == 0) {
            System.err.println("User " + userNameMap.get(userToReadMsg) + " has not sent any messages!");
        }
        else {
            System.out.println("Messages sent from " + userNameMap.get(userToReadMsg) + " to the system: ");
            for (String string : userMessages) {
                System.out.println(" - " + string);
            }
        }
        
        
    }
}
