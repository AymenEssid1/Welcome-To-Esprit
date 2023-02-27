package tn.esprit.springfever.dto;

import lombok.Data;
import tn.esprit.springfever.entities.Likes;
import tn.esprit.springfever.entities.Post;

import java.util.List;
@Data
public class CommentDTO {
    private Long id;
    private String content;

    private Long user;
    private List<Likes> likes;
    private Post post;


}