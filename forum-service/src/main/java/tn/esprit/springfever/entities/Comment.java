package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_id")
    private List<Likes> likes;

    @ManyToOne
    @JsonIgnore
    private Post post;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME )
    private LocalDateTime createdAt;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME )
    private LocalDateTime updatedAt;

    @OneToMany (cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_id")
    private List<Media> media;

    private Long user;



}