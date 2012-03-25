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
	private Label healthLabel;
	private Label ammoLabel;
	
	public HudManager(Scene2D scene, Character player1, Character player2) {
		this.scene = scene;
		this.player1 = player1;
		this.player2 = player2;
	}
	
	public final void load()
	{
		Group hud = new Group();
		CoreFont messageFont = CoreFont.load("complex.font.png");
		
		FilledSprite bg = new FilledSprite(Colors.BLACK);
		bg.alpha.set(110);
		bg.setSize(800, 50);
		bg.setLocation(0, 550);
		hud.add(bg);
		
		ImageSprite coeur = new ImageSprite("coeur.png", 10, 550);
		coeur.setSize(50, 50);
		healthLabel = new Label(messageFont, "" + this.player1.getNbHealth(), 70, 545);

		ImageSprite bullet = new ImageSprite("bullet.png", 140, 550);
		bullet.setSize(50, 50);
		ammoLabel = new Label(messageFont, "" + this.player1.getNbAmmo(), 200, 545);
		
		hud.add(coeur);
		hud.add(healthLabel);
		
		hud.add(bullet);
		hud.add(ammoLabel);
		
		this.scene.add(hud);
	}
	
	public final void update(int elapsedTime)
    {
		healthLabel.setText("" + this.player1.getNbHealth());
		ammoLabel.setText("" + this.player1.getNbAmmo());
    }
}
