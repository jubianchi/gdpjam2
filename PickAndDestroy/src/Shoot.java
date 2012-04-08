
public class Shoot extends Entity
{
	private int	type;
	private int	vx;
	private int	vy;

	public final int getType()
	{
		return type;
	}

	Shoot(int type, int vx, int vy)
	{
		super ( "projectile.png", 10, 10 );
		this.type = type;
		this.vx = vx;
		this.vy = vy;
	}

	public void update(int elapsedTime)
	{
		moveOf ( vx, vy );
	}

	public int getHitPoints()
	{
		switch ( type )
		{
			case Gun.MACHINEGUN: return ConfigManager.gameModesConfig.getValue ( "machineGunHitPoints" );
			case Gun.SHOTGUN: return ConfigManager.gameModesConfig.getValue ( "shotGunHitPoints" );
			case Gun.SNIPER: return ConfigManager.gameModesConfig.getValue ( "sniperHitPoints" );
			case Gun.GUN: return ConfigManager.gameModesConfig.getValue ( "gunHitPoints" );
			default: return 1;
		}
	}
}
