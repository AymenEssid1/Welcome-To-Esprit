package tn.esprit.springfever.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.springfever.entities.Log;

@Repository
public interface LogRepository extends CrudRepository<Log,Integer> {



}
