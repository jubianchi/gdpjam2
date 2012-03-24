import java.util.ArrayList;
import java.util.Timer;
import pulpcore.Input;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.ImageSprite;


public class ItemManager {
	public static final int GRID_WIDTH_CASES = 15;
	public static final int GRID_HEIGHT_CASES = 15; 
	
	private Scene2D scene;
	private int itemCount = 0;
	private Timer spawnTimer;
	private ArrayList<ImageSprite> sprites;
	
	public final void load(Scene2D scene)
	{
		this.sprites = new ArrayList<ImageSprite>();
		
		this.scene = scene;
		this.spawnItem();
	}
	
	public void spawnItem() 
	{
		spawnTimer = new Timer();
		spawnTimer.scheduleAtFixedRate(new SpawnGoodiesTask(this), 0, 2000);
	}
	
	public void addItem(ImageSprite item) 
	{
		if(this.itemCount > 20) return;
		
		this.sprites.add(item);
		this.scene.add(item);
		this.itemCount++;
	}
	
	public void pickItem(ImageSprite item) 
	{
		this.sprites.remove(item);
		this.scene.remove(item);
	}
	
	public void cleanItem() 
	{
		for(ImageSprite item : this.sprites) 
		{
			this.scene.remove(item);
		}
		
		this.itemCount = 0;	
	}
	
	public final void update(int elapsedTime)
    {
		if(Input.isDown(Input.KEY_F7)) {
			System.out.println("Switched to LifeSpawn");			
			
			this.spawnTimer.cancel();
			this.cleanItem();
			
			spawnTimer = new Timer();
			spawnTimer.scheduleAtFixedRate(new SpawnGoodiesTask(this), 0, 2000);
		} else if(Input.isDown(Input.KEY_F8)) {
			System.out.println("Switched to AmmoSpawn");
			
			this.spawnTimer.cancel();
			this.cleanItem();
			
			spawnTimer = new Timer();
			spawnTimer.scheduleAtFixedRate(new SpawnGunTask(this), 0, 2000);
		}
    }
}
