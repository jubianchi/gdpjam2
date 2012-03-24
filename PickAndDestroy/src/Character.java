import pulpcore.Stage;
import pulpcore.sprite.ImageSprite;


public class Character {
	public static final int MOVE_STEP = 4;
	
	private ImageSprite sprite;
	
	public Character() {
		sprite = new ImageSprite("ecureuil/face/ecu01.png", 5, 5);
		sprite.setSize(68, 43);
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
		
		if(this.getSprite().y.getAsInt() + MOVE_STEP < Stage.getWidth()) {
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
		
		if(this.getSprite().x.getAsInt() + MOVE_STEP < Stage.getWidth()) {
			this.getSprite().x.set(this.getSprite().x.getAsInt() + MOVE_STEP);
		}
	}
}
