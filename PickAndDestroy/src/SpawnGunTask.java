import static pulpcore.math.CoreMath.rand;

import java.util.ArrayList;
import java.util.TimerTask;

import pulpcore.animation.Easing;
import pulpcore.sprite.ImageSprite;

class SpawnGunTask extends TimerTask 
{
	private ItemManager itemManager;
	private ArrayList<String> guns = new ArrayList<String>();
	
	public SpawnGunTask(ItemManager manager) {
		this.itemManager = manager;
		
		guns.add("pistolet");
		guns.add("mitraillette");
		guns.add("fusilpompe");
	}

	public void run()
	{
		if(rand(1, 10) <= 5) return;
		
		int x = rand(1, TilemapManager.GRID_WIDTH);
		int y = rand(1, TilemapManager.GRID_HEIGHT);
		
		String gun = this.guns.get(rand(0, this.guns.size() - 1));
		
		Item item = new Item ( Item.GUN, gun + ".png", 40, 30 );
		item.getSprite().setSize(0, 0);
		item.setLocationOnTilemap ( x, y );
		item.getSprite().setAnchor(0.5, 0.5);
		item.getSprite().scaleTo(47.25, 30, 500, Easing.ELASTIC_IN_OUT);
		
		itemManager.addItem(item);
	}
}