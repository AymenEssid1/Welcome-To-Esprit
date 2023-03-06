package tn.esprit.springfever.entities;



import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@ToString
@Data

public class Job_Application implements Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)


    private Long Id_Job_Application ;
    //@NotBlank(message = "Le champ 'location_Cv' ne peut pas être vide.")
     private String location_Cv ;
    //@NotBlank(message = "Le champ 'location_LettreMotivation' ne peut pas être vide.")
    private String location_LettreMotivation;
    private String locationCandidate;
    @Column(nullable=true)
    private double LatitudeCandidate;
    @Column(nullable=true)
    private double LongitudeCandidate;

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

    @ManyToOne
    @JsonIgnore
    private User user ;

    public Job_Application(byte[] cv, byte[]lettreMotivation ) {
        this.cv = cv;
        this.lettreMotivation = lettreMotivation;
    }
    public Job_Application(){

    }
    public Job_Application(String location_Cv,String location_LettreMotivation){
        this.location_Cv=location_Cv;
        this.location_LettreMotivation=location_LettreMotivation;
    }

    public Job_Application(String location_Cv){
        this.location_Cv=location_Cv;
    }
    public Job_Application(String location_Cv,byte[] cv ){
        this.location_Cv=location_Cv;
        this.cv=cv;
    }
    public Job_Application(byte[]cv, String location_Cv){
        //this.location_LettreMotivation=location_LettreMotivation;
        this.location_Cv=location_Cv;
        //this.lettreMotivation=lettreMotivation;
        this.cv=cv;

    }

}
