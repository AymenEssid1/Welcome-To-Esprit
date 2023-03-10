package tn.esprit.springfever.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import tn.esprit.springfever.model.Cursus;
import tn.esprit.springfever.model.Diplome;
import tn.esprit.springfever.model.Niveau;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

    @Enumerated(EnumType.STRING)
    private TypeDemande typeDemande;

    @Enumerated(EnumType.STRING)
    private Diplome diplome;


    @Column
    @Enumerated(EnumType.STRING)
    private Niveau niveau;

    @Column
    @Enumerated(EnumType.STRING)
    private Cursus cursus;

    @Size(max = 20)
    private String nomParent;

    @Size(max = 20)
    private String prenomParent;

    @NotBlank
    @Size(max = 50)
    @Email
    private String mailParent;

    @NotBlank
    @Size(max = 8)
    @Size(min = 8)
    @NumberFormat
    private String telParent;



    @PrePersist
    public void setDateAdmission() {
        this.dateAdmission = LocalDate.now();
    }

    @OneToOne
    @JoinColumn(name = "condidat")
    @JsonIgnore
    private User condidat;

    @ManyToOne
    @JoinColumn(name = "evaluateur")
    @JsonIgnore
    private User evaluateur ;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "demandeRdv")
    @JsonIgnore
    private RDV rdvDemande;







}
