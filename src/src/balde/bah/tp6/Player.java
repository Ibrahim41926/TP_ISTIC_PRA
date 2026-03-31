package src.balde.bah.tp6;

public class Player {
    private String name;
    private int score;
    private long startTime;
    private long totalTime;
    private int correctAnswers;
    private int totalAnswers;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.correctAnswers = 0;
        this.totalAnswers = 0;
        this.totalTime = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public void startTurn() {
        this.startTime = System.currentTimeMillis();
    }

    public void endTurn() {
        if (startTime > 0) {
            this.totalTime += (System.currentTimeMillis() - startTime);
            this.startTime = 0;
        }
    }

    public long getTotalTime() {
        return totalTime + (startTime > 0 ? (System.currentTimeMillis() - startTime) : 0);
    }

    public void addCorrectAnswer() {
        correctAnswers++;
        totalAnswers++;
        addScore(10); // 10 points par bonne réponse
    }

    public void addWrongAnswer() {
        totalAnswers++;
        addScore(-2); // -2 points par mauvaise réponse
    }

    public double getAccuracy() {
        return totalAnswers > 0 ? (double) correctAnswers / totalAnswers * 100 : 0;
    }

    public String getFormattedTime() {
        long seconds = getTotalTime() / 1000;
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }

    @Override
    public String toString() {
        return String.format("%s - Score: %d - Précision: %.1f%%",
                           name, score, getAccuracy());
    }
}