package invaders.observer;

import invaders.ConfigReader;
import invaders.engine.GameEngine;
import javafx.scene.control.Label;
import org.json.simple.JSONObject;

public class GameSettings implements Observer{

    private int score = 0;
    private String time = "0:00";
    private Label scoreLabel;
    private Label timeLabel;
    public GameSettings(Label scoreLabel, Label timeLabel) {
        this.scoreLabel = scoreLabel;
        this.timeLabel = timeLabel;
    }


    @Override
    public void update(String difficulty, GameEngine model) {
        String configPath;
        switch (difficulty) {
            case "medium":
                configPath = "src/main/resources/config_medium.json";
                break;
            case "hard":
                configPath = "src/main/resources/config_hard.json";
                break;
            default:
                configPath = "src/main/resources/config_easy.json";
                break;
        }
        ConfigReader.parse(configPath);
        model.iGame();
    }

    @Override
    public void updateScore(int addedScore) {
        this.score += addedScore;
        scoreLabel.setText("Score: " + score);
    }

    @Override
    public void updateTime(String newTime) {
        this.time = newTime;
        timeLabel.setText("Time: " + time);
    }
}
