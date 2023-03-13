package tn.esprit.springfever.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.springfever.entities.CategorieEvent;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Getter
@Setter
@ToString

public class TeamsDTO implements Serializable{

    private Long idTeam;

    @NotBlank
    @Size(max = 30)
    private String NameTeam ;
    @NotBlank
    private String QRcertificat;
    @NotBlank
    private String NiveauEtude ;

    public TeamsDTO(Long idTeam, String NameTeam, String QRcertificat, String NiveauEtude) {
        this.idTeam = idTeam;
        this.NameTeam = NameTeam;
        this.QRcertificat = QRcertificat;
        this.NiveauEtude = NiveauEtude;

    }

}
