package tn.esprit.springfever.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@ToString
@Data

public class Job_Offer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long Id_Job_Offer ;
    private String Subject ;

    @ManyToOne
    Job_Category jobCategory;

    @OneToMany(mappedBy = "jobOffer", cascade = CascadeType.ALL)
    private List<Job_Application> jobApplications;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image_JobOffer image;

}
