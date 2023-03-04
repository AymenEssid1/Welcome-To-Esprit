package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.RedisHash;
import tn.esprit.springfever.entities.TargetPopulation;

@EnableJpaRepositories
@RedisHash
public interface TargetPopulationRepository extends JpaRepository<TargetPopulation, Long> {
    public TargetPopulation findByName(String name);
}
