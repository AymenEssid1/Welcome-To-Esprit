package tn.esprit.springfever.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tn.esprit.springfever.entities.Job_Offer;
import tn.esprit.springfever.entities.Job_RDV;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class Job_ApplicationDTO  {
    private Long Id_Job_Application ;

    @Lob
    @Column(name = "Candidate_cv")
    private byte[] cv;

    @Lob
    @Column(name = "Candidate_lettre_motivation")
    private byte[] lettreMotivation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_Job_Offer")
    private Job_Offer jobOffer;

    @OneToOne(mappedBy = "jobApplication", cascade = CascadeType.ALL, optional = true)
    private Job_RDV rdv;

    private Long user ;



}
