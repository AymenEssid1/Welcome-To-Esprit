package tn.esprit.springfever.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
    @NotBlank(message = "Ennonce cannot be blank")
    @Size(max = 500, message = "Ennonce cannot be longer than 500 characters")
    private String ennonce;

    @NotBlank(message = "option 1 cannot be blank")
    @Size(max = 500, message = "Option 1 cannot be longer than 500 characters")
    private String option1;

    @NotBlank(message = "option 2 cannot be blank")
    @Size(max = 500, message = "Option 2 cannot be longer than 500 characters")
    private String option2;

    @NotBlank(message = "option 3 cannot be blank")
    @Size(max = 500, message = "Option 3 cannot be longer than 500 characters")
    private String option3;

    @NotBlank(message = "Answer cannot be blank")
    @Size(max = 500, message = "Answer cannot be longer than 500 characters")
    private String answer;

    @ManyToMany(cascade = CascadeType.ALL , mappedBy = "questions")
    @JsonIgnore
     private List<Mcq> mcqs ;
}
