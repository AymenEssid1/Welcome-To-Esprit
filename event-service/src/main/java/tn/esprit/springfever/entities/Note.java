package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@ToString
@Data

public class Note implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter

    private Long idNote;
    @NotBlank(message = "Le softskillsNote ne peut pas être vide.")
    @Min(value = 0, message = "La note doit être supérieure ou égale à 0.")
    @Max(value = 20, message = "La note doit être inférieure ou égale à 20.")
    @NotEmpty(message = "Please provide a softskillsNote")
    private float softskillsNote ;

    @NotNull(message = "La note ne peut pas être nulle.")
    @Min(value = 0, message = "La note doit être supérieure ou égale à 0.")
    @Max(value = 20, message = "La note doit être inférieure ou égale à 20.")
    @NotBlank(message = "Le hardskillsNote ne peut pas être vide.")
    private float hardskillsNote;

    @NotBlank(message = "Le presentationNote ne peut pas être vide.")
    @Min(value = 0, message = "La note doit être supérieure ou égale à 0.")
    @Max(value = 20, message = "La note doit être inférieure ou égale à 20.")
    private float presentationNote ;

    @NotBlank(message = "Le consistencyNote ne peut pas être vide.")
    @Min(value = 0, message = "La note doit être supérieure ou égale à 0.")
    @Max(value = 20, message = "La note doit être inférieure ou égale à 20.")
    private float consistencyNote;

    @NotBlank(message = "Le originalityNote ne peut pas être vide.")
    @Min(value = 0, message = "La note doit être supérieure ou égale à 0.")
    @Max(value = 20, message = "La note doit être inférieure ou égale à 20.")
    private float originalityNote ;

    @NotBlank(message = "Le contentNote ne peut pas être vide.")
    @Min(value = 0, message = "La note doit être supérieure ou égale à 0.")
    @Max(value = 20, message = "La note doit être inférieure ou égale à 20.")
    private float contentNote;

    @NotBlank(message = "Le relevanceNote ne peut pas être vide.")
    @Min(value = 0, message = "La note doit être supérieure ou égale à 0.")
    @Max(value = 20, message = "La note doit être inférieure ou égale à 20.")
    private float relevanceNote ;

    @NotBlank(message = "Le comment ne peut pas être vide.")
    @Size(max = 50, message = "Le comment ne peut pas dépasser {max} caractères.")
    private String comment ;

    @NotBlank(message = "Le submitDate ne peut pas être vide.")
    @Temporal(value=TemporalType.DATE)
    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date submitDate ;

 @NotEmpty(message = "Please provide a projectNote")
    @NotBlank(message = "Le projectNote ne peut pas être vide.")
    private float projectNote ;


    @OneToMany(cascade = CascadeType.ALL, mappedBy="note")
    @JsonIgnore
    private java.util.Set<Project> project;


    public Note(String s, String message, Date date) {
    }

    public Note() {

    }


    public boolean filterBadWords() {
        return BadWords.containsBadWord(comment);
    }

}
