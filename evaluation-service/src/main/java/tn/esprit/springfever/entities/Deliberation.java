package tn.esprit.springfever.entities;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import  tn.esprit.springfever.enums.Result;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@ToString
@Data

public class Deliberation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter

    private Long idDeliberation;
    @Enumerated(EnumType.STRING)
    private Result result;


    @Temporal(TemporalType.DATE)
    @Column(name = "dateDelib", nullable = true)
    @FutureOrPresent(message = "Date of deliberation must be in the future or present.")
    Date dateDelib;


    @NotBlank(message = "Feedback cannot be empty.")
    @Size(min = 1, max = 500, message = "Feedback must be between {min} and {max} characters long.")
    private String feedback;


    @NotNull(message = "MCQ score must not be null.")
    @DecimalMin(value = "0.0", inclusive = false, message = "MCQ score must be greater than {value}.")
    private Float mcqScore;

    @NotNull(message = "Communication score must not be null.")
    @Min(value = 0, message = "Communication score must be greater than or equal to {value}.")
    @Max(value = 10, message = "Communication score must be less than or equal to {value}.")
    private Integer communicationScore;

    @NotNull(message = "Technical score must not be null.")
    @Min(value = 0, message = "Technical score must be greater than or equal to {value}.")
    @Max(value = 10, message = "Technical score must be less than or equal to {value}.")
    private Integer technicalScore;

    @NotNull(message = "Professionalism score must not be null.")
    @Min(value = 0, message = "Professionalism score must be greater than or equal to {value}.")
    @Max(value = 10, message = "Professionalism score must be less than or equal to {value}.")
    private Integer professionalismScore;

}