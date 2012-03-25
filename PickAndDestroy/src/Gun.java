import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import pulpcore.Stage;
import pulpcore.sound.Playback;
import pulpcore.sound.Sound;



public class Gun extends Item
{
	public static final int PISTOLET = 0;
	public static final int MITRAILLETTE = 1;
	public static final int POMPE = 2;
	public static final int SNIPER = 3;
	
	private int gunType;
	
	
	public Gun ( int gunType, String name, int width, int height )  
	{
		super ( Item.GUN, name, width, height );
		
		this.gunType = gunType;
	}
	
	public int getGunType() {
		return this.gunType;
	}
	
	public String getSpriteName() {
		String name = "";
		
		switch(this.getGunType()) {
			case Gun.PISTOLET:
				name = name + "_pistolet";
				break;
			case Gun.MITRAILLETTE:
				name = name + "_mitraillette";
				break;
			case Gun.SNIPER:
				name = name + "_sniper";
				break;
			case Gun.POMPE:
				name = name + "_fusilpompe";
				break;				
		}
		
		return name;
	}
	
	public void shoot(Character shooter) {						
		switch(this.getGunType()) {
			case Gun.PISTOLET:
				shooter.removeAmmo(1);
				break;
			case Gun.MITRAILLETTE:
				shooter.removeAmmo(1);
				break;
			case Gun.SNIPER:
				shooter.removeAmmo(1);
				break;
			case Gun.POMPE:
				shooter.removeAmmo(1);
				break;				
		}
						
		final Entity projectile = new Entity("projectile_fusilpompe.png", 17, 17);					
		EntityManager.shared.addEntity ( projectile );		
	}
}
