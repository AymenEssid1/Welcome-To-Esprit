package tn.esprit.springfever.model;


import lombok.Getter;
import lombok.Setter;
import tn.esprit.springfever.domain.RDV;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Getter
@Setter
public class DemandeAdmissionDTO {
/*
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



 */



        @Id
        @Column(nullable = false, updatable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idAdmission;

        private LocalDate dateAdmission;

        private String CIN;

        @Enumerated(EnumType.STRING)
        private TypeDemande typeDemande;


        @Enumerated(EnumType.STRING)
        private Diplome diplome;


        @Enumerated(EnumType.STRING)
        private Niveau niveau;

        @Enumerated(EnumType.STRING)
        private Cursus cursus;

        private String nomParent;

        private String prenomParent;

        private String mailParent;

        private String telParent;


        public void setDateAdmission() {
            this.dateAdmission = LocalDate.now();
        }


        private Long condidat;


        private Long evaluateeur ;


        private RDV rdvDemande;




}
