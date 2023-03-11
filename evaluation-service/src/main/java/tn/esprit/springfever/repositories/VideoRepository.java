package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.springfever.entities.Video;


@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

 Video findByName(String name);
 boolean existsByName(String name);


}