package games.bomberman;

import java.io.File;
import java.util.*;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import app.AppGame;
import app.AppInput;
import app.AppPlayer;
import app.AppWorld;
import games.bomberman.board.Board;
import games.bomberman.board.cases.Case;
import games.bomberman.board.cases.Ground;
import games.bomberman.bonus.*;

public class World extends AppWorld {

	private int ID;

	public final static String GAME_FOLDER_NAME="bomberman";
	public final static String DIRECTORY_SOUNDS="musics"+File.separator+GAME_FOLDER_NAME+File.separator;
	public final static String DIRECTORY_MUSICS="musics"+File.separator+GAME_FOLDER_NAME+File.separator;
	public final static String DIRECTORY_IMAGES="images"+File.separator+"bomberman"+File.separator;
	private static Board board;
	private static long time; //used to test stuff, don't bother
	
	private int width;
	private int height;

	private static List<Player> players;
	private static List<Bonus> bonus;

	public World (int ID) {
		this.ID = ID;
	}

	@Override
	public int getID () {
		return this.ID;
	}

	@Override
	public void init (GameContainer container, StateBasedGame game) {
		this.width = container.getWidth ();
		this.height = container.getHeight ();
		board=new Board(13,25);
		bonus = new ArrayList<Bonus>();
		bonus.add(new Accelerate(10,10,10));
		bonus.add(new Life(3,5));
		bonus.add(new Reverse(5,5));
		bonus.add(new Cooldown(5,3));
		bonus.add(new Shield(7,7));
	}

	@Override
	public void play (GameContainer container, StateBasedGame game) {
		AppGame appGame = (AppGame) game;
		int n = appGame.appPlayers.size ();
		World.players = new ArrayList<Player>();
		for (int i = 0; i < n; i++) {
			World.players.add(new Player (appGame.appPlayers.get (i)));
		};
	}

	@Override
	public void enter (GameContainer container, StateBasedGame game) {
		AppInput appInput = (AppInput) container.getInput ();
		appInput.clearKeyPressedRecord ();
		appInput.clearControlPressedRecord ();
		time = System.currentTimeMillis();
	}

	public static void removeBonus(Bonus b) {
		bonus.remove(b);
	}
	
	@Override
	public void leave (GameContainer container, StateBasedGame game) {}

	@Override
	public void update (GameContainer container, StateBasedGame game, int delta) throws SlickException {
		/*AppInput appInput = (AppInput) container.getInput ();
		AppGame appGame = (AppGame) game;
		for (Player player: this.players) {
			String name = player.getName ();
			int controllerID = player.getControllerID ();
			for (int i = 0, l = appInput.getControlCount (controllerID); i < l; i++) {
				if (appInput.isControlPressed (1 << i, controllerID)) {
					System.out.println ("(" + name + ").isControlPressed: " + i);
				}
			}
			for (int i = 0, l = appInput.getButtonCount (controllerID); i < l; i++) {
				if (appInput.isButtonPressed (1 << i, controllerID)) {
					System.out.println ("(" + name + ").isButtonPressed: " + i);
				}
			}
			for (int i = 0, l = appInput.getAxisCount (controllerID); i < l; i++) {
				float j = appInput.getAxisValue (i, controllerID);
				if (j <= -.5f || j >= .5f) {
					System.out.println ("(" + name + ").getAxisValue: " + i + " -> " + j);
				}
			}
		}
		if (appInput.isKeyPressed (AppInput.KEY_ESCAPE)) {
			appGame.enterState (AppGame.PAGES_GAMES, new FadeOutTransition (), new FadeInTransition ());
		}*/
		for (Player p : players) {
			p.update(container, game, delta);
		}
		
		for(int i=0 ; i<players.size() ; i++) {
			if (players.get(i).getLife() <= 0) {
				players.remove(i);
			}
		}
		
		for(Bonus d : this.bonus){
			d.update(container, game, delta);
		}
		if(System.currentTimeMillis()-time>=5000 && System.currentTimeMillis()-time<=5020) {
			generateBonus();
			time=System.currentTimeMillis();
		}
		
	}

	@Override
	public void render (GameContainer container, StateBasedGame game, Graphics context) {
		board.render(container, game, context);

		for (Player p : this.players) {
			p.render(container, game, context);
		}
		
		for (Bonus b : this.bonus) {
			if (!b.isActivated())
				b.render(container, game, context);
		}
	}

	
	public static Board getBoard() {
		return board;
	}
	
	public static List<Player> getPlayers() {
		return players;
	}
	
	private void generateBonus() {
		//Find the ground
		ArrayList<Case> freeCases=new ArrayList<Case>() ;
		for(Case c:board.getAllCases()) {
			if(c instanceof Ground) {
				freeCases.add(c);
			}
		}
		
		int i=(int) (Math.random()*freeCases.size());
		
		//Generate the bonus
		int k= (int)(Math.random()*3);
		switch (k) {
		case 0:
			freeCases.get(i).setBonus(new Accelerate(freeCases.get(i).getJ(),freeCases.get(i).getI(),10));
			break;
		case 1:
			freeCases.get(i).setBonus(new Life(freeCases.get(i).getJ(),freeCases.get(i).getI()));
			break;
		case 2:
			freeCases.get(i).setBonus(new Reverse(freeCases.get(i).getJ(),freeCases.get(i).getI()));
			break;
		}
	}
	
}
