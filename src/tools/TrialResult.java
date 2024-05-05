package tools;

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
