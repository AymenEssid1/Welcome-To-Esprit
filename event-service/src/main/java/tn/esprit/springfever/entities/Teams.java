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

    @NotBlank
    @Size(max = 30)
    private String NameTeam ;
    @NotBlank
    private String QRcertificat;
    @NotBlank
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

}
