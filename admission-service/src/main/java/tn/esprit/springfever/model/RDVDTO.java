package tn.esprit.springfever.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.springfever.domain.DemandeAdmission;
import tn.esprit.springfever.domain.Salle;

import java.time.LocalDate;


@Getter
@Setter
public class RDVDTO {
    /*

    private Long idRDV;



    private LocalDate date;

    @JsonProperty("rDVuser")
    private Long rDVuser;

    private Long demandeRdv;

     */


    private Long idRDV;
    private LocalDate date;
    private Salle salle;
    private DemandeAdmission demande;

}
