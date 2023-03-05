package tn.esprit.springfever.Services.Implementation;




import io.github.flashvayne.chatgpt.service.ChatgptService;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DocumentCategorizer;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.doccat.FeatureGenerator;
import opennlp.tools.doccat.NGramFeatureGenerator;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.ObjectStreamUtils;
import opennlp.tools.util.Span;
import opennlp.tools.util.StringUtil;

import opennlp.tools.util.featuregen.TokenFeatureGenerator;






import lombok.extern.slf4j.Slf4j;
import opennlp.tools.tokenize.Tokenizer;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.Services.Interfaces.IServiceMcq;
import tn.esprit.springfever.Services.Interfaces.IStringsimilarity;
import tn.esprit.springfever.entities.Mcq;
import tn.esprit.springfever.entities.Question;
import tn.esprit.springfever.repositories.McqRepository;
import tn.esprit.springfever.repositories.QuestionRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Slf4j
public class ServiceMcqImpl implements IServiceMcq {

    @Autowired
    private McqRepository mcqRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    IStringsimilarity iStringsimilarity ;
    @Autowired
    ChatgptService chatgptService ;

    @Override
    public Mcq addMcq(Mcq mcq) {
        return mcqRepository.save(mcq);
    }

    @Override
    @Cacheable(cacheNames = "getAllMcqs")
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
        InputStream modelIn = new FileInputStream("M:\\piSpring\\welcome-to-esprit\\evaluation-service\\en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);
        Tokenizer tokenizer = new TokenizerME(model);
        String[] tokens = tokenizer.tokenize(diplomaTitle);
        // Retrieve questions containing any of the keywords
        List<Question> matchingQuestions   =new ArrayList<>();
        //= questionRepository.findByKeywords(tokens);
        for(String s : tokens) {
            List<Question> listeQuestionsFromS = questionRepository.findByKeywords(s) ;
            for (Question q : listeQuestionsFromS) {
                if(!((matchingQuestions.contains(q)))){
                    matchingQuestions.add(q) ;
                }
            }
        }

        // Compute the cosine similarity between the embeddings of the tokens and the embeddings of the questions
        Map<Question, Double> questionSimilarities = new HashMap<>();
        for (Question question : questionRepository.findAll()) {
            double similarity =  iStringsimilarity.calculateSimilarity(diplomaTitle,question.getEnnonce());
            /*
            double similarity = Double.parseDouble(chatgptService.sendMessage("give me only without desciption (without any word just value) a similarity  score" +
                    " between the  diploma : " + diplomaTitle + "nd the question : "+
                    question.getEnnonce() ));
            System.out.println(similarity);
             */
            questionSimilarities.put(question, similarity);
        }
        // ************* Print the question similarities
        for (Map.Entry<Question, Double> entry : questionSimilarities.entrySet()) {
            Question question = entry.getKey();
            double similarity = entry.getValue();
            log.info("Question: " + question.getEnnonce());
            log.info("Similarity: " + similarity);
        }
        // Sort the questions by similarity in descending order
        List<Map.Entry<Question, Double>> sortedQuestions = new ArrayList<>(questionSimilarities.entrySet());
        Collections.sort(sortedQuestions, (a, b) -> Double.compare(b.getValue(), a.getValue()));
        // Select up to 5 questions with the highest similarity
        List<Question> selectedQuestions = new ArrayList<>();
        int numQuestions = Math.min(5, sortedQuestions.size());
        for (int i = 0; i < numQuestions; i++) {
            selectedQuestions.add(sortedQuestions.get(i).getKey());
        }
        // Create a new MCQ
        Mcq mcq = new Mcq();
        mcq.setMcqTitle(diplomaTitle);
        mcq.setDuration(60); // 1 hour
        // Add the selected questions to the MCQ
        mcq.setQuestions(selectedQuestions);
        // Set the MCQ for each selected question
        for (Question question : selectedQuestions) {
            question.getMcqs().add(mcq);
         }
        mcqRepository.save(mcq) ;
        // Save the MCQ to the database
        return mcq;
    }



}
