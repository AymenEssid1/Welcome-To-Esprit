package tn.esprit.springfever.repos;

import org.springframework.stereotype.Repository;
import tn.esprit.springfever.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
}
