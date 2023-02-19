package tn.esprit.springfever.entities;



import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@ToString
@Data

public class Job_Application implements Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)


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

    @ManyToOne
    @JsonIgnore
    private User user ;

    public Job_Application(byte[] cv, byte[]lettreMotivation ) {
        this.cv = cv;
        this.lettreMotivation = lettreMotivation;
    }
    public Job_Application(){

    }

}
