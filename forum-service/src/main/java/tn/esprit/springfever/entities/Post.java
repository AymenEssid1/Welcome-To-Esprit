package tn.esprit.springfever.entities;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
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

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "post")
    private List<PostLike> likes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Comment> comments;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<PostMedia> media;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<PostViews> views;
}