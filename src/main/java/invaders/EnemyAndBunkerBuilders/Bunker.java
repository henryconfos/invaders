package invaders.EnemyAndBunkerBuilders;

import invaders.BunkerStates.BunkerState;
import invaders.GameObject;
import invaders.physics.Collider;
import invaders.physics.Vector2D;
import invaders.rendering.Animator;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

public class Bunker implements Renderable, GameObject, Collider {
    protected Vector2D position;
    private final Animator anim = null;
    protected double width;
    protected double height;

    protected Image image;

    protected BunkerState state;
    @Override
    public void start() {

    }

    @Override
    public void update() {

    }

    @Override
    public Image getImage() {
        return state.getImage(this.width, this.height);
    }
    public void changeState() {
        this.state = state.nextState();
    }

    public BunkerState getState() {
        return state;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }
}
