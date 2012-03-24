import pulpcore.scene.Scene2D;
import pulpcore.sprite.ImageSprite;


public class TilemapManager
{
	public static final int TILE_WIDTH = 53;
	public static final int TILE_HEIGHT = 40;
	
	public static final int tileXToPixel ( int tx )
	{
		return tx * TILE_WIDTH;
	}
	
	public static final int tileYToPixel ( int ty )
	{
		return ty * TILE_HEIGHT;
	}
	
	public static final int pixelXToTile ( int x )
	{
		return x / TILE_WIDTH;
	}
	
	public static final int pixelYToTile ( int y )
	{
		return y / TILE_HEIGHT;
	}
	
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
