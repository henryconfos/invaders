package invaders.PFactory;

import invaders.physics.Vector2D;

public class SlowStraightProjectileFactory implements ProjectileFactory{
    @Override
    public Projectile createProjectile(Vector2D position) {
        return new SlowStraightProjectile(position);
    }
}
