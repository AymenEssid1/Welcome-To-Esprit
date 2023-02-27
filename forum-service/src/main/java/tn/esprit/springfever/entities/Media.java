package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Media")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode
public class Media implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "media_id")
    private Long mediaId;
    private String name;
    private String location;
    @Lob
    @JsonIgnore
    byte[] content;
    private String type;

    public Media(String name, String location, byte[] content, String type) {
        this.name = name;
        this.location = location;
        this.content = content;
        this.type=type;
    }
}
