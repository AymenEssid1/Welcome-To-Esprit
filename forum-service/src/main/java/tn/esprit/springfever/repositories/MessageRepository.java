package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Message;


import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface MessageRepository extends JpaRepository<Message,Long> {
    //public List<Message> findBySenderAndReceiver(int user);
}