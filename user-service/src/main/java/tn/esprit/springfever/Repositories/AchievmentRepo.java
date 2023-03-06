package tn.esprit.springfever.Repositories;

import org.springframework.data.jpa.repository.Query;
import tn.esprit.springfever.entities.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

import tn.esprit.springfever.entities.User;

import java.util.List;

public interface AchievmentRepo extends JpaRepository<Achievement, Long>  {

     /*   @Query("SELECT u, COUNT(DISTINCT p.id) + COUNT(DISTINCT c.id) AS totalPostsAndComments " +
                "FROM User u " +
                "LEFT JOIN Post p ON p.user = u " +
                "LEFT JOIN Comment c ON c.user = u " +
                "GROUP BY u " +
                "ORDER BY totalPostsAndComments DESC")
        List<User> findTop3ByOrderByTotalPostsAndCommentsDesc();*/


}
