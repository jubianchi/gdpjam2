import static pulpcore.math.CoreMath.rand;

import java.util.TimerTask;

import pulpcore.animation.Easing;
import pulpcore.sprite.ImageSprite;

class SpawnAmmoTask extends TimerTask 
{
	private ItemManager manager;
	
	public SpawnAmmoTask(ItemManager manager) {
		this.manager = manager;
	}

	public void run()
	{
		if(rand(1, 100) <= 50) return;
		
		int x = rand(1, ItemManager.GRID_WIDTH_CASES);
		int y = rand(1, ItemManager.GRID_HEIGHT_CASES);		
		
		ImageSprite sprite = new ImageSprite("bullet.png", 5, 5);
		sprite.setSize(0, 0);
		sprite.setLocation((x * 53.3) - 32.65, (y * 40) - 35);
		sprite.setAnchor(0.5, 0.5);
		manager.addItem(sprite);
		
		sprite.scaleTo(13, 30, 500, Easing.ELASTIC_IN_OUT);
	}
}