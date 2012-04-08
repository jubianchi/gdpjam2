import pulpcore.sound.Playback;
import pulpcore.sound.Sound;

public class MusicManager
{
	private Playback	musicPlayback;

	private Playback	quietMusicPlayback;
	private Sound		quietMusic;

	private Playback	brutalMusicPlayback;
	private Sound		brutalMusic;

	public final void load()
	{
		quietMusic = Sound.load ( "Bande son calme.ogg" );
		brutalMusic = Sound.load ( "Bande son metal.ogg" );

		quietMusicPlayback = quietMusic.loop ();
		quietMusicPlayback.setPaused ( true );

		brutalMusicPlayback = brutalMusic.loop ();
		brutalMusicPlayback.setPaused ( true );
	}

	private final void stopMusic()
	{
		if (musicPlayback != null)
		{
			musicPlayback.setPaused ( true );
		}
	}

	public final void playQuietMusic()
	{
		stopMusic ();
		musicPlayback = quietMusicPlayback;
		musicPlayback.setPaused ( false );
	}

	public final void playBrutalMusic()
	{
		stopMusic ();
		musicPlayback = brutalMusicPlayback;
		musicPlayback.setPaused ( false );
	}

	public final void update(int elapsedTime)
	{
	}

	public void releaseResources()
	{
		quietMusicPlayback.stop ();
		brutalMusicPlayback.stop ();
	}
}
