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
		
		// sprites 2x2
		addSprite ( 2, 2, 2,2, "2x2Obj_01.png", 0, -13 );
		addSprite ( 10, 10, 2,2, "2x2Obj_01.png", 0,-13 );
		addSprite ( 2, 10, 2,2, "2x2Obj_01.png", 0,-13 );
		addSprite ( 10, 2, 2,2, "2x2Obj_01.png", 0,-13 );
		
		//
		
		// addSprite ( 200, 20 );
		// addSprite ( 200, 2100 );

		scene.add(elements);
	}
	
	private void addSprite ( int x, int y, int sx ,int sy, String name, int tx, int ty )
	{
		ImageSprite star = new ImageSprite ( name, 0, 0);
		int px = TilemapManager.tileXToPixel(x);
		int py = TilemapManager.tileYToPixel(y);
		px -= ( star.getImage().getWidth() - TilemapManager.tileXToPixel(sx) ) / 2;
		py -= ( star.getImage().getHeight() - TilemapManager.tileXToPixel(sx) ) / 2;
		px += tx;
		py += ty;
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
