package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tn.esprit.springfever.enums.RDV_Type;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@ToString
@Data
public class Job_RDV implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter

    private Long ID_Job_DRV;


    private Long jury;

    //private Date date_Rdv ;
    //@NotBlank(message = "La salle de RDV ne peut pas être vide.")
    private String salle_Rdv ;
    //@NotNull(message = "Le type de RDV ne peut pas être vide.")
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private RDV_Type Type_RDV;
    //@NotNull(message = "La date de rendez-vous ne peut pas être vide.")
    //@FutureOrPresent(message = "La date de rendez-vous doit être dans le futur ou le présent.")
    @Column(name = "appointment_date")
    @JsonIgnore
    private LocalDateTime appointmentDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Id_Job_Application")
    @JsonIgnore
    private Job_Application jobApplication;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "disponibilites_candidat_id")
    @JsonIgnore
    private Disponibilites disponibilitesCandidat;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "disponibilites_jury_id")
    @JsonIgnore
    private Disponibilites disponibilitesJury;



    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entretien_id")
    @JsonIgnore
    private Entretien entretien;








}
