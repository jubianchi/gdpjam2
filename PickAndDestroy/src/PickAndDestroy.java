import pulpcore.scene.Scene2D;

public class PickAndDestroy extends Scene2D
{
	MusicManager musicManager;
	EntityManager entityManager;
	TilemapManager tilemapManager;
	KeyManager keyManager;
	CharacterManager characterManager;
	ConfigManager configManager;
	ItemManager itemManager;

    public void load()
    {
    	tilemapManager = new TilemapManager ();
        tilemapManager.load ( this );
        
        // musicManager = new MusicManager();
        // musicManager.load ();
        
        entityManager = new EntityManager ();
        entityManager.load ( this );
           
        characterManager = new CharacterManager();
        characterManager.load(this);
        
        itemManager = new ItemManager();
        itemManager.load(this);
        
        keyManager = new KeyManager(characterManager.getPlayer(0), characterManager.getPlayer(1));
        
        configManager = new ConfigManager();
        configManager.load(this);
    }
    
    public void update(int elapsedTime)
    {
    	tilemapManager.update ( elapsedTime );
    	// musicManager.update ( elapsedTime );
    	// entityManager.update ( elapsedTime );
    	keyManager.update ( elapsedTime );
    	configManager.update ( elapsedTime );
    	itemManager.update(elapsedTime);
    }
    
}

