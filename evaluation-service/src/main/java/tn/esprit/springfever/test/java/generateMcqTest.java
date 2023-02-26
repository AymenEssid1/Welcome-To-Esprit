package tn.esprit.springfever.test.java;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.springfever.DTO.ClaimDTO;
import tn.esprit.springfever.EvaluationService;
import tn.esprit.springfever.Services.Implementation.ServiceClaimsImpl;
import tn.esprit.springfever.Services.Implementation.ServiceMcqImpl;
import tn.esprit.springfever.Services.Interfaces.ClaimMapper;
import tn.esprit.springfever.entities.Claim;
import tn.esprit.springfever.entities.Mcq;
import tn.esprit.springfever.entities.Question;
import tn.esprit.springfever.repositories.ClaimRepository;
import tn.esprit.springfever.repositories.McqRepository;
import tn.esprit.springfever.repositories.QuestionRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EvaluationService.class)
public class generateMcqTest {

    @Autowired
    private ServiceMcqImpl mcqService;

    @MockBean
    private McqRepository mcqRepository;

    @MockBean
    private QuestionRepository questionRepository;

    @Test
    public void testGenerateMcq() throws Exception {
        /*
        // Mock the dependencies
        Question question1 = new Question();
        question1.setEnnonce("What is Java?");
        question1.setOption1("A programming language");
        question1.setOption2("A coffee brand");
        question1.setOption3("An operating system");
        question1.setAnswer("A programming language");

        Question question2 = new Question();
        question2.setEnnonce("What is Spring?");
        question2.setOption1("A season of the year");
        question2.setOption2("A framework for Java");
        question2.setOption3("A type of mattress");
        question2.setAnswer("A framework for Java");

        List<Question> matchingQuestions = Arrays.asList(question1, question2);
        when(questionRepository.findByKeywords(any())).thenReturn(matchingQuestions);

        Mcq mcq = new Mcq();
        mcq.setMcqTitle("Diploma in Computer Science with a specialization in Web Development");
        mcq.setDuration(60);
        mcq.setQuestions(matchingQuestions);
        when(mcqRepository.save(any())).thenReturn(mcq);

        // Call the method under test
        Mcq result = mcqService.generateMcq("Diploma in Computer Science with a specialization in Web Development");
        // test infos
             System.out.print(mcq.getMcqTitle());
             mcq.getQuestions().forEach(q-> System.out.print(q.toString()));
        // Verify the result
        assertNotNull(result);
        assertEquals("Diploma in Computer Science with a specialization in Web Development", result.getMcqTitle());
        assertEquals(60, result.getDuration());
        assertEquals(2, result.getQuestions().size());
        assertTrue(result.getQuestions().contains(question1));
        assertTrue(result.getQuestions().contains(question2));
    }

         */
    }
}
