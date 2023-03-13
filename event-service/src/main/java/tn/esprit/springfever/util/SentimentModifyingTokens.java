package tn.esprit.springfever.util;

/**
 * This is list of tokens that modifying the valence of tokens of a string if found in the same string.
 *
 * @author Animesh Pandey
 */
//CHECKSTYLE.OFF: Javadoc*
public enum SentimentModifyingTokens {
    NEVER("never"),
    SO("so"),
    THIS("this"),
    AT("at"),
    LEAST("least"),
    KIND("kind"),
    OF("of"),
    VERY("very"),
    BUT("but"),
    EXCLAMATION_MARK("!"),
    QUESTION_MARK("?"),
    CONTRACTION("n't");

    private final String value;

    SentimentModifyingTokens(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
//CHECKSTYLE.ON: Javadoc*

