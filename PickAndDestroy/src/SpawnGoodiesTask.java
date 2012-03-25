import static pulpcore.math.CoreMath.rand;

import java.util.TimerTask;

import pulpcore.animation.Easing;
import pulpcore.sprite.ImageSprite;

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
		
		Entity entity;
		if(rand(1, 10) <= 5)
		{
			entity = new Entity("coeur.png", w, h);
		}
		else
		{
			entity = new Entity("bullet.png", w, h);
		}
		
		entity.getSprite().setSize(0, 0);
		entity.setLocationOnTilemap ( x, y );
		entity.getSprite().setAnchor(0.5, 0.5);
		entity.getSprite().scaleTo(w, h, 500, Easing.ELASTIC_IN_OUT);
		
		itemManager.addItem(entity);
	}
}