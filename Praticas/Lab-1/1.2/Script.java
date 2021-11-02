import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Script {

    public static void main(String[] args) {

        Map<String, Integer> mapinha = new HashMap<>();

        try {
            File myObj = new File("names.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                String character = String.valueOf(line.charAt(0)).toUpperCase();

                if (!mapinha.containsKey(character)) {
                    mapinha.put(String.valueOf(character), 1);
                } else {
                    mapinha.put(String.valueOf(character), mapinha.get(String.valueOf(character)) + 1);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("filename.txt");
            if (myObj.createNewFile()) {
                FileWriter myWriter = new FileWriter("names_counting.txt");

                for (String string : mapinha.keySet()) {
                    myWriter.write("SET " + string + " " + mapinha.get(string) + "\n");
                }

                myWriter.close();
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

}
