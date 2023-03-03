package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@ToString
@Data

public class Event implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter

    private Long idEvent;
    @NotBlank(message = "Le budget ne peut pas être vide.")
    private String budget ;
    @Size(max = 10, message = "L'espace ne peut pas dépasser {max} caractères.")
    @NotBlank(message = "L'espace ne peut pas être vide.")
    private String espace;
    @NotBlank(message = "Le matriel ne peut pas être vide.")
    private String materiels ;
    @NotNull(message = "La categorie de l'event ne peut pas être vide.")
    @Enumerated(EnumType.STRING)
    private CategorieEvent typeEvent ;
    @NotBlank(message = "La date ne peut pas être vide.")
    private Date startDate ;
    @NotBlank(message = "La date ne peut pas être vide.")
    private Date endDate ;

    @ManyToOne
    @JsonIgnore
    Teams teams;




}
