package tn.esprit.springfever.configuration;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
@Service
public class UsernameValidator   {

    private List<String> badUsernames;


    public  UsernameValidator() {
        try {
            badUsernames = Files.readAllLines(Paths.get(System.getProperty("user.dir")+"/user-service/src/main/resources/bad-usernames.txt"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read bad usernames file", e);
        }
    }


    public boolean isValid(String s) {
        if (s == null) {
            return true;
        }
        return !badUsernames.contains(s.toLowerCase());
    }
}
