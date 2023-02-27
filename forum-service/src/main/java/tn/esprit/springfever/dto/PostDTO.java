package tn.esprit.springfever.dto;

import lombok.Data;
import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.Likes;
import tn.esprit.springfever.entities.Media;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
@Data
public class PostDTO {
    @NotBlank
    private Long id;
    @NotBlank
    @Size(max = 200)
    private String title;
    private String content;

    private Long user;

    private List<Likes> likes;

    private List<Comment> comments;
    private List<Media> media;



}
