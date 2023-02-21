package tn.esprit.springfever.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tn.esprit.springfever.enums.RDV_Type;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class Job_RDV_DTO implements Serializable {

    private Long idJobRDV;
    private Long userId;
    private Long user2Id;
    private Date dateRdv;
    private String salleRdv;
    @Enumerated(EnumType.STRING)
    private RDV_Type typeRDV;
    private Long jobApplicationId;
    private Long entretienId;



}
