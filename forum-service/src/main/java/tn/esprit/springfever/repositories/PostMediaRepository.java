package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.PostMedia;

@EnableJpaRepositories
public interface PostMediaRepository extends JpaRepository<PostMedia,Long> {

}