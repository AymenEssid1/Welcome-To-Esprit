package tn.esprit.springfever.entities;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.Serializable;

@Entity
@Data
public class testin implements Serializable {


    @Id
    @GeneratedValue
    Long id;

    String name;

    String location;

    @Lob
    byte[] content;
}
