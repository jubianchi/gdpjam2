import java.util.ArrayList;

import pulpcore.Stage;
import pulpcore.scene.Scene2D;


public class CharacterManager 
{
	private ArrayList<Character> characters;
	
	public final void load(Scene2D scene)
	{
		this.characters = new ArrayList<Character>();
		
		Character p1 = new Character();
        Character p2 = new Character();
        
        this.addCharacter(scene, p1);
        this.addCharacter(scene, p2);
	}
	
	public void addCharacter(Scene2D scene, Character character) {	
		switch(this.characters.size()) {
			case 1:
				character.getSprite().setLocation(Stage.getWidth() - 47, Stage.getHeight() - 47);
				break;
			case 2:
				character.getSprite().setLocation(7, Stage.getHeight() - 47);
				break;
			case 3:
				character.getSprite().setLocation(Stage.getWidth() - 47, 7);
				break;
			default:
				character.getSprite().setLocation(7, 7);
				break;
		}
		
		this.characters.add(character);
		scene.add(character.getSprite());
	}
	
	public Character getPlayer(int index)
	{
		return this.characters.get(index);
	}
	
	public final void update( int elapsedTime )
	{
	}
}
