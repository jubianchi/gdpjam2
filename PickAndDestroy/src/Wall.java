
public class Wall extends Entity
{
	private int type;
	public final int getType() { return type; }

	public static final int WALL = 0;
	public static final int TABLE = 1;
	
	public Wall ( int type, String name, int width, int height )
	{
		super ( name, width, height );
		this.type = type;
	}
}
