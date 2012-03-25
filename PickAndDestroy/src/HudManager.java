import pulpcore.image.Colors;
import pulpcore.image.CoreFont;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.Group;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Label;


public class HudManager {
	private Character player1;
	private Character player2;
	private Scene2D scene;
	
	private CoreFont messageFont;
	private Group hud;
	
	private Label healthLabelPlayer1;
	private Label ammoLabelPlayer1;
	
	private Label healthLabelPlayer2;
	private Label ammoLabelPlayer2;
	
	public HudManager(Scene2D scene, Character player1, Character player2) {
		this.scene = scene;
		this.player1 = player1;
		this.player2 = player2;
		
		hud = new Group();
		messageFont = CoreFont.load("complex.font.png");
	}
	
	public final void load()
	{			
		FilledSprite bg = new FilledSprite(Colors.BLACK);
		bg.alpha.set(110);
		bg.setSize(800, 50);
		bg.setLocation(0, 550);
		hud.add(bg);
		
		this.makePlayer1Hud();
		this.makePlayer2Hud();
		
		this.scene.add(hud);
	}
	
	public void makePlayer2Hud() {
		ImageSprite coeur = new ImageSprite("coeur.png", 10, 550);
		coeur.setSize(50, 50);
		healthLabelPlayer2 = new Label(messageFont, "" + this.player2.getNbHealth(), 70, 545);

		ImageSprite bullet = new ImageSprite("bullet.png", 140, 550);
		bullet.setSize(50, 50);
		ammoLabelPlayer2 = new Label(messageFont, "" + this.player2.getNbAmmo(), 200, 545);
		
		hud.add(coeur);
		hud.add(healthLabelPlayer2);
		
		hud.add(bullet);
		hud.add(ammoLabelPlayer2);
	}
	
	public void makePlayer1Hud() {
		ImageSprite coeur = new ImageSprite("coeur.png", 560, 550);
		coeur.setSize(50, 50);
		healthLabelPlayer1 = new Label(messageFont, "" + this.player1.getNbHealth(), 620, 545);

		ImageSprite bullet = new ImageSprite("bullet.png", 680, 550);
		bullet.setSize(50, 50);
		ammoLabelPlayer1 = new Label(messageFont, "" + this.player1.getNbAmmo(), 740, 545);
		
		hud.add(coeur);
		hud.add(healthLabelPlayer1);
		
		hud.add(bullet);
		hud.add(ammoLabelPlayer1);
	}
	
	public final void update(int elapsedTime)
    {
		healthLabelPlayer1.setText("" + this.player1.getNbHealth());
		ammoLabelPlayer1.setText("" + this.player1.getNbAmmo());
		
		healthLabelPlayer2.setText("" + this.player2.getNbHealth());
		ammoLabelPlayer2.setText("" + this.player2.getNbAmmo());
    }
}
