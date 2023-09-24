package invaders.PFactory;

import invaders.physics.Vector2D;

public class FastStraightProjectileFactory implements ProjectileFactory{
    @Override
    public Projectile createProjectile(Vector2D position, int dir) {
        return new FastStraightProjectile(position, dir);
    }
}
