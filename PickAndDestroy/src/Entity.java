import pulpcore.math.Rect;
import pulpcore.sprite.ImageSprite;


public class Entity
{
	private ImageSprite sprite;
	private Rect rect;
	
	public final ImageSprite getSprite() { return sprite; }
	public final Rect getRect() { return rect; }

	public Entity ( String name, int width, int height )
	{
		sprite = new ImageSprite ( name, 0, 0 );
		sprite.setAnchor(0.5, 0.5);
		rect = new Rect ( 0,0, width, height );
	}
	
	public final void setLocation ( int x, int y )
	{
		rect.x = x;
		rect.y = y;

		sprite.setLocation ( rect.x + rect.width / 2, rect.y + rect.height / 2 );
		sprite.setAnchor ( 0.5, 0.5 );
	}
	
	public final void setLocationOnTilemap ( int tx, int ty )
	{
		setLocation
		(
			(int) ( ( tx * TilemapManager.TILE_WIDTH ) + ( TilemapManager.TILE_WIDTH - rect.width ) / 2 ),
			(int) ( ( ty * TilemapManager.TILE_HEIGHT ) + ( TilemapManager.TILE_HEIGHT - rect.height ) / 2 )
		);
	}
	
	public final void moveOf(int dx, int dy)
	{
		rect.x += dx;
		rect.y += dy;
		sprite.setLocation ( sprite.x.getAsInt () + dx, sprite.y.getAsInt () + dy );
	}
}