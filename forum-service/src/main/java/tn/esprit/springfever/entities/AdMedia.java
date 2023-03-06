package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
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
    private Long id;
    private String name;
    private String location;
    @Lob
    @JsonIgnore
    byte[] content;
    private String type;

    @ManyToOne
    @JsonIgnore
    private Ad ad;

    public AdMedia(String name, String location, Ad ad, byte[] content, String type) {
        this.name = name;
        this.location = location;
        this.ad = ad;
        this.content = content;
        this.type = type;
    }
}

