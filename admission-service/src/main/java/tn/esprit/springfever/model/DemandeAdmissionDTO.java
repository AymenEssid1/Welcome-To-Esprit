package tn.esprit.springfever.model;


import lombok.Getter;
import lombok.Setter;
import tn.esprit.springfever.domain.RDV;

import javax.validation.constraints.Size;
import java.time.LocalDate;


@Getter
@Setter
public class DemandeAdmissionDTO {

    private Long idAdmission;

    private LocalDate dateAdmission;

    private TypeDemande typeDemande;

    private Diplome diplome;

    private Niveau niveau;

    private Cursus cursus;
    private String CIN;
    @Size(max = 255)
    private String nomParent;

    @Size(max = 255)
    private String prenomParent;

    @Size(max = 255)
    private String mailParent;

    @Size(max = 255)
    private String telParent;

    private Long user;
    private Long rdvDemande;


}
