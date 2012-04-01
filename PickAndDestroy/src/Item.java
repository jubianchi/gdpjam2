public class Item extends Entity
{
	public static final int	HEART	= 0;
	public static final int	BULLET	= 1;
	public static final int	GUN		= 2;

	private int				type;

	public final int getType()
	{
		return type;
	}

	public Item(int type, String name, int width, int height)
	{
		super ( name, width, height );
		this.type = type;
	}
}
