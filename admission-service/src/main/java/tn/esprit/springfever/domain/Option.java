package tn.esprit.springfever.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Entity
@Table(name = "\"option\"")
@Getter
@Setter
public class Option {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOption;

    @Column
    private String nomOption;

    @OneToMany(mappedBy = "specialiteOption")
    private Set<Specialite> specialiteOptionSpecialites;

}
