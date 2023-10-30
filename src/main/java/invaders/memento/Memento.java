package invaders.memento;

import invaders.entities.Player;
import invaders.gameobject.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Memento {
    private final List<GameObject> gameObjectsState;
    private final Player playerState;

    public Memento(List<GameObject> gameObjects, Player player) {
        this.gameObjectsState = new ArrayList<>(gameObjects);
        this.playerState = player.cloneState();
    }

    public List<GameObject> getGameObjectsState() {
        return gameObjectsState;
    }

    public Player getPlayerState() {
        return playerState;
    }
}
