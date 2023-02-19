package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.springfever.entities.Mcq;
import tn.esprit.springfever.entities.Question;

import java.util.List;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByMcqsIdMcq(Long mcqs_idMcq) ;
 
}