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
    private float softskillsNote ;
    @NotBlank(message = "Le hardskillsNote ne peut pas être vide.")
    private float hardskillsNote;
    @NotBlank(message = "Le presentationNote ne peut pas être vide.")
    private float presentationNote ;
    @NotBlank(message = "Le consistencyNote ne peut pas être vide.")
    private float consistencyNote;
    @NotBlank(message = "Le originalityNote ne peut pas être vide.")
    private float originalityNote ;
    @NotBlank(message = "Le contentNote ne peut pas être vide.")
    private float contentNote;
    @NotBlank(message = "Le relevanceNote ne peut pas être vide.")
    private float relevanceNote ;
    @NotBlank(message = "Le comment ne peut pas être vide.")
    @Size(max = 50, message = "Le comment ne peut pas dépasser {max} caractères.")
    private String comment ;
    @NotBlank(message = "Le submitDate ne peut pas être vide.")
    private Date submitDate ;
    @NotBlank(message = "Le projectNote ne peut pas être vide.")
    private float projectNote ;


    @OneToMany(cascade = CascadeType.ALL, mappedBy="note")
    @JsonIgnore
    private java.util.Set<Project> project;



}
