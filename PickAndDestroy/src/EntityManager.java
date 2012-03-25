import java.util.ArrayList;
import java.util.List;

import pulpcore.scene.Scene2D;
import pulpcore.sprite.Group;
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
	}
	
	private int totalElapsedTime = 0;
	
	public void update(int elapsedTime)
    {
		// dumpGroup ();
		
		int i = 1;
		while ( i < group.size() )
        {
			Sprite sprite = group.get ( i );
			
			int j = i-1;
			while ( j >= 0 )
			{
				Sprite previous = group.get ( j );
				
				int y = sprite.y.getAsInt ();
				int previousY = previous.y.getAsInt ();
				
				if ( y < previousY )
				{
					// System.out.println ( "Moving " + y + " < " + previousY );
					group.moveDown ( sprite );
					// dumpGroup ();
				}
				else
					break;
				
				j--;
			}
			i++;
        }
		
		// dumpGroup ();
		
		/*
		int i = 0;
		while ( i < group.size() )
        {
			Sprite element = group.get ( i );
			Sprite lastElement = group.get ( group.size () - 1 );
			int elementY =element.y.getAsInt ();
			int lastElementY = lastElement.y.getAsInt ();
			if ( elementY > lastElementY )
			{
				group.moveToTop ( element );
			}
			else
			{
				i++;
			}
        }
		*/
    }

	private void dumpGroup()
	{
		System.out.println("---");
		int i;
		i = 0;
		while ( i < group.size() )
        {
			Sprite element = group.get ( i );
			System.out.println("At " + i + " y = " + element.y.getAsInt () );
			i++;
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
