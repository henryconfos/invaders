package invaders.BunkerStates;

import javafx.scene.image.Image;

import java.io.File;

public class GreenState implements BunkerState {
    @Override
    public Image getImage(double width, double height) {
        return new Image(new File("src/main/resources/bunkerGreen.jpg").toURI().toString(), width, height, false, true);
    }

    @Override
    public BunkerState nextState() {
        return new YellowState();
    }
}
