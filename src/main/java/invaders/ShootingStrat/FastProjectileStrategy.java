package invaders.ShootingStrat;

import invaders.EnemyAndBunkerBuilders.Enemy;
import invaders.PFactory.FastStraightProjectileFactory;
import invaders.PFactory.Projectile;
import invaders.engine.GameEngine;
import invaders.physics.Vector2D;

public class FastProjectileStrategy implements ShootingStrategy{
    private final FastStraightProjectileFactory projectileFactory = new FastStraightProjectileFactory();
    @Override
    public void shoot(Enemy enemy, GameEngine gameEngine) {
        Vector2D projectilePosition = new Vector2D(enemy.getPosition().getX(), enemy.getPosition().getY() + enemy.getHeight() + 15);
        Projectile projectile = projectileFactory.createProjectile(projectilePosition, 2);
        gameEngine.addNewObject(projectile);
        gameEngine.addNewGameObject(projectile);
    }
}
