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
    @NotBlank
    private float ProjectNote ;
    @NotBlank
    private float PresentationNote;
    @NotBlank
    private float SoftSkillsNote ;
    @NotBlank
    private String comment ;
    @NotBlank
    private Date submitDate ;


    @OneToMany(cascade = CascadeType.ALL, mappedBy="note")
    private java.util.Set<Project> project;



}
