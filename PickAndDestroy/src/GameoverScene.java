import pulpcore.animation.Easing;
import pulpcore.animation.Timeline;
import pulpcore.image.CoreFont;
import pulpcore.scene.Scene2D;
import pulpcore.sound.Sound;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Label;
import pulpcore.Input;
import pulpcore.Stage;

public class GameoverScene extends Scene2D
{
	String[]	messageText;
	Timeline	timeline;

	GameoverScene()
	{
		this ( new String[] { "GameScene over", "Player one" } );
	}

	GameoverScene(String[] text)
	{
		messageText = text;
	}

	@Override
	public void load()
	{
		ImageSprite background = new ImageSprite ( "restart_screen.jpg", 0, 0 );
		add ( background );

		// Add word-wrapped background text
		int x = 50;
		int y = 45;
		int startTime = 0;

		// Add messages (play in a loop)
		timeline = new Timeline ();
		x = Stage.getWidth () / 2;
		y = (Stage.getHeight () / 2) - 150;
		startTime = 0;
		CoreFont messageFont = CoreFont.load ( "gameover.font.png" );
		for (String line : messageText)
		{
			// Add the sprite
			int labelWidth = -1; // auto
			int labelHeight = 10;
			Label label = new Label ( messageFont, line, x, y, labelWidth, labelHeight );
			label.setAnchor ( 0.5, 0.5 );
			label.alpha.set ( 0 );
			add ( label );

			// Animate (zoom)
			timeline.at ( startTime ).animate ( label.alpha, 0, 255, 500 );
			timeline.at ( startTime ).animate ( label.height, 10, 102, 1500, Easing.STRONG_OUT );
			timeline.at ( startTime ).animate ( label.angle, -0.1, 0.1, 2000 );
			timeline.at ( startTime + 1750 ).animate ( label.alpha, 255, 0, 250 );
			startTime += 2000;
		}
		timeline.loopForever ( 1000 );
		addTimeline ( timeline );

		Sound dieSound = Sound.load ( "rire+mort.wav" );
		dieSound.play ();
	}

	public void update(int elapsedTime)
	{
		if (Input.isPressed ( Input.KEY_SPACE ))
		{
			Stage.replaceScene ( new GameScene () );
		}
	}

}
