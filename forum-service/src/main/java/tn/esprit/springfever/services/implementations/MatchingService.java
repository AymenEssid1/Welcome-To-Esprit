package tn.esprit.springfever.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.repositories.PostRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class MatchingService {
    @Autowired
    private PostRepository postRepository;


    public List<Post> getPostsByUserInterests(Long userId) {
        // Step 1: Retrieve user's interests from database
        List<String> interests = getUserInterests(userId);

        // Step 2: Compute user's interest vector
        double[] userVector = getUserVector(interests);

        // Step 3: Retrieve all posts from database
        List<Post> posts = postRepository.findAll();

        // Step 4: Compute cosine similarity for each post and user's interest vector
        for (Post post : posts) {
            double[] postVector = getPostVector(post.getTopic());
            double similarity = getCosineSimilarity(userVector, postVector);
            post.setSimilarity(similarity);
        }

        // Step 5: Sort posts in descending order of similarity and return list
        Collections.sort(posts, Comparator.comparing(Post::getSimilarity).reversed());
        return posts;
    }

    private List<String> getUserInterests(Long userId) {
        // Retrieve the user's interests from the database
        List<String> interests = new ArrayList<>();
        List<UserInterest> userInterests = userInterestRepository.findByUserId(userId);
        for (UserInterest userInterest : userInterests) {
            interests.add(userInterest.getInterest());
        }
        return interests;
    }

    private double[] getUserVector(List<String> interests) {
        // Compute the user's interest vector
        double[] vector = new double[interests.size()];
        List<UserInterest> allInterests = userInterestRepository.findAll();
        for (int i = 0; i < interests.size(); i++) {
            String interest = interests.get(i);
            for (UserInterest userInterest : allInterests) {
                if (userInterest.getInterest().equals(interest)) {
                    vector[i] = userInterest.getWeight();
                    break;
                }
            }
        }
        return vector;
    }

    private double[] getPostVector(String topic) {
        // Compute the post's topic vector
        double[] vector = new double[userInterestRepository.count()];
        List<UserInterest> allInterests = userInterestRepository.findAll();
        for (UserInterest userInterest : allInterests) {
            if (topic.toLowerCase().contains(userInterest.getInterest().toLowerCase())) {
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
}
