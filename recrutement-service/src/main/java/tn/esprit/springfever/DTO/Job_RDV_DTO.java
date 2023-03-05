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
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
public class Job_RDV_DTO implements Serializable {

    private Long idJobRDV;
    private Long userId;
    private Long user2Id;
     private LocalDateTime appointmentDate;
    private String salleRdv;
    @Enumerated(EnumType.STRING)
    private RDV_Type typeRDV;
    private Long jobApplicationId;
    private Long entretienId;

    public Job_RDV_DTO(){

    }
    public Job_RDV_DTO(Long idJobRDV,String salleRdv){
        this.idJobRDV=idJobRDV;
        this.salleRdv=salleRdv;
    }
    public Job_RDV_DTO(Long idJobRDV,Long userId,Long user2Id , LocalDateTime appointmentDate,String salleRdv,RDV_Type typeRDV, Long
                       jobApplicationId, Long entretienId){
        this.idJobRDV=idJobRDV;
        this.userId=userId;
        this.userId=user2Id;
        this.appointmentDate=appointmentDate;
        this.salleRdv=salleRdv;
        this.typeRDV=typeRDV;
        this.jobApplicationId=jobApplicationId;
        this.entretienId=entretienId;
    }



}
