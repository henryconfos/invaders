package invaders.engine;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import invaders.BunkerStates.RedState;
import invaders.EnemyAndBunkerBuilders.Bunker;
import invaders.EnemyAndBunkerBuilders.BunkerBuilder;
import invaders.EnemyAndBunkerBuilders.Enemy;
import invaders.EnemyAndBunkerBuilders.EnemyBuilder;
import invaders.GameObject;
import invaders.PFactory.Projectile;
import invaders.ShootingStrat.FastProjectileStrategy;
import invaders.ShootingStrat.SlowProjectileStrategy;
import invaders.entities.EntityView;
import invaders.entities.Player;
import invaders.physics.Collider;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GameEngine {

	private List<GameObject> gameobjects;
	private List<Renderable> renderables;
	private List<Renderable> newGameRenderable = new ArrayList<>();
	private List<GameObject> newGameObject = new ArrayList<>();
	private Player player;
	private int tCount;

	private ArrayList<JSONObject> gameList = new ArrayList<>();
	private ArrayList<JSONObject> playerList = new ArrayList<>();
	private ArrayList<JSONObject> bunkersList = new ArrayList<>();
	private ArrayList<JSONObject> enemiesList = new ArrayList<>();

	private boolean left;
	private boolean init = false;
	private boolean right;
	private  boolean isLoaded;

	private int wHeight;
	private int wWidth;
	private Image eImage = new Image(new File("src/main/resources/enemy.png").toURI().toString(), 100, 100, true, true);
	private Image bImage = new Image(new File("src/main/resources/bunkerGreen.jpg").toURI().toString(), 100, 100, true, true);

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

		// Check if game config has loaded, then load in elements to the screen!
		if(!init){
			if(this.isLoaded){
				this.init = true;
				populateEnemys();
				populateBunkers();
			}
		}

		if (!hasEnemies()) {
			System.out.println("You win!");
			Stage stage = (Stage) gWindow.getScene().getWindow();
			stage.close();
			return;
		}

		Random random = new Random();
		tCount++;
		for(GameObject go: gameobjects){
			go.update();
			if(tCount % 250 == 0){
				if (go instanceof Enemy && random.nextInt(100) < 10) {
					((Enemy) go).shoot(this);
				}
			}
		}

		renderables.addAll(newGameRenderable);
		gameobjects.addAll(newGameObject);
		newGameObject.clear();
		newGameRenderable.clear();

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
								player.setShooting(false);
								break;
							}
						}
				}
				if(ro.getPosition().getY() >= 380){
					for (EntityView view : gWindow.getEntityViews()) {
						if (view.matchesEntity(ro)) {
							view.markForDelete();
							break;
						}
					}
				}

			}

			// Check if enemy has reached the bottom of the screen.
			if(ro instanceof Enemy){
				if(ro.getPosition().getY() >= 380){
					System.out.println("You Loose! An enemy reached the end!!");
					Stage stage = (Stage) gWindow.getScene().getWindow();
					stage.close();
				}
			}

			// Check if player has run out of lives.
			if(ro instanceof Player){
				if(player.getHealth() <= 0.0){
					System.out.println("You Loose! You ran out of lives");
					Stage stage = (Stage) gWindow.getScene().getWindow();
					stage.close();
				}
			}

			// Handle Enemy Movement!!!
			if(ro instanceof Enemy){
				if (((Enemy) ro).getDirection() == 1 && ro.getPosition().getX() + ro.getWidth() >= 598) {
					for(Renderable changeRo: renderables){
						if(changeRo instanceof Enemy){
							((Enemy) changeRo).down();
							((Enemy) changeRo).setDirection(-1);
						}
					}
					return;
				} else if (((Enemy) ro).getDirection() == -1 && ro.getPosition().getX() <= 10) {
					for(Renderable changeRo: renderables){
						if(changeRo instanceof Enemy){
							((Enemy) changeRo).down();
							((Enemy) changeRo).setDirection(1);
						}
					}
					return;
				}
			}



			// Check for projectile with the enemys
			if (ro instanceof Projectile) {
				Collider projectileCollider = (Collider) ro;
				for (Renderable enemyRo : renderables) {
					if (enemyRo instanceof Enemy) {
						Collider enemyCollider = (Collider) enemyRo;
						if (projectileCollider.isColliding(enemyCollider)) {
							for (EntityView view : gWindow.getEntityViews()) {
								// Get em off my screen!!!!
								if (view.matchesEntity(enemyRo)) {
									view.markForDelete();
									for(Renderable changeRo: renderables){
										if(changeRo instanceof Enemy){
											((Enemy) enemyRo).setSpeedMulitpler(((Enemy) enemyRo).getSpeedMulitpler()+0.1);
										}
									}
								}
								if (view.matchesEntity(ro)) {
									view.markForDelete();
									player.setShooting(false);
								}
							}
						}
					}
				}
			}

			// Check if player is hit by projectile.
			if (ro instanceof Projectile) {
				Collider projectileCollider = (Collider) ro;
				if (projectileCollider.isColliding(player)) {
					player.setHealth(player.getHealth()-1);
					System.out.println(player.getHealth());
					for (EntityView view : gWindow.getEntityViews()) {
						if (view.matchesEntity(ro)) {
							view.markForDelete();
						}
					}
				}
			}

			// Check if bunker is hit by projectile
			if (ro instanceof Projectile) {
				Collider projectileCollider = (Collider) ro;
				for (Renderable bunkerRo : renderables) {
					if (bunkerRo instanceof Bunker) {
						Collider bunkerCollider = (Collider) bunkerRo;
						if (projectileCollider.isColliding(bunkerCollider)) {

							// If red state get rid of bunker:
							if(((Bunker) bunkerRo).getState() instanceof RedState){
								for (EntityView view : gWindow.getEntityViews()) {
									if (view.matchesEntity(bunkerRo)) {
										view.markForDelete();
										player.setShooting(false);
									}
								}
							}

							((Bunker) bunkerRo).changeState();

							for (EntityView view : gWindow.getEntityViews()) {
								if (view.matchesEntity(ro)) {
									view.markForDelete();
									player.setShooting(false);
								}
							}
						}
					}
				}
			}

			// Check if an enemy hits a bunker:
			if (ro instanceof Enemy) {
				Collider enemyCollider = (Collider) ro;
				for (Renderable bunkerRo : renderables) {
					if (bunkerRo instanceof Bunker) {
						Collider bunkerCollider = (Collider) bunkerRo;
						if (enemyCollider.isColliding(bunkerCollider)) {
							for (EntityView view : gWindow.getEntityViews()) {
								if (view.matchesEntity(ro)) {
									view.markForDelete();
								}
							}
						}
					}
				}
			}


		}
	}

	public List<Renderable> getRenderables(){
		return renderables;
	}

	public void populateEnemys(){

		for(JSONObject e: enemiesList ){
			JSONObject positionObject = (JSONObject) e.get("position");
			String projectile = (String) e.get("projectile");

			long xPos = (long) positionObject.get("x");
			long yPos = (long) positionObject.get("y");

			Enemy enemy = new EnemyBuilder().setPosition(new Vector2D(xPos, yPos)).setWidth(40).setHeight(40).setImage(eImage).setPType(projectile).build();

			if ("fast_straight".equals(enemy.getpType())) {
				enemy.setShootingStrategy(new FastProjectileStrategy());
			} else if ("slow_straight".equals(enemy.getpType())) {
				enemy.setShootingStrategy(new SlowProjectileStrategy());
			}

			renderables.add(enemy);
			gameobjects.add(enemy);
		}

	}

	public void populateBunkers(){
		for(JSONObject e: bunkersList ){
			JSONObject positionObject = (JSONObject) e.get("position");

			long xPos = (long) positionObject.get("x");
			long yPos = (long) positionObject.get("y");

			JSONObject sizeObject = (JSONObject) e.get("size");

			long xSize = (long) sizeObject.get("x");
			long ySize = (long) sizeObject.get("y");

			Bunker bunker = new BunkerBuilder().setPosition(new Vector2D(xPos, yPos)).setWidth(xSize).setHeight(ySize).setImage(bImage).build();

			renderables.add(bunker);
			gameobjects.add(bunker);
		}
	}

	public boolean hasEnemies(){
		for (Renderable ro : renderables) {
			if (ro instanceof Enemy) {
				return true;
			}
		}
		return false;
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

	public void removeRenderable(Renderable renderable) {
		this.renderables.remove(renderable);
	}

	public void addNewObject(Renderable renderable){
		this.newGameRenderable.add(renderable);
	}

	public void addNewGameObject(GameObject go){
		this.newGameObject.add(go);
	}

	public void addGameObject(GameObject go) {
		this.gameobjects.add(go);
	}

	public void removeGameObject(GameObject go) {
		this.gameobjects.remove(go);
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
