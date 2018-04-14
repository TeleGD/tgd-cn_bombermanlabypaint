package games.bomberman;

import java.io.File;
import java.util.*;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
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
	private LesTrucsQuiVontSAfficherACoteDuPlateau gui;
	
	private static List<Player> players;
	private static List<Bomb> bombs;
	
	private static Music music;
	private static Sound poseBombe;
	private static Sound theEnd;
	

	public World (int ID) {
		this.ID = ID;
	}

	@Override
	public int getID () {
		return this.ID;
	}

	@Override
	public void init (GameContainer container, StateBasedGame game) throws SlickException {
		this.width = container.getWidth ();
		this.height = container.getHeight ();
		board=new Board(13,25);
		
		//music = new Music("musics/main_music/amazon_rain_2.ogg");
		poseBombe = new Sound("musics/bomb/pose_bombe_3.ogg");
		theEnd = new Sound("musics/bomb/criWilhelm.ogg");
	
	}

	@Override
	public void play (GameContainer container, StateBasedGame game) {
		AppGame appGame = (AppGame) game;
		int n = appGame.appPlayers.size ();
		
		World.players = new ArrayList<Player>();
		World.bombs = new ArrayList<Bomb>();
		
		for (int i = 0; i < n; i++) {
			World.players.add(new Player (appGame.appPlayers.get (i)));
		}
		gui = new LesTrucsQuiVontSAfficherACoteDuPlateau(this, (int) (board.getCaseSize()*board.cases.length));
	}

	@Override
	public void enter (GameContainer container, StateBasedGame game) {
		AppInput appInput = (AppInput) container.getInput ();
		appInput.clearKeyPressedRecord ();
		appInput.clearControlPressedRecord ();
		time = System.currentTimeMillis();
		
		//music.loop(1, (float) 0.5);
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
		
		for (Bomb b : bombs) {
			b.update(delta);
		}
		
		for(int i=0 ; i<players.size() ; i++) {
			if (players.get(i).getLife() <= 0) {
				theEnd.play(1, (float) 1);
				players.remove(i);
			}
		}
		
		for(int i=0 ; i<bombs.size() ; i++) {
			if (bombs.get(i).isDetruite()) {
				board.getCase(bombs.get(i).getI(), bombs.get(i).getJ()).setBomb(null);
				bombs.remove(i);
			}
		}
		
//		if(System.currentTimeMillis()-time>=2000 && System.currentTimeMillis()-time<=7020) {
//			generateBonus();
//			time=System.currentTimeMillis();
//		}
		board.update(container, game, delta);
		gui.update(container, game, delta);
	}

	@Override
	public void render (GameContainer container, StateBasedGame game, Graphics context) {
		board.render(container, game, context);
		gui.render(container, game, context);
		for (Bomb b : bombs) {
			b.render(container,game,context);
		}
		
		for (Player p : this.players) {
			p.render(container, game, context);
		}
	}

	
	public static Board getBoard() {
		return board;
	}
	
	public static List<Player> getPlayers() {
		return players;
	}
	
	public static List<Bomb> getBombs() {
		return bombs;
	}
	
	private void generateBonus() {
		//Find the ground without bonus and player
		ArrayList<Case> freeCases=new ArrayList<Case>() ;
		for(Case c:board.getAllCases()) {
			if(c instanceof Ground && c.getBonus()==null && c.getBomb()==null) {
				boolean temp = true;
				for (Player p : this.players) {
					if(p.getI() == c.getI() && p.getJ() == c.getJ())
						temp = false;
				}
				
				if(temp)
					freeCases.add(c);
			}
		}
		
		int i=(int) (Math.random()*freeCases.size());
		
		//Generate the bonus
		int k= (int)(Math.random()*9);
		switch (k) {
		case 0:
			freeCases.get(i).setBonus(new Accelerate(freeCases.get(i).getJ(),freeCases.get(i).getI()));
			break;
		case 1:
			freeCases.get(i).setBonus(new Life(freeCases.get(i).getJ(),freeCases.get(i).getI()));
			break;
		case 2:
			freeCases.get(i).setBonus(new Reverse(freeCases.get(i).getJ(),freeCases.get(i).getI()));
			break;
		case 3:
			freeCases.get(i).setBonus(new Capacity(freeCases.get(i).getJ(),freeCases.get(i).getI()));
			break;
		case 4:
			freeCases.get(i).setBonus(new Shield(freeCases.get(i).getJ(),freeCases.get(i).getI()));
			break;
		case 5:
			freeCases.get(i).setBonus(new Teleport(freeCases.get(i).getJ(),freeCases.get(i).getI()));
			break;
		case 6:
			freeCases.get(i).setBonus(new Cooldown(freeCases.get(i).getJ(),freeCases.get(i).getI()));
			break;
		case 7:
			freeCases.get(i).setBonus(new Slow(freeCases.get(i).getJ(),freeCases.get(i).getI()));
			break;
		case 8:
			freeCases.get(i).setBonus(new Range(freeCases.get(i).getJ(),freeCases.get(i).getI()));
			break;
		}
	}
	
	public static void addBomb(int numJoueur,int i, int j,int porteep,int tpsRestantp) {
		poseBombe.play(1, (float) 0.4);
		
		bombs.add(new Bomb(numJoueur, i, j, porteep, tpsRestantp));
		board.getCase(i, j).setBomb(bombs.get(bombs.size()-1));
	}
	
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
}
