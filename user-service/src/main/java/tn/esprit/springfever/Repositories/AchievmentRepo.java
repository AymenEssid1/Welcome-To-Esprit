package tn.esprit.springfever.Repositories;

import org.springframework.data.jpa.repository.Query;
import tn.esprit.springfever.entities.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

import tn.esprit.springfever.entities.User;

import java.util.List;

public interface AchievmentRepo extends JpaRepository<Achievement, Long>  {




}
