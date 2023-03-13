package tn.esprit.springfever.processor;

import java.io.IOException;
import java.io.StringReader;
import java.util.function.Consumer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * This class defines a Lucene analyzer that is applied on the input string in
 * {@link com.vader.sentiment.analyzer.SentimentAnalyzer}.
 *
 * @author Animesh Pandey
 */
class InputAnalyzer implements InputAnalyzerInterface {
    /**
     * This function applies a Lucene tokenizer that splits a string into a tokens.
     *
     * @param inputString   The input string to be pre-processed with Lucene tokenizer
     * @param tokenizer     The tokenizer to use for processing the input string
     * @param tokenConsumer The consumer of the tokens
     * @throws IOException if Lucene's tokenizer encounters any error
     */
    protected void tokenize(final String inputString, final Tokenizer tokenizer,
                            final Consumer<String> tokenConsumer) throws IOException {
        tokenizer.setReader(new StringReader(inputString));

        try (TokenStream tokenStream = new LengthFilter(tokenizer, 2, Integer.MAX_VALUE)) {
            final CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();

            while (tokenStream.incrementToken()) {
                tokenConsumer.accept(charTermAttribute.toString());
            }

            tokenStream.end();
        }
    }

    /**
     * Performs tokenization using Lucene's {@link WhitespaceTokenizer}, which tokenizes from the white spaces.
     * {@inheritDoc}
     */
    @Override
    public void keepPunctuation(final String inputString, final Consumer<String> tokenConsumer) throws IOException {
        tokenize(inputString, new WhitespaceTokenizer(), tokenConsumer);
    }

    /**
     * Performs tokenization using Lucene's {@link StandardTokenizer}, which tokenizes from white space
     * as well as removed any punctuations.
     * {@inheritDoc}
     */
    @Override
    public void removePunctuation(final String inputString, final Consumer<String> tokenConsumer) throws IOException {
        tokenize(inputString, new StandardTokenizer(), tokenConsumer);
    }
}

