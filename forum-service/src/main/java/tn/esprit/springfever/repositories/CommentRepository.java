package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.Post;


import java.util.List;

@EnableJpaRepositories
public interface CommentRepository extends JpaRepository<Comment, Long> {
    public List<Comment> findByPost(Post id);

    @Query(value = "SELECT comment.*\n" +
            "FROM comment\n" +
            "INNER JOIN (\n" +
            "    SELECT comment_id, COUNT(*) as num_reports\n" +
            "    FROM report\n" +
            "    GROUP BY comment_id\n" +
            "    HAVING num_reports > 5\n" +
            ") AS report_counts\n" +
            "ON comment.id = report_counts.comment_id;", nativeQuery = true)
    public List<Comment> getReportedComments();
}

