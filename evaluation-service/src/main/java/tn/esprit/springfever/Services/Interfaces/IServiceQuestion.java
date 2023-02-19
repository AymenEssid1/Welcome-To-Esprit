package tn.esprit.springfever.Services.Interfaces;




import tn.esprit.springfever.entities.Question;

import java.util.List;

public interface IServiceQuestion {


 Question addQuestion(Question question);
 List<Question> getAllQuestions();
 String deleteQuestion(Long idQuestion);
 Question updateQuestion(Long idQuestion, Question question);
 List<Question> getQuestionsByMcq(Long idMcq);
 Boolean AssignQuestionToMcq(Long Mcq , Long idQuestion) ;

}
