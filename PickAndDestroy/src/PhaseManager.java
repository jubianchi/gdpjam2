public class PhaseManager
{
	private MusicManager	musicManager;

	private boolean			playingQuietPhase	= true;

	private int				phaseElapsedTime;
	private int				phaseSwitchTime		= ConfigManager.gameModesConfig.getValue ( "minQuietPlaytime" );
	private int				phaseSpanTime		= ConfigManager.gameModesConfig.getValue ( "quietSpanPlaytime" );

	public boolean isInQuietPhase()
	{
		return playingQuietPhase;
	}

	public boolean isInBrutalPhase()
	{
		return ! playingQuietPhase;
	}

	public static PhaseManager	shared;

	public PhaseManager(MusicManager musicManager)
	{
		shared = this;
		this.musicManager = musicManager;
	}

	public final void load()
	{
		this.musicManager.playQuietMusic ();
	}

	public final void update(int elapsedTime)
	{
		phaseElapsedTime += elapsedTime;

		if (phaseElapsedTime >= phaseSwitchTime)
		{
			ItemManager.shared.cleanItems ();

			if (playingQuietPhase)
			{
				musicManager.playBrutalMusic ();

				phaseSwitchTime = ConfigManager.gameModesConfig.getValue ( "minBrutalPlaytime" );
				phaseSpanTime = ConfigManager.gameModesConfig.getValue ( "brutalSpanPlaytime" );
			}
			else
			{
				musicManager.playQuietMusic ();

				phaseSwitchTime = ConfigManager.gameModesConfig.getValue ( "minQuietPlaytime" );
				phaseSpanTime = ConfigManager.gameModesConfig.getValue ( "quietSpanPlaytime" );
			}

			playingQuietPhase = !playingQuietPhase;
			phaseElapsedTime = 0;
		}
	}
}
