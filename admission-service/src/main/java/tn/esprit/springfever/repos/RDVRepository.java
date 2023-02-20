package tn.esprit.springfever.repos;

import org.springframework.stereotype.Repository;
import tn.esprit.springfever.domain.RDV;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RDVRepository extends JpaRepository<RDV, Long> {
}
