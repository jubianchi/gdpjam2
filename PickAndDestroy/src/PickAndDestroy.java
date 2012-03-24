import pulpcore.scene.Scene2D;

public class PickAndDestroy extends Scene2D
{
	MusicManager musicManager;
	EntityManager entityManager;
	TilemapManager tilemapManager;
	
    public void load()
    {
    	tilemapManager = new TilemapManager ();
        tilemapManager.load ( this );
        
        // musicManager = new MusicManager();
        // musicManager.load ();
        
        // entityManager = new EntityManager ();
        // entityManager.load ( this );
    }
    
    public void update(int elapsedTime)
    {
    	tilemapManager.update ( elapsedTime );
    	// musicManager.update ( elapsedTime );
    	// entityManager.update ( elapsedTime );
    }

    
}

