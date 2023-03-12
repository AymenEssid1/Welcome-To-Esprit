package tn.esprit.springfever.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.springfever.entities.Ban;
import tn.esprit.springfever.entities.User;

import java.time.LocalDateTime;


public interface BanRepository extends JpaRepository<Ban, Long> {

   public Ban findByUserAndExpiryTimeAfter(User user, LocalDateTime LocalDateTime);
   public Ban findBanByUser(User u);
   @Modifying
   @Transactional
   @Query("DELETE FROM Ban b  WHERE b.id=:id")
   public void fasakh(Long id );
}
