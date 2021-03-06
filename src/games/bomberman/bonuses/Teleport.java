package games.bomberman.bonuses;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

import games.bomberman.Bonus;
import games.bomberman.Player;
import games.bomberman.World;

public class Teleport extends Bonus {

	private boolean activated;
	private boolean deleted;
	private static Audio sound;
	private static Image sprite;
	private World w;

	static {
		sprite = AppLoader.loadPicture(World.DIRECTORY_IMAGES + "bonus_teleport.png");
		sound = AppLoader.loadAudio(World.DIRECTORY_SOUNDS_BONUS + "tp.ogg");
	}

	public Teleport(World w, int caseX, int caseY) {
		super(caseX, caseY);
		super.setSprite(sprite);
		this.w = w;
		this.activated = false;
		this.deleted = false;
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {}

	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		super.render(container, game, context);
	}

	public void activate(Player player) {
		if (!activated) {
			this.activated = true;
			int i;
			int j;
			do {
				i = (int) (Math.random() * w.getBoard().getDim()[0]);
				j = (int) (Math.random() * w.getBoard().getDim()[1]);
			} while (!w.getBoard().getCase(i, j).isPassable());
			player.setIJ(i, j);
			sound.playAsSoundEffect(1f, .4f, false);
			this.deleted = true;
		}
	}

	public boolean isActivated() {
		return this.activated;
	}

	public boolean isDeleted() {
		return this.deleted;
	}

}
