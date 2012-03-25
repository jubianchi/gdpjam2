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
