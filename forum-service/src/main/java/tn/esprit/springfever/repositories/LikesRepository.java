package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Likes;

import java.util.List;

@EnableJpaRepositories
public interface LikesRepository extends JpaRepository<Likes,Long> {
    public List<Likes> findByUser(Long user);
}
