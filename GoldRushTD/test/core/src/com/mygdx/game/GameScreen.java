package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import Tower_Def_Board.EmptyTile;
import Tower_Def_Board.InfernoTower;
import Tower_Def_Board.Map;
import Tower_Def_Board.ShockTower;
import Tower_Def_Board.TerraTower;
import Tower_Def_Board.AquaTower;
import Tower_Def_Board.BaseTower;
import Tower_Def_Board.Tile;
import Tower_Def_Board.Tower;
import Tower_Def_Board.ZephyrTower;
import Tower_Def_Enemy_Manager.Enemy;
import Tower_Def_Enemy_Manager.EnemyManager;
import Tower_Def_Projectile.Projectile;

public class GameScreen implements Screen{

	final Group7 game;
	final MainMenuScreen previousScreen;
	Screen gameOver;
	Map GameMap;
	Tile tile;
	Animation<TextureRegion> animation; //animation for the coin
	EnemyManager EnManage;
	int Enemy_y = 0;
	
	//setup tile information
	Sprite TileTexture =  new Sprite(new Texture(Gdx.files.internal("TileSkins/GridTile_Grass.png")));
	String TileName = "[0][0]";
	
	//use aspRatio when scaling
	float aspRatio = Gdx.graphics.getWidth()/Gdx.graphics.getHeight();
	
	//textures used for drawing the screen
	Sprite BckGnd, PauseBckGnd, WaveEndBckGnd, Tooltip, TowerTooltip, Health;
	Texture projectileTexture;
	Skin skin;
	
	//buttons for the menus
	Button MenuBtn, InfoBtn, BuildBtn, InfernoBuildBtn, AquaBuildBtn, TerraBuildBtn, ZephyrBuildBtn, ShockBuildBtn;
	ButtonStyle MenuBtnStyle, InfoBtnStyle, BuildBtnStyle, InfernoBuildBtnStyle, AquaBuildBtnStyle, TerraBuildBtnStyle, ZephyrBuildBtnStyle, ShockBuildBtnStyle;
	TextButton MenuRetBtn, GameRetBtn, NextWaveBtn;
	TextButtonStyle PauseBtnStyle;
	
	//create the array of buttons for the board
	Button[][] GridBtns;
	ButtonStyle GridBtnStyle;
	Button btn;
		
	//set the region for the buttons
	Stage stage, pauseStage, waveEndStage;
	
	//set the camera
	OrthographicCamera camera;
	FitViewport port;
	
	//projectile's
	public Array<Projectile> projectiles;
	
	//display round information
	int roundNumber = 1;
	int enemyCount = 0;
	int currency = 1000, earnedCurrency = 0;
	private State state = State.RUNNING;
	float elapsed;
	int lives = 12, livesLost = 0;
	
	//longs for all times
	long startTime = 0, Time = 0;
	long elapsedWaveTime = 0;
	long waveTime = 0;
	long lastSpawnTime = 0;
	
	public GameScreen(final Group7 game, final MainMenuScreen previous) {
		//store the mainmenu screen as previous,
		this.game = game;
		previousScreen = previous;
		//create a new game map and initialize time variables
		GameMap = new Map();
		startTime = TimeUtils.millis();
		elapsedWaveTime = TimeUtils.millis();
		lastSpawnTime = TimeUtils.millis();
		
		//create a new EnemyManager
		EnManage = new EnemyManager(roundNumber);
		
		//setup a new 2D array of Buttons for the game grid
		GridBtns = new Button[GameMap.yTowers][GameMap.xTowers]; //[y][x]
		
		//set the camera and initialize all needed stages useing the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);
		port = new FitViewport(1920, 1080, camera);
		stage = new Stage(); //normal game stage
		stage.setViewport(port);
		pauseStage = new Stage(); //stage for when the game is paused
		pauseStage.setViewport(port);
		waveEndStage = new Stage();	//stage for when the wave ends
		waveEndStage.setViewport(port);
		
		//create an array of projectiles and load the proper texture
		projectiles = new Array<Projectile>();
		projectileTexture = new Texture(Gdx.files.internal("cannon_ball_30x30.png"));
		
		//setup a new skinset and initialise the input processor
		skin = new Skin();
		Gdx.input.setInputProcessor(stage);

		//load the animations and set the health texture to be dependant on the player's lives counter
		animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("gold_coin.gif").read());
		Health = new Sprite(new Texture(Gdx.files.internal("Health/health_" + lives + ".png")));
		
		//Create the background image
		BckGnd = new Sprite(new Texture(Gdx.files.internal("grass_template2.v1.jpg")));
		PauseBckGnd = new Sprite(new Texture(Gdx.files.internal("PauseOverlay.png")));
		WaveEndBckGnd = new Sprite(new Texture(Gdx.files.internal("WaveEndTooltip.png")));
		
		//add the toolTip backgrounds
		Tooltip = new Sprite(new Texture(Gdx.files.internal("TooltipCover_full.png")));
		
		//setup Menu Button
		createMenuButton(stage);
		
		//Setup Info Button
		createInfoButton();
		
