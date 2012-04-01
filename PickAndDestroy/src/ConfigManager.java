import pulpcore.Input;
import pulpcore.scene.Scene2D;

public class ConfigManager
{
	public static final ConfigFile	gameModesConfig	= new ConfigFile ( "gamemodes.txt" );

	public final void load(Scene2D scene)
	{
		reloadFiles ();
	}

	public final void update(int elapsedTime)
	{
		if (Input.isDown ( Input.KEY_F9 ))
		{
			reloadFiles ();
		}
	}

	public final void reloadFiles()
	{
		gameModesConfig.reloadFile ();
	}

}
