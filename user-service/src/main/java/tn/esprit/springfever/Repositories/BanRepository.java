package tn.esprit.springfever.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.springfever.entities.Ban;


public interface BanRepository extends JpaRepository<Ban, Long> {
}