		//setup the main tower grid
		for (int y = 0; y < GameMap.yTowers ; y++) {
			for (int x = 0; x < GameMap.xTowers; x++) {
				//store the tile of the needed boardtile
				tile = GameMap.getBoardTile(x, y);
				
				//get the texture and set the grid button's style to match
				skin.add(tile.getName(), tile.getTexture());
				skin.add(tile.getName() + "Over", tile.getOverTexture());
				GridBtnStyle = new ButtonStyle(skin.getDrawable(tile.getName()),skin.getDrawable(tile.getName()),skin.getDrawable(tile.getName()));
				GridBtnStyle.over = skin.getDrawable(tile.getName() + "Over");
				
				//set the respective grid button to this. The name represents the position on the board
				btn = new Button(GridBtnStyle);
				btn.setPosition(this.camera.viewportWidth*aspRatio/2 - 77*GameMap.xTowers/2 + x*77, 20 + y*77);
				
				btn.setName(tile.getName() + "[" + y + "][" + x + "]");
				
				GridBtns[y][x] = btn;
				
				//add this button to the stage
				stage.addActor(GridBtns[y][x]);
				
				//create the button's listener 
				GridBtns[y][x].addListener(new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						//set the TileName and Texture for the bottom right tooltip to the selected tile/button
						TileName = actor.getName();
						TileTexture = skin.getSprite(TileName.substring(0, TileName.indexOf('[')));
						
						//get the x and y coordinates from the tile's name
						int y = Integer.parseInt(TileName.substring(TileName.indexOf("[") +1, TileName.indexOf("]")));
						String name = TileName.substring(TileName.indexOf("[")+ 1, TileName.length());
						int x = Integer.parseInt(name.substring(name.indexOf("[") + 1, name.length()-1));
						tile = GameMap.getBoardTile(x, y);
						
						//if the tile name contains grass, create a buildbutton, 
						//if it is a tower, create a sell button, and if it is a physicaltower create the upgrade buttons
						if (TileName.substring(0, TileName.indexOf('[')).equals("Grass") && currency >= 150)
							createBuildButton(actor.getName());	
						else if (TileName.substring(0, TileName.indexOf('[')).contains("Tower")) 
							createSellButton(actor.getName());
						if (TileName.substring(0, TileName.indexOf('[')).equals("PhysicalTower") && currency >= 100) {
							createInfernoUpgradeButton(actor.getName());
							createAquaUpgradeButton(actor.getName());
							createTerraUpgradeButton(actor.getName());
							createZephyrUpgradeButton(actor.getName());
							createShockUpgradeButton(actor.getName());
						}
					}
				});	
			}
		}
		
		//initialize the enemycount
		enemyCount = EnManage.spawnEnemy(
				this.camera.viewportWidth*aspRatio - 800,
				this.camera.viewportHeight*aspRatio - 100);
	}

	
	@Override
	public void show() {
		game.GameScreen.play();
	}
	
	@Override
	public void render(float delta) {
		//determine the elapsed time for the puase menu statistics
		elapsed += Gdx.graphics.getDeltaTime();
		
		//remove any unneeded actors depending on the game's requirements
		for(Actor actor : stage.getActors()) {
			if (actor.getName() == "Build" && !TileName.substring(0, TileName.indexOf('[')).equals("Grass"))
				actor.remove();
			else if (actor.getName() == "Sell" && !TileName.substring(0, TileName.indexOf('[')).contains("Tower"))
				actor.remove();
			else if ((actor.getName() == "AquaBuild" || actor.getName() == "InfernoBuild" || actor.getName() == "TerraBuild" || actor.getName() == "ZephyrBuild" || actor.getName() == "ShockBuild") && 
					(TileName.substring(0, TileName.indexOf('[')).contains("Aqua") || TileName.substring(0, TileName.indexOf('[')).contains("Inferno")|| TileName.substring(0, TileName.indexOf('[')).contains("Terra") || TileName.substring(0, TileName.indexOf('[')).contains("Zephyr") || TileName.substring(0, TileName.indexOf('[')).contains("Shock")))
				actor.remove();
			if ((actor.getName() == "InfernoBuild" ||actor.getName() == "AquaBuild" || actor.getName() == "TerraBuild" || actor.getName() == "ZephyrBuild" || actor.getName() == "ShockBuild") 
						&& !TileName.substring(0, TileName.indexOf('[')).contains("Tower"))
				actor.remove();
			}
				
		//clear the previous render and update the camera
				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				
				camera.update();
				
				//if M is pressed open the menu, if ESCAPE is pressed, clear the tile selection
				if (Gdx.input.isKeyPressed(Keys.M)) {
					pause();
				}
				else if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
					TileTexture = new Sprite (new Texture(Gdx.files.internal("TileSkins/GridTile.png")));
					TileName = "[x][y]";
				}
				
				//draw all elements of the base game's batch
				game.batch.setProjectionMatrix(camera.combined);
				game.batch.begin();
				game.batch.draw(BckGnd, 0 , 0);
				game.batch.draw(Tooltip, this.camera.viewportWidth*aspRatio - Tooltip.getWidth(), 0);
				game.batch.draw(Tooltip, 0, 0);
				game.batch.draw(TileTexture,this.camera.viewportWidth*aspRatio - Tooltip.getWidth() + 20 ,Tooltip.getHeight() - TileTexture.getHeight() - 20 );
				game.batch.draw(Health, this.camera.viewportWidth*aspRatio - Health.getWidth() - 20 , this.camera.viewportHeight*aspRatio - MenuBtn.getHeight() - Health.getHeight() - 40);
				game.font.draw(game.batch, TileName , this.camera.viewportWidth*aspRatio - Tooltip.getWidth() + TileTexture.getWidth() + 40 , Tooltip.getHeight() - 20 );
				game.font.draw(game.batch, "" + lives + "/12", this.camera.viewportWidth*aspRatio - Health.getWidth() - 20, this.camera.viewportHeight*aspRatio - MenuBtn.getHeight() - Health.getHeight() - 45);
				game.font.draw(game.batch, "Round: " + roundNumber, 10 , this.camera.viewportHeight*aspRatio - 10);
				game.font.draw(game.batch, "Enemies left: " +  EnManage.getRemainingNumber() + "/" + EnManage.roundEnemyCount, 10, this.camera.viewportHeight*aspRatio - 30);
				game.font.draw(game.batch, "" + currency + "", 50, this.camera.viewportHeight*aspRatio - 50);
				game.batch.draw(animation.getKeyFrame(elapsed), 0, this.camera.viewportHeight*aspRatio - animation.getKeyFrame(elapsed).getRegionHeight() - 50);
				 
				//if a Tower is selected, display stats in the bottom right
				if (tile instanceof Tower) {
					game.font.draw(game.batch, "Damage: " + ((Tower) tile).getDamage() + "" + ((Tower) tile).getElementName(), this.camera.viewportWidth*aspRatio - Tooltip.getWidth() + TileTexture.getWidth() + 40 , Tooltip.getHeight() - 40 );
					game.font.draw(game.batch, "Speed: " + ((Tower) tile).getAttackSpeed() + "", this.camera.viewportWidth*aspRatio - Tooltip.getWidth() + TileTexture.getWidth() + 40 , Tooltip.getHeight() - 60 );
					game.font.draw(game.batch, "Range: " + ((Tower) tile).getRange() + "", this.camera.viewportWidth*aspRatio - Tooltip.getWidth() + TileTexture.getWidth() + 40 , Tooltip.getHeight() - 80 );
				}
					
				game.batch.end();
				
				
				//draw and act the stage
				stage.act();
				stage.draw();
				
				//if the right DPAD is pressed, speed up the spawn and movements times
				if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT))
					Time = TimeUtils.timeSinceMillis(lastSpawnTime)/333 % 60;
				else
					Time = TimeUtils.timeSinceMillis(lastSpawnTime)/1000 % 60;

				//if Time>1 begin spawning enemies
				if(Time > 1) {
					enemyCount = EnManage.spawnEnemy(this.camera.viewportWidth*aspRatio - 800,
										this.camera.viewportHeight*aspRatio - 100);
					lastSpawnTime = TimeUtils.millis();
				}
				
				
				//as long as the enemy count is above 0, draw and move their sprites
				if (enemyCount > 0) {
				for (int i = 0; i < EnManage.getRemainingNumber(); i++) {
						game.batch.begin();
						game.batch.draw(EnManage.getEnemy(i).getSprite(), EnManage.Enemies.get(i).x, EnManage.Enemies.get(i).y);
						
						//call moveEnemy from the enemy manager to determine the needed translation
						EnManage.moveEnemy(enemyOverlay(i), i, Gdx.input.isKeyPressed(Keys.DPAD_RIGHT));
						
						//if the enemy reaches the bottom of the screen remove it and lose a life.
						if(EnManage.Enemies.get(i).y + Enemy_y + i*20 + 32 < (1080 - 14 *77)) {
							EnManage.removeEnemy(i);
							lives--;
							livesLost++;
							//reflect this change in the healt bar
							Health = new Sprite(new Texture(Gdx.files.internal("Health/health_" + lives + ".png")));
							enemyCount--;
						}
				
				game.batch.end();
				}}
				//if the enemycount is 0 and the game's state is not in the WAVEND, set it to WAVEND reset the elapsed time
				if(enemyCount == 0 && state != State.WAVEND) {
					waveEnd();
					elapsedWaveTime = TimeUtils.millis();
				}
				
			//if lives = 0 then the game has ended, create the gameover screen and change to this screen
			if(lives == 0) {
				state = State.STOPPED;
				gameOver = new GameOver(game, previousScreen, roundNumber);
				game.setScreen(gameOver);}
			
			//if the game's state has been set to PAUSE then draw the pause stats and the pause buttons
			if (state == State.PAUSE) {
				
				camera.update();
				
				game.batch.setProjectionMatrix(camera.combined);
				
				game.batch.begin();
				game.batch.draw(WaveEndBckGnd, 0, this.camera.viewportHeight*aspRatio/2 - WaveEndBckGnd.getHeight()/2);
				game.font.draw(game.batch, "Time Elapsed: " + TimeUtils.timeSinceMillis(startTime)/3600000 % 24 + ":" + TimeUtils.timeSinceMillis(startTime)/60000 % 60 + ":" + TimeUtils.timeSinceMillis(startTime)/1000 % 60 + "", 0, this.camera.viewportHeight*aspRatio);
				game.batch.end();
				
				pauseStage.act();
				pauseStage.draw();
			}
			//else if the state is WAVEND then display all wave information and draw the WAVEND stage
			else if (state == State.WAVEND) {
				camera.update();
				
				game.batch.setProjectionMatrix(camera.combined);
				
				game.batch.begin();
				game.batch.draw(WaveEndBckGnd, 0, this.camera.viewportHeight*aspRatio/2 - WaveEndBckGnd.getHeight()/2);
				
				game.font.draw(game.batch, "Round: " + roundNumber, 20 , this.camera.viewportHeight*aspRatio/2 + 275);
				game.font.draw(game.batch, "Lives lost: " + livesLost, 20 , this.camera.viewportHeight*aspRatio/2 + 250);
				game.font.draw(game.batch, "WaveTime Elapsed: " + waveTime/3600000 % 24 + ":" + waveTime/60000 % 60 + ":" + waveTime/1000 % 60 + "", 20 , this.camera.viewportHeight*aspRatio/2 + 225);
				game.font.draw(game.batch, "Gold Earned: " + earnedCurrency, 20 , this.camera.viewportHeight*aspRatio/2 + 200);
				
				game.batch.end();
				
				waveEndStage.act();
				waveEndStage.draw();
				//if enter is pressed, start the next wave
				if (Gdx.input.isKeyPressed(Keys.ENTER)) {
					startNewWave();
				}
				
			}
			
			else if(state == State.RUNNING) {
				for (Tile[] t : GameMap.board) {
					for (Tile tt : t) {
						if (tt instanceof Tower) {
							for(Rectangle r : EnManage.Enemies) {
								if (((Tower) tt).intersects(r)) {
									if (((Tower) tt).getAttackTimer() <= 0) {
										projectiles.add(((Tower) tt).attack(((Tower) tt).getDamage(), r));
										((Tower) tt).setAttackTimer(((Tower) tt).getCooldown());
									}
									else 
										((Tower) tt).setAttackTimer(((Tower) tt).getAttackTimer() - 1);
								}
							}
						}
					}
				}
			}
			
			game.batch.begin();
			for(Projectile projectile : projectiles) {
				game.batch.draw(projectileTexture, projectile.x, projectile.y);
				
				//calculate the required translations for the projectile based off of the enemy's pixel location
				if(projectile.x < projectile.enemy.x) {
					projectile.x += 10;
				} else if (projectile.x > projectile.enemy.x ){
					projectile.x -= 10;
				}
				
				if(projectile.y < projectile.enemy.y) {
					projectile.y += 10;
				} else if (projectile.y > projectile.enemy.y ){
					projectile.y -= 10;
				}
				
				//create a rectangle from the projectile's position for use with .overlaps
				Rectangle projtangle = new Rectangle();
				projtangle.x = projectile.x;
				projtangle.y = projectile.y;
				projtangle.width = 30;
				projtangle.height = 30;
				
				if(projectile.enemy.overlaps(projtangle)) {
		
				//scan though the list of RoundEnemies and create a rectangle from them 
				for(int i = 0; i < EnManage.RoundEnemies.size; i++) {
					Enemy enemy = EnManage.RoundEnemies.get(i);
					Rectangle enangle = new Rectangle();
					enangle.x = enemy.x;
					enangle.y = enemy.y;
					enangle.width = 180;
					enangle.height = 180;
					
					//detemrmine if the projectile overlaps the enemy rectangle
					//if so remove the projectile and deal damage to the enemy
					if (enangle.overlaps(projectile.enemy)) {
						enemy.health -= projectile.damage;
						projectile.x = 1111111111;
						projectile.y = 1111111111;
					} 
					
					/* TODO
					 * remove the projectile if another projectile kills the enemy 
					 * currently it is translated to far off the screen
					 */
					if (enemy.health <= 0) {
						//if the enemy's health goes below 0, remove the enemy
					EnManage.removeEnemy(i);
					projectile.x = 1111111111;
					projectile.y = 1111111111;
					}
				}
				
				
				// in case the enemy was killed by a different projectile
				projectile.x = 1111111111;
				projectile.y = 1111111111;
				
			}
				
			}
			game.batch.end();
			
			
			
			
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		pauseStage.getViewport().update(width, height, true);
	}

	public int enemyOverlay(int i) {
		//control when the enemy should move in a certain direction based off of the surronding Grid Buttons
		for(int j = 1; j < GameMap.yTowers - 1; j++) {
			for(int k = 1; k < GameMap.xTowers - 1; k++) {
				if(GridBtns[j][k].getName().contains("Path") && EnManage.Enemies.get(i).x >= (GridBtns[j][k].getX() - 45) && EnManage.Enemies.get(i).x < (GridBtns[j][k].getX() + 45)
															&& EnManage.Enemies.get(i).y >= (GridBtns[j][k].getY() - 77) && EnManage.Enemies.get(i).y < (GridBtns[j][k].getY() + 45)) {
					if (GridBtns[j][k - 1].getName().contains("Path")) {return 2;}
					else if(GridBtns[j][k].getName().contains("Path")) {return 1;}
					else if(GridBtns[j][k + 1].getName().contains("Path")) {return -2;}
					if(k == 0) {return 1;}
				
				}
			}
		}
		
		return 1;
	}
	
	//create a button that allow for new BaseTowers to be built
	public void createBuildButton(String btnName) {

		skin.add("Build", new Texture(Gdx.files.internal("ButtonSkins/Plus__Orange.png")));
		skin.add("BuildPressed", new Texture(Gdx.files.internal("ButtonSkins/Plus__OrangePressed.png")));
		
		BuildBtnStyle = new ButtonStyle(skin.getDrawable("Build"), skin.getDrawable("BuildPressed"), skin.getDrawable("Build"));
		BuildBtn = new Button(BuildBtnStyle);
		
		BuildBtn.setPosition(this.camera.viewportWidth*aspRatio - Tooltip.getWidth() + 20 ,Tooltip.getHeight() - TileTexture.getHeight() - 97);
		BuildBtn.setName("Build");
		stage.addActor(BuildBtn);
		
		//set the listener to change the selected button
		BuildBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				int x = tile.getX();
				int y = tile.getY();
				
				GameMap.updateBoardTile(x, y, new BaseTower(x, y));
				tile = GameMap.getBoardTile(x, y);
				
				//if the required currency is held b the player then apply the change
				if (currency >= ((Tower) tile).getCost()) {
					currency -= ((Tower) tile).getCost();
				skin.add(tile.getName(), tile.getTexture());
				skin.add(tile.getName() + "Over", tile.getOverTexture());
				
				ButtonStyle buildStyle = new ButtonStyle(skin.getDrawable(tile.getName()),skin.getDrawable(tile.getName()),skin.getDrawable(tile.getName()));
				buildStyle.over = skin.getDrawable(tile.getName() + "Over");
				GridBtns[y][x].setStyle(buildStyle);
				
				//fully update the appropriate button
				TileName = tile.getName() + "[" + y + "][" + x + "]";
				GridBtns[y][x].setName(TileName);
				
				//once the BaseTower is created, create all buttons to upgrade the tower
				TileTexture = skin.getSprite(tile.getName());
				createSellButton(GridBtns[y][x].getName());
				createInfernoUpgradeButton(GridBtns[y][x].getName());
				createAquaUpgradeButton(GridBtns[y][x].getName());
				createTerraUpgradeButton(GridBtns[y][x].getName());
				createZephyrUpgradeButton(GridBtns[y][x].getName());
				createShockUpgradeButton(GridBtns[y][x].getName());
				}}
		});
	}
		
	//once the BaseTower is created, create a sell button
	public void createSellButton(String btnName) {

		skin.add("Sell", new Texture(Gdx.files.internal("ButtonSkins/Minus_Orange.png")));
		skin.add("SellPressed", new Texture(Gdx.files.internal("ButtonSkins/Minus_OrangePressed.png")));
		
		BuildBtnStyle = new ButtonStyle(skin.getDrawable("Sell"), skin.getDrawable("SellPressed"), skin.getDrawable("Sell"));
		BuildBtn = new Button(BuildBtnStyle);
		
		BuildBtn.setPosition(this.camera.viewportWidth*aspRatio - Tooltip.getWidth() + 20 ,Tooltip.getHeight() - TileTexture.getHeight() - 97);
		BuildBtn.setName("Sell");
		stage.addActor(BuildBtn);
		
		//if the sell button is pressed, revert the changes to that tile back to Grass
		BuildBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				int x = tile.getX();
				int y = tile.getY();
				
				//provide some selling currency
				currency += ((Tower) tile).getRefund();
				
				GameMap.updateBoardTile(x, y, new EmptyTile(x, y, "Grass"));
				tile = GameMap.getBoardTile(x, y);
				
				ButtonStyle buildStyle = new ButtonStyle(skin.getDrawable(tile.getName()),skin.getDrawable(tile.getName()),skin.getDrawable(tile.getName()));
				buildStyle.over = skin.getDrawable(tile.getName() + "Over");
				GridBtns[y][x].setStyle(buildStyle);
				
				TileName = tile.getName() + "[" + y + "][" + x + "]";
				GridBtns[y][x].setName(TileName);
				
				//create another build button for that same tile
				TileTexture = skin.getSprite(tile.getName());
				createBuildButton(GridBtns[y][x].getName());
				}
		});
	}
	
	//display a menu(pause) button
	public void createMenuButton(Stage stage) {
		skin.add("Menu", new Texture(Gdx.files.internal("ButtonSkins/MenuButton.png")));
		skin.add("MenuPressed", new Texture(Gdx.files.internal("ButtonSkins/MenuButtonPressed.png")));
		
		MenuBtnStyle = new ButtonStyle(skin.getDrawable("Menu"), skin.getDrawable("MenuPressed"), skin.getDrawable("Menu"));
		MenuBtnStyle.over = skin.getDrawable("MenuPressed");
		MenuBtn = new Button(MenuBtnStyle);
		
		MenuBtn.setPosition(this.camera.viewportWidth*aspRatio - MenuBtn.getWidth() - 20 , this.camera.viewportHeight*aspRatio - MenuBtn.getHeight() - 20 );
		
		stage.addActor(MenuBtn);
		
		//if pressed, pause the game
		MenuBtn.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				game.MenuSelect.play();
				pause();
			}
		});
	}
	
	//create a currently unimplemented Info button intended to display round information and enemy combat tips
	public void createInfoButton() {
		skin.add("Info", new Texture(Gdx.files.internal("ButtonSkins/InfoButton.png")));
		skin.add("InfoPressed", new Texture(Gdx.files.internal("ButtonSkins/InfoButtonPressed.png")));
		
		InfoBtnStyle = new ButtonStyle(skin.getDrawable("Info"), skin.getDrawable("InfoPressed"), skin.getDrawable("InfoPressed"));
		InfoBtnStyle.over = skin.getDrawable("InfoPressed");
		InfoBtn = new Button(InfoBtnStyle);
		
		InfoBtn.setPosition(this.camera.viewportWidth*aspRatio - 2*MenuBtn.getWidth() - 2*20 , this.camera.viewportHeight*aspRatio - MenuBtn.getHeight() - 20 );
		
		stage.addActor(InfoBtn);
	}
	
	
	@Override
	//pause the game and set the input processor to the pause stage's
	public void pause() {
		state = State.PAUSE; //changes in render will only display when set to .PAUSE
		game.GameScreen.pause();
		Gdx.input.setInputProcessor(pauseStage);
		setupPause();
	}
	
	//end the wave and set the game's input processor to the wave end display
	public void waveEnd() {
		state = State.WAVEND; //changes in render will only display when set to .WAVEND
		Gdx.input.setInputProcessor(waveEndStage);
		//if the wave ends, reset the wave end, lastspawntime, and award the required amount of currency. 
		//determined from the amount to time spent in that wave
		waveTime = TimeUtils.timeSinceMillis(elapsedWaveTime);
		lastSpawnTime = TimeUtils.millis();
		earnedCurrency = (int) (1*(5000000/waveTime));
		currency += earnedCurrency; 
		setupWaveEnd();
	}
	
	@Override
	public void resume() {

		state = State.RUNNING;
		game.GameScreen.play();
		
		Gdx.input.setInputProcessor(stage);
		
	}

	@Override
	public void hide() {
		game.GameScreen.pause();
	}


	public void setupPause() {
		//Setup Button Style for the Pause menu
		skin.add("Paused", new Texture(Gdx.files.internal("ButtonSkins/Dialog Box.png")));
		skin.add("PausedPressed", new Texture(Gdx.files.internal("ButtonSkins/Dialog Box.png")));
		
		PauseBtnStyle = new TextButtonStyle(skin.getDrawable("Paused"), skin.getDrawable("PausedPressed"), skin.getDrawable("PausedPressed"), new BitmapFont());
		PauseBtnStyle.over = skin.getDrawable("PausedPressed");
		
		//Setup the Return to menu button
		MenuRetBtn = new TextButton("Exit", PauseBtnStyle);
		MenuRetBtn.setPosition(25, this.camera.viewportHeight*aspRatio/2 + 50);
		pauseStage.addActor(MenuRetBtn);
		
		//setup the MenuReturn Button's function
		MenuRetBtn.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				
				game.MenuSelect.stop();
				state = State.STOPPED;
				//set the stage to the Main Menu's stage
				Gdx.input.setInputProcessor(previousScreen.stage);
				game.setScreen(previousScreen);
			}
		});
		
		//setup the Return to Game Button
		GameRetBtn = new TextButton("Return to Game", PauseBtnStyle);
		GameRetBtn.setPosition(30, this.camera.viewportHeight*aspRatio/2 - GameRetBtn.getHeight() - 50);
		pauseStage.addActor(GameRetBtn);
		
		GameRetBtn.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				
				game.MenuSelect.play();
				resume();
				
			}
		});
		
	}

	public void setupWaveEnd() {
		//Setup Button Style for the Pause menu
		skin.add("WaveEnd", new Texture(Gdx.files.internal("ButtonSkins/Dialog Box.png")));
		skin.add("WaveEndPressed", new Texture(Gdx.files.internal("ButtonSkins/Dialog BoxOver.png")));
		
		PauseBtnStyle = new TextButtonStyle(skin.getDrawable("WaveEnd"), skin.getDrawable("WaveEndPressed"), skin.getDrawable("WaveEnd"), new BitmapFont());
		PauseBtnStyle.over = skin.getDrawable("WaveEndPressed");
		
		//Setup the Return to menu button
		NextWaveBtn = new TextButton("Continue", PauseBtnStyle);
		NextWaveBtn.setPosition(25, this.camera.viewportHeight*aspRatio/2 - NextWaveBtn.getHeight() - 100);
		waveEndStage.addActor(NextWaveBtn);
		
		//setup the MenuReturn Button's function
		NextWaveBtn.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				startNewWave();
				
			}
		});
		createMenuButton(waveEndStage);
	}
	
	//start a new wave by creating a new enemymanager and reseting the wave stats. 
	public void startNewWave() {
		livesLost = 0;
		elapsedWaveTime = TimeUtils.millis();
		roundNumber++;
		EnManage = new EnemyManager(roundNumber);
		enemyCount = EnManage.spawnEnemy(
				camera.viewportWidth*aspRatio - 800,
				camera.viewportHeight*aspRatio - 100);
		earnedCurrency = 0;
		lastSpawnTime = TimeUtils.millis();
		resume();
	}
	
	/*5 methods for each tower upgrade type. each upgrade is the exact same except each button calls its respective tower decorator
	 * these upgrades are handled the same way the build button is
	 * 
	 * creating buttons and adding them into the stage outside of this class causes errors. 
	 * for now this will be done in GameScreen
	 * 
	 */
	public void createInfernoUpgradeButton(String BtnName) {

		skin.add("InfernoBuild", new Texture(Gdx.files.internal("ButtonSkins/GridTile_InfernoTower.png")));
		skin.add("InfernoBuildPressed", new Texture(Gdx.files.internal("ButtonSkins/GridTileOver_InfernoTower.png")));
		
		InfernoBuildBtnStyle = new ButtonStyle(skin.getDrawable("InfernoBuild"), skin.getDrawable("InfernoBuildPressed"), skin.getDrawable("InfernoBuild"));
		InfernoBuildBtn = new Button(InfernoBuildBtnStyle);
		
		InfernoBuildBtn.setPosition(this.camera.viewportWidth*aspRatio - InfernoBuildBtn.getWidth(),0);
		InfernoBuildBtn.setName("InfernoBuild");
		stage.addActor(InfernoBuildBtn);
		
		
		InfernoBuildBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				int x = tile.getX();
				int y = tile.getY();
				
				GameMap.updateBoardTile(x, y, new InfernoTower((BaseTower)(tile)));
				tile = GameMap.getBoardTile(x, y);

				if (currency >= ((Tower) tile).getCost()) {
					currency -= ((Tower) tile).getCost();
				skin.add(tile.getName(), tile.getTexture());
				skin.add(tile.getName() + "Over", tile.getOverTexture());
				
				ButtonStyle buildStyle = new ButtonStyle(skin.getDrawable(tile.getName()),skin.getDrawable(tile.getName()),skin.getDrawable(tile.getName()));
				buildStyle.over = skin.getDrawable(tile.getName() + "Over");
				GridBtns[y][x].setStyle(buildStyle);
				
				TileName = tile.getName() + "[" + y + "][" + x + "]";
				GridBtns[y][x].setName(TileName);
				
				TileTexture = skin.getSprite(tile.getName());
				}}
		});
	};
	
	public void createAquaUpgradeButton(String BtnName) {

		skin.add("AquaBuild", new Texture(Gdx.files.internal("ButtonSkins/GridTile_AquaTower.png")));
		skin.add("AquaBuildPressed", new Texture(Gdx.files.internal("ButtonSkins/GridTileOver_AquaTower.png")));
		
		AquaBuildBtnStyle = new ButtonStyle(skin.getDrawable("AquaBuild"), skin.getDrawable("AquaBuildPressed"), skin.getDrawable("AquaBuild"));
		AquaBuildBtn = new Button(AquaBuildBtnStyle);
		
		AquaBuildBtn.setPosition(this.camera.viewportWidth*aspRatio - 2*AquaBuildBtn.getWidth(),0);
		AquaBuildBtn.setName("AquaBuild");
		stage.addActor(AquaBuildBtn);
		
		
		AquaBuildBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				int x = tile.getX();
				int y = tile.getY();
				
				GameMap.updateBoardTile(x, y, new AquaTower((BaseTower)(tile)));
				tile = GameMap.getBoardTile(x, y);

				if (currency >= ((Tower) tile).getCost()) {
					currency -= ((Tower) tile).getCost();
				skin.add(tile.getName(), tile.getTexture());
				skin.add(tile.getName() + "Over", tile.getOverTexture());
				
				ButtonStyle buildStyle = new ButtonStyle(skin.getDrawable(tile.getName()),skin.getDrawable(tile.getName()),skin.getDrawable(tile.getName()));
				buildStyle.over = skin.getDrawable(tile.getName() + "Over");
				GridBtns[y][x].setStyle(buildStyle);
				
				TileName = tile.getName() + "[" + y + "][" + x + "]";
				GridBtns[y][x].setName(TileName);
				
				TileTexture = skin.getSprite(tile.getName());
				}}
		});
	};
	
	public void createTerraUpgradeButton(String BtnName) {

		skin.add("TerraBuild", new Texture(Gdx.files.internal("ButtonSkins/GridTile_TerraTower.png")));
		skin.add("TerraBuildPressed", new Texture(Gdx.files.internal("ButtonSkins/GridTileOver_TerraTower.png")));
		
		TerraBuildBtnStyle = new ButtonStyle(skin.getDrawable("TerraBuild"), skin.getDrawable("TerraBuildPressed"), skin.getDrawable("TerraBuild"));
		TerraBuildBtn = new Button(TerraBuildBtnStyle);
		
		TerraBuildBtn.setPosition(this.camera.viewportWidth*aspRatio - 3*TerraBuildBtn.getWidth(),0);
		TerraBuildBtn.setName("TerraBuild");
		stage.addActor(TerraBuildBtn);
		
		
		TerraBuildBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				int x = tile.getX();
				int y = tile.getY();
				
				GameMap.updateBoardTile(x, y, new TerraTower((BaseTower)(tile)));
				tile = GameMap.getBoardTile(x, y);

				if (currency >= ((Tower) tile).getCost()) {
					currency -= ((Tower) tile).getCost();
				skin.add(tile.getName(), tile.getTexture());
				skin.add(tile.getName() + "Over", tile.getOverTexture());
				
				ButtonStyle buildStyle = new ButtonStyle(skin.getDrawable(tile.getName()),skin.getDrawable(tile.getName()),skin.getDrawable(tile.getName()));
				buildStyle.over = skin.getDrawable(tile.getName() + "Over");
				GridBtns[y][x].setStyle(buildStyle);
				
				TileName = tile.getName() + "[" + y + "][" + x + "]";
				GridBtns[y][x].setName(TileName);
				
				TileTexture = skin.getSprite(tile.getName());
				}}
		});
	};
	
	public void createZephyrUpgradeButton(String BtnName) {

		skin.add("ZephyrBuild", new Texture(Gdx.files.internal("ButtonSkins/GridTile_ZephyrTower.png")));
		skin.add("ZephyrBuildPressed", new Texture(Gdx.files.internal("ButtonSkins/GridTileOver_ZephyrTower.png")));
		
		ZephyrBuildBtnStyle = new ButtonStyle(skin.getDrawable("ZephyrBuild"), skin.getDrawable("ZephyrBuildPressed"), skin.getDrawable("ZephyrBuild"));
		ZephyrBuildBtn = new Button(ZephyrBuildBtnStyle);
		
		ZephyrBuildBtn.setPosition(this.camera.viewportWidth*aspRatio - 4*ZephyrBuildBtn.getWidth(),0);
		ZephyrBuildBtn.setName("ZephyrBuild");
		stage.addActor(ZephyrBuildBtn);
		
		
		ZephyrBuildBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				int x = tile.getX();
				int y = tile.getY();
				
				GameMap.updateBoardTile(x, y, new ZephyrTower((BaseTower)(tile)));
				tile = GameMap.getBoardTile(x, y);

				if (currency >= ((Tower) tile).getCost()) {
					currency -= ((Tower) tile).getCost();
				skin.add(tile.getName(), tile.getTexture());
				skin.add(tile.getName() + "Over", tile.getOverTexture());
				
				ButtonStyle buildStyle = new ButtonStyle(skin.getDrawable(tile.getName()),skin.getDrawable(tile.getName()),skin.getDrawable(tile.getName()));
				buildStyle.over = skin.getDrawable(tile.getName() + "Over");
				GridBtns[y][x].setStyle(buildStyle);
				
				TileName = tile.getName() + "[" + y + "][" + x + "]";
				GridBtns[y][x].setName(TileName);
				
				TileTexture = skin.getSprite(tile.getName());
				}}
		});
	};
	
	public void createShockUpgradeButton(String BtnName) {

		skin.add("ShockBuild", new Texture(Gdx.files.internal("ButtonSkins/GridTile_ShockTower.png")));
		skin.add("ShockBuildPressed", new Texture(Gdx.files.internal("ButtonSkins/GridTileOver_ShockTower.png")));
		
		ShockBuildBtnStyle = new ButtonStyle(skin.getDrawable("ShockBuild"), skin.getDrawable("ShockBuildPressed"), skin.getDrawable("ShockBuild"));
		ShockBuildBtn = new Button(ShockBuildBtnStyle);
		
		ShockBuildBtn.setPosition(this.camera.viewportWidth*aspRatio - 5*ShockBuildBtn.getWidth(),0);
		ShockBuildBtn.setName("ShockBuild");
		stage.addActor(ShockBuildBtn);
		
		
		ShockBuildBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				int x = tile.getX();
				int y = tile.getY();
				
				GameMap.updateBoardTile(x, y, new ShockTower((BaseTower)(tile)));
				tile = GameMap.getBoardTile(x, y);

				if (currency >= ((Tower) tile).getCost()) {
					currency -= ((Tower) tile).getCost();
				skin.add(tile.getName(), tile.getTexture());
				skin.add(tile.getName() + "Over", tile.getOverTexture());
				
				ButtonStyle buildStyle = new ButtonStyle(skin.getDrawable(tile.getName()),skin.getDrawable(tile.getName()),skin.getDrawable(tile.getName()));
				buildStyle.over = skin.getDrawable(tile.getName() + "Over");
				GridBtns[y][x].setStyle(buildStyle);
				
				TileName = tile.getName() + "[" + y + "][" + x + "]";
				GridBtns[y][x].setName(TileName);
				
				TileTexture = skin.getSprite(tile.getName());
				}}
		});
	};

	@Override
	public void dispose() {
		game.GameScreen.dispose();
		
	}
}
