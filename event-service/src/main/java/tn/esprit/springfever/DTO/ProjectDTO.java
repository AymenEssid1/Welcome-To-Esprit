package tn.esprit.springfever.DTO;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.springfever.entities.CategorieEvent;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@ToString

public class ProjectDTO implements Serializable {
    private Long idProject;
    @NotBlank

    private String URI ;
    @NotBlank
    private String presentation;
    @NotBlank
    private String description ;
    @NotBlank
    private Date submitDate ;

    public ProjectDTO(Long idProject, String URI, String presentation, String description,Date submitDate) {
        this.idProject = idProject;
        this.URI = URI;
        this.presentation = presentation;
        this.description = description;
        this.submitDate = submitDate;
    }
}
