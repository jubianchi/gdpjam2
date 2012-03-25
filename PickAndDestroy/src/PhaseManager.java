import pulpcore.sound.Playback;
import pulpcore.sound.Sound;


public class PhaseManager {
	private MusicManager musicManager;
	private ItemManager itemManager;
	
	private boolean playingQuietPhase = true;
	
	private int phaseElapsedTime;
	private int phaseSwitchTime = ConfigManager.gameModesConfig.getValue("minQuietPlaytime");;
	private int phaseSpanTime = ConfigManager.gameModesConfig.getValue("quietSpanPlaytime");;
	
	public PhaseManager(MusicManager musicManager, ItemManager itemManager) {
		this.musicManager = musicManager;
		this.itemManager = itemManager;
	}
	
	public final void load()
	{
		this.musicManager.playQuietMusic ();
	}	
	
	public final void update(int elapsedTime)
    {
		phaseElapsedTime += elapsedTime;
	   	
    	if ( phaseElapsedTime >= phaseSwitchTime )
    	{
	    	if ( playingQuietPhase ) {
	    		musicManager.playBrutalMusic();	    		    	
	    		
	    		phaseSwitchTime = ConfigManager.gameModesConfig.getValue("minBrutalPlaytime");
	    		phaseSpanTime = ConfigManager.gameModesConfig.getValue("brutalSpanPlaytime");;
	    		
	    		this.itemManager.spawnBrutalItem();
	    	} else {
	    		musicManager.playQuietMusic();	    			    	
	    		
	    		phaseSwitchTime = ConfigManager.gameModesConfig.getValue("minQuietPlaytime");
	    		phaseSpanTime = ConfigManager.gameModesConfig.getValue("quietSpanPlaytime");
	    		
	    		this.itemManager.spawnQuietItem();
	    	}
	    	
	    	playingQuietPhase = ! playingQuietPhase;
	    	phaseElapsedTime = 0;
    	}
    }
}
