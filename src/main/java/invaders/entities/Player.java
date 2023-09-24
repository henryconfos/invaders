package invaders.entities;

import invaders.PFactory.Projectile;
import invaders.PFactory.ProjectileFactory;
import invaders.PFactory.SlowStraightProjectileFactory;
import invaders.engine.GameEngine;
import invaders.logic.Damagable;
import invaders.physics.Collider;
import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Animator;
import invaders.rendering.Renderable;

import javafx.scene.image.Image;

import java.io.File;

public class Player implements Moveable, Damagable, Renderable, Collider {

    private final Vector2D position;
    private final Animator anim = null;
    private double health = 100;
    private boolean isShooting = false;
    private ProjectileFactory slowProjectileFactory = new SlowStraightProjectileFactory();
    private ProjectileFactory currentFactory = slowProjectileFactory;
    private Projectile currentProjectile;

    private final double width = 25;
    private final double height = 30;
    private final Image image;

    public Player(Vector2D position){
        this.image = new Image(new File("src/main/resources/player.png").toURI().toString(), width, height, true, true);
        this.position = position;
    }

    @Override
    public void takeDamage(double amount) {
        this.health -= amount;
    }

    public boolean isShooting() {
        return isShooting;
    }

    public void setShooting(boolean shooting) {
        isShooting = shooting;
    }

    @Override
    public double getHealth() {
        return this.health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }

    @Override
    public void up() {
        return;
    }

    @Override
    public void down() {
        return;
    }

    @Override
    public void left() {
        this.position.setX(this.position.getX() - 1);
    }

    @Override
    public void right() {
        this.position.setX(this.position.getX() + 1);
    }

    public void shoot(GameEngine model){
        if(!isShooting){
            Vector2D newPosition = new Vector2D(this.position.getX(), this.position.getY());
            currentProjectile = currentFactory.createProjectile(newPosition, -1);
            model.addRenderable(currentProjectile);
            model.addGameObject(currentProjectile);
            isShooting = true;
        }

    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

}
