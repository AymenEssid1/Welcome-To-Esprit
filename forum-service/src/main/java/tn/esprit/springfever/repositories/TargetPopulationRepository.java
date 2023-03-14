package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import tn.esprit.springfever.entities.TargetPopulation;

@EnableJpaRepositories
public interface TargetPopulationRepository extends JpaRepository<TargetPopulation, Long> {
    public TargetPopulation findByName(String name);
}
