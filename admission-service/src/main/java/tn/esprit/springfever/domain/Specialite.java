package tn.esprit.springfever.domain;

import tn.esprit.springfever.model.NomSpecialite;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Specialite {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSpecialite;

    @Column
    @Enumerated(EnumType.STRING)
    private NomSpecialite nomSpecialite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demande_specialite_id")
    private DemandeAdmission demandeSpecialite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialite_option_id")
    private Option specialiteOption;

}
