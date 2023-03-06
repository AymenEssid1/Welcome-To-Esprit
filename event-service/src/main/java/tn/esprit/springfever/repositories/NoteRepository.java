package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.springfever.DTO.NoteDTO;
import tn.esprit.springfever.entities.Note;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Project;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@EnableJpaRepositories

public interface NoteRepository extends JpaRepository<Note,Long> {
    //Note getNoteById(Long idNote);

    List<Note> findAllByOrderByProjectNoteDesc();

    @Query("SELECT AVG(n.softskillsNote) FROM Note n")
    Float getAverageSoftskillsNote();

    @Query("SELECT AVG(n.presentationNote) FROM Note n")
    Float getAveragePresentationNote();

    @Query("SELECT AVG(n.consistencyNote) FROM Note n")
    Float getAverageConsistencyNote();

    @Query("SELECT AVG(n.originalityNote) FROM Note n")
    Float getAverageOriginalityNote();

    @Query("SELECT AVG(n.contentNote) FROM Note n")
    Float getAverageContentNote();

    @Query("SELECT AVG(n.relevanceNote) FROM Note n")
    Float getAverageRelevanceNote();








/*
    JdbcTemplate jdbcTemplate = null;


    public default List<NoteDTO> getNotesWithProjectAndTeamInfo() {
        String query = "SELECT " +
                "Note.idNote, " +
                "Note.softskillsNote, " +
                "Note.hardskillsNote, " +
                "Note.presentationNote, " +
                "Note.consistencyNote, " +
                "Note.originalityNote, " +
                "Note.contentNote, " +
                "Note.relevanceNote, " +
                "Note.comment, " +
                "Note.submitDate, " +
                "Note.projectNote, " +
                "Project.idProject, " +
                "Project.projectName, " +
                "Project.projectDescription, " +
                "Teams.idTeam, " +
                "Teams.qrcertificat, " +
                "Teams.niveauEtude, " +
                "Teams.nameTeam " +
                "FROM " +
                "Note " +
                "JOIN Project ON Note.projectNote = Project.idProject " +
                "JOIN Teams ON Project.teamId = Teams.idTeam";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            NoteDTO noteDTO = new NoteDTO();
            noteDTO.setIdNote(rs.getLong("idNote"));
            noteDTO.setSoftskillsNote(rs.getFloat("softskillsNote"));
            noteDTO.setHardskillsNote(rs.getFloat("hardskillsNote"));
            noteDTO.setPresentationNote(rs.getFloat("presentationNote"));
            noteDTO.setConsistencyNote(rs.getFloat("consistencyNote"));
            noteDTO.setOriginalityNote(rs.getFloat("originalityNote"));
            noteDTO.setContentNote(rs.getFloat("contentNote"));
            noteDTO.setRelevanceNote(rs.getFloat("relevanceNote"));
            noteDTO.setComment(rs.getString("comment"));
            noteDTO.setSubmitDate(rs.getDate("submitDate"));
            noteDTO.setProjectNote(rs.getLong("projectNote"));
            noteDTO.setIdProject(rs.getLong("idProject"));

            noteDTO.setDescription(rs.getString("projectDescription"));
            noteDTO.setIdTeam(rs.getLong("idTeam"));
            noteDTO.setQRcertificat(rs.getString("qrcertificat"));
            noteDTO.setNiveauEtude(rs.getString("niveauEtude"));
            noteDTO.setNameTeam(rs.getString("nameTeam"));
            return noteDTO;
        });
    }*/

}
