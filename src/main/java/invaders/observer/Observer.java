package invaders.observer;

import invaders.engine.GameEngine;

public interface Observer {
    void update(String difficulty, GameEngine model);
}
