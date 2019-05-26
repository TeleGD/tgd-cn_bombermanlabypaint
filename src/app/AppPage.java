package app;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import app.utils.FontUtils;

public abstract class AppPage extends BasicGameState {

	static protected Color foregroundColor = Color.white;
	static protected Color backgroundColor = Color.black;
	static protected Color highlightColor = Color.red;

	static protected Font titleFont = FontUtils.loadFont ("press-start-2p.ttf", java.awt.Font.BOLD, 40, false);
	static protected Font subtitleFont = FontUtils.loadFont ("Kalinga", java.awt.Font.BOLD, 24, true);
	static protected Font hintFont = FontUtils.loadFont ("press-start-2p.ttf", java.awt.Font.PLAIN, 20, false);

	static protected int titleLineHeight = 50;
	static protected int subtitleLineHeight = 30;
	static protected int hintLineHeight = 30;

	static protected int gap = 40;

	private String title;
	private String subtitle;
	private String hint;

	protected boolean titleVisibility;
	protected boolean subtitleVisibility;
	protected boolean hintVisibility;

	protected int contentWidth;
	protected int contentHeight;
	protected int contentX;
	protected int contentY;

	protected int titleBoxWidth;
	protected int titleBoxHeight;
	protected int titleBoxX;
	protected int titleBoxY;

	protected int subtitleBoxWidth;
	protected int subtitleBoxHeight;
	protected int subtitleBoxX;
	protected int subtitleBoxY;

	protected int hintBoxWidth;
	protected int hintBoxHeight;
	protected int hintBoxX;
	protected int hintBoxY;

	private int titleWidth;
	private int titleHeight;
	private int titleX;
	private int titleY;

	private int subtitleWidth;
	private int subtitleHeight;
	private int subtitleX;
	private int subtitleY;

	private int hintWidth;
	private int hintHeight;
	private int hintX;
	private int hintY;

	protected boolean hintBlink;
	private int hintBlinkPeriod;
	private int hintBlinkCountdown;

	public AppPage () {}

	@Override
	public void init (GameContainer container, StateBasedGame game) {
		this.titleBoxWidth = this.contentWidth;
		this.titleBoxHeight = AppPage.titleLineHeight;
		this.titleBoxX = this.contentX;
		this.titleBoxY = this.contentY;

		this.subtitleBoxWidth = this.contentWidth;
		this.subtitleBoxHeight = AppPage.subtitleLineHeight;
		this.subtitleBoxX = this.contentX;
		this.subtitleBoxY = this.titleBoxY + this.titleBoxHeight + AppPage.gap;

		this.hintBoxWidth = this.contentWidth;
		this.hintBoxHeight = AppPage.hintLineHeight;
		this.hintBoxX = this.contentX;
		this.hintBoxY = this.contentY + this.contentHeight - this.hintBoxHeight;

		this.titleVisibility = true;
		this.subtitleVisibility = true;
		this.hintVisibility = true;

		this.hintBlink = false;
		this.hintBlinkPeriod = 4000;
		this.hintBlinkCountdown = 0;

		this.setTitle ("");
		this.setSubtitle ("");
		this.setHint ("");
	}

	@Override
	public void update (GameContainer container, StateBasedGame game, int delta) {
		if (this.hintBlink) {
			this.hintBlinkCountdown = (this.hintBlinkCountdown + this.hintBlinkPeriod - delta) % this.hintBlinkPeriod;
		}
	}

	@Override
	public void render (GameContainer container, StateBasedGame game, Graphics context) {
		this.renderBackground (container, game, context);
		this.renderTitle (container, game, context);
		this.renderSubtitle (container, game, context);
		this.renderHint (container, game, context);
	}

	private void renderBackground (GameContainer container, StateBasedGame game, Graphics context) {
		context.setBackground (AppPage.backgroundColor);
	}

	private void renderTitle (GameContainer container, StateBasedGame game, Graphics context) {
		if (this.titleVisibility) {
			context.setFont (AppPage.titleFont);
			context.setColor (AppPage.highlightColor);
			context.drawString (this.title, this.titleX - 2, this.titleY - 2);
			context.setColor (AppPage.foregroundColor);
			context.drawString (this.title, this.titleX + 2, this.titleY + 2);
		}
	}

	private void renderSubtitle (GameContainer container, StateBasedGame game, Graphics context) {
		if (this.subtitleVisibility) {
			context.setFont (AppPage.subtitleFont);
			context.setColor (AppPage.foregroundColor);
			context.drawRect (this.subtitleBoxX, this.subtitleBoxY, this.subtitleBoxWidth, this.subtitleBoxHeight);
			context.drawString (this.subtitle, this.subtitleX, this.subtitleY);
		}
	}

	private void renderHint (GameContainer container, StateBasedGame game, Graphics context) {
		if (this.hintVisibility) {
			context.setFont (AppPage.hintFont);
			context.setColor (AppPage.foregroundColor);
			context.drawRect (this.hintBoxX, this.hintBoxY, this.hintBoxWidth, this.hintBoxHeight);
			if (this.hintBlink) {
				int r = AppPage.foregroundColor.getRed ();
				int g = AppPage.foregroundColor.getGreen ();
				int b = AppPage.foregroundColor.getBlue ();
				int a = 1024 * this.hintBlinkCountdown / this.hintBlinkPeriod - 512;
				if (a < 0) {
					a *= -1;
				}
				a -= 128;
				a = Math.max (Math.min (a, 255), 0);
				context.setColor (new Color (r, g, b, a));
			}
			context.drawString (this.hint, this.hintX, this.hintY);
		}
	}

	public void setTitle (String title) {
		this.title = title;
		this.titleWidth = AppPage.titleFont.getWidth (title);
		this.titleHeight = AppPage.titleFont.getHeight (title);
		this.titleX = this.titleBoxX + (this.titleBoxWidth - this.titleWidth) / 2;
		this.titleY = this.titleBoxY + (this.titleBoxHeight - this.titleHeight) / 2;
	}

	public String getTitle () {
		return this.title;
	}

	public void setSubtitle (String subtitle) {
		this.subtitle = subtitle;
		this.subtitleWidth = AppPage.subtitleFont.getWidth (subtitle);
		this.subtitleHeight = AppPage.subtitleFont.getHeight (subtitle);
		this.subtitleX = this.subtitleBoxX + (this.subtitleBoxWidth - this.subtitleWidth) / 2;
		this.subtitleY = this.subtitleBoxY + (this.subtitleBoxHeight - this.subtitleHeight) / 2;
	}

	public String getSubtitle () {
		return this.subtitle;
	}

	public void setHint (String hint) {
		this.hint = hint;
		this.hintWidth = AppPage.hintFont.getWidth (hint);
		this.hintHeight = AppPage.hintFont.getHeight (hint);
		this.hintX = this.hintBoxX + (this.hintBoxWidth - this.hintWidth) / 2;
		this.hintY = this.hintBoxY + (this.hintBoxHeight - this.hintHeight) / 2;
	}

	public String getHint () {
		return this.hint;
	}

	public void initSize (GameContainer container, StateBasedGame game, int width, int height) {
		this.contentWidth = width;
		this.contentHeight = height;
		this.contentX = (container.getWidth () - this.contentWidth) / 2;
		this.contentY = (container.getHeight () - this.contentHeight) / 2;
	}

}
