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

@Entity
@ToString
@Data

public class Project implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter

    private Long idProject;
    @NotBlank

    private String URI ;
    @NotBlank
    private String presentation;
    @NotBlank
    private String description ;
    @NotBlank
    private Date submitDate ;

    @OneToOne(mappedBy="project")
    @JsonIgnore
    private Teams teams;

    @ManyToOne
    @JsonIgnore
    Note note;

}
