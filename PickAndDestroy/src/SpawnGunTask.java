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
		
		Entity entity = new Entity ( "pistolet.png", 40, 30 );
		entity.getSprite().setSize(0, 0);
		entity.setLocationOnTilemap ( x, y );
		entity.getSprite().setAnchor(0.5, 0.5);
		entity.getSprite().scaleTo(47.25, 30, 500, Easing.ELASTIC_IN_OUT);
		
		itemManager.addItem(entity);
	}
}