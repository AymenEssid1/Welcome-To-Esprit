package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.Report;

@EnableJpaRepositories
public interface ReportRepository extends JpaRepository<Report,Long> {
    public Report findByUserAndPost(Long user, Post post);
    public Report findByUserAndComment(Long user, Comment comment);
}
