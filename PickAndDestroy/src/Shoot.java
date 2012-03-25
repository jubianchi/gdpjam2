

public class Shoot extends Entity
{
	private int type;
	private int vx;
	private int vy;
	
	public final int getType() { return type; }
	
	Shoot( int type, int vx, int vy )
	{
		super ( "projectile.png", 10, 10 );
		this.vx = vx;
		this.vy = vy;
	}

	public void update(int elapsedTime)
	{
		moveOf ( vx, vy );
	}
}
