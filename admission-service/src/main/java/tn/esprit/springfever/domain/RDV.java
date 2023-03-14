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


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salle")
    private Salle salle;

    @OneToOne(cascade ={CascadeType.ALL} )
    @JoinColumn(name = "demande")
    private DemandeAdmission demande;

}
