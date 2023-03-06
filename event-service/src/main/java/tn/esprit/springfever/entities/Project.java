package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.time.LocalDateTime;
@Entity
@ToString
@Data

public class Project implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter

    private Long idProject;
    //@NotBlank(message = "Le URI ne peut pas être vide.")
    private String URI ;
    //@NotBlank(message = "La presentation ne peut pas être vide.")
    private String presentation;
    //@NotBlank(message = "La description ne peut pas être vide.")
    @Size(max = 50, message = "La description ne peut pas dépasser {max} caractères.")
    private String description ;
    //@NotBlank(message = "Le submitDate ne peut pas être vide.")
    private LocalDateTime submitDate ;

    @Lob
    private byte[] video;
    private String location_video ;
    @Lob
    @Column(name = "Rapport")
    private byte[] rapport;
    private String location_rapport ;

    @OneToOne(mappedBy="project")
    @JsonIgnore
    private Teams teams;

    @ManyToOne
    @JsonIgnore
    Note note;

    public Project(byte[] rapport) {
        this.rapport = rapport;
    }
    public Project(String location_rapport){
        this.location_rapport=location_rapport;

    }
    public Project(String location_rapport,byte[] rapport ){
        this.location_rapport=location_rapport;
        this.rapport=rapport;
    }
    public Project(byte[]rapport, String location_rapport){
        this.location_rapport=location_rapport;
        this.rapport=rapport;

    }


    public Project() {

    }
    public Project(String location_rapport,String location_video){
        this.location_rapport=location_rapport;
        this.location_video=location_video;
    }
}
