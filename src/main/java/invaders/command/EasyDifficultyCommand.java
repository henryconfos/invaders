package invaders.command;

import invaders.engine.GameEngine;

public class EasyDifficultyCommand implements Command {
    private GameEngine gameEngine;

    public EasyDifficultyCommand(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public void execute() {
        gameEngine.setDifficulty("easy");
    }
}

