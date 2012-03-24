import static pulpcore.math.CoreMath.rand;

import java.util.TimerTask;

import pulpcore.animation.Easing;
import pulpcore.sprite.ImageSprite;

class SpawnGunTask extends TimerTask 
{
	private ItemManager manager;
	
	public SpawnGunTask(ItemManager manager) {
		this.manager = manager;
	}

	public void run()
	{
		if(rand(1, 100) <= 50) return;
		
		int x = rand(1, TilemapManager.GRID_WIDTH);
		int y = rand(1, TilemapManager.GRID_HEIGHT);
		
		Entity sprite = new Entity ( "pistolet.png", 40, 30 );
		// ImageSprite sprite = new ImageSprite("pistolet.png", 5, 5);
		sprite.getSprite().setSize(0, 0);
		// sprite.setCenterLocation ( (int) ( (x * 53.3) - 50.3 ), ( (y * 40) - 35 ) );
		sprite.setLocationOnTilemap ( x, y );
		sprite.getSprite().setAnchor(0.5, 0.5);
		sprite.getSprite().scaleTo(47.25, 30, 500, Easing.ELASTIC_IN_OUT);
		
		manager.addItem(sprite);
	}
}