import pulpcore.sound.Playback;
import pulpcore.sound.Sound;


public class MusicManager
{
	private boolean playingQuietMusic = true;
	private Playback musicPlayback;
	
	private int musicElapsedTime;
	private int musicSwitchTime = 5000;
	private int musicSpanTime = 1000;
	
	private Playback quietMusicPlayback;
	private Sound quietMusic;
	
	private Playback brutalMusicPlayback;
	private Sound brutalMusic;
	
	public final void load()
	{
		quietMusic = Sound.load("Bande son calme.ogg");
    	brutalMusic = Sound.load("Bande son metal.ogg");
        
        quietMusicPlayback = quietMusic.play ();
        quietMusicPlayback.setPaused ( true );
        
        brutalMusicPlayback = brutalMusic.play ();
        brutalMusicPlayback.setPaused ( true );
        
        playQuietMusic ();
	}
	
	public final void update(int elapsedTime)
    {
		musicElapsedTime += elapsedTime;
	   	
    	// System.out.println("Music time : " + musicElapsedTime);
    	if ( musicElapsedTime >= musicSwitchTime )
    	{
    		//System.out.println("Switching music at " + musicElapsedTime );
	    	if ( playingQuietMusic )
	    		playBrutalMusic();
	    	else
	    		playQuietMusic();
	    	
	    	playingQuietMusic = ! playingQuietMusic;
    	}
    }
	
	private final void stopMusic()
    {
    	if ( musicPlayback != null )
    	{
    		//System.out.println("Stopping music...");

    		musicPlayback.setPaused ( true );
    		musicElapsedTime = 0;
    	}
    }
    
	private final void playQuietMusic()
	{
		stopMusic();
		
		//System.out.println("Playing quiet music...");
		
		musicPlayback = quietMusicPlayback;
		musicSwitchTime = ConfigManager.gameModesConfig.getValue("minQuietPlaytime");
		musicSpanTime = ConfigManager.gameModesConfig.getValue("quietSpanPlaytime");
		musicPlayback.setPaused ( false );
	}
	
	private final void playBrutalMusic()
	{
		stopMusic();
		
		//System.out.println("Playing brutal music...");
		
		musicPlayback = brutalMusicPlayback;
		musicSwitchTime = ConfigManager.gameModesConfig.getValue("minBrutalPlaytime");
		musicSpanTime = ConfigManager.gameModesConfig.getValue("brutalSpanPlaytime");;
		musicPlayback.setPaused ( false );
	}
}