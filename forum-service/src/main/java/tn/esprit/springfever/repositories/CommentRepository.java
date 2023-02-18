package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.Post;


import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface CommentRepository extends JpaRepository<Comment,Long> {
    public List<Comment> findByPost(Post id);
}

