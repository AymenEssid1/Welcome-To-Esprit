package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.UserInterest;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface InterestRepository extends JpaRepository<UserInterest,Long> {
    public UserInterest findByUserAndTopic(Long user, String Topic);
    public List<UserInterest> findByTopicIn(List<String> topics);
    public List<UserInterest> findByUser(Long user);
}
