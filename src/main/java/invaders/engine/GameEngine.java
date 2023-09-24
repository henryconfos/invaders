package invaders.engine;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import invaders.GameObject;
import invaders.PFactory.Projectile;
import invaders.entities.EntityView;
import invaders.entities.Player;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GameEngine {

	private List<GameObject> gameobjects;
	private List<Renderable> renderables;
	private Player player;

	private ArrayList<JSONObject> gameList = new ArrayList<>();
	private ArrayList<JSONObject> playerList = new ArrayList<>();
	private ArrayList<JSONObject> bunkersList = new ArrayList<>();
	private ArrayList<JSONObject> enemiesList = new ArrayList<>();

	private boolean left;
	private boolean right;
	private  boolean isLoaded;

	private int wHeight;
	private int wWidth;

	public GameEngine(String config){
		// read the config here
		gameobjects = new ArrayList<GameObject>();
		renderables = new ArrayList<Renderable>();

		player = new Player(new Vector2D(200, 380));
		this.isLoaded = loadInConfig(config);
		renderables.add(player);
	}

	/**
	 * Updates the game/simulation
	 */
	public void update(GameWindow gWindow){
		movePlayer();
		for(GameObject go: gameobjects){
			go.update();
		}

		// ensure that renderable foreground objects don't go off-screen
		for(Renderable ro: renderables){
			if(!ro.getLayer().equals(Renderable.Layer.FOREGROUND)){
				continue;
			}

			if(ro.getPosition().getX() + ro.getWidth() >= 640) {
				ro.getPosition().setX(639-ro.getWidth());
			}

			if(ro.getPosition().getX() <= 0) {
				ro.getPosition().setX(1);
			}

			if(ro.getPosition().getY() + ro.getHeight() >= 400) {
				ro.getPosition().setY(399-ro.getHeight());
			}

			if(ro.getPosition().getY() <= 0) {
				ro.getPosition().setY(1);
			}
			if (ro instanceof Projectile) {
				if(ro.getPosition().getY() <= 1.0){
						for (EntityView view : gWindow.getEntityViews()) {
							if (view.matchesEntity(ro)) {
								view.markForDelete();
								break;
							}
						}
				}

			}
		}
	}

	public List<Renderable> getRenderables(){
		return renderables;
	}


	public void leftReleased() {
		this.left = false;
	}

	public void rightReleased(){
		this.right = false;
	}

	public void leftPressed() {
		this.left = true;
	}
	public void rightPressed(){
		this.right = true;
	}

	public boolean getLoadStatus(){ return this.isLoaded; }

	public ArrayList<JSONObject> getGameList() {
		return gameList;
	}

	public ArrayList<JSONObject> getPlayerList() {
		return playerList;
	}

	public ArrayList<JSONObject> getBunkersList() {
		return bunkersList;
	}

	public ArrayList<JSONObject> getEnemiesList() {
		return enemiesList;
	}

	public boolean shootPressed(){
		player.shoot(this);
		return true;
	}

	private void movePlayer(){
		if(left){
			player.left();
		}

		if(right){
			player.right();
		}
	}

	public void addRenderable(Renderable renderable) {
		this.renderables.add(renderable);
	}

	public void addGameObject(GameObject go) {
		this.gameobjects.add(go);
	}

	public boolean loadInConfig(String path) {
		// Creating ArrayLists for Game, Player, Bunkers, and Enemies

		JSONParser parser = new JSONParser();

		try (FileReader reader = new FileReader(path)) {
			JSONObject config = (JSONObject) parser.parse(reader);

			this.gameList.add((JSONObject) config.get("Game"));
			this.playerList.add((JSONObject) config.get("Player"));
			this.bunkersList.addAll((JSONArray) config.get("Bunkers"));
			this.enemiesList.addAll((JSONArray) config.get("Enemies"));


			return true;

		} catch (IOException | ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
}
