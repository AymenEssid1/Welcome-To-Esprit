package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.CommentMedia;

@EnableJpaRepositories
public interface CommentMediaRepository extends JpaRepository<CommentMedia,Long> {
}