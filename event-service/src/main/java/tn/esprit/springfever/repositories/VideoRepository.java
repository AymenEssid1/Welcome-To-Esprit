package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.esprit.springfever.entities.Video;

import java.util.List;
import java.util.Optional;


@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    Video findByName(String name);
    boolean existsByName(String name);


}
