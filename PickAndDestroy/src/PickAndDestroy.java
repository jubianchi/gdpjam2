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
           
        characterManager = new CharacterManager();
        characterManager.load(this);
        
        itemManager = new ItemManager();
        itemManager.load(this);
        
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
    	hudManager.update(elapsedTime);
    }
    
}

