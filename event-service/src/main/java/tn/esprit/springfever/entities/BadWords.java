package tn.esprit.springfever.entities;

import java.util.Arrays;
import java.util.List;

public class BadWords {
    private static final List<String> BAD_WORDS = Arrays.asList("bad", "offensive", "hate");

    public static boolean containsBadWord(String comment) {
        for (String badWord : BAD_WORDS) {
            if (comment.toLowerCase().contains(badWord)) {
                return true;
            }
        }
        return false;
    }
}
