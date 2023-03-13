package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.springfever.entities.Project;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


import java.util.List;
import java.util.Optional;
@EnableJpaRepositories

public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query("SELECT p.presentation, n.softskillsNote, n.hardskillsNote, n.presentationNote, n.consistencyNote, n.originalityNote, n.contentNote, n.relevanceNote, n.comment "
            + "FROM Project p JOIN Note n ON p.presentation = CAST(n.presentationNote AS string)")
    List<Object[]> findAllProjectsAndNotes();

}
