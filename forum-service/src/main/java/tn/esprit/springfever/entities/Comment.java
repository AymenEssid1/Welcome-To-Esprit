package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Comment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comment")
    @JsonIgnore
    private List<CommentLike> likes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comment")
    @JsonIgnore
    private List<CommentMedia> comments;

    @ManyToOne
    private Post post;



}