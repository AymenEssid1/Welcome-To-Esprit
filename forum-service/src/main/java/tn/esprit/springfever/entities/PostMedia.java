package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import tn.esprit.springfever.enums.MediaType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PostMedia")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode
public class PostMedia implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String location;
    @Lob
    @JsonIgnore
    byte[] content;
    private String type;

    @ManyToOne
    @JsonIgnore
    private Post post;

    public PostMedia(String name, String location, Post post, byte[] content) {
        this.name = name;
        this.location = location;
        this.post = post;
        this.content = content;
    }

    public PostMedia(String name, String location, Post post, byte[] content, String type) {
        this.name = name;
        this.location = location;
        this.post = post;
        this.content = content;
        this.type= type;
    }
}