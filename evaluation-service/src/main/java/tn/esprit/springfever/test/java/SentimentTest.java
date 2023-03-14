package tn.esprit.springfever.test.java;

  import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.springfever.EvaluationService;
import tn.esprit.springfever.Services.Implementation.ServiceClaimsImpl;
 import tn.esprit.springfever.analyzer.SentimentPolarities;
 import tn.esprit.springfever.repositories.ClaimRepository;
 import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
 import tn.esprit.springfever.analyzer.SentimentAnalyzer;
 @ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EvaluationService.class)


public class SentimentTest {

    @Autowired
    private ServiceClaimsImpl entityService;

    @MockBean
    private ClaimRepository entityRepository;

    @Test
    public void testDetectSentiment() {

        /*
        Properties props = new Properties();
         props.setProperty("annotators", "tokenize, ssplit");
        props.setProperty("parse.model", "M:/piSpring/welcome-to-esprit/evaluation-service/src/main/resources/englishPCFG.ser");
        props.setProperty("sentiment.model", "M:/piSpring/welcome-to-esprit/evaluation-service/src/main/resources/sentiment.ser");
        // props.setProperty("sentiment.model", "M:/piSpring/welcome-to-esprit/evaluation-service/src/main/resources/englishPCFG.ser.gz");

        // Create the pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        // Create an empty Annotation just with the given text
        Annotation annotation = new Annotation("good");

        // Run all Annotators on this text
        pipeline.annotate(annotation);

String sentiment = "" ;
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            // Get the sentiment for the sentence
            System.out.println("***"+sentence.get(SentimentCoreAnnotations.SentimentClass.class));
            sentiment = (sentence.get(SentimentCoreAnnotations.SentimentClass.class));
              System.out.println("Sentiment: " + sentiment + ", Score: " );
        }
         System.out.print(sentiment);

*/

        final SentimentPolarities sentimentPolarities = SentimentAnalyzer.getScoresFor(
                "Your team was incredibly efficient and professional in " +
                "handling my issue, and I am extremely satisfied with the resolution that was provided , i am hyper satisfied ");
        System.out.println(sentimentPolarities);

    }


}
