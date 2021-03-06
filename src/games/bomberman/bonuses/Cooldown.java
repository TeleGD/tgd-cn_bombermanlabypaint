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

public class Cooldown extends Bonus {

	private boolean activated;
	private boolean deleted;
	private Player player;
	private int duration;
	private static Image sprite;
	private static Audio sound;

	static {
		sprite = AppLoader.loadPicture(World.DIRECTORY_IMAGES + "bonus_cooldown.png");
		sound = AppLoader.loadAudio(World.DIRECTORY_SOUNDS_BONUS + "tataa.ogg");
	}

	public Cooldown(int caseX, int caseY) {
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
		if (!activated) {
			this.activated = true;
			player.setDropCoolDown(500);
			this.player = player;
			duration = 15000;
			sound.playAsSoundEffect(1f, .4f, false);
		}
	}

	public void desactivate() {
		player.setDropCoolDown(1000);
		deleted = true;
	}

	public boolean isActivated() {
		return this.activated;
	}

	public boolean isDeleted() {
		return this.deleted;
	}

}
