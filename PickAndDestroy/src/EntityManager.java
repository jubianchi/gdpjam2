import java.util.Iterator;

import pulpcore.scene.Scene2D;
import pulpcore.sprite.Group;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Sprite;


public class EntityManager
{
	Group elements;
	
	public final void load( Scene2D scene )
	{
		elements = new Group ();
		
		addSprite ( 0, 0, "Star.png" );
		addSprite ( 14, 14, "Star.png" );
		addSprite ( 0, 5, "Star.png" );

		// addSprite ( 200, 20 );
		// addSprite ( 200, 2100 );

		scene.add(elements);
	}
	
	private void addSprite ( int tx, int ty, String name )
	{
		ImageSprite star = new ImageSprite ( name, 0, 0);
		int px = TilemapManager.tileXToPixel(tx);
		int py = TilemapManager.tileYToPixel(ty);
		star.setLocation ( px, py );
		// star.setLocation ( x - star.getImage ().getWidth () / 2, y- star.getImage ().getWidth () / 2 );
        // star.angle.set(rand(Math.PI*2));
        elements.add(star);
	}
	
	public void update(int elapsedTime)
    {
		int i = 0;
		while ( i < elements.size() )
        {
			Sprite element = elements.get ( i );
			Sprite lastElement = elements.get ( elements.size () - 1 );
			int elementY =element.y.getAsInt ();
			int lastElementY =lastElement.y.getAsInt ();
			if ( elementY > lastElementY )
			{
				elements.moveToTop ( element );
			}
			else
			{
				i++;
			}
        }
    }
}
