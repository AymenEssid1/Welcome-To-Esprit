package tn.esprit.springfever.domain;

import tn.esprit.springfever.model.Cursus;
import tn.esprit.springfever.model.Diplome;
import tn.esprit.springfever.model.Niveau;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.springfever.model.TypeDemande;

import java.time.LocalDate;
import java.util.Set;


@Entity
@Getter
@Setter
public class DemandeAdmission {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAdmission;

    @Column(name = "date_admission")
    private LocalDate dateAdmission;

    @Column(name="CIN")
    private String CIN;

    @Column
    @Enumerated(EnumType.STRING)
    private TypeDemande typeDemande;

    @Column
    @Enumerated(EnumType.STRING)
    private Diplome diplome;


    @Column
    @Enumerated(EnumType.STRING)
    private Niveau niveau;

    @Column
    @Enumerated(EnumType.STRING)
    private Cursus cursus;

    @Column
    private String nomParent;

    @Column
    private String prenomParent;

    @Column
    private String mailParent;

    @Column
    private String telParent;


    @PrePersist
    public void setDateAdmission() {
        this.dateAdmission = LocalDate.now();
    }

    @OneToOne
    @JoinColumn(name = "user_user_id")
    private User user;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "demandeRdv")
    private RDV rdvDemande;







}
