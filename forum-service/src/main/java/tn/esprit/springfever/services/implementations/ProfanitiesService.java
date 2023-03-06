package tn.esprit.springfever.services.implementations;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.entities.Profanities;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
public class ProfanitiesService {

    public Profanities bannedWords = new Profanities();
    public boolean containsBannedWords(String postContent) {
        for (String word : bannedWords.getBadWords()) {
            // Add variations of the banned word
            List<String> wordVariations = generateWordVariations(word);
            for (String variation : wordVariations) {
                // Account for hidden characters and concatenated words
                String regex = ".*(?:\\b|[^a-zA-Z0-9])" + Pattern.quote(variation) + "(?:\\b|[^a-zA-Z0-9]).*";
                        if (postContent.matches(regex)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<String> generateWordVariations(String word) {
        List<String> variations = new ArrayList<>();
        variations.add(word);
        // Add misspellings
        variations.addAll(generateMisspellings(word));
        Set<String> bannedWordsSet = new HashSet<String>(bannedWords.getBadWords());
        // Add acronyms
        variations.addAll(generateAcronyms(word,10));

        return variations;
    }

    private static final Map<String, Set<String>> MISSPELLINGS_CACHE = new ConcurrentHashMap<>();
    private static final int MAX_MISSPELLINGS = 10;


    public List<String> generateMisspellings(String word) {
        Set<String> misspellings = MISSPELLINGS_CACHE.get(word);
        if (misspellings != null) {
            return new ArrayList<>(misspellings);
        }
        misspellings = new HashSet<>();

        // Swapping adjacent characters
        for (int i = 0; i < word.length() - 1; i++) {
            char[] arr = word.toCharArray();
            char temp = arr[i];
            arr[i] = arr[i+1];
            arr[i+1] = temp;
            misspellings.add(new String(arr));
            if (misspellings.size() >= MAX_MISSPELLINGS) {
                break;
            }
        }

        // Replacing characters with similar-looking characters
        Map<Character, String> replacements = new HashMap<>();
        replacements.put('a', "4");
        replacements.put('e', "3");
        replacements.put('i', "1");
        replacements.put('o', "0");
        replacements.put('s', "5");
        replacements.put('t', "7");
        replacements.put('v', "u");
        replacements.put('u', "v");
        for (char j = 'a'; j <= 'z'; j++) {
            replacements.put(j,"*");
        }
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (replacements.containsKey(c)) {
                char[] arr = word.toCharArray();
                arr[i] = replacements.get(c).charAt(0);
                misspellings.add(new String(arr));
                if (misspellings.size() >= MAX_MISSPELLINGS) {
                    break;
                }
            }
        }

        // Adding or removing a single character
        for (int i = 0; i < word.length(); i++) {
            StringBuilder sb = new StringBuilder(word);
            sb.deleteCharAt(i);
            misspellings.add(sb.toString());
            if (misspellings.size() >= MAX_MISSPELLINGS) {
                break;
            }
            for (char j = 'a'; j <= 'z'; j++) {
                sb.insert(i, j);
                misspellings.add(sb.toString());
                if (misspellings.size() >= MAX_MISSPELLINGS) {
                    break;
                }
                sb.deleteCharAt(i);
            }
        }

        // Cache the result for future use
        MISSPELLINGS_CACHE.put(word, misspellings);

        return new ArrayList<>(misspellings);
    }


    public List<String> generateAcronyms(String word, int maxAcronyms) {
        List<String> acronyms = new ArrayList<>();
        int maxLength = Math.min(word.length(), 20);
        for (int i = 1; i <= maxLength; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < i; j++) {
                sb.append(word.charAt(j));
            }
            String acronym = sb.toString();
            acronyms.add(acronym);
            if (acronyms.size() >= maxAcronyms) {
                break;
            }
        }
        return acronyms;
    }
}
