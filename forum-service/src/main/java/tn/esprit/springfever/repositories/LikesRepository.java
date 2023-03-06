package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.Likes;
import tn.esprit.springfever.entities.Post;

import java.util.List;

@EnableJpaRepositories
public interface LikesRepository extends JpaRepository<Likes,Long> {
    public List<Likes> findByUser(Long user);
    public Likes findByUserAndPost(Long user, Post postId);
    public Likes findByUserAndComment(Long user, Comment commentId);
}
