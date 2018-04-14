package games.bomberman.bonus;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;

import games.bomberman.Player;
import games.bomberman.World;

public class Shield extends Bonus{
	private boolean activated, deleted;
	private Player player;
	private long initTime;
	private Sound sound;
	
	public Shield(int caseX, int caseY) {
		super(caseX, caseY);
		this.activated = false;
		this.deleted = false;
		
		try {
			Image sprite = new Image(World.DIRECTORY_IMAGES+"bonus_shield.png");
			super.setSprite(sprite);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			sound = new Sound("musics/bonus/tataa.ogg");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void activate(Player player) {
		if (!isActivated()) {
			this.activated = true;
		
			player.setBouclier(true);
		
			this.player = player;
			initTime = System.currentTimeMillis();

			sound.play(1, (float) 0.4);
		}
	}
	
	public void desactivate() {
		if (this.player.isBouclier())
			this.player.setBouclier(false);
		this.deleted = true;
	}
	
	public boolean isActivated() {
		return this.activated;
	}
	
	public boolean isDeleted() {
		return this.deleted;
	}
	
	public void update (GameContainer container, StateBasedGame game, int delta) {
		if (activated && (System.currentTimeMillis() - initTime > 30000)) {
			this.desactivate();
		}
	}	
	
	public void render (GameContainer container, StateBasedGame game, Graphics context) {
		super.render(container, game, context);
	}
}
