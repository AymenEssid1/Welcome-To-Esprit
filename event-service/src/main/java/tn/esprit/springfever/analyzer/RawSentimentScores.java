package tn.esprit.springfever.analyzer;

/**
 * This class defines the three types of raw sentiment scores which are non-normalized.
 *
 * @author Animesh Pandey
 */
public final class RawSentimentScores {
    /**
     * This is the raw positive sentiment score.
     */
    private final float positiveScore;

    /**
     * This is the raw negative sentiment score.
     */
    private final float negativeScore;

    /**
     * This is the raw neutral sentiment score.
     */
    private final float neutralScore;

    /**
     * Creates an object of this class and sets all the fields.
     *
     * @param positiveScore positive score
     * @param negativeScore negative score
     * @param neutralScore  neutral score
     */
    public RawSentimentScores(float positiveScore, float negativeScore, float neutralScore) {
        this.positiveScore = positiveScore;
        this.negativeScore = negativeScore;
        this.neutralScore = neutralScore;
    }

    public float getPositiveScore() {
        return positiveScore;
    }

    public float getNegativeScore() {
        return negativeScore;
    }

    public float getNeutralScore() {
        return neutralScore;
    }
}

