import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pulpcore.Input;
import pulpcore.scene.Scene2D;



public class ItemManager
{
	// public static final int GRID_WIDTH_CASES = 15;
	// public static final int GRID_HEIGHT_CASES = 15; 
	
	private Scene2D scene;
	private int itemCount = 0;
	private Timer spawnTimer;
	private ArrayList<Item> items;
	private EntityManager entityManager;
	
	private int spawnDelay = ConfigManager.gameModesConfig.getValue("spawnDelay");
	
	public final List<Item> getItems() { return items; }
	
	public final void load(Scene2D scene, EntityManager entityManager)
	{
		this.items = new ArrayList<Item>();
		
		this.entityManager = entityManager;
		this.scene = scene;
		this.spawnItem();
	}
	
	public void spawnItem() 
	{
		spawnTimer = new Timer();
		spawnTimer.scheduleAtFixedRate(new SpawnGoodiesTask(this), 0, spawnDelay);
	}
	
	public void addItem(Item item) 
	{
		if(this.itemCount > 20) return;
		
		this.items.add(item);
		entityManager.addEntity ( item );
		this.itemCount++;
	}
	
	public void removeItem(Item item)
	{
		this.items.remove(item);		
		entityManager.removeEntity ( item );
		this.itemCount--;
	}
	
	public void cleanItem() { this.cleanItem(0); }
	public void cleanItem(int delay) 
	{
		final Timer cleanTimer = new Timer();
		final ArrayList<Item> items = this.items;
		
		cleanTimer.schedule(new TimerTask() {									
			@Override
			public void run() {
				cleanTimer.cancel();
				
				for(int i = 0, limit = items.size(); i < limit; i++)
				{					
					entityManager.removeEntity ( items.get(i) );								
				}
				
				items.clear();
				itemCount = 0;	
			}
			
		}, spawnDelay);
		
		/*
		for(int i = 0, limit = this.items.size(); i < limit; i++)
		{					
			entityManager.removeEntity ( this.items.get(i) );								
		}
		
		this.items.clear();
		this.itemCount = 0;	
		*/
	}
	
	public void spawnQuietItem() {
		this.spawnTimer.cancel();
		this.cleanItem();
		
		spawnTimer = new Timer();
		spawnTimer.scheduleAtFixedRate(new SpawnGoodiesTask(this), 0, spawnDelay);
	}
	
	public void spawnBrutalItem() {
		this.spawnTimer.cancel();
		this.cleanItem();
		
		spawnTimer = new Timer();
		spawnTimer.scheduleAtFixedRate(new SpawnGunTask(this), 0, spawnDelay);
	}
	
	public final void update(int elapsedTime)
    {

    }
	
	public final void checkCollisionsWithCharacter ( Character character )
	{
		int i = 0;
		while ( i < getItems().size () )
		{
			Item item = getItems ().get ( i );
			if ( item.getRect ().intersects ( character.getRect () ) )
			{
				switch ( item.getType () )
				{
					case Item.BULLET:
						character.addBullets();
						break;
					case Item.GUN:
						character.addGun();
						break;
					case Item.HEART:
						character.addHeart();
						break;
				}
				removeItem ( item );
			}
			else
			{
				i++;
			}
		}
	}
}
