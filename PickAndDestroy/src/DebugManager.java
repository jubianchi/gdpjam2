import pulpcore.Input;
import pulpcore.image.BlendMode;
import pulpcore.image.Colors;
import pulpcore.image.CoreGraphics;
import pulpcore.image.CoreImage;
import pulpcore.math.Rect;
import pulpcore.sprite.Group;
import pulpcore.sprite.ImageSprite;
import pulpcore.scene.Scene2D;

public class DebugManager
{
	boolean activated;
	Group debugGroup;
	CoreImage debugImage;
	ImageSprite debugImageSprite;
	EntityManager entityManager;

	public final void load ( Scene2D scene, EntityManager entityManager )
	{
		this.entityManager = entityManager;
		
		debugImage = new CoreImage ( 800, 600 );
		clear();
		
		CoreGraphics graphics = debugImage.createGraphics ();
		graphics.setColor ( 0xFFFF0000 );
		graphics.drawLine ( 0,0, 800, 600 );
		
		debugImageSprite = new ImageSprite(debugImage,0,0);
		debugGroup = new Group();
		debugGroup.setBlendMode ( BlendMode.Add() );
		// debugGroup.add ( debugImageSprite );
		scene.add ( debugGroup );
	}
	
	public final void clear()
	{
		CoreGraphics graphics = debugImage.createGraphics ();
		graphics.setColor ( 0xFF000000 );
		graphics.fillRect ( 0,0,800,600 );
	}
	
	public final void drawRect ( Rect rect, int color )
	{
		CoreGraphics graphics = debugImage.createGraphics ();
		graphics.setColor ( color );
		graphics.fillRect ( rect.x, rect.y, rect.width, rect.height );
	}
	
	public final void update ( int elapsedTime )
	{
		// drawRect ( new Rect(50,50,100,100), Colors.RED );
		
		if ( Input.isPressed ( Input.KEY_F12 ) )
		{
			activated = ! activated;
			
			if ( ! activated )
			{
				debugGroup.remove ( debugImageSprite );
			}
			else
			{
				debugGroup.add ( debugImageSprite );
			}
		}

		if ( activated )
		{
			clear();
			for ( int i = 0; i < entityManager.getEntities ().size (); i++ )
			{
				Entity entity = entityManager.getEntities ().get ( i );
				drawRect ( entity.getRect (), Colors.GREEN );
			}
			debugImageSprite.setDirty ( true );
		}
		
	}
}
