
package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.springfever.entities.Event;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;
@EnableJpaRepositories

public interface EventRepository extends JpaRepository<Event,Long> {
}
