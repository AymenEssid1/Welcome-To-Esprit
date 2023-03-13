package tn.esprit.springfever.DTO;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.springfever.entities.Note;
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
    private float projectNote ;
    @NotBlank
    private float presentationNote;
    @NotBlank
    private float softskillsNote ;
    @NotBlank
    private String comment ;
    @NotBlank
    private Date submitDate ;



/*

    private float hardskillsNote;

    private float consistencyNote;
    private float originalityNote;
    private float contentNote;
    private float relevanceNote;



    private Long idProject;

    private String description;

    private Long idTeam;
    private String QRcertificat;
    private String NiveauEtude;
    private String NameTeam;

*/

    public NoteDTO(Note note , Long idNote, float projectNote, float presentationNote, float softskillsNote, String comment, Date submitDate) {
        this.idNote = idNote;
        this.projectNote = projectNote;
        this.presentationNote = presentationNote;
        this.softskillsNote = softskillsNote;
        this.comment = comment;
        this.submitDate = submitDate;


/*
        this.idNote = note.getIdNote();
        this.softskillsNote = note.getSoftskillsNote();
        this.hardskillsNote = note.getHardskillsNote();
        this.presentationNote = note.getPresentationNote();
        this.consistencyNote = note.getConsistencyNote();
        this.originalityNote = note.getOriginalityNote();
        this.contentNote = note.getContentNote();
        this.relevanceNote = note.getRelevanceNote();
        this.comment = note.getComment();
        this.submitDate = note.getSubmitDate();

        this.projectNote = note.getProjectNote().getIdProject();
        this.idProject = note.getProjectNote().getIdProject();

        this.description = note.getProjectNote().getProjectDescription();
        this.idTeam = note.getProjectNote().getTeamId().getIdTeam();
        this.QRcertificat = note.getProjectNote().getTeamId().getQrcertificat();
        this.NiveauEtude = note.getProjectNote().getTeamId().getNiveauEtude();
        this.NameTeam = note.getProjectNote().getTeamId().getNameTeam();
*/

    }


    public NoteDTO() {

    }
}
