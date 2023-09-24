package invaders.PFactory;

import invaders.physics.Vector2D;

public class FastStraightProjectileFactory implements ProjectileFactory{
    @Override
    public Projectile createProjectile(Vector2D position) {
        return new FastStraightProjectile(position);
    }
}
