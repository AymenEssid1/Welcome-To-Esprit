package tn.esprit.springfever.dto;

import lombok.Data;
import tn.esprit.springfever.entities.CommentLike;
import tn.esprit.springfever.entities.CommentMedia;
import tn.esprit.springfever.entities.Post;

import java.util.List;
@Data
public class CommentDTO {
    private Long id;
    private String content;

    private Long user;
    private List<CommentLike> likes;
    private Post post;


}