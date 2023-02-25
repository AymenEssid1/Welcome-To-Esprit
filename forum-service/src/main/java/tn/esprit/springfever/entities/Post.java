package tn.esprit.springfever.entities;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Post")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode
public class Post implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 200)
    private String title;
    private String content;

    private String topic;

    private Long user;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME )
    private LocalDateTime createdAt;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME )
    private LocalDateTime updatedAt;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "post")
    private List<PostLike> likes;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "post")
    private List<Comment> comments;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "post")
    private List<PostMedia> media;
    @Transient
    private double similarity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<PostViews> views;
}