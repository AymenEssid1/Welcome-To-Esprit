package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.PostViews;

import java.util.List;

@EnableJpaRepositories
public interface PostViewsRepository extends JpaRepository<PostViews,Long> {
    public PostViews findByPostAndUser(Post post, Long user);
}
