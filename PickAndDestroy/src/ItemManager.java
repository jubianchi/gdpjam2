import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import pulpcore.Input;
import pulpcore.scene.Scene2D;
import pulpcore.sound.Sound;



public class ItemManager
{
	// public static final int GRID_WIDTH_CASES = 15;
	// public static final int GRID_HEIGHT_CASES = 15; 
	
	private int itemCount = 0;
	private Timer spawnTimer;
	private ArrayList<Item> items;
	private EntityManager entityManager;
	
	public final List<Item> getItems() { return items; }
	
	public final void load(Scene2D scene, EntityManager entityManager)
	{
		this.items = new ArrayList<Item>();
		
		this.entityManager = entityManager;
		this.spawnItem();
	}
	
	public void spawnItem() 
	{
		spawnTimer = new Timer();
		spawnTimer.scheduleAtFixedRate(new SpawnGoodiesTask(this), 0, 2000);
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
	}
	
	public void cleanItem() 
	{
		for(int i = 0, limit = this.items.size(); i < limit; i++)
		{					
			entityManager.removeEntity ( this.items.get(i) );								
		}
		
		this.items.clear();
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
						Sound bulletSound = Sound.load("ramasse_munitions.wav");
						bulletSound.play();
						break;
					case Item.GUN:
						character.addGun();
						Sound gunSound = Sound.load("ramasse_arme_speciale.wav");
						gunSound.play();
						break;
					case Item.HEART:
						character.addHeart();
						Sound heartSound = Sound.load("ramasse_les_pv.wav");
						heartSound.play();
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
