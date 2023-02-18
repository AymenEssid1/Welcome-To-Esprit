package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import tn.esprit.springfever.enums.MediaType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "AdMedia")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode
public class AdMedia implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    private String name;

    private String location;

    @Lob
    byte[] content;

    @Enumerated(EnumType.STRING)
    private MediaType type;
    @ManyToOne
    private Ad ad;

    public AdMedia(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public AdMedia(String name, byte[] content) {
        this.name = name;
        this.content = content;
    }
}

