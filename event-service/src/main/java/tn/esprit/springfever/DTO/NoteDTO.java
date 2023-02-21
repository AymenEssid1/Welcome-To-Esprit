package tn.esprit.springfever.DTO;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.springfever.entities.User;
import tn.esprit.springfever.entities.CategorieEvent;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@ToString
public class NoteDTO implements Serializable {
    private Long idNote;
    @NotBlank
    private float ProjectNote ;
    @NotBlank
    private float PresentationNote;
    @NotBlank
    private float SoftSkillsNote ;
    @NotBlank
    private String comment ;
    @NotBlank
    private Date submitDate ;

    public NoteDTO(Long idNote, float ProjectNote, float PresentationNote, float SoftSkillsNote,String comment,Date submitDate) {
        this.idNote = idNote;
        this.ProjectNote = ProjectNote;
        this.PresentationNote = PresentationNote;
        this.SoftSkillsNote = SoftSkillsNote;
        this.comment = comment;
        this.submitDate = submitDate;
    }
}
