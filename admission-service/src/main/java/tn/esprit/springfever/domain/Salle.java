package tn.esprit.springfever.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
public class Salle {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idsalle;

    @Column
    private Integer numSalle;

    @Column
    private String etat;


    @OneToMany(mappedBy = "rDVsalle")
    private Set<RDV> rDVsalleRDVs;



}
