import java.util.ArrayList;
import pulpcore.Stage;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.ImageSprite;


public class Map {
	private ImageSprite sprite;
	private Scene2D scene;
	private ArrayList<Character> characters;
	
	public Map(Scene2D scene) {
		sprite = new ImageSprite("map_beta.png", 0, 0);
		sprite.setSize(Stage.getWidth(), Stage.getHeight());
		
		this.characters = new ArrayList<Character>();
		
		this.scene = scene;
		
		this.scene.add(this.getSprite());
	}
	
	public ImageSprite getSprite() {
		return sprite;
	}
	
	public void addCharacter(Character character) {	
		switch(this.characters.size()) {
			case 1:
				character.getSprite().setLocation(Stage.getWidth() - 45, Stage.getHeight() - 45);
				break;
			case 2:
				character.getSprite().setLocation(5, Stage.getHeight() - 45);
				break;
			case 3:
				character.getSprite().setLocation(Stage.getWidth() - 45, 5);
				break;
			default:
				character.getSprite().setLocation(5, 5);
				break;
		}
		
		
		
		this.characters.add(character);
		this.scene.add(character.getSprite());
	}
}
