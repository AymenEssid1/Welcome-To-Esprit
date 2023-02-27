package tn.esprit.springfever.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rometools.rome.io.FeedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.security.UserPrincipal;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface IPostService {
    public ResponseEntity<?> addPost(String title, String content, String topic, HttpServletRequest authentication, List<MultipartFile> images) throws JsonProcessingException;
    public ResponseEntity<?> updatePost(Long id, String title, String content, String topic, HttpServletRequest authentication, List<MultipartFile> images) throws IOException;
    public String deletePost(Long post, HttpServletRequest authentication) throws IOException;
    public Post getSinglePost(Long id, HttpServletRequest request);
    public List<Post> getAllLazy(int skip, int take, HttpServletRequest request) ;
    public List<Post> getByUserLazy(int skip, int take, Long id, HttpServletRequest request);
    public String rssFeed() throws FeedException;
    public List<Post> searchPosts(String searchString,int page, int size, HttpServletRequest request) throws IOException;
    public Object likePost (Long reaction, Long post, HttpServletRequest request) throws JsonProcessingException;
    public Object changeReaction (Long id,Long reaction, HttpServletRequest request);
    public String deleteReaction(Long id, HttpServletRequest request) throws JsonProcessingException;
}
