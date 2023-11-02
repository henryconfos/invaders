package invaders.command;

import invaders.engine.GameEngine;

public class MediumDifficultyCommand implements Command {
    private GameEngine gameEngine;

    public MediumDifficultyCommand(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public void execute() {
        gameEngine.setDifficulty("medium");
    }
}
