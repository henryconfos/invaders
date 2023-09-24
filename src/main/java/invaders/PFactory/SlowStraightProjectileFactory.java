package invaders.PFactory;

import invaders.physics.Vector2D;

public class SlowStraightProjectileFactory implements ProjectileFactory{
    @Override
    public Projectile createProjectile(Vector2D position, int dir) {
        return new SlowStraightProjectile(position, dir);
    }
}
