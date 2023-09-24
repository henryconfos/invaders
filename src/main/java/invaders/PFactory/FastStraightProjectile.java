package invaders.PFactory;

import invaders.physics.Vector2D;
import javafx.scene.image.Image;

import java.io.File;

public class FastStraightProjectile implements Projectile {
    private Vector2D position;
    private final Image image;
    private final double width = 10;
    private final double height = 15;

    public FastStraightProjectile(Vector2D p){
        this.position = p;
        this.image = new Image(new File("src/main/resources/player.png").toURI().toString(), width, height, true, true);
    }
    @Override
    public void move() {
        position.setY(position.getY() - 2);
    }

    @Override
    public Image getImage() {
        return this.image;
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
        return position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    @Override
    public void start() {
        // Initialization logic if any, otherwise leave empty
    }

    @Override
    public void update() {
        move();
    }
}
