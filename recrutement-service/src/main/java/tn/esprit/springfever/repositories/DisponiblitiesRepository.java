package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.springfever.entities.Disponibilites;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DisponiblitiesRepository extends JpaRepository<Disponibilites,Long> {


        @Query("SELECT d FROM Disponibilites d WHERE d.user.id = :userId")
        List<Disponibilites> findByUserId(@Param("userId") Long userId);






}
