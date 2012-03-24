import java.util.Timer;
import java.util.TimerTask;

import pulpcore.scene.Scene2D;


public class ItemManager {
	public static final int GRID_WIDTH_CASES = 15;
	public static final int GRID_HEIGHT_CASES = 15; 
	
	public final void load(Scene2D scene)
	{
		
	}
	
	public void spawnItem() 
	{
		Timer spawnTimer = new Timer();
		spawnTimer.scheduleAtFixedRate(new SpawnLifeTask(), 0, 5);
	}
	
	class SpawnLifeTask extends TimerTask 
	{
		public void run()
		{
			
		}
	}
	
	class SpawnAmmoTask extends TimerTask 
	{
		public void run()
		{
			
		}
	}
	
	public final void update(int elapsedTime)
    {
		
    }
}
