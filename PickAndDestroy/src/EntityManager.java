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
		
		addSprite ( 0, 0 );
		addSprite ( 0, 15 );
		addSprite ( 0, 10 );

		// addSprite ( 200, 20 );
		// addSprite ( 200, 2100 );

		scene.add(elements);
	}
	
	private void addSprite ( int x, int y )
	{
		ImageSprite star = new ImageSprite("star.png", 0, 0);
		star.setLocation ( x, y );
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
