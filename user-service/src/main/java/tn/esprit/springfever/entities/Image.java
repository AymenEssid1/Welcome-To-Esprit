package tn.esprit.springfever.entities;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;


@Entity
@Data
@ToString
public class Image implements Serializable {

    @Id
    @GeneratedValue
    Long id;

    String name;

    String location;

    @Lob
    byte[] content;






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
