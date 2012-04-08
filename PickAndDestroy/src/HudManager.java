import pulpcore.image.Colors;
import pulpcore.image.CoreFont;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.Group;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Label;

public class HudManager
{
	private Character	rightPlayer;
	private Character	leftPlayer;
	private Scene2D		scene;

	private CoreFont	messageFont;
	private Group		hud;

	private Label		healthLabelRightPlayer;
	private Label		ammoLabelRightPlayer;

	private Label		healthLabelLeftPlayer;
	private Label		ammoLabelLeftPlayer;

	public HudManager(Scene2D scene, Character leftPlayer, Character rightPlayer )
	{
		this.scene = scene;
		this.rightPlayer = rightPlayer;
		this.leftPlayer = leftPlayer;

		hud = new Group ();
		messageFont = CoreFont.load ( "hud.font.png" );
	}

	public final void load()
	{
		FilledSprite bg = new FilledSprite ( Colors.BLACK );
		bg.alpha.set ( 128 );
		bg.setSize ( 800, 35 );
		bg.setLocation ( 0, 600 - bg.height.getAsInt () );
		hud.add ( bg );

		this.makeRightPlayerHud ();
		this.makeLeftPlayerHud ();

		this.scene.add ( hud );
	}

	public void makeLeftPlayerHud()
	{
		ImageSprite coeur = new ImageSprite ( "coeur.png", 10, 560 );
		coeur.setSize ( 40, 40 );
		healthLabelLeftPlayer = new Label ( messageFont, "" + this.leftPlayer.getNbHealth (), 50, 545 );

		ImageSprite bullet = new ImageSprite ( "bullet.png", 90, 560 );
		bullet.setSize ( 40, 40 );
		ammoLabelLeftPlayer = new Label ( messageFont, "" + this.leftPlayer.getNbAmmo (), 130, 545 );

		hud.add ( coeur );
		hud.add ( healthLabelLeftPlayer );

		hud.add ( bullet );
		hud.add ( ammoLabelLeftPlayer );
	}

	public void makeRightPlayerHud()
	{
		ImageSprite coeur = new ImageSprite ( "coeur.png", 600, 560 );
		coeur.setSize ( 40, 40 );
		healthLabelRightPlayer = new Label ( messageFont, "" + this.rightPlayer.getNbHealth (), 640, 545 );

		ImageSprite bullet = new ImageSprite ( "bullet.png", 680, 560 );
		bullet.setSize ( 40, 40 );
		ammoLabelRightPlayer = new Label ( messageFont, "" + this.rightPlayer.getNbAmmo (), 720, 545 );

		hud.add ( coeur );
		hud.add ( healthLabelRightPlayer );

		hud.add ( bullet );
		hud.add ( ammoLabelRightPlayer );
	}

	public final void update(int elapsedTime)
	{
		healthLabelRightPlayer.setText ( "" + this.rightPlayer.getNbHealth () );
		ammoLabelRightPlayer.setText ( "" + this.rightPlayer.getNbAmmo () );

		healthLabelLeftPlayer.setText ( "" + this.leftPlayer.getNbHealth () );
		ammoLabelLeftPlayer.setText ( "" + this.leftPlayer.getNbAmmo () );
	}
}
