import pulpcore.Stage;
import pulpcore.sound.Playback;
import pulpcore.sound.Sound;
import pulpcore.sprite.ImageSprite;


public class Character extends Entity
{
	public static final int MOVE_STEP = 4;
	
	// private ImageSprite sprite;
	private int currentDirection = 0;
	
	private Sound stepSound;
	private Playback stepPlayback;
	
	public Character()
	{
		super ( "ecureuil/frise_face.png", 40, 30 );
		stepSound = Sound.load("Bruit de pas.wav");
		stepPlayback = stepSound.play();
		stepPlayback.setPaused ( true );	
	}
	
/*	public ImageSprite getSprite() {
		return sprite;
	}*/
	
	public void moveStop() 
	{
		switch(this.currentDirection)
		{
			case 0:
				getSprite().setImage("ecureuil/fixe_dos.png");
				break;
			case 1:
				getSprite().setImage("ecureuil/fixe_face.png");
				break;
			case 2:
				getSprite().setImage("ecureuil/fixe_gauche.png");
				break;
			case 3:
				getSprite().setImage("ecureuil/fixe_droite.png");
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
		
		if(currentDirection != 0) getSprite().setImage("ecureuil/frise_dos.png");
		currentDirection = 0;
		
		int limit = getRect().height / 2;
		int step = ConfigManager.gameModesConfig.getValue("characterMoveStep");
		if(this.getSprite().y.getAsInt() - step > limit) {			
			this.getSprite().y.set(this.getSprite().y.getAsInt() - MOVE_STEP);
		}		
	}
	
	public void moveBottom() {					
		this.moveStart();
		
		if(currentDirection != 1) getSprite ().setImage("ecureuil/frise_face.png");
		currentDirection = 1;
		
		int limit = (Stage.getHeight() - getRect().height / 2);
		int step = ConfigManager.gameModesConfig.getValue("characterMoveStep");
		if(this.getSprite().y.getAsInt() + step < limit) {
			this.getSprite().y.set(this.getSprite().y.getAsInt() + MOVE_STEP);
		}
	}
	
	public void moveLeft() {
		this.moveStart();
		
		if(currentDirection != 2) getSprite().setImage("ecureuil/frise_gauche.png");
		currentDirection = 2;
		
		int limit = getRect().width / 2;
		int step = ConfigManager.gameModesConfig.getValue("characterMoveStep");
		if(this.getSprite().x.getAsInt() - step > limit) {			
			this.getSprite().x.set(this.getSprite().x.getAsInt() - MOVE_STEP);
		}
	}
	
	public void moveRight() {
		this.moveStart();
		
		if(currentDirection != 3) getSprite().setImage("ecureuil/frise_droite.png");
		currentDirection = 3;
		
		int limit = (Stage.getWidth() - getRect().width / 2);
		int step = ConfigManager.gameModesConfig.getValue("characterMoveStep");
		if(this.getSprite().x.getAsInt() + step < limit) {
			this.getSprite().x.set(this.getSprite().x.getAsInt() + MOVE_STEP);
		}
	}
}
