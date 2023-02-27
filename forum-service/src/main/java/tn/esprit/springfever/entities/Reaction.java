package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "Reaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode
public class Reaction implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "reaction_id")
    private Long reactionId;
    private String name;
    private String location;
    @Lob
    @JsonIgnore
    byte[] content;

    public Reaction(String name, String location, byte[] content){
        this.name = name;
        this.location = location;
        this.content = content;
    }

    public Reaction(String name){
        this.name = name;
    }


}