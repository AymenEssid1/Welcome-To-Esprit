package tn.esprit.springfever.Services.Implementation;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.Services.Interfaces.IServiceMcq;
import tn.esprit.springfever.entities.Mcq;
import tn.esprit.springfever.entities.Question;
import tn.esprit.springfever.repositories.McqRepository;
import tn.esprit.springfever.repositories.QuestionRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ServiceMcqImpl implements IServiceMcq {

    @Autowired
    private McqRepository mcqRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public Mcq addMcq(Mcq mcq) {
        return mcqRepository.save(mcq);
    }

    @Override
    public List<Mcq> getAllMcqs() {
        return mcqRepository.findAll();
    }

    @Override
    public String deleteMcq(Long idMcq) {
        Mcq existingMcq = mcqRepository.findById(idMcq).orElse(null);
        if (existingMcq != null) {
            mcqRepository.delete(existingMcq);
            return "Mcq with ID " + idMcq + " deleted successfully";
        }
        return "Mcq with ID " + idMcq + " not found";
    }

    @Override
    public Mcq updateMcq(Long idMcq, Mcq mcq) {
        Mcq existingMcq = mcqRepository.findById(idMcq).orElse(null);
        if (existingMcq != null) {
            existingMcq.setMcqTitle(mcq.getMcqTitle());
            existingMcq.setDuration(mcq.getDuration());
            existingMcq.setQuestions(mcq.getQuestions());
            mcqRepository.save(existingMcq);
        }
        return existingMcq;
    }

    @Override
    public Mcq getMcq(Long idMcq) {
        return mcqRepository.findById(idMcq).orElse(null);
    }

    @Override
    public Mcq generateMcq(String diplomaTitle) throws IOException {
        // Tokenize the diploma title
        InputStream modelIn = new FileInputStream("en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);
        Tokenizer tokenizer = new TokenizerME(model);
        String[] tokens = tokenizer.tokenize(diplomaTitle);

        // Retrieve questions containing any of the keywords
        List<Question> matchingQuestions = questionRepository.findByKeywords(tokens);

        // Randomly select up to 5 questions from the matching questions
        Collections.shuffle(matchingQuestions);
        int numQuestions = Math.min(5, matchingQuestions.size());
        List<Question> selectedQuestions = matchingQuestions.subList(0, numQuestions);

        // Create a new MCQ
        Mcq mcq = new Mcq();
        mcq.setMcqTitle(diplomaTitle);
        mcq.setDuration(60); // 1 hour

        // Add the selected questions to the MCQ
        mcq.setQuestions(selectedQuestions);

        // Save the MCQ to the database
        return mcqRepository.save(mcq);


    }



}
