package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.springfever.entities.ImageData;

import java.util.Optional;

public interface ImageDataRepository extends JpaRepository<ImageData, Long> {
    Optional<ImageData> findByName(String name);
}