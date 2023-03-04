package tn.esprit.springfever.Repositories;

import tn.esprit.springfever.entities.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievmentRepo extends JpaRepository<Achievement, Long>  {

}
