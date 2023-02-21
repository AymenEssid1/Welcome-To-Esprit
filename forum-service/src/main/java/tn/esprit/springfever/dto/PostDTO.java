package tn.esprit.springfever.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.PostLike;
import tn.esprit.springfever.entities.PostMedia;

import javax.persistence.*;
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

    private List<PostLike> likes;

    private List<Comment> comments;
    private List<PostMedia> media;



}
