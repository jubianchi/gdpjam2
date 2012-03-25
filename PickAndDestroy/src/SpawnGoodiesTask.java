import static pulpcore.math.CoreMath.rand;

import java.util.TimerTask;

import pulpcore.animation.Easing;

class SpawnGoodiesTask extends TimerTask 
{
	private ItemManager itemManager;
	
	public SpawnGoodiesTask(ItemManager manager) {
		this.itemManager = manager;
	}
	
	public void run()
	{
		if(rand(1, 100) <= 50) return;
		
		int x = rand(1, TilemapManager.GRID_WIDTH);
		int y = rand(1, TilemapManager.GRID_HEIGHT);

		int w = 40;
		int h = 30;
		
		Item item;
		if(rand(1, 10) <= 5)
		{
			item = new Item(Item.HEART, "coeur.png", w, h);
		}
		else
		{
			item = new Item(Item.BULLET, "bullet.png", w, h);
		}
		
		item.getSprite().setSize(0, 0);
		item.setLocationOnTilemap ( x, y );
		item.getSprite().setAnchor(0.5, 0.5);
		item.getSprite().scaleTo(w, h, 1000, Easing.ELASTIC_IN_OUT);

		itemManager.addItem(item);
	}
}