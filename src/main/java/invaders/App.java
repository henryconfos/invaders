package invaders;

import invaders.observer.GameSettings;
import javafx.application.Application;
import javafx.stage.Stage;
import invaders.engine.GameEngine;
import invaders.engine.GameWindow;

import java.util.Map;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GameSettings gameSettings = new GameSettings();

        GameEngine model = new GameEngine("src/main/resources/config_easy.json");
        model.addObserver(gameSettings);
        GameWindow window = new GameWindow(model);
        model.setWindow(window);
        window.run();

        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(window.getScene());
        primaryStage.show();

        window.run();
    }
}
