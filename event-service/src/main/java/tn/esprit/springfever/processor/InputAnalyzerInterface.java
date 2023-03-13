package tn.esprit.springfever.processor;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * This interface defines methods that use two methods for splitting up a raw string.
 *
 * @author Animesh Pandey
 */
interface InputAnalyzerInterface {
    /**
     * This method performs tokenization without punctuation removal.
     *
     * @param inputString   The input string to be pre-processed with Lucene tokenizer
     * @param tokenConsumer The consumer of the tokens
     * @throws IOException if Lucene's analyzer encounters any error
     */
    void keepPunctuation(String inputString, Consumer<String> tokenConsumer) throws IOException;

    /**
     * This method performs tokenization with punctuation removal.
     *
     * @param inputString   The input string to be pre-processed with Lucene tokenizer
     * @param tokenConsumer The consumer of the tokens
     * @throws IOException if Lucene's analyzer encounters any error
     */
    void removePunctuation(String inputString, Consumer<String> tokenConsumer) throws IOException;
}

