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
	DebugManager debugManager;
	HudManager hudManager;

    public void load()
    {
    	configManager = new ConfigManager();
        configManager.load(this);
        
    	tilemapManager = new TilemapManager ();
        tilemapManager.load ( this );        
        
        if(ConfigManager.gameModesConfig.getValue("enableSound") == 1) 
        {
        	musicManager = new MusicManager();
            musicManager.load ();
        }        
        
        entityManager = new EntityManager ();
        entityManager.load ( this );
           
        itemManager = new ItemManager();
        itemManager.load(this, entityManager);
        
        characterManager = new CharacterManager();
        characterManager.load(this, entityManager, itemManager);
        
        keyManager = new KeyManager(characterManager.getPlayer(0), characterManager.getPlayer(1));
        
        debugManager = new DebugManager ();
        debugManager.load(this, entityManager);

        hudManager = new HudManager(this, characterManager.getPlayer(0), characterManager.getPlayer(1));
        hudManager.load();
        
        keyManager = new KeyManager(characterManager.getPlayer(0), characterManager.getPlayer(1));               
    }
    
    public void update(int elapsedTime)
    {
    	configManager.update ( elapsedTime );
    	tilemapManager.update ( elapsedTime );
    	
    	if(ConfigManager.gameModesConfig.getValue("enableSound") == 1) 
        {
    		musicManager.update ( elapsedTime );
        }
    	
    	entityManager.update ( elapsedTime );
    	keyManager.update ( elapsedTime );    	
    	itemManager.update(elapsedTime);
    	characterManager.update (elapsedTime );

    	debugManager.update(elapsedTime);

    	hudManager.update(elapsedTime);
    }
    
}

