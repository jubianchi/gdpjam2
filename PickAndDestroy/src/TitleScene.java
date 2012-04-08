import pulpcore.scene.Scene2D;
import pulpcore.sprite.ImageSprite;
import pulpcore.Input;
import pulpcore.Stage;

public class TitleScene extends Scene2D
{
    @Override
    public void load()
    {
    	ImageSprite background = new ImageSprite ( "title_screen.jpg", 0, 0 );
		add ( background );
    }

    public void update ( int elapsedTime )
    {
    	if ( Input.isPressed ( Input.KEY_SPACE ) )
    	{
    		Stage.replaceScene ( new GameScene() );
    	}
    }

}
