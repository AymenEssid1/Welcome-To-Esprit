package tn.esprit.springfever.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
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
    @Size(max = 500)

    private String ennonce ;
    @Size(max = 500)

    private String option1 ;
    @Size(max = 500)

    private String option2 ;
    @Size(max = 500)

    private String option3 ;
    @Size(max = 500)

    private String answer ;
    @Size(max = 500)

    @ManyToMany(cascade = CascadeType.ALL , mappedBy = "questions")
    @JsonIgnore
     private List<Mcq> mcqs ;
}
