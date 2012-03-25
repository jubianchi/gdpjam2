import static pulpcore.math.CoreMath.rand;

import java.util.TimerTask;

import pulpcore.animation.Easing;
import pulpcore.sprite.ImageSprite;

class SpawnGunTask extends TimerTask 
{
	private ItemManager itemManager;
	
	public SpawnGunTask(ItemManager manager) {
		this.itemManager = manager;
	}

	public void run()
	{
		if(rand(1, 100) <= 50) return;
		
		int x = rand(1, TilemapManager.GRID_WIDTH);
		int y = rand(1, TilemapManager.GRID_HEIGHT);
		
		Item item = new Item ( Item.GUN, "pistolet.png", 40, 30 );
		item.getSprite().setSize(0, 0);
		item.setLocationOnTilemap ( x, y );
		item.getSprite().setAnchor(0.5, 0.5);
		item.getSprite().scaleTo(47.25, 30, 500, Easing.ELASTIC_IN_OUT);
		
		itemManager.addItem(item);
	}
}