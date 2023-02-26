package tn.esprit.springfever.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import   tn.esprit.springfever.Services.Interfaces.*;
import tn.esprit.springfever.entities.Question;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/questions")
@Slf4j
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
    @PostMapping(value="/importQuestions" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<List<Question>> importQuestions(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            List<Question> questions =questionService.importQuestionsFromExcel(file);
            log.info("questions imported successfully");
            return ResponseEntity.ok(questions);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
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
