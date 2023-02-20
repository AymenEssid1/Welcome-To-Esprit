package tn.esprit.springfever.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@ToString
@Data

public class Question implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long idQuestion ;
    private String ennonce ;
    private String option1 ;
    private String option2 ;
    private String option3 ;
    private String answer ;
    @ManyToMany(cascade = CascadeType.ALL , mappedBy = "questions")
    @JsonIgnore
    private List<Mcq> mcqs;
}
