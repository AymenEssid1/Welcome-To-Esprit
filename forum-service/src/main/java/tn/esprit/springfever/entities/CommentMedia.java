package tn.esprit.springfever.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import tn.esprit.springfever.enums.MediaType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CommentMedia")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode
public class CommentMedia implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String location;
    @Lob
    @JsonIgnore
    byte[] content;
    @Enumerated(EnumType.STRING)
    private MediaType type;

    @ManyToOne
    @JsonIgnore
    private Comment comment;

    public CommentMedia(String name, String location, Comment comment, byte[] content) {
        this.name = name;
        this.location = location;
        this.comment = comment;
        this.content = content;
    }
}
