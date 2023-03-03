package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.awt.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@ToString
@Data

public class Teams implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter

    private Long idTeam;

    @NotBlank(message = "Le NameTeam ne peut pas être vide.")
    @Size(max = 10, message = "Le nom du team ne peut pas dépasser {max} caractères.")
    private String NameTeam ;
    @NotBlank(message = "Le QRcertificat ne peut pas être vide.")
    private String QRcertificat;
    @NotBlank(message = "Le NiveauEtude ne peut pas être vide.")
    private String NiveauEtude ;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="teams")
    @JsonIgnore
    private java.util.Set<Event> Events;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="teams")
    @JsonIgnore
    private java.util.Set<User> User;

    @OneToOne
    @JsonIgnore
    private Project project;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private ImageData image;


}
