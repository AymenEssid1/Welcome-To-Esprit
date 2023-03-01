package tn.esprit.springfever.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

    public MatchingService() {
        try (InputStream tokenizerModelStream = classLoader.getResourceAsStream(TOKENIZER_MODEL_PATH);
             InputStream posModelStream = classLoader.getResourceAsStream(POS_MODEL_PATH)) {
            TokenizerModel tokenizerModel = new TokenizerModel(tokenizerModelStream);
            POSModel posModel = new POSModel(posModelStream);
            posTagger = new POSTaggerME(posModel);
            tokenizer = new TokenizerME(tokenizerModel);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public UserDTO getLoggedInUser(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) {
            return null;
        }
        try {
            return userService.getUserDetailsFromToken(authHeader);
        } catch (JsonProcessingException ex) {
            log.error("Error calling user service1: {}", ex.getMessage());
            return null;
        }
    }

    public void addInterestsFromPost(HttpServletRequest request, Post post) {
        if (request == null || request.getHeader(HttpHeaders.AUTHORIZATION) == null) {
            return;
        }
        UserDTO user = this.getLoggedInUser(request);
        if (user == null) {
            return;
        }
        List<String> postTopics = extractTopicsFromPost(post.getTopic() + " " + post.getTitle() + " " + post.getContent());
        for (String topic : postTopics) {
            if (isNumeric(topic)) {
                continue;
            }
            UserInterest userInterest = findUserInterest(user.getId(), topic);
            double weightIncrement = calculateWeightIncrement(post, userInterest);
            double initialWeight = calculateInitialWeight(post, user);
            double newWeight = (initialWeight + weightIncrement); // Scale weight between 0 and 10
            newWeight = Math.min(newWeight, 100.0); // Ensure weight is no greater than 10
            if (userInterest == null) {
                userInterest = new UserInterest();
                userInterest.setUser(user.getId());
                userInterest.setTopic(topic);
                userInterest.setWeight(newWeight);
            } else {
                userInterest.setWeight(newWeight);
            }
            interestRepository.save(userInterest);
        }
    }


    public List<String> extractTopicsFromPost(String post) {
        List<String> topics = new ArrayList<>();
        String text = post;
        String[] tokens = tokenizer.tokenize(text);
        String[] posTags = posTagger.tag(tokens);
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            String posTag = posTags[i];
            if (posTag.startsWith("N") || posTag.startsWith("J")) {
                topics.add(token);
            }
        }
        return topics;
    }


    public double calculateInitialWeight(Post post, UserDTO user) {
        double initialWeight = 0.0;
        List<String> postTopics = extractTopicsFromPost(post.getTopic() + " " + post.getTitle() + " " + post.getContent());
        for (String topic : postTopics) {
            if (isNumeric(topic)) {
                continue;
            }
            UserInterest userInterest = findUserInterest(user.getId(), topic);
            if (userInterest != null) {
                initialWeight += userInterest.getWeight();
            }
        }
        return initialWeight / postTopics.size();
    }

    public double calculateWeightIncrement(Post post, UserInterest userInterest) {
        double weightIncrement = 0.0;
        if (userInterest != null) {
            weightIncrement = (100.0 - userInterest.getWeight()) * 0.1;
        } else {
            weightIncrement = 1.0;
        }
        return weightIncrement;
    }


    public List<Post> getPostsByUserInterests(Pageable pageable, HttpServletRequest request) {
        // Get user's interests
        Long userId = getLoggedInUser(request).getId();
        Set<UserInterest> interests = new HashSet<UserInterest>(findUserInterests(userId));
        List<String> userInterests = interests.stream()
                .map(UserInterest::getTopic)
                .collect(Collectors.toList());

        // Get posts by interests
        List<Post> posts = findAll();
        List<Double> userInterestVector = getVector(userInterests);
        for (Post p : posts) {
            List<String> t = extractTopicsFromPost(p.getTopic() + " " + p.getContent() + " " + p.getTitle());
            List<Double> postVector = getVector(t);
            p.setSimilarity(getCosineSimilarity(postVector, userInterestVector));
        }
        Collections.sort(posts, Collections.reverseOrder(Comparator.comparingDouble(Post::getSimilarity)));
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int startIndex = pageSize * pageNumber;
        int endIndex = Math.min(startIndex + pageSize, posts.size());
        List<Post> pagePosts = posts.subList(startIndex, endIndex);

        return new PageImpl<>(pagePosts, PageRequest.of(pageNumber, pageSize), posts.size()).getContent();
    }


    public double getCosineSimilarity(List<Double> vecA, List<Double> vecB) {
        if (vecA == null || vecB == null) {
            return 0.0;
        }
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        int size = Math.min(vecA.size(), vecB.size());
        for (int i = 0; i < size; i++) {
            dotProduct += vecA.get(i) * vecB.get(i);
            normA += vecA.get(i) * vecA.get(i);
            normB += vecB.get(i) * vecB.get(i);
        }
        double denom = Math.sqrt(normA) * Math.sqrt(normB);
        if (denom == 0) {
            return 0.0;
        }
        return dotProduct / denom;
    }

    public List<Double> getVector(List<String> topics) {
        List<Double> vector = new ArrayList<>();
        for (String topic : topics) {
            vector.add((double) Collections.frequency(topics, topic));
        }
        return vector;
    }
    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public List<Post> getPostsByAdvancedSearch(String searchQuery, int pageNumber, int pageSize) throws IOException {
        // Extract topics from query
        List<Post> posts = findAll();
        for (Post p : posts) {
            p.setSimilarity(calculateRelevance(p,searchQuery));
            System.out.println(p.getSimilarity()+"-"+p.getTitle());
        }
        Collections.sort(posts, Collections.reverseOrder(Comparator.comparingDouble(Post::getSimilarity)));

        int startIndex = pageSize * pageNumber;
        int endIndex = Math.min(startIndex + pageSize, posts.size());
        List<Post> pagePosts = posts.subList(startIndex, endIndex);

        return new PageImpl<>(pagePosts, PageRequest.of(pageNumber, pageSize), posts.size()).getContent();
    }
    private int calculateRelevance(Post post, String searchQuery) {
        // Calculate the relevance of the post based on search query, included keywords, and excluded keywords
        int relevance = 0;
        String postContent = post.getContent();
        String postTitle = post.getTitle();
        String postTopic = post.getTopic();
        List<String> text = extractTopicsFromPost(searchQuery);
        List<String> topics= extractTopicsFromPost(postTopic+" "+postTitle+" "+postContent);
        for (String query : text) {
            String regex = ".*(?:\\b|[^a-zA-Z0-9]) " + query + " (?:\\b|[^a-zA-Z0-9]).*";

            if (postTopic.toLowerCase().matches(query.toLowerCase())||postTopic.toLowerCase().matches(searchQuery.toLowerCase())) {
                relevance += 20;
            }
            // Check if search query is present in post content or title
            if (postContent.toLowerCase().contains(query.toLowerCase())||postContent.toLowerCase().contains(searchQuery.toLowerCase())) {
                relevance += 10;
            }
            if (postTitle.toLowerCase().contains(query.toLowerCase())||postTitle.toLowerCase().contains(searchQuery.toLowerCase())) {
                relevance += 15;
            }
            for(String topic : topics){
                if (topic.toLowerCase().contains(query.toLowerCase())||topic.toLowerCase().contains(searchQuery.toLowerCase())){
                    relevance+=5;
                }
            }

        }


        return relevance;
    }

    @Cacheable("matching")
    public List<Post> findAll(){
        return postRepository.findAll();
    }
    @Cacheable("matching")
    public UserInterest findUserInterest(Long id, String topic){
        return interestRepository.findByUserAndTopic(id, topic);
    }
    @Cacheable("matching")
    public List<UserInterest> findAllInterests(){
        return interestRepository.findAll();
    }
    @Cacheable("matching")
    public List<UserInterest> findUserInterests(Long id){
        return interestRepository.findByUser(id);
    }

}
