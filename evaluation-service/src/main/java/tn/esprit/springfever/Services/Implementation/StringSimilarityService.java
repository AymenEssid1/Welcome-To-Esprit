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
import org.springframework.stereotype.Service;
 import tn.esprit.springfever.Services.Interfaces.IStringsimilarity;

import java.io.*;
import java.util.*;

@Service
public class StringSimilarityService implements IStringsimilarity {





    public double calculateSimilarity(String diplomaTitle, String question) throws IOException {
        if (diplomaTitle == null || diplomaTitle.isEmpty() || question == null || question.isEmpty()) {
            throw new IllegalArgumentException("Input strings cannot be null or empty");
        }

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse");
        System.out.println(props.getProperty("pos.model"));
        System.out.println(props);

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
        System.out.println("graph1" + graph1 );

        System.out.println("vertices1" + vertices1 );
        System.out.println("numVertices1" + vertices1.size() );


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
        System.out.println("similarity" + similarity );

        return similarity;
    }







}




 /*

@Service
public class StringSimilarityService implements IStringsimilarity {

    private final StanfordCoreNLP pipeline;

    public StringSimilarityService() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse");
        pipeline = new StanfordCoreNLP(props);
    }

    public double calculateSimilarity(String diplomaTitle, String question) throws IOException {
        Annotation diplomaAnnotation = new Annotation(diplomaTitle);
        pipeline.annotate(diplomaAnnotation);
        List<CoreMap> diplomaSentences = diplomaAnnotation.get(CoreAnnotations.SentencesAnnotation.class);
        List<String> diplomaLemmas = new ArrayList<String>();
        for (CoreMap diplomaSentence : diplomaSentences) {
            SemanticGraph diplomaDependencies = diplomaSentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
            diplomaLemmas.addAll(getLemmas(diplomaDependencies));
        }

        Annotation questionAnnotation = new Annotation(question);
        pipeline.annotate(questionAnnotation);
        List<CoreMap> questionSentences = questionAnnotation.get(CoreAnnotations.SentencesAnnotation.class);
        List<String> questionLemmas = new ArrayList<String>();
        for (CoreMap questionSentence : questionSentences) {
            SemanticGraph questionDependencies = questionSentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
            questionLemmas.addAll(getLemmas(questionDependencies));
        }

        List<String> intersection = new ArrayList<String>(diplomaLemmas);
        intersection.retainAll(questionLemmas);

        double similarity = (double) intersection.size() / ((double) diplomaLemmas.size() + (double) questionLemmas.size() - (double) intersection.size());
        return similarity;
    }

    private List<String> getLemmas(SemanticGraph dependencies) {
        List<String> lemmas = new ArrayList<String>();
        for (SemanticGraphEdge edge : dependencies.edgeIterable()) {
            if (!edge.isExtra()) {
                CoreLabel source = CoreLabel.wordFromString(edge.getSource().word());
                CoreLabel target = CoreLabel.wordFromString(edge.getTarget().word());
                String sourceLemma = source.get(CoreAnnotations.LemmaAnnotation.class);
                String targetLemma = target.get(CoreAnnotations.LemmaAnnotation.class);
                lemmas.add(sourceLemma);
                lemmas.add(targetLemma);
            }
        }
        return lemmas;
    }
}

  */

