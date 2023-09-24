package invaders.ShootingStrat;

import invaders.EnemyAndBunkerBuilders.Enemy;
import invaders.engine.GameEngine;

public interface ShootingStrategy {
    void shoot(Enemy enemy, GameEngine gameEngine);
}
