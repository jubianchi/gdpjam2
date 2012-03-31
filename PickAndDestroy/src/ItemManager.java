import static pulpcore.math.CoreMath.rand;

import java.util.ArrayList;
import java.util.List;

import pulpcore.animation.Easing;
import pulpcore.scene.Scene2D;
import pulpcore.sound.Sound;

public class ItemManager
{
	private ArrayList<Item>		items;
	private EntityManager		entityManager;

	private int					spawnTimer			= 0;
	private int					spawnDelay			= ConfigManager.gameModesConfig.getValue ( "spawnDelay" );

	private ArrayList<String>	guns				= new ArrayList<String> ();

	public static ItemManager shared;

	ItemManager()
	{
		shared = this;
	}
	
	public final List<Item> getItems()
	{
		return items;
	}

	public final void load(Scene2D scene, EntityManager entityManager)
	{
		this.items = new ArrayList<Item> ();
		this.entityManager = entityManager;

		guns.add ( "pistolet" ); // 0
		guns.add ( "mitraillette" ); // 1
		guns.add ( "fusilpompe" ); // 2
		guns.add ( "sniper" ); // 3
	}

	public void addItem(Item item)
	{
		if (items.size () > 20) return;

		this.items.add ( item );
		entityManager.addEntity ( item );
	}

	public void removeItem(Item item)
	{
		this.items.remove ( item );
		entityManager.removeEntity ( item );
	}

	public void cleanItems()
	{
		for (int i = 0; i < items.size (); i++)
		{
			entityManager.removeEntity ( items.get ( i ) );
		}

		items.clear ();
	}

	public final void update(int elapsedTime)
	{
		spawnTimer += elapsedTime;
		if (spawnTimer > spawnDelay)
		{
			spawnTimer -= spawnDelay;
			spawnPhaseItems ();
		}
	}

	private final void spawnPhaseItems()
	{
		if (rand ( 1, 10 ) <= 5) return;

		if (PhaseManager.shared.isInQuietPhase ())
		{
			if (rand ( 1, 10 ) <= 5) spawnItem ( new Item ( Item.HEART, "coeur.png", 40, 30 ), 1000 );
			else spawnItem ( new Item ( Item.BULLET, "bullet.png", 40, 30 ), 1000 );
		}
		else if (PhaseManager.shared.isInBrutalPhase ())
		{
			int gunType = rand(0, this.guns.size() - 1);
			String gun = this.guns.get(gunType);
			spawnItem ( new Gun ( gunType, gun + ".png", 40, 30 ), 500 );
		}
	}

	private final void spawnItem(Item item, int popupTime)
	{
		int x = rand ( 1, TilemapManager.GRID_WIDTH );
		int y = rand ( 1, TilemapManager.GRID_HEIGHT );

		item.setLocationOnTilemap ( x, y );
		int spriteWidth = item.getSprite ().width.getAsInt ();
		int spriteHeight = item.getSprite ().height.getAsInt ();
		item.getSprite ().setSize ( 0, 0 );
		item.getSprite ().scaleTo ( spriteWidth, spriteHeight, popupTime, Easing.ELASTIC_IN_OUT );

		addItem ( item );
	}

	public final void checkCollisionsWithCharacter(Character character)
	{
		int i = 0;
		while (i < getItems ().size ())
		{
			Item item = getItems ().get ( i );
			if (item.getRect ().intersects ( character.getRect () ))
			{
				switch (item.getType ())
				{
					case Item.BULLET:
						character.addBullets ();
						Sound bulletSound = Sound.load ( "ramasse_munitions.wav" );
						bulletSound.play ();
						break;
					case Item.GUN:
						character.addGun ( (Gun) item );
						Sound gunSound = Sound.load ( "ramasse_arme_speciale.wav" );
						gunSound.play ();
						break;
					case Item.HEART:
						character.addHeart ();
						Sound heartSound = Sound.load ( "ramasse_les_pv.wav" );
						heartSound.play ();
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
