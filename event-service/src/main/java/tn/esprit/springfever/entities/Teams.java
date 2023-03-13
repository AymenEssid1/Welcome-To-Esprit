package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
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
import java.util.UUID;

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
    private String nameTeam ;

    @NotBlank(message = "Le QRcertificat ne peut pas être vide.")
    @ApiModelProperty(notes = "The QR code image in base64-encoded PNG format", value = "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAIAAAAC64paAAAAA3NCSVQICAjb4U/gAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAJZJREFUeNpiYBhgeP//PwMWQAKcAizF+/PjEwMzAxHgIYGZiYmZiYs3AhLCT//37LnQUyNiAeTbQ2MLFiwFEly6UxLihgZ2YAKXl6VdnBzMQl/CwMDwGJibGoqalBVMxh3NhYGDZfPmLXz/z5PhgcG5gCIwN7AOAC8gP4VhjEG2xgAAEI0F5fSNA5kwAAAABJRU5ErkJggg==")
    @Column(unique = true, nullable = false)
    private String QRcertificat;

    @NotBlank(message = "Le NiveauEtude ne peut pas être vide.")
    private String NiveauEtude ;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="teams")
    @JsonIgnore
    private java.util.Set<Event> Events;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="teams")
    @JsonIgnore
    private java.util.Set<Participant> User;

    @OneToOne
    @JsonIgnore
    private Project project;


    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private ImageData image;

    public Teams() {
        // Generate the QR code using a random UUID
        this.QRcertificat = UUID.randomUUID().toString();
    }

}
