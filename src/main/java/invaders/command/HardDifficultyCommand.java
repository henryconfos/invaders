package invaders.command;

import invaders.engine.GameEngine;

public class HardDifficultyCommand implements Command {
    private GameEngine gameEngine;

    public HardDifficultyCommand(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public void execute() {
        gameEngine.setDifficulty("hard");
    }
}
