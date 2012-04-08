import java.util.ArrayList;
import pulpcore.scene.Scene2D;

public class ShootManager
{
	private final ArrayList<Shoot>	shoots	= new ArrayList<Shoot> ();
	private EntityManager			entityManager;

	public static ShootManager		shared;

	ShootManager()
	{
		shared = this;
	}

	public final void load(Scene2D scene, EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	public final void update(int elapsedTime)
	{
		Character player1 = CharacterManager.shared.getPlayer ( 0 );
		Character player2 = CharacterManager.shared.getPlayer ( 1 );
		
		ArrayList<Shoot> removed = new ArrayList<Shoot> ();

		for (int i = 0; i < shoots.size (); i++)
		{
			Shoot s = shoots.get ( i );
			s.update ( elapsedTime );

			if ( s.getRect ().intersects ( player1.getRect () ) ) // collide player 1
			{
				removed.add ( s );
				player1.hitBy ( s.getHitPoints() );
			}
			else
			if ( s.getRect ().intersects ( player2.getRect () ) ) // collide player 2
			{
				removed.add ( s );
				player2.hitBy ( s.getHitPoints() );
			}
			else
			if (!s.getRect ().intersects ( TilemapManager.getRect () )) // out of tilemap
			{
				removed.add ( s );
			}
			else if (Entity.isWallAtRect ( s.getRect () )) // collide a wall
			{
				removed.add ( s );
			}
		}

		for (int i = 0; i < removed.size (); i++)
		{
			removeShoot ( removed.get ( i ) );
		}
	}

	public final void addShoot(Shoot shoot)
	{
		shoots.add ( shoot );
		entityManager.addEntity ( shoot );
	}

	public final void removeShoot(Shoot shoot)
	{
		entityManager.removeEntity ( shoot );
		shoots.remove ( shoot );
	}
}
