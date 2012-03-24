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
	KeyManager keyManager;
	CharacterManager characterManager;
	ItemManager itemManager;
	
    public void load()
    {
    	tilemapManager = new TilemapManager ();
        tilemapManager.load ( this );
        
        // musicManager = new MusicManager();
        // musicManager.load ();
        
        // entityManager = new EntityManager ();
        // entityManager.load ( this );
           
        characterManager = new CharacterManager();
        characterManager.load(this);
        
        itemManager = new ItemManager();
        itemManager.load(this);
        
        keyManager = new KeyManager(characterManager.getPlayer(0), characterManager.getPlayer(1));
    }
    
    public void update(int elapsedTime)
    {
    	tilemapManager.update ( elapsedTime );
    	// musicManager.update ( elapsedTime );
    	// entityManager.update ( elapsedTime );
    	keyManager.update ( elapsedTime );
    }

    
}

