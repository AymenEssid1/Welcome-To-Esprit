package tn.esprit.springfever.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    private String location;

    @Lob
    private byte[] data;

    public Video(String name, byte[] data) {
        this.name = name;
        this.data = null;
    }

    public Video(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Video(String name){
        this.name = name;
    }
}
