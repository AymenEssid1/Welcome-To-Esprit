package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    @JsonIgnore
    private List<PostLike> likes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    @JsonIgnore
    private List<Comment> comments;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    @JsonIgnore
    private List<PostMedia> media;



}