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
	private static int MIN_QUIET_PLAYTIME = 5000;
	private static int MAX_QUIET_PLAYTIME = 10000;
	private static int QUIET_SPAN_PLAYTIME = 1000;
	
	private Playback brutalMusicPlayback;
	private Sound brutalMusic;
	private static int MIN_BRUTAL_PLAYTIME = 5000;
	private static int MAX_BRUTAL_PLAYTIME = 10000;
	private static int BRUTAL_SPAN_PLAYTIME = 1000;
	
	public final void load()
	{
		quietMusic = Sound.load("Happy Tree Friends Soundtrack - Intro.ogg");
    	brutalMusic = Sound.load("Metal.ogg");
        
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
    		System.out.println("Switching music at " + musicElapsedTime );
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
    		System.out.println("Stopping music...");

    		musicPlayback.setPaused ( true );
    		musicElapsedTime = 0;
    	}
    }
    
	private final void playQuietMusic()
	{
		stopMusic();
		
		System.out.println("Playing quiet music...");
		
		musicPlayback = quietMusicPlayback;
		musicSwitchTime = MIN_QUIET_PLAYTIME;
		musicSpanTime = QUIET_SPAN_PLAYTIME;
		musicPlayback.setPaused ( false );
	}
	
	private final void playBrutalMusic()
	{
		stopMusic();
		
		System.out.println("Playing brutal music...");
		
		musicPlayback = brutalMusicPlayback;
		musicSwitchTime = MIN_BRUTAL_PLAYTIME;
		musicSpanTime = BRUTAL_SPAN_PLAYTIME;
		musicPlayback.setPaused ( false );
	}
}