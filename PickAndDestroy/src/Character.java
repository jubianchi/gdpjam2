import pulpcore.Stage;
import pulpcore.sound.Playback;
import pulpcore.sound.Sound;
import pulpcore.sprite.ImageSprite;


public class Character {
	public static final int MOVE_STEP = 4;
	
	private ImageSprite sprite;
	private int currentDirection = 0;
	
	private Sound stepSound;
	private Playback stepPlayback;
	
	public Character() {
		sprite = new ImageSprite("ecureuil/frise_face.png", 5, 5);		
		
		stepSound = Sound.load("Bruit de pas.wav");
		stepPlayback = stepSound.play();
		stepPlayback.setPaused ( true );	
	}
	
	public ImageSprite getSprite() {
		return sprite;
	}
	
	public void moveStop() 
	{
		switch(this.currentDirection)
		{
			case 0:
				sprite.setImage("ecureuil/fixe_dos.png");
				break;
			case 1:
				sprite.setImage("ecureuil/fixe_face.png");
				break;
			case 2:
				sprite.setImage("ecureuil/fixe_gauche.png");
				break;
			case 3:
				sprite.setImage("ecureuil/fixe_droite.png");
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
		
		if(currentDirection != 0) sprite.setImage("ecureuil/frise_dos.png");
		currentDirection = 0;
		
		int limit = (int)(sprite.height.get() / 2);		
		int step = ConfigManager.gameModesConfig.getValue("characterMoveStep");
		if(this.getSprite().y.getAsInt() - step > limit) {			
			this.getSprite().y.set(this.getSprite().y.getAsInt() - step);
		}		
	}
	
	public void moveBottom() {					
		this.moveStart();
		
		if(currentDirection != 1) sprite.setImage("ecureuil/frise_face.png");
		currentDirection = 1;
		
		int limit = (Stage.getHeight() - (int)(sprite.height.get() / 2));
		int step = ConfigManager.gameModesConfig.getValue("characterMoveStep");
		if(this.getSprite().y.getAsInt() + step < limit) {
			this.getSprite().y.set(this.getSprite().y.getAsInt() + step);
		}
	}
	
	public void moveLeft() {
		this.moveStart();
		
		if(currentDirection != 2) sprite.setImage("ecureuil/frise_gauche.png");
		currentDirection = 2;
		
		int limit = (int)(sprite.width.get() / 2);
		int step = ConfigManager.gameModesConfig.getValue("characterMoveStep");
		if(this.getSprite().x.getAsInt() - step > limit) {			
			this.getSprite().x.set(this.getSprite().x.getAsInt() - step);
		}
	}
	
	public void moveRight() {
		this.moveStart();
		
		if(currentDirection != 3) sprite.setImage("ecureuil/frise_droite.png");
		currentDirection = 3;
		
		int limit = (Stage.getWidth() - (int)(sprite.width.get() / 2));
		int step = ConfigManager.gameModesConfig.getValue("characterMoveStep");
		if(this.getSprite().x.getAsInt() + step < limit) {
			this.getSprite().x.set(this.getSprite().x.getAsInt() + step);
		}
	}
}
