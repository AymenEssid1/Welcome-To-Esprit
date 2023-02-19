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

public class Job_Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private  Long Id_Job_Category ;
    private String Name_Category ;
    @OneToMany(mappedBy = "jobCategory", cascade = CascadeType.ALL)
    private List<Job_Offer> jobOffers;



}
