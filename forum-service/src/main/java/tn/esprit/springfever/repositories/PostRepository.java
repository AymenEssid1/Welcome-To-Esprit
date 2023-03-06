package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Post;


import java.util.List;

@EnableJpaRepositories
public interface PostRepository extends JpaRepository<Post,Long> {
    public List<Post> findByUser(Long id);

    //public List<Object[]> countPostsByUser();


    @Query(value = "SELECT p.user FROM (SELECT p.id, p.user, COALESCE(l.numLikes, 0) + COALESCE(c.numComments, 0) AS total FROM posts p LEFT JOIN (SELECT l.post_id, COUNT() AS numLikes FROM likes l GROUP BY l.post_id) l ON p.id = l.post_id LEFT JOIN (SELECT c.post_id, COUNT() AS numComments FROM comments c GROUP BY c.post_id) c ON p.id = c.post_id) post ORDER BY post.total DESC LIMIT 1", nativeQuery = true)
    public List<Long> countPostsByLikesAndComments();



}