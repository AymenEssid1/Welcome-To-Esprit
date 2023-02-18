package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.AdMedia;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface AdMediaRepository extends JpaRepository<AdMedia,Long> {
}