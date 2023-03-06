package tn.esprit.springfever.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
public class User {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @Column
    private String etatuser;




    @OneToOne
    @JsonIgnore
    private  DemandeAdmission DemandeAdmissionStudent ;


    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DemandeAdmission> demandeAdmissionsEvaluateur;

}
