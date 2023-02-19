package tn.esprit.springfever.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tn.esprit.springfever.enums.RDV_Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@ToString
@Data
public class Job_RDV implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter

    private Long ID_Job_DRV;

    @ManyToOne
    @JoinColumn(name = "Candidate_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "Jury_ID")
    private User user2;

    private Date date_Rdv ;
    private String salle_Rdv ;
    @Enumerated(EnumType.STRING)
    private RDV_Type Type_RDV;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Job_Application")
    private Job_Application jobApplication;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entretien_id")
    private Entretien entretien;






}
