package tn.esprit.springfever.entities;

import java.io.Serializable;
import lombok.Data;

import javax.persistence.*;


@Entity
@Data
public class Image implements Serializable {

    @Id
    @GeneratedValue
    Long id;

    String name;

    String location;

    @Lob
    byte[] content;



    @OneToOne(mappedBy = "image")
    private User user;

    public Image(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Image(String name, byte[] content) {
        this.name = name;
        this.content = content;
    }

    public Image() {
    }



}
