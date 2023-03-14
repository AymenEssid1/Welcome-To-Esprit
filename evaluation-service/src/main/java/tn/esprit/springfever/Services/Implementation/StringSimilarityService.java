package tn.esprit.springfever.Services.Implementation;


import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;

import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
 import tn.esprit.springfever.Services.Interfaces.IStringsimilarity;

import java.io.*;
import java.util.*;

@Service
@Slf4j
public class StringSimilarityService implements IStringsimilarity {





    public double calculateSimilarity(String diplomaTitle, String question) throws IOException {
        if (diplomaTitle == null || diplomaTitle.isEmpty() || question == null || question.isEmpty()) {
            throw new IllegalArgumentException("Input strings cannot be null or empty");
        }

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse");
        log.info(props.getProperty("pos.model"));

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation diplomaAnnotation = new Annotation(diplomaTitle);
        pipeline.annotate(diplomaAnnotation);
        List<CoreMap> diplomaSentences = diplomaAnnotation.get(CoreAnnotations.SentencesAnnotation.class);

        Annotation questionAnnotation = new Annotation(question);
        pipeline.annotate(questionAnnotation);
        List<CoreMap> questionSentences = questionAnnotation.get(CoreAnnotations.SentencesAnnotation.class);

        double maxSimilarity = 0.0;
        for (CoreMap diplomaSentence : diplomaSentences) {
            SemanticGraph diplomaDependencies = diplomaSentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
            if (diplomaDependencies == null) {
                continue;
            }
            for (CoreMap questionSentence : questionSentences) {
                SemanticGraph questionDependencies = questionSentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
                if (questionDependencies == null) {
                    continue;
                }
                double similarity = compareSemanticGraphs(diplomaDependencies, questionDependencies);
                if (similarity > maxSimilarity) {
                    maxSimilarity = similarity;
                }
            }
        }
       return  Math.min(1.0, Math.max(0.0, maxSimilarity));
    }

    private double compareSemanticGraphs(SemanticGraph graph1, SemanticGraph graph2) {
        Set<IndexedWord> vertices1 = graph1.vertexSet();
        Set<IndexedWord> vertices2 = graph2.vertexSet();
        int numVertices1 = vertices1.size();
        int numVertices2 = vertices2.size();
        log.info("graph1" + graph1 );

        log.info("vertices1" + vertices1 );
        log.info("numVertices1" + vertices1.size() );


        int numCommonVertices = 0;
        for (IndexedWord vertex1 : vertices1) {
            for (IndexedWord vertex2 : vertices2) {
                if (vertex1.equals(vertex2)) {
                    numCommonVertices++;
                    break;
                }
            }
        }
        double similarity = (double) numCommonVertices / (numVertices1 + numVertices2 - numCommonVertices);
        log.info("similarity" + similarity );
        return similarity;
    }


}


