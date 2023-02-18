package tn.esprit.springfever.entities;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import  tn.esprit.springfever.enums.Result;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@ToString
@Data

public class Deliberation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter

    private Long idDeliberation ;
    @Enumerated(EnumType.STRING)
    private Result result ;



}
