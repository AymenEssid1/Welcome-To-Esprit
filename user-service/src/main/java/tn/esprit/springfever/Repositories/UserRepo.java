package tn.esprit.springfever.Repositories;


import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.esprit.springfever.entities.RoleType;
import tn.esprit.springfever.entities.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Repository

public interface UserRepo extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    List<User> findByRolesRolenameAndCreationDateBetween(RoleType r, LocalDateTime s,LocalDateTime e);
    List<User> findByRolesRolename(RoleType r);
    List<User> findByCreationDateBetween(LocalDateTime startOfYear, LocalDateTime endOfYear);

    Boolean existsByEmail(String email);
    @Query (value = "SELECT u.*\n" +
            "FROM user u \n" +
            "INNER JOIN (\n" +
            "  SELECT p.user, SUM(l.num_likes + c.num_comments) AS total_likes_and_comments\n" +
            "  FROM post p\n" +
            "  LEFT JOIN (\n" +
            "    SELECT post_id, COUNT(*) AS num_likes\n" +
            "    FROM likes\n" +
            "    GROUP BY post_id\n" +
            "  ) l ON l.post_id = p.id\n" +
            "  LEFT JOIN (\n" +
            "    SELECT post_id, COUNT(*) AS num_comments\n" +
            "    FROM comment\n" +
            "    GROUP BY post_id\n" +
            "  ) c ON c.post_id = p.id\n" +
            "  GROUP BY p.user\n" +
            "  ORDER BY total_likes_and_comments DESC\n" +
            "  LIMIT 3\n" +
            ") t ON u.userid = t.user;",nativeQuery = true)
    List<User> mostLikedUsers();

//Statistics

    @Query (value = "SELECT COUNT(*)/DATEDIFF(MAX(created_at), MIN(created_at)) AS avg_posts_per_day FROM post",nativeQuery = true)
    List<Object> averagepostsperday();


    @Query(value ="SELECT COUNT(*) / (YEAR(CURDATE()) - YEAR(MIN(creation_date)) + 1) AS avg_users_per_year FROM user",nativeQuery = true)
    List<Object> averageUsersCreatedPerYear();


    @Query(value = "SELECT FLOOR(DATEDIFF(NOW(), dob)/365) AS age_range, COUNT(*) AS user_count\n" +
            "FROM user\n" +
            "GROUP BY age_range\n" +
            "ORDER BY age_range ASC",nativeQuery = true)
    List<Object> UsersByAge();

    @Query(value = "SELECT u.username\n" +
            "FROM user u\n" +
            "INNER JOIN Ban b ON u.userid = b.user_userid",nativeQuery = true)
    List<String> bannedUsers();
}
