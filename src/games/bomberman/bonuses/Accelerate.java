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

public class Accelerate extends Bonus {

	private boolean activated;
	private boolean deleted;
	private Player player;
	private int duration;
	private static Audio sound;
	private static Image sprite;

	static {
		sound = AppLoader.loadAudio(World.DIRECTORY_SOUNDS_BONUS + "sncf.ogg");
		sprite = AppLoader.loadPicture(World.DIRECTORY_IMAGES + "bonus_accelerate.png");
	}

	public Accelerate(int caseX, int caseY) {
		super(caseX, caseY);
		super.setSprite(sprite);
		this.activated = false;
		this.deleted = false;
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		duration -= delta;
		if (activated && (duration <= 0)) {
			activated = false;
			this.desactivate();
		}
	}

	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		super.render(container, game, context);
	}

	public void activate(Player player) {
		if (!isActivated()) {
			this.activated = true;
			player.setSpeed(player.getSpeed() * 10f);
			duration = 7000;
			this.player = player;
			sound.playAsSoundEffect(1f, .4f, false);
		}
	}

	public void desactivate() {
		this.deleted = true;
		this.player.setSpeed(player.getSpeed() * .1f);
	}

	public boolean isDeleted() {
		return deleted;
	}

	public boolean isActivated() {
		return this.activated;
	}

}
