package invaders.engine;

import java.util.ArrayList;
import java.util.List;

import invaders.ConfigReader;
import invaders.builder.BunkerBuilder;
import invaders.builder.Director;
import invaders.builder.EnemyBuilder;
import invaders.entities.EntityView;
import invaders.factory.EnemyProjectile;
import invaders.factory.PlayerProjectile;
import invaders.factory.Projectile;
import invaders.gameobject.Bunker;
import invaders.gameobject.Enemy;
import invaders.gameobject.GameObject;
import invaders.entities.Player;
import invaders.memento.Caretaker;
import invaders.memento.Memento;
import invaders.observer.Observer;
import invaders.rendering.Renderable;
import invaders.strategy.FastProjectileStrategy;
import invaders.strategy.SlowProjectileStrategy;
import org.json.simple.JSONObject;

/**
 * This class manages the main loop and logic of the game
 */
public class GameEngine {
	private List<GameObject> gameObjects = new ArrayList<>(); // A list of game objects that gets updated each frame
	private List<GameObject> pendingToAddGameObject = new ArrayList<>();
	private List<GameObject> pendingToRemoveGameObject = new ArrayList<>();

	private List<Renderable> pendingToAddRenderable = new ArrayList<>();
	private List<Renderable> pendingToRemoveRenderable = new ArrayList<>();
	private List<Observer> observers = new ArrayList<>();

	private List<Renderable> renderables =  new ArrayList<>();

	private Player player;
	private int score = 0;
	private long startTime;
	private long elapsedTime;
	private GameWindow window;
	private boolean windowSet = false;

	private boolean left;
	private boolean right;
	private int gameWidth;
	private int gameHeight;
	private int timer = 45;
	private final Caretaker caretaker = new Caretaker();

	public GameEngine(String config){
		// Read the config here
		setDifficulty("easy");
		ConfigReader.parse(config);
		this.startTime = System.currentTimeMillis();
		iGame();

	}

	/**
	 * Updates the game/simulation
	 */
	public void update(){
		timer+=1;

		movePlayer();

		elapsedTime = System.currentTimeMillis() - startTime;
		String formattedTime = formatTime(elapsedTime);
		notifyTimeObservers(formattedTime);

		for(GameObject go: gameObjects){
			go.update(this);
		}

		for (int i = 0; i < renderables.size(); i++) {
			Renderable renderableA = renderables.get(i);
			for (int j = i+1; j < renderables.size(); j++) {
				Renderable renderableB = renderables.get(j);

				if((renderableA.getRenderableObjectName().equals("Enemy") && renderableB.getRenderableObjectName().equals("EnemyProjectile"))
						||(renderableA.getRenderableObjectName().equals("EnemyProjectile") && renderableB.getRenderableObjectName().equals("Enemy"))||
						(renderableA.getRenderableObjectName().equals("EnemyProjectile") && renderableB.getRenderableObjectName().equals("EnemyProjectile"))){
				}else{
					if(renderableA.isColliding(renderableB) && (renderableA.getHealth()>0 && renderableB.getHealth()>0)) {
						renderableA.takeDamage(1);
						renderableB.takeDamage(1);
						System.out.println(renderableA);
						System.out.println(renderableB);

						if(renderableA instanceof EnemyProjectile && renderableB instanceof PlayerProjectile){
							if(((EnemyProjectile) renderableA).getStrategy() instanceof SlowProjectileStrategy){
								this.notifyScoreObservers(1);
							}
							if(((EnemyProjectile) renderableA).getStrategy() instanceof FastProjectileStrategy){
								this.notifyScoreObservers(2);
							}
						}

						if(renderableA instanceof Enemy && renderableB instanceof PlayerProjectile){
							if(((Enemy) renderableA).getProjectileStrategy() instanceof SlowProjectileStrategy){
								this.notifyScoreObservers(3);
							}
							if (((Enemy) renderableA).getProjectileStrategy() instanceof FastProjectileStrategy){
								this.notifyScoreObservers(4);
							}
						}

					}
				}
			}
		}


		// ensure that renderable foreground objects don't go off-screen
		int offset = 1;
		for(Renderable ro: renderables){
			if(!ro.getLayer().equals(Renderable.Layer.FOREGROUND)){
				continue;
			}
			if(ro.getPosition().getX() + ro.getWidth() >= gameWidth) {
				ro.getPosition().setX((gameWidth - offset) -ro.getWidth());
			}

			if(ro.getPosition().getX() <= 0) {
				ro.getPosition().setX(offset);
			}

			if(ro.getPosition().getY() + ro.getHeight() >= gameHeight) {
				ro.getPosition().setY((gameHeight - offset) -ro.getHeight());
			}

			if(ro.getPosition().getY() <= 0) {
				ro.getPosition().setY(offset);
			}
		}

	}

