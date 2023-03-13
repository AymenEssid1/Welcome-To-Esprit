package tn.esprit.springfever.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.springfever.domain.Image;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String name);
}
