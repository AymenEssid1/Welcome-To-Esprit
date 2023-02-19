package tn.esprit.springfever.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import   tn.esprit.springfever.Services.Interfaces.*;
import tn.esprit.springfever.entities.Mcq;
import tn.esprit.springfever.entities.Question;

import java.util.List;
@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private IServiceQuestion questionService;


    @PostMapping("/addQuestion")
    public ResponseEntity<Question> addQuestion(@RequestBody Question question) {
        return new ResponseEntity<>(questionService.addQuestion(question), HttpStatus.CREATED);
    }

    @GetMapping("/getAllQuestions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return new ResponseEntity<>(questionService.getAllQuestions(), HttpStatus.OK);
    }

    @DeleteMapping("/deleteQuestion/{idQuestion}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long idQuestion) {
        return new ResponseEntity<>(questionService.deleteQuestion(idQuestion), HttpStatus.OK);
    }

    @PutMapping("/updateQuestion/{idQuestion}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long idQuestion, @RequestBody Question question) {
        return new ResponseEntity<>(questionService.updateQuestion(idQuestion, question), HttpStatus.OK);
    }

    @GetMapping("/getQuestionsByMcq/{idMcq}")
    public ResponseEntity<List<Question>> getQuestionsByMcq(@PathVariable Long idMcq) {
        return new ResponseEntity<>(questionService.getQuestionsByMcq(idMcq), HttpStatus.OK);
    }

    @PostMapping("/assignQuestionToMcq/{mcqId}/{questionId}")
    public ResponseEntity<?> assignQuestionToMcq(@PathVariable Long mcqId, @PathVariable Long questionId) {
        Boolean result = questionService.AssignQuestionToMcq(mcqId, questionId);
        if (result) {
            return ResponseEntity.ok("Question was assigned to MCQ successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to assign question to MCQ");
        }
    }
}
