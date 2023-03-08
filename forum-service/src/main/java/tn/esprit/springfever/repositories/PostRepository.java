package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Post;


import java.util.List;

@EnableJpaRepositories
public interface PostRepository extends JpaRepository<Post, Long> {
    public List<Post> findByUser(Long id);

    @Query(value = "SELECT post.*\n" +
            "FROM post\n" +
            "INNER JOIN (\n" +
            "    SELECT post_id, COUNT(*) as num_reports\n" +
            "    FROM report\n" +
            "    GROUP BY post_id\n" +
            "    HAVING num_reports > 5\n" +
            ") AS report_counts\n" +
            "ON post.id = report_counts.post_id;", nativeQuery = true)
    public List<Post> getReportedPosts();
}