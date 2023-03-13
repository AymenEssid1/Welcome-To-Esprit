package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.springfever.DTO.UserDTO;
import tn.esprit.springfever.entities.Participant;

import java.util.List;

public interface ParticpantRepository extends JpaRepository<Participant,Long> {
}
