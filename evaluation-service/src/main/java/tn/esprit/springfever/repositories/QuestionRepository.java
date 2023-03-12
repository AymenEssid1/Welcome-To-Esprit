package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.springfever.entities.Mcq;
import tn.esprit.springfever.entities.Question;

import java.util.List;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByMcqsIdMcq(Long mcqs_idMcq) ;
    @Query("SELECT q FROM Question q WHERE q.ennonce LIKE %:keyword% OR q.option1 LIKE %:keyword% OR q.option2 LIKE %:keyword% OR q.option3 LIKE %:keyword% OR q.answer LIKE %:keyword%")
    List<Question> findByKeywords(@Param("keyword") String keywords);

 
}