package invaders.ShootingStrat;

import invaders.EnemyAndBunkerBuilders.Enemy;
import invaders.PFactory.Projectile;
import invaders.PFactory.SlowStraightProjectile;
import invaders.PFactory.SlowStraightProjectileFactory;
import invaders.engine.GameEngine;
import invaders.physics.Vector2D;

public class SlowProjectileStrategy implements ShootingStrategy{
    private SlowStraightProjectileFactory projectileFactory = new SlowStraightProjectileFactory();
    @Override
    public void shoot(Enemy enemy, GameEngine gameEngine) {
        Vector2D projectilePosition = new Vector2D(enemy.getPosition().getX(), enemy.getPosition().getY() + enemy.getHeight() + 15);
        Projectile projectile = projectileFactory.createProjectile(projectilePosition, 1);
        gameEngine.addNewObject(projectile);
        gameEngine.addNewGameObject(projectile);
    }
}
