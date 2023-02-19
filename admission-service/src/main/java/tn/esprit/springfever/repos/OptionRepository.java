package tn.esprit.springfever.repos;

import tn.esprit.springfever.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OptionRepository extends JpaRepository<Option, Long> {
}
