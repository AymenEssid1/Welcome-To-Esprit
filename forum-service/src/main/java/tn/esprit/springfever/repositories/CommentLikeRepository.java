package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.CommentLike;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface CommentLikeRepository extends JpaRepository<CommentLike,Long> {
    public List<CommentLike> findLikeByComment(int comment);
}