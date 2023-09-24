package invaders.EnemyAndBunkerBuilders;

import invaders.BunkerStates.GreenState;
import invaders.BunkerStates.RedState;
import invaders.BunkerStates.YellowState;
import invaders.physics.Vector2D;
import javafx.scene.image.Image;

public class BunkerBuilder {
    private Vector2D position;
    private double width;
    private double height;


    private Image image;
    public BunkerBuilder setPosition(Vector2D position) {
        this.position = position;
        return this;
    }

    public BunkerBuilder setWidth(double width) {
        this.width = width;
        return this;
    }

    public BunkerBuilder setHeight(double height) {
        this.height = height;
        return this;
    }

    public BunkerBuilder setImage(Image image){
        this.image = image;
        return this;
    }


    public Bunker build() {
        Bunker bunker = new Bunker();
        bunker.position = this.position;
        bunker.width = this.width;
        bunker.height = this.height;
        bunker.image = this.image;
        bunker.state = new GreenState();
        return bunker;
    }
}
