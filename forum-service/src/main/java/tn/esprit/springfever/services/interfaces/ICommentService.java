package tn.esprit.springfever.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.dto.CommentDTO;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.Likes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface ICommentService {
    public ResponseEntity<?> addComment(String comment, List<MultipartFile> images, Long postId, HttpServletRequest request) throws JsonProcessingException;
    public ResponseEntity<?> updateComment(Long id, String comment, List<MultipartFile> images, HttpServletRequest authentication) throws IOException;
    public String deleteComment(Long comment, HttpServletRequest request) throws JsonProcessingException;
    public CommentDTO getSingleComment(Long id) throws JsonProcessingException;
    public Object likeComment (Long reaction, Long comment, HttpServletRequest request) throws JsonProcessingException;
    public Object changeReaction (Long id,Long reaction, HttpServletRequest request);
    public String deleteReaction(Long id, HttpServletRequest request) throws JsonProcessingException;
    public List<CommentDTO> convertToLikesDTOS(List<Comment> comments) throws JsonProcessingException;
    public CommentDTO convertToLikesDTO(Comment comment) throws JsonProcessingException;

    public ResponseEntity<?> reportComment(Long id, HttpServletRequest request, String desc) throws JsonProcessingException;
}
