import java.util.Iterator;

import pulpcore.math.Rect;
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
		addSprite ( 2, 2, 2,2, "2x2Obj_01.png", 0, 0 );
		addSprite ( 11, 11, 2,2, "2x2Obj_01.png", 0,-5 );
		addSprite ( 2, 11, 2,2, "2x2Obj_01.png", 0,-5 );
		addSprite ( 11, 2, 2,2, "2x2Obj_01.png", 0,-5 );
		
		//sprites barre du haut
		addSprite ( 5, 2, 1,1, "1x1ObjP2.png", -23, -10 );
		addSprite ( 6, 2, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 7, 2, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( 8, 2, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 9, 2, 1,1, "1x1ObjP2.png", -23, -10 );
		
		// sprites angle haut gauche
		addSprite ( 3, 5, 1,1, "1x1ObjP2.png", -23,-10 );
		addSprite ( 3, 6, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 4, 5, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( 5, 5, 1,1, "PtTableHz.png", 0,-5 );

		// sprites angle bas gauche
		addSprite ( 3, 8, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 3, 9, 1,1, "1x1ObjP2.png", -23, -10 );
		addSprite ( 4, 9, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( 5, 9, 1,1, "PtTableHz.png", 0,-5 );
		
		// sprites angle haut droite
		addSprite ( 9, 5, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 10, 5, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( 11, 5, 1,1, "1x1ObjP2.png", -23,-10 );
		addSprite ( 11, 6, 1,1, "PtTableHz.png", 0,-5 );

		// sprites angle bas droite
		addSprite ( 11, 9, 1,1, "1x1ObjP2.png", -23,-10 );
		addSprite ( 11, 8, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 9, 9, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( 10, 9, 1,1, "PtTableHz.png", 0,-5 );
		
		//sprites barre du bas
		addSprite ( 5, 12, 1,1, "1x1ObjP2.png", -23,-10 );
		addSprite ( 6, 12, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 7, 12, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( 8, 12, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 9, 12, 1,1, "1x1ObjP2.png", -23,-10 );
		
		
		
		// addSprite ( 200, 20 );
		// addSprite ( 200, 2100 );

		scene.add(elements);
	}
	
	private void addSprite ( int x, int y, int sx ,int sy, String name, int tx, int ty )
	{
		Entity entity = new Entity
		(
			name,
			TilemapManager.tileXToPixel(sx),
			TilemapManager.tileYToPixel(sy),
			tx, ty
		);
		int px = TilemapManager.tileXToPixel(x);
		int py = TilemapManager.tileYToPixel(y);
		entity.setLocation ( px, py );
		elements.add(entity.getSprite());
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


class Entity
{
	ImageSprite sprite;
	Rect rect;
	int tx, ty;
	
	public final ImageSprite getSprite() { return sprite; }
	
	Entity ( String name, int width, int height, int tx, int ty )
	{
		sprite = new ImageSprite ( name, 0, 0 );
		this.tx = tx;
		this.ty = ty;
		rect = new Rect ( 0,0, width, height );
	}
	
	public final void setLocation ( int x, int y )
	{
		rect.x = x;
		rect.y = y;
		
		x -= ( sprite.getImage().getWidth() - rect.width ) / 2;
		y -= ( sprite.getImage().getHeight() - rect.height ) / 2;
		x += tx;
		y += ty;
		
		sprite.setLocation ( x, y );
	}
}
