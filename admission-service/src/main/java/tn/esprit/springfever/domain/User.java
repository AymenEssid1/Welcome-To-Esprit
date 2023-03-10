package tn.esprit.springfever.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;

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

    @Size(max = 20)
    @Column
    private String etatuser;




    @OneToOne
    @JsonIgnore
    private  DemandeAdmission DemandeAdmissionStudent ;


    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private List<DemandeAdmission> demandeAdmissionsEvaluateur;

}
