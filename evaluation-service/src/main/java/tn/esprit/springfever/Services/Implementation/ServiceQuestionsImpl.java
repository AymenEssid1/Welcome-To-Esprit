package tn.esprit.springfever.Services.Implementation;


import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.Services.Interfaces.IServiceQuestion;
import tn.esprit.springfever.entities.Faq;
import tn.esprit.springfever.entities.FaqCategory;
import tn.esprit.springfever.entities.Mcq;
import tn.esprit.springfever.entities.Question;
import tn.esprit.springfever.enums.Faq_Category_enum;
import tn.esprit.springfever.repositories.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ServiceQuestionsImpl implements IServiceQuestion {

    @Autowired
     QuestionRepository questionRepository;
    @Autowired
    McqRepository mcqRepository ;

    @Override
    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public String deleteQuestion(Long idQuestion) {
        Optional<Question> existingQuestion = questionRepository.findById(idQuestion);
        if (existingQuestion.isPresent()) {
            questionRepository.delete(existingQuestion.get());
            return "Question successfully deleted!";
        } else {
            return "Question not found.";
        }
    }

    @Override
    public Question updateQuestion(Long idQuestion, Question question) {
        Question existingQuestion = questionRepository.findById(idQuestion).orElse(null);
        if (existingQuestion!=null) {
            existingQuestion.setEnnonce(question.getEnnonce());
            existingQuestion.setOption1(question.getOption1());
            existingQuestion.setOption2(question.getOption2());
            existingQuestion.setOption3(question.getOption3());
            existingQuestion.setAnswer(question.getAnswer());

            return questionRepository.save(question);
        }
        return question ;
    }

    @Override
    public List<Question> getQuestionsByMcq(Long idMcq) {
        return questionRepository.findAllByMcqsIdMcq(idMcq);
    }



    @Override
    public Boolean AssignQuestionToMcq(Long mcqId, Long questionId) {
        Optional<Mcq> mcqOptional = mcqRepository.findById(mcqId);
        Optional<Question> questionOptional = questionRepository.findById(questionId);

        if (mcqOptional.isPresent() && questionOptional.isPresent()) {
            Mcq mcq = mcqOptional.get();
            Question question = questionOptional.get();

            if (!mcq.getQuestions().contains(question)) {
                mcq.getQuestions().add(question);
                mcqRepository.save(mcq);
                return true;
            }
        }


        return false;
    }

    @Override
    public List<Question> importQuestionsFromExcel(MultipartFile file) throws IOException {


        List<Question> questions = new ArrayList<>();

        // Load the Excel file using Apache POI
        Workbook workbook = new  XSSFWorkbook(file.getInputStream()) {
        };

        // Get the first sheet of the workbook
        Sheet sheet = workbook.getSheetAt(0);

        // Loop through the rows of the sheet
        for (Row row : sheet) {
            Question question = new Question();
            // Set the properties of the Question object based on the cell values in the row
            question.setEnnonce(row.getCell(0).getStringCellValue());
            question.setOption1(row.getCell(1).getStringCellValue());
            question.setOption2(row.getCell(2).getStringCellValue());
            question.setOption3(row.getCell(3).getStringCellValue());
            question.setAnswer(row.getCell(4).getStringCellValue());
            questions.add(question);
        }
        // Save the list of questions to the database
        questionRepository.saveAll(questions);
        return questions ;
    }


}

