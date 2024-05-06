package tools;

/**
 * Represents the result of a trial in the Hangman game.
 *
 * This class encapsulates the status of the trial (whether the guessed letter or word is correct),
 * and the current progression of the guessed word.
 *
 * @author Nassim Lattab
 * @date 2024-05-06
 */
public class TrialResult {
    private int status;
    private String progress;

    public TrialResult(int status, String progress) {
        this.status = status;
        this.progress = progress;
    }

    public int getStatus() {
        return status;
    }

    public String getProgress() {
        return progress;
    }

    @Override
    public String toString() {
        return status + ";" + progress;
    }
}
