package invaders.EnemyAndBunkerBuilders;

import invaders.GameObject;
import invaders.ShootingStrat.ShootingStrategy;
import invaders.engine.GameEngine;
import invaders.logic.Damagable;
import invaders.physics.Collider;
import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Animator;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

public class Enemy implements Moveable, Damagable, Renderable, GameObject, Collider {
    protected Vector2D position;
    private final Animator anim = null;
    protected double health = 100;
    private ShootingStrategy shootingStrategy;

    private int direction = 1;

    private double speedMulitpler = 1;

    protected double width;
    protected double height;

    protected Image image;
    protected String pType;
    @Override
    public void takeDamage(double amount) {

    }

    @Override
    public double getHealth() {
        return this.health;
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }

    public String getpType() {
        return pType;
    }
    public void setShootingStrategy(ShootingStrategy strategy) {
        this.shootingStrategy = strategy;
    }
    public void shoot(GameEngine gameEngine) {
        if (shootingStrategy != null) {
            shootingStrategy.shoot(this, gameEngine);
        }
    }

    @Override
    public void up() {
        position.setY(position.getY() - 1);
    }

    public double getSpeedMulitpler() {
        return speedMulitpler;
    }

    public void setSpeedMulitpler(double speedMulitpler) {
        this.speedMulitpler = speedMulitpler;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public void down() {
        position.setY(position.getY() + 8);
    }

    @Override
    public void left() {
        this.position.setX(this.position.getX() - 0.2*speedMulitpler);
    }

    @Override
    public void right() {
        this.position.setX(this.position.getX() + 0.2*speedMulitpler);
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
        return this.position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        if(this.direction == 1){
            right();
        }else{
            left();
        }

    }
}
