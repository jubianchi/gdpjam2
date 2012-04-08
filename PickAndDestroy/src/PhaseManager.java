import pulpcore.Stage;
import pulpcore.animation.Easing;
import pulpcore.animation.Timeline;
import pulpcore.image.CoreFont;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.Label;
import pulpcore.sprite.Sprite;

public class PhaseManager
{
	private MusicManager	musicManager;

	private boolean			isInQuietPhase	= true;

	private int				phaseElapsedTime;
	private int				phaseSwitchTime	= ConfigManager.gameModesConfig.getValue ( "minQuietPlaytime" );
//	private int				phaseSpanTime		= ConfigManager.gameModesConfig.getValue ( "quietSpanPlaytime" );

	private Label 			collectLabel;
	private Label 			fightLabel;
	
	private Timeline		collectTimeline;
	private Timeline		fightTimeline;

	private Sprite			currentLabel;
	private Timeline		currentTimeline;

	private Scene2D			scene;

	public boolean isInQuietPhase()
	{
		return isInQuietPhase;
	}

	public boolean isInBrutalPhase()
	{
		return !isInQuietPhase;
	}

	public static PhaseManager	shared;

	public PhaseManager(MusicManager musicManager)
	{
		shared = this;
		this.musicManager = musicManager;
	}

	public final void load(Scene2D scene)
	{
		this.musicManager.playQuietMusic ();
		this.scene = scene;

		CoreFont messageFont = CoreFont.load ( "game.font.png" );
		
		int centerX = Stage.getWidth () / 2;
		int centerY = Stage.getHeight () / 2;

		collectLabel = new Label ( messageFont, "Keep cool...", centerX, centerY, -1, 70 );
		collectLabel.setAnchor ( 0.5, 0.5 );
		collectLabel.alpha.set ( 0 );
		collectTimeline = new Timeline ();
		collectTimeline.at ( 0 ).animate ( collectLabel.alpha, 0, 255, 1500 );
		collectTimeline.at ( 0 ).animate ( collectLabel.x, centerX-100, centerX+100, 3000, Easing.REGULAR_OUT );
		collectTimeline.at ( 1500 ).animate ( collectLabel.alpha, 255, 0, 1500 );

		fightLabel = new Label ( messageFont, "Fight !", centerX, centerY, -1, 10 );
		fightLabel.setAnchor ( 0.5, 0.5 );
		fightLabel.alpha.set ( 0 );
		fightTimeline = new Timeline ();
		fightTimeline.at ( 0 ).animate ( fightLabel.alpha, 0, 255, 200 );
		fightTimeline.at ( 0 ).animate ( fightLabel.height, 10, 100, 500, Easing.ELASTIC_OUT );
		fightTimeline.at ( 1500 ).animate ( fightLabel.alpha, 255, 0, 500 );
	}

	public final void update(int elapsedTime)
	{
		phaseElapsedTime += elapsedTime;

		if (phaseElapsedTime >= phaseSwitchTime)
		{
			ItemManager.shared.cleanItems ();

			if (currentLabel != null) scene.remove ( currentLabel );
			if (currentTimeline != null)
			{
				currentTimeline.stop ();
				scene.removeTimeline ( currentTimeline, false );
			}

			if (isInQuietPhase)
			{
				musicManager.playBrutalMusic ();

				phaseSwitchTime = ConfigManager.gameModesConfig.getValue ( "minBrutalPlaytime" );
//				phaseSpanTime = ConfigManager.gameModesConfig.getValue ( "brutalSpanPlaytime" );
				
				currentLabel = fightLabel;
				currentTimeline = fightTimeline;
			}
			else
			{
				musicManager.playQuietMusic ();

				phaseSwitchTime = ConfigManager.gameModesConfig.getValue ( "minQuietPlaytime" );
//				phaseSpanTime = ConfigManager.gameModesConfig.getValue ( "quietSpanPlaytime" );
				
				currentLabel = collectLabel;
				currentTimeline = collectTimeline;				
			}
			
			scene.add ( currentLabel );
			scene.addTimeline ( currentTimeline );
			currentTimeline.play ();

			isInQuietPhase = !isInQuietPhase;
			phaseElapsedTime = 0;
		}
	}
}
