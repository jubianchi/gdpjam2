import pulpcore.sprite.ImageSprite;
import pulpcore.animation.Timeline;

public class Coeur {
	private ImageSprite sprite;
	private Timeline timeline;
	
	public Coeur() {
		sprite = new ImageSprite("coeur.png", 80, 74);
		timeline = new Timeline();
	}
	
	public ImageSprite getSprite() {
		return sprite;
	}
	
	public Timeline getTimeline() {
		return timeline;
	}
	
	public void startAnimation() {
		timeline.at(0).animate(sprite.width, 80, 82, 100);
		timeline.at(0).animate(sprite.height, 74, 76, 100);
		
		timeline.at(100).animate(sprite.width, 82, 80, 150);
		timeline.at(100).animate(sprite.height, 76, 74, 150);
		
		timeline.at(150).animate(sprite.width, 82, 80, 250);
		timeline.at(150).animate(sprite.height, 76, 74, 250);
		
		timeline.loopForever();
	}
	
	public void stopAnimation() {
		timeline.stop();
	}
}