	public List<Renderable> getRenderables(){
		return renderables;
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}
	public List<GameObject> getPendingToAddGameObject() {
		return pendingToAddGameObject;
	}

	public List<GameObject> getPendingToRemoveGameObject() {
		return pendingToRemoveGameObject;
	}

	public List<Renderable> getPendingToAddRenderable() {
		return pendingToAddRenderable;
	}

	public List<Renderable> getPendingToRemoveRenderable() {
		return pendingToRemoveRenderable;
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

	public boolean shootPressed(){
		if(timer>45 && player.isAlive()){
			Projectile projectile = player.shoot();
			gameObjects.add(projectile);
			renderables.add(projectile);
			saveState();
			timer=0;
			return true;
		}
		return false;
	}

	private void movePlayer(){
		if(left){
			player.left();
		}

		if(right){
			player.right();
		}
	}

	public int getGameWidth() {
		return gameWidth;
	}

	public int getGameHeight() {
		return gameHeight;
	}

	public Player getPlayer() {
		return player;
	}

	public void addObserver(Observer observer) {
		observers.add(observer);
	}

	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}

	public void notifyObservers(String difficulty) {
		for (Observer observer : observers) {
			observer.update(difficulty, this);
		}
	}

	public void notifyScoreObservers(int addedScore) {
		for (Observer observer : observers) {
			observer.updateScore(addedScore);
		}
	}

	public void notifyTimeObservers(String newTime) {
		for (Observer observer : observers) {
			observer.updateTime(newTime);
		}
	}
	public void setWindow(GameWindow window) {
		this.window = window;
		this.windowSet = true;
	}

	public void setDifficulty(String difficulty) {
		System.out.println("here");
		notifyObservers(difficulty);
	}
	private String formatTime(long millis) {
		int seconds = (int) (millis / 1000) % 60;
		int minutes = (int) (millis / (1000 * 60));
		String addZero = "";
		if(seconds < 10){
			addZero = "0";
		}else{
			addZero = "";
		}
		return String.format(minutes + ":"+ addZero + seconds);
	}

	public void iGame(){
		resetGameState();
		// Get game width and height
		gameWidth = ((Long)((JSONObject) ConfigReader.getGameInfo().get("size")).get("x")).intValue();
		gameHeight = ((Long)((JSONObject) ConfigReader.getGameInfo().get("size")).get("y")).intValue();

		//Get player info
		this.player = new Player(ConfigReader.getPlayerInfo());
		renderables.add(player);


		Director director = new Director();
		BunkerBuilder bunkerBuilder = new BunkerBuilder();
		//Get Bunkers info
		for(Object eachBunkerInfo:ConfigReader.getBunkersInfo()){
			Bunker bunker = director.constructBunker(bunkerBuilder, (JSONObject) eachBunkerInfo);
			gameObjects.add(bunker);
			renderables.add(bunker);
		}


		EnemyBuilder enemyBuilder = new EnemyBuilder();
		//Get Enemy info
		for(Object eachEnemyInfo:ConfigReader.getEnemiesInfo()){
			Enemy enemy = director.constructEnemy(this,enemyBuilder,(JSONObject)eachEnemyInfo);
			gameObjects.add(enemy);
			renderables.add(enemy);
		}
	}

	private void resetGameState() {
		gameObjects.clear();
		pendingToAddGameObject.clear();
		pendingToRemoveGameObject.clear();
		renderables.clear();
		pendingToAddRenderable.clear();
		pendingToRemoveRenderable.clear();

		if(this.windowSet){
			for (EntityView entityView : this.window.getEntityViews()){
				entityView.markForDelete();
			}
		}

	}

	public void saveState() {
		caretaker.saveStateToMemento(new Memento(gameObjects, player));
	}

	public void undoState() {
		Memento previousState = caretaker.getStateFromMemento();
		if (previousState != null) {
			iGame();
			this.gameObjects = previousState.getGameObjectsState();
			this.player = previousState.getPlayerState();
		}
	}


}
