import com.jcraft.jorbis.JOrbisException;

import pulpcore.scene.Scene2D;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Label;
import pulpcore.animation.Fixed;
import pulpcore.image.Colors;


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

