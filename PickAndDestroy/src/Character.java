import pulpcore.Stage;
import pulpcore.sound.Playback;
import pulpcore.sound.Sound;


public class Character extends Entity
{	
	private int currentDirection = 0;
	
	private Sound stepSound;
	private Playback stepPlayback;

	private int healthPoint;
	private int ammoPoint;
	
	public Character()
	{
		super ( "ecureuil/frise_face.png", 40, 30 );
		
		this.healthPoint = ConfigManager.gameModesConfig.getValue("startHealth");
		this.ammoPoint = ConfigManager.gameModesConfig.getValue("startAmmo");
		
		stepSound = Sound.load("Bruit de pas.wav");
		stepPlayback = stepSound.play();
		stepPlayback.setPaused ( true );	
	}

	public void setImage ( String name )
	{
		getSprite().setImage(name);
		getSprite ().setAnchor ( 0.5, 0.65 );
	}
	
	public void getAmmo(int nb) 
	{
		int maxAmmo = ConfigManager.gameModesConfig.getValue("maxAmmo");
		
		if(this.ammoPoint + nb <= maxAmmo)
		{
			this.ammoPoint += nb;
		} 
		else 
		{
			this.ammoPoint = maxAmmo;
		}
	}
	
	public int getNbAmmo() {
		return this.ammoPoint;
	}
	
	public void getHealth(int nb) 
	{
		int maxHealth = ConfigManager.gameModesConfig.getValue("maxHealth");
		
		if(this.healthPoint + nb <= maxHealth)
		{
			this.healthPoint += nb;
		} 
		else 
		{
			this.healthPoint = maxHealth;
		}
	}
	
	public int getNbHealth() {
		return this.healthPoint;
	}
	
	public void removeAmmo(int nb) {
		this.ammoPoint -= nb;
	}
	
	public void removeHealth(int nb) {
		if(this.healthPoint - nb <= 0) {
			this.healthPoint = 0;
			
			//DIE MOTHER FUCKER!!!!
			Sound dieSound = Sound.load("Rire_Mort.wav");
			dieSound.play();
		} else {
			this.healthPoint -= nb;
		}
	}
	
	public void moveStop() 
	{
		switch(this.currentDirection)
		{
			case 0:
				setImage("ecureuil/fixe_dos.png");
				break;
			case 1:
				setImage("ecureuil/fixe_face.png");
				break;
			case 2:
				setImage("ecureuil/fixe_gauche.png");
				break;
			case 3:
				setImage("ecureuil/fixe_droite.png");
				break;				
		}
		
		stepPlayback.setPaused ( true );
		stepPlayback.rewind();
	}
	
	public void moveStart() 
	{
		if(stepPlayback.isPaused()) stepPlayback.setPaused ( false );
	}
	
	public void moveTop() {					
		this.moveStart();
		
		if(currentDirection != 0) setImage("ecureuil/frise_dos.png");
		currentDirection = 0;
		
		int limit = getRect().height / 2;
		int step = ConfigManager.gameModesConfig.getValue("characterMoveStep");
		
		if ( this.getRect().y - step > limit )
		{ this.moveOf ( 0, -step ); }
	}

	public void moveBottom() {					
		this.moveStart();
		
		if(currentDirection != 1) getSprite ().setImage("ecureuil/frise_face.png");
		currentDirection = 1;
		
		int limit = (Stage.getHeight() - getRect().height / 2);
		int step = ConfigManager.gameModesConfig.getValue("characterMoveStep");
		
		if ( this.getRect().y + step < limit )
		{ this.moveOf ( 0, +step ); }
	}
	
	public void moveLeft() {
		this.moveStart();
		
		if(currentDirection != 2) setImage("ecureuil/frise_gauche.png");
		currentDirection = 2;
		
		int limit = getRect().width / 2;
		int step = ConfigManager.gameModesConfig.getValue("characterMoveStep");
		
		if ( this.getRect().x - step > limit )
		{ this.moveOf ( -step, 0 ); }
	}
	
	public void moveRight() {
		this.moveStart();
		
		if(currentDirection != 3) setImage("ecureuil/frise_droite.png");
		currentDirection = 3;
		
		int limit = (Stage.getWidth() - getRect().width / 2);
		int step = ConfigManager.gameModesConfig.getValue("characterMoveStep");
		
		if ( this.getRect().x + step < limit )
		{ this.moveOf ( +step, 0 ); }
	}
}
