package invaders.BunkerStates;

import javafx.scene.image.Image;

import java.io.File;

public class YellowState implements BunkerState {
    @Override
    public Image getImage() {
        return new Image(new File("src/main/resources/bunkerYellow.jpg").toURI().toString(), 100, 100, true, true);
    }

    @Override
    public BunkerState nextState() {
        return new RedState();
    }
}
