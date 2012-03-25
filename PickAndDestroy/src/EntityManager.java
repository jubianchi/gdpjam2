import java.util.ArrayList;
import java.util.List;

import pulpcore.math.Rect;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.Group;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Sprite;


public class EntityManager
{
	private final ArrayList<Entity> entities = new ArrayList<Entity> ();
	public final List<Entity> getEntities() { return entities; }
	
	private Group group;

	public final void load( Scene2D scene )
	{
		group = new Group ();


		
		//sprites barre du haut
		addSprite ( 5, 2, 1,1, "1x1ObjP2.png", -0, -10 );
		addSprite ( 6, 2, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 7, 2, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( 8, 2, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 9, 2, 1,1, "1x1ObjP2.png", -0, -10 );
		
		// sprites angle haut gauche
		addSprite ( 3, 5, 1,1, "1x1ObjP2.png", -0,-10 );
		addSprite ( 3, 6, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 4, 5, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( 5, 5, 1,1, "PtTableHz.png", 0,-5 );

		// sprites angle bas gauche
		addSprite ( 3, 8, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 3, 9, 1,1, "1x1ObjP2.png", -0, -10 );
		addSprite ( 4, 9, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( 5, 9, 1,1, "PtTableHz.png", 0,-5 );
		
		// sprites angle haut droite
		addSprite ( 9, 5, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 10, 5, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( 11, 5, 1,1, "1x1ObjP2.png", -0,-10 );
		addSprite ( 11, 6, 1,1, "PtTableHz.png", 0,-5 );

		// sprites angle bas droite
		
		addSprite ( 11, 8, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 9, 9, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( 10, 9, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 11, 9, 1,1, "1x1ObjP2.png", -0,-10 );
		
		//sprites barre du bas
		addSprite ( 5, 12, 1,1, "1x1ObjP2.png", -0,-10 );
		addSprite ( 6, 12, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 7, 12, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( 8, 12, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 9, 12, 1,1, "1x1ObjP2.png", -0,-10 );			
		
		// sprites 2x2
		addSprite ( 2, 2, 2,2, "2x2Obj_01.png", 0, 0 );
		addSprite ( 11, 11, 2,2, "2x2Obj_01.png", 0,-5 );
		addSprite ( 2, 11, 2,2, "2x2Obj_01.png", 0,-5 );
		addSprite ( 11, 2, 2,2, "2x2Obj_01.png", 0,-5 );
		// addSprite ( 200, 20 );
		// addSprite ( 200, 2100 );

		scene.add(group);

		/*
		debugImage = new CoreImage ( 800, 600 );
		CoreGraphics graphics = debugImage.createGraphics ();
		graphics.setColor ( 0xFF00FF00 );
		// graphics.setAlpha ( 0x00 );
		graphics.fillRect ( 0,0,800,600 );
		// graphics.clear ();
		graphics.drawLine ( 0,0, 800, 600 );
		debugImageSprite = new ImageSprite(debugImage,0,0);
		debugGroup = new Group();
		debugGroup.setBlendMode ( BlendMode.Add() );
		debugGroup.add ( debugImageSprite );
		scene.add ( debugGroup );
		*/
	}
	
	private void addSprite ( int x, int y, int sx ,int sy, String name, int tx, int ty )
	{
		Entity entity = new Entity
		(
			name,
			TilemapManager.tileXToPixel(sx),
			TilemapManager.tileYToPixel(sy)
		);
		getEntities ().add ( entity );
		
		// entity.getSprite ().getImage ().width
		// entity.setTranslation ( tx, ty );
		
		int px = TilemapManager.tileXToPixel(x);
		int py = TilemapManager.tileYToPixel(y);
		entity.setLocation ( px, py );

		double anchorX = -(double)tx / (double)entity.getSprite ().getImage ().getWidth ();
		double anchorY = -(double)ty / (double)entity.getSprite ().getImage ().getHeight ();
		entity.getSprite().setAnchor ( 0.5 + anchorX, 0.5 + anchorY );

		group.add(entity.getSprite());
	}
	
	public void update(int elapsedTime)
    {
		int i = 0;
		while ( i < group.size() )
        {
			Sprite element = group.get ( i );
			Sprite lastElement = group.get ( group.size () - 1 );
			int elementY =element.y.getAsInt ();
			int lastElementY =lastElement.y.getAsInt ();
			if ( elementY > lastElementY )
			{
				group.moveToTop ( element );
			}
			else
			{
				i++;
			}
        }
    }

	public void addEntity(Entity entity)
	{
		entities.add ( entity );
		group.add ( entity.getSprite () );
	}

	public void removeEntity(Entity entity)
	{
		group.remove ( entity.getSprite () );
		entities.remove ( entity );
	}
}



class Entity
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
