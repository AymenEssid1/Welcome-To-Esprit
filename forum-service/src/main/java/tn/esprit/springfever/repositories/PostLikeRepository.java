package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.PostLike;


import java.util.List;

@EnableJpaRepositories
public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
    public List<PostLike> findPostLikeByPost(Post post);
    public List<PostLike> findByUser(int id);
}