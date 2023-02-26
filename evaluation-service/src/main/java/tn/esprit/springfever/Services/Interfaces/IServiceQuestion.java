package tn.esprit.springfever.Services.Interfaces;




import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.entities.Faq;
import tn.esprit.springfever.entities.Question;

import java.io.IOException;
import java.util.List;

public interface IServiceQuestion {


 Question addQuestion(Question question);
 List<Question> getAllQuestions();
 String deleteQuestion(Long idQuestion);
 Question updateQuestion(Long idQuestion, Question question);
 List<Question> getQuestionsByMcq(Long idMcq);
 Boolean AssignQuestionToMcq(Long Mcq , Long idQuestion) ;
 public List<Question> importQuestionsFromExcel(MultipartFile file) throws IOException;


}
