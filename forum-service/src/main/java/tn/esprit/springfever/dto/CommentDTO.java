package tn.esprit.springfever.dto;

import lombok.Data;
import tn.esprit.springfever.entities.Likes;
import tn.esprit.springfever.entities.Media;
import tn.esprit.springfever.entities.Post;

import java.util.List;
@Data
public class CommentDTO {
    private Long id;
    private String content;
    private UserDTO user;
    private List<LikesDTO> likes;
    private Post post;
    private List<Media> media;


}