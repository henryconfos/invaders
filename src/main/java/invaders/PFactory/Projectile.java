package invaders.PFactory;

import invaders.GameObject;
import invaders.physics.Collider;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;

public interface Projectile extends Renderable, GameObject, Collider {
    void move();
}
