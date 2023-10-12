package invaders.BunkerStates;

import invaders.EnemyAndBunkerBuilders.Bunker;
import javafx.scene.image.Image;

public interface BunkerState {
    Image getImage(double width, double height);
    BunkerState nextState();
}
