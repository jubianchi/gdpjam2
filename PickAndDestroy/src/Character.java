import pulpcore.Stage;
import pulpcore.sprite.ImageSprite;


public class Character {
	public static final int MOVE_STEP = 4;
	public static final int SPRITE_WIDTH = 68;
	public static final int SPRITE_HEIGHT = 43;
	
	private ImageSprite sprite;
	
	public Character() {
		sprite = new ImageSprite("ecureuil/face/ecu01.png", 5, 5);
		sprite.setSize(SPRITE_WIDTH, SPRITE_HEIGHT);
	}
	
	public ImageSprite getSprite() {
		return sprite;
	}
	
	public void moveTop() {					
		sprite.setImage("ecureuil/dos/ecu01.png");
		
		if(this.getSprite().y.getAsInt() - MOVE_STEP > 0) {			
			this.getSprite().y.set(this.getSprite().y.getAsInt() - MOVE_STEP);
		}		
	}
	
	public void moveBottom() {					
		sprite.setImage("ecureuil/face/ecu01.png");
		
		int limit = (Stage.getHeight() - SPRITE_HEIGHT);
		
		if(this.getSprite().y.getAsInt() + MOVE_STEP < limit) {
			this.getSprite().y.set(this.getSprite().y.getAsInt() + MOVE_STEP);
		}
	}
	
	public void moveLeft() {
		sprite.setImage("ecureuil/gauche/ecu01.png");
		
		if(this.getSprite().x.getAsInt() - MOVE_STEP > 0) {			
			this.getSprite().x.set(this.getSprite().x.getAsInt() - MOVE_STEP);
		}
	}
	
	public void moveRight() {
		sprite.setImage("ecureuil/droite/ecu01.png");
		
		int limit = (Stage.getWidth() - SPRITE_WIDTH);
		
		if(this.getSprite().x.getAsInt() + MOVE_STEP < limit) {
			this.getSprite().x.set(this.getSprite().x.getAsInt() + MOVE_STEP);
		}
	}
}
