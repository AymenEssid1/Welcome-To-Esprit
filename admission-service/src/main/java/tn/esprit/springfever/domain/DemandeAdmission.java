package tn.esprit.springfever.domain;

import tn.esprit.springfever.model.Cursus;
import tn.esprit.springfever.model.Diplome;
import tn.esprit.springfever.model.Niveau;
import tn.esprit.springfever.model.Status;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column
    private LocalDate dateAdmission;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

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
    private String specialite;

    @Column(name = "\"option\"")
    private String option;

    @Column
    private String frais;

    @Column
    private String nomParent;

    @Column
    private String prenomParent;

    @Column
    private String mailParent;

    @Column
    private String telParent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demande_user_id")
    private User demandeUser;

    @OneToMany(mappedBy = "demandeSpecialite")
    private Set<Specialite> demandeSpecialiteSpecialites;

}
