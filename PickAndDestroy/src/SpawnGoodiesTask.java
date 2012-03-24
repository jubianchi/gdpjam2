import static pulpcore.math.CoreMath.rand;

import java.util.TimerTask;

import pulpcore.animation.Easing;
import pulpcore.sprite.ImageSprite;

class SpawnGoodiesTask extends TimerTask 
{
	private ItemManager manager;
	
	public SpawnGoodiesTask(ItemManager manager) {
		this.manager = manager;
	}
	
	public void run()
	{
		if(rand(1, 100) <= 50) return;
		
		int x = rand(1, TilemapManager.GRID_WIDTH);
		int y = rand(1, TilemapManager.GRID_HEIGHT);

		int w = 40;
		int h = 30;
		
		Entity sprite;
		// ImageSprite sprite;
		if(rand(1, 10) <= 5)
		{
			// sprite = new ImageSprite("coeur.png", 5, 5);
			sprite = new Entity("coeur.png", w, h);
		}
		else
		{
			// sprite = new ImageSprite("bullet.png", 5, 5);
			sprite = new Entity("bullet.png", w, h);
		}		
		
		sprite.getSprite().setSize(0, 0);
		// sprite.setCenterLocation ( (int) ( (x * 53.3) - (26.65 + (w / 2)) ), (y * 40) - (20 + (h / 2)) );
		sprite.setLocationOnTilemap ( x, y );
		sprite.getSprite().setAnchor(0.5, 0.5);
		sprite.getSprite().scaleTo(w, h, 500, Easing.ELASTIC_IN_OUT);
		
		manager.addItem(sprite);
	}
}