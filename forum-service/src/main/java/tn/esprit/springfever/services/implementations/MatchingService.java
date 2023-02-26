package tn.esprit.springfever.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.UserInterest;
import tn.esprit.springfever.repositories.InterestRepository;
import tn.esprit.springfever.repositories.PostRepository;
import tn.esprit.springfever.services.interfaces.IUserService;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MatchingService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private IUserService userService;
    @Autowired
    private InterestRepository interestRepository;
    private static final String TOKENIZER_MODEL_PATH = "opennlp-en-ud-ewt-tokens-1.0-1.9.3.bin";
    private static final String POS_MODEL_PATH = "opennlp-en-ud-ewt-pos-1.0-1.9.3.bin";
    private static Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
    private static POSTaggerME posTagger;
    private ClassLoader classLoader = getClass().getClassLoader();
    public MatchingService(){
        try (InputStream tokenizerModelStream = classLoader.getResourceAsStream(TOKENIZER_MODEL_PATH);
             InputStream posModelStream = classLoader.getResourceAsStream(POS_MODEL_PATH)) {
            TokenizerModel tokenizerModel = new TokenizerModel(tokenizerModelStream);
            POSModel posModel = new POSModel(posModelStream);
            posTagger = new POSTaggerME(posModel);
            tokenizer = SimpleTokenizer.INSTANCE;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public UserDTO getLoggedInUSer(HttpServletRequest request){
        if (request != null) {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null) {
                try {
                    return userService.getUserDetailsFromToken(authHeader);
                } catch (JsonProcessingException ex) {
                    log.error("Error calling user service1: {}", ex.getMessage());
                }
            }
        }
        return null;
    }

    public void addInterestsFromPost(HttpServletRequest request, Post post) {
        // Step 2: Extract topics from the post
        List<String> postTopics = extractTopicsFromPost(post);
        if (request!=null && request.getHeader(HttpHeaders.AUTHORIZATION)!=null){
            UserDTO user = this.getLoggedInUSer(request);
            for (String topic : postTopics) {
                UserInterest userInterest1 = interestRepository.findByUserAndTopic(user.getId(),topic);
                if (userInterest1 !=null) {
                    // User already has an interest in this topic, update the weight
                    double weightIncrement = calculateWeightIncrement(post, userInterest1);
                    userInterest1.setWeight(userInterest1.getWeight() + weightIncrement);
                    interestRepository.save(userInterest1);
                } else {
                    // User does not have an interest in this topic, create a new interest
                    UserInterest userInterest = new UserInterest();
                    userInterest.setUser(user.getId());
                    userInterest.setTopic(topic);
                    double weight = calculateInitialWeight(post, user);
                    userInterest.setWeight(weight);
                    interestRepository.save(userInterest);
                }
            }
        }
        // Step 3 and 4: Create or update UserInterest entities for each topic

    }
    public List<String> extractTopicsFromPost(Post post) {
        List<String> topics = new ArrayList<>();

        String text = post.getContent();

        // Tokenize the text into individual words
        String[] tokens = tokenizer.tokenize(text);

        // Use OpenNLP to tag the parts of speech of each word
        String[] posTags = posTagger.tag(tokens);

        // Loop through each word and its part of speech
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            String posTag = posTags[i];

            // If the word is a noun or adjective, add it as a topic
            if (posTag.startsWith("N") || posTag.startsWith("J")) {
                topics.add(token);
            }
        }

        return topics;
    }
    public double calculateInitialWeight(Post post, UserDTO user) {
        List<String> postTopics = extractTopicsFromPost(post);
        List<UserInterest> list = interestRepository.findByUser(user.getId());

        double weight = 0.0;

        // Loop through each topic in the post
        for (String topic : postTopics) {
            // Check if the user is interested in the topic
            UserInterest userInterest = list.stream()
                    .filter(ui -> ui.getTopic().equals(topic))
                    .findFirst()
                    .orElse(null);

            // If the user is interested in the topic, add 1 to the weight
            if (userInterest != null) {
                weight += 1.0;
            }
        }

        return weight;
    }

    public double calculateWeightIncrement(Post post, UserInterest userInterest) {
        List<String> postTopics = extractTopicsFromPost(post);

        double increment = 0.0;

        // Loop through each topic in the post
        for (String postTopic : postTopics) {
            // Check if the post topic matches the user interest topic
            if (postTopic.equals(userInterest.getTopic())) {
                // Calculate an increment based on how closely the post topic matches the user interest topic
                increment += userInterest.getWeight();
            }
        }

        return increment;
    }
    public PageImpl<Post> getPostsByUserInterests(Pageable pageable, HttpServletRequest request) {
        List<Post> posts = postRepository.findAll();
        if (request != null && request.getHeader(HttpHeaders.AUTHORIZATION)!= null){
            UserDTO user = this.getLoggedInUSer(request);
            List<String> interests = getUserInterests(user.getId());

            // Step 2: Compute user's interest vector
            double[] userVector = getUserVector(interests);

            // Step 3: Retrieve all posts from database

            // Step 4: Compute cosine similarity for each post and user's interest vector
            for (Post post : posts) {
                double[] postVector = getPostVector(post.getTopic());
                double similarity = getCosineSimilarity(userVector, postVector);
                post.setSimilarity(similarity);
                this.addInterestsFromPost(request, post);
            }
            // Step 5: Sort posts in descending order of similarity and return list
            Collections.sort(posts, Comparator.comparing(Post::getSimilarity).reversed());
        }
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int startIndex = pageSize * pageNumber;
        int endIndex = Math.min(startIndex + pageSize, posts.size());
        List<Post> pagePosts = posts.subList(startIndex, endIndex);

        return new PageImpl<>(pagePosts, PageRequest.of(pageNumber, pageSize), posts.size());
    }

    private List<String> getUserInterests(Long userId) {
        // Retrieve the user's interests from the database
        List<String> interests = new ArrayList<>();
        List<UserInterest> userInterests = interestRepository.findByUser(userId);
        for (UserInterest userInterest : userInterests) {
            interests.add(userInterest.getTopic());
        }
        return interests;
    }

    private double[] getUserVector(List<String> interests) {
        // Compute the user's interest vector
        double[] vector = new double[interests.size()];
        List<UserInterest> allInterests = interestRepository.findAll();
        for (int i = 0; i < interests.size(); i++) {
            String interest = interests.get(i);
            for (UserInterest userInterest : allInterests) {
                if (userInterest.getTopic().equals(interest)) {
                    vector[i] = userInterest.getWeight();
                    break;
                }
            }
        }
        return vector;
    }

    private double[] getPostVector(String topic) {
        // Compute the post's topic vector
        double[] vector = new double[Math.toIntExact(interestRepository.count())];
        List<UserInterest> allInterests = interestRepository.findAll();
        for (UserInterest userInterest : allInterests) {
            if (topic.toLowerCase().contains(userInterest.getTopic().toLowerCase())) {
                vector[userInterest.getId().intValue() - 1] = 1;
            }
        }
        return vector;
    }

    private double getCosineSimilarity(double[] vector1, double[] vector2) {
        // Compute the cosine similarity between two vectors
        double dotProduct = 0;
        double norm1 = 0;
        double norm2 = 0;
        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            norm1 += Math.pow(vector1[i], 2);
            norm2 += Math.pow(vector2[i], 2);
        }
        double denominator = Math.sqrt(norm1) * Math.sqrt(norm2);
        if (denominator == 0) {
            return 0;
        } else {
            return dotProduct / denominator;
        }
    }

    public List<Post> getPostsByAdvancedSearch(String searchQuery, int pageNumber, int pageSize) {
        List<Post> matchedPosts = new ArrayList<>();
        List<Post> sortedPosts;

        // Get all posts from database
        List<Post> allPosts = postRepository.findAll();

        // Loop through each post to determine its relevance
        for (Post post : allPosts) {
            int relevance = calculateRelevance(post, searchQuery);
            if (relevance > 0) {
                post.setRelevance(relevance);
                matchedPosts.add(post);
            }
        }

        // Sort the matched posts by relevance
        sortedPosts = matchedPosts.stream()
                .sorted(Comparator.comparing(Post::getRelevance).reversed())
                .collect(Collectors.toList());

        // Apply pagination
        int fromIndex = pageNumber * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, sortedPosts.size());
        List<Post> pagePosts = sortedPosts.subList(fromIndex, toIndex);

        return new PageImpl<>(pagePosts, PageRequest.of(pageNumber, pageSize), sortedPosts.size()).getContent();
    }

    private int calculateRelevance(Post post, String searchQuery) {
        // Calculate the relevance of the post based on search query, included keywords, and excluded keywords
        int relevance = 0;
        String postContent = post.getContent();
        String postTitle = post.getTitle();
        String postTopic = post.getTopic();

        if (postTopic.toLowerCase().contains(searchQuery.toLowerCase())) {
            relevance += 5;
        }
        // Check if search query is present in post content or title
        if (postContent.toLowerCase().contains(searchQuery.toLowerCase())) {
            relevance += 6;
        }
        if (postTitle.toLowerCase().contains(searchQuery.toLowerCase())) {
            relevance += 4;
        }


        return relevance;
    }
}
