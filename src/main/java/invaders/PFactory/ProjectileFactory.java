package invaders.PFactory;

import invaders.physics.Vector2D;

public interface ProjectileFactory {
    Projectile createProjectile(Vector2D position, int dir);
}
