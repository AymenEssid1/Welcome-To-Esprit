package tn.esprit.springfever.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.entities.Comment;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface ICommentService {
    public ResponseEntity<?> addComment(String comment, List<MultipartFile> images, Long postId, HttpServletRequest request);
    public ResponseEntity<?> updateComment(Long id, String comment, List<MultipartFile> images, HttpServletRequest authentication) throws IOException;
    public String deleteComment(Long comment, HttpServletRequest request) throws JsonProcessingException;
    public Comment getSingleComment(Long id);
}
