import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import pulpcore.Stage;
import pulpcore.sound.Playback;
import pulpcore.sound.Sound;



public class Gun extends Item
{
	public static final int GUN = 0;
	public static final int MACHINEGUN = 1;
	public static final int SHOTGUN = 2;
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
			case Gun.GUN:
				name = name + "_pistolet";
				break;
			case Gun.MACHINEGUN:
				name = name + "_mitraillette";
				break;
			case Gun.SNIPER:
				name = name + "_sniper";
				break;
			case Gun.SHOTGUN:
				name = name + "_fusilpompe";
				break;				
		}
		
		return name;
	}
	
	public void shoot(Character shooter)
	{
		int vx = 0;
		int vy = 0;
		switch ( shooter.getDirection() )
		{
			case Character.UP:		vx = 0; vy = -1; break;
			case Character.DOWN:	vx = 0; vy = 1; break;
			case Character.LEFT:	vx = -1; vy = 0; break;
			case Character.RIGHT:	vx = 1; vy = 0; break;
		}
		
		switch(getGunType())
		{
			case Gun.GUN:
				{
					shooter.removeAmmo(1);
					vx *= 6; vy *= 6;
					Shoot shoot = new Shoot ( getGunType(), vx, vy );
					shooter.setShootLocation ( shoot );
					ShootManager.shared.addShoot ( shoot );
					
					Sound.load("pistolet.wav").play ();
				}
				break;
			case Gun.MACHINEGUN:
				{
					shooter.removeAmmo(2);
					vx *= 10; vy *= 10;
					Shoot shoot1 = new Shoot ( getGunType(), vx, vy );
					shooter.setShootLocation ( shoot1 );
					ShootManager.shared.addShoot ( shoot1 );
					
					Sound.load("mitraillette.wav").play ();
				}
				break;
			case Gun.SNIPER:
				{
					shooter.removeAmmo(1);
					vx *= 16; vy *= 16;
					Shoot shoot1 = new Shoot ( getGunType(), vx, vy );
					shooter.setShootLocation ( shoot1 );
					ShootManager.shared.addShoot ( shoot1 );
					
					Sound.load("sniper.wav").play ();
					break;
				}
			case Gun.SHOTGUN:
				shooter.removeAmmo(3);
				vx *= 12; vy *= 12;
				
				Sound.load("fusil_a_pompe.wav").play ();
				break;
		}
		
		
						
		final Entity projectile = new Entity("projectile_fusilpompe.png", 17, 17);					
		EntityManager.shared.addEntity ( projectile );		
	}

	public int getNextShootDelay()
	{
		switch ( getGunType () )
		{
			case GUN : return 500;
			case MACHINEGUN : return 350;
			case SHOTGUN : return 600;
			case SNIPER : return 1500;
		}
		return 0; 
	}
}
