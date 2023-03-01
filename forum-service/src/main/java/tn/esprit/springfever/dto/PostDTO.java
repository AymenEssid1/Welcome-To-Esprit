package tn.esprit.springfever.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.Likes;
import tn.esprit.springfever.entities.Media;
import tn.esprit.springfever.entities.PostViews;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private String topic;
    private UserDTO user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<Likes> likes;

    private List<Comment> comments;

    private List<Media> media;

    private int views;


}
