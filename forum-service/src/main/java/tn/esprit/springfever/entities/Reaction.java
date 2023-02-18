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
    private long id;

    private String name;

    private String label;

    private String location;

    @Lob
    byte[] content;

    public Reaction(String name, String location, String label) {
        this.name = name;
        this.location = location;
        this.label=label;
    }

    public Reaction(String name, byte[] content, String label) {
        this.name = name;
        this.content = content;
        this.label=label;
    }

}