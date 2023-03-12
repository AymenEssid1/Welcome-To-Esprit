package tn.esprit.springfever.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.Serializable;
@Entity
@Data
public class Image_JobOffer  {
    @Id
    @GeneratedValue
    Long id;

    String name;

    String location;

    @Lob
    byte[] content;

    public Image_JobOffer(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Image_JobOffer(String name, byte[] content) {
        this.name = name;
        this.content = content;
    }

    public Image_JobOffer() {
    }
}
