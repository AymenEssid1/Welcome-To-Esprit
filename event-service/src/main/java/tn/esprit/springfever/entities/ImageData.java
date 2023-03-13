package tn.esprit.springfever.entities;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;


@Entity
@Data
public class ImageData {

    @Id
    @GeneratedValue
    Long id;

    String name;

    String location;

    @Lob
    byte[] content;

    public ImageData(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public ImageData(String name, byte[] content) {
        this.name = name;
        this.content = content;
    }

    public ImageData() {
    }

}
