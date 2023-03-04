package tn.esprit.springfever.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import  tn.esprit.springfever.enums.ClaimRate;
import  tn.esprit.springfever.enums.ClaimStatus;
import tn.esprit.springfever.enums.ClaimSubject;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Data

public class Mcq implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long idMcq;
    private String McqTitle ;
    private int Duration ;
    @ManyToMany(cascade = CascadeType.ALL )
    @JsonIgnore
     private List<Question> questions ;
}

