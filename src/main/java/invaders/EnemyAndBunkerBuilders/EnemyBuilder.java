package invaders.EnemyAndBunkerBuilders;

import invaders.physics.Vector2D;
import javafx.scene.image.Image;

public class EnemyBuilder {
    private Vector2D position;
    private double width;
    private double height;

    private String pType;

    private Image image;
    public EnemyBuilder setPosition(Vector2D position) {
        this.position = position;
        return this;
    }

    public EnemyBuilder setWidth(double width) {
        this.width = width;
        return this;
    }

    public EnemyBuilder setHeight(double height) {
        this.height = height;
        return this;
    }

    public EnemyBuilder setImage(Image image){
        this.image = image;
        return this;
    }

    public EnemyBuilder setPType(String p){
        this.pType = p;
        return this;
    }

    public Enemy build() {
        Enemy enemy = new Enemy();
        enemy.position = this.position;
        enemy.width = this.width;
        enemy.height = this.height;
        enemy.image = this.image;
        enemy.pType = this.pType;
        return enemy;
    }
}
