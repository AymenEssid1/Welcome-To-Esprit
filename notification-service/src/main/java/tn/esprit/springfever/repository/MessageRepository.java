package tn.esprit.springfever.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entity.Notification;

import java.util.List;

@EnableJpaRepositories
public interface MessageRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiver(String receiver);
}

