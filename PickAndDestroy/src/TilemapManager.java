import pulpcore.scene.Scene2D;
import pulpcore.sprite.ImageSprite;


public class TilemapManager
{
	public final void load(Scene2D scene)
	{
		ImageSprite background = new ImageSprite("background.png", 0,0 );
		// background.setLocation ( background.getImage ().getWidth(), background.getImage ().getHeight() );
		// background.setLocation ( 0,0 );
		scene.add( background );
	}
	
	public final void update( int elapsedTime )
	{
	}
}
