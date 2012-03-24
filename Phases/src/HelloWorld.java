import com.jcraft.jorbis.JOrbisException;

import pulpcore.scene.Scene2D;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Label;
import pulpcore.animation.Fixed;
import pulpcore.image.Colors;


public class HelloWorld extends Scene2D
{
	MusicManager musicManager;

    public void load()
    {
        add(new FilledSprite(Colors.WHITE));
        add(new ImageSprite("success.png", 5, 5));
        add(new Label("Hello World!", 26, 6));
        
        musicManager = new MusicManager();
        musicManager.load ();
    }
    
    public void update(int elapsedTime)
    {
    	musicManager.update ( elapsedTime );
    }

    
}

