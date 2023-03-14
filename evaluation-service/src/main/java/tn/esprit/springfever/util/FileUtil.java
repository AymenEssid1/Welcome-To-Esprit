package tn.esprit.springfever.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    public static void writeToFile(String text, String filePath) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
            writer.write(text);
            writer.newLine(); // add a newline character after the string
            writer.close();
            System.out.println("String appended to file successfully.");
        } catch (IOException e) {
            System.out.println("Error appending string to file: " + e.getMessage());
        }
    }
}
