package tn.esprit.springfever.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import tn.esprit.springfever.dto.LikesDTO;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.Likes;

import java.util.List;

public interface ILikesService {
    public Likes addLike(Likes like);
    public Likes updatePostLike(Post post, Long reaction, Long user);
    public Likes updateCommentLike(Comment comment, Long reaction, Long user);
    public String deletePostLike(Post post, Long user);
    public String deleteCommentLike(Comment comment, Long user);
    public List<Likes> findByUser(Long user);
    public Likes findById(Long id);
    public List<LikesDTO> convertToLikesDTOS(List<Likes> likes) throws JsonProcessingException;
    public LikesDTO convertToLikesDTO(Likes like) throws JsonProcessingException;
}
