package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.springfever.entities.Note;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Project;

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






}
