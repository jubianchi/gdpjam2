import java.util.Timer;
import java.util.TimerTask;
import static pulpcore.math.CoreMath.rand;
import pulpcore.Input;
import pulpcore.animation.Easing;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.ImageSprite;


public class ItemManager {
	public static final int GRID_WIDTH_CASES = 15;
	public static final int GRID_HEIGHT_CASES = 15; 
	
	private Scene2D scene;
	private int itemCount = 0;
	private Timer spawnTimer;
	
	public final void load(Scene2D scene)
	{
		this.scene = scene;
		this.spawnItem();
	}
	
	public void spawnItem() 
	{
		spawnTimer = new Timer();
		spawnTimer.scheduleAtFixedRate(new SpawnLifeTask(this), 0, 2000);
	}
	
	public void addItem(ImageSprite item) 
	{
		if(this.itemCount > 20) return;
		
		this.scene.add(item);
		this.itemCount++;
	}
	
	public void pickItem(ImageSprite item) 
	{
		this.scene.remove(item);
	}
	
	class SpawnLifeTask extends TimerTask 
	{
		private ItemManager manager;
		
		public SpawnLifeTask(ItemManager manager) {
			this.manager = manager;
		}
		
		public void run()
		{
			if(rand(1, 100) <= 50) return;
			
			int x = rand(1, ItemManager.GRID_WIDTH_CASES);
			int y = rand(1, ItemManager.GRID_HEIGHT_CASES);
			
			System.out.println("Spawning in : " + x + ":" + y);
			
			ImageSprite sprite = new ImageSprite("coeur.png", 5, 5);
			sprite.setSize(0, 0);
			sprite.setLocation((x * 53.3) - 42.65, (y * 40) - 35);
			sprite.setAnchor(0.5, 0.5);
			manager.addItem(sprite);
			
			sprite.scaleTo(33, 30, 500, Easing.ELASTIC_IN_OUT);
		}
	}
	
	class SpawnAmmoTask extends TimerTask 
	{
		private ItemManager manager;
		
		public SpawnAmmoTask(ItemManager manager) {
			this.manager = manager;
		}

		public void run()
		{
			if(rand(1, 100) <= 50) return;
			
			int x = rand(1, ItemManager.GRID_WIDTH_CASES);
			int y = rand(1, ItemManager.GRID_HEIGHT_CASES);
			
			System.out.println("Spawning in : " + x + ":" + y);
			
			ImageSprite sprite = new ImageSprite("bullet.png", 5, 5);
			sprite.setSize(0, 0);
			sprite.setLocation((x * 53.3) - 32.65, (y * 40) - 35);
			sprite.setAnchor(0.5, 0.5);
			manager.addItem(sprite);
			
			sprite.scaleTo(13, 30, 500, Easing.ELASTIC_IN_OUT);
		}
	}
	
	public final void update(int elapsedTime)
    {
		if(Input.isDown(Input.KEY_F7)) {
			System.out.println("Switched to LifeSpawn");			
			
			this.spawnTimer.cancel();
			this.itemCount = 0;
			
			spawnTimer = new Timer();
			spawnTimer.scheduleAtFixedRate(new SpawnLifeTask(this), 0, 2000);
		} else if(Input.isDown(Input.KEY_F8)) {
			System.out.println("Switched to AmmoSpawn");
			
			this.spawnTimer.cancel();
			this.itemCount = 0;
			
			spawnTimer = new Timer();
			spawnTimer.scheduleAtFixedRate(new SpawnAmmoTask(this), 0, 2000);
		}
    }
}
