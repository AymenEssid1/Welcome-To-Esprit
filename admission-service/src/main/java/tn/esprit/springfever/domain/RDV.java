package tn.esprit.springfever.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Getter
@Setter
public class RDV {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRDV;


    @Column
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rdvuser_id")
    private User rDVuser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rdvsalle_id")
    private Salle rDVsalle;

    @OneToOne(cascade ={CascadeType.ALL} )
    @JoinColumn(name = "rdvDemande")
    private DemandeAdmission demandeRdv;

}
