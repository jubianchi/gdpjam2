import pulpcore.scene.Scene2D;

public class PickAndDestroy extends Scene2D
{
	MusicManager		musicManager;
	EntityManager		entityManager;
	TilemapManager		tilemapManager;
	LevelManager		levelManager;
	CharacterManager	characterManager;
	ConfigManager		configManager;
	ItemManager			itemManager;
	DebugManager		debugManager;
	HudManager			hudManager;
	PhaseManager		phaseManager;
	ShootManager		shootManager;

	public void load()
	{
		configManager = new ConfigManager ();
		configManager.load ( this );

		tilemapManager = new TilemapManager ();
		tilemapManager.load ( this );

		entityManager = new EntityManager ();
		entityManager.load ( this );

		levelManager = new LevelManager ();
		levelManager.load ( this, entityManager );

		musicManager = new MusicManager ();
		musicManager.load ();

		itemManager = new ItemManager ();
		itemManager.load ( this, entityManager );

		phaseManager = new PhaseManager ( musicManager );
		phaseManager.load ();

		characterManager = new CharacterManager ();
		characterManager.load ( this, entityManager, itemManager );

		debugManager = new DebugManager ();
		debugManager.load ( this, entityManager );

		hudManager = new HudManager ( this, characterManager.getPlayer ( 0 ), characterManager.getPlayer ( 1 ) );
		hudManager.load ();

		shootManager = new ShootManager ();
		shootManager.load ( this, entityManager );

		setDirtyRectanglesEnabled ( false );
	}

	public void update(int elapsedTime)
	{
		configManager.update ( elapsedTime );
		tilemapManager.update ( elapsedTime );

		if (ConfigManager.gameModesConfig.getValue ( "enableSound" ) == 1)
		{
			musicManager.update ( elapsedTime );
		}

		phaseManager.update ( elapsedTime );

		entityManager.update ( elapsedTime );
		itemManager.update ( elapsedTime );
		characterManager.update ( elapsedTime );

		debugManager.update ( elapsedTime );

		hudManager.update ( elapsedTime );

		shootManager.update ( elapsedTime );
	}

}
