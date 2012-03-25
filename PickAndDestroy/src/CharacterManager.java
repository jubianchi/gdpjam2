import java.util.ArrayList;

import pulpcore.Stage;
import pulpcore.scene.Scene2D;


public class CharacterManager 
{
	private ArrayList<Character> characters;
	private EntityManager entityManager;
	
	public final void load(Scene2D scene, EntityManager entityManager)
	{
		this.entityManager = entityManager;
		this.characters = new ArrayList<Character>();
		
		Character p1 = new Character();
        Character p2 = new Character();
        
        this.addCharacter(scene, p1);
        this.addCharacter(scene, p2);
	}
	
	public void addCharacter(Scene2D scene, Character character) {	
		switch(this.characters.size()) {			
			case 1:
				character.setLocationOnTilemap ( 0,0 );
				break;	
			/*case 2:
				character.setLocationOnTilemap ( 0, TilemapManager.GRID_HEIGHT-1 );
				break;
			case 3:
				character.setLocationOnTilemap ( TilemapManager.GRID_WIDTH-1, 0 );
				break;*/			
			default:
				character.setLocationOnTilemap ( TilemapManager.GRID_WIDTH-1, TilemapManager.GRID_HEIGHT-1 );
				break;
		}
		
		this.characters.add(character);
		entityManager.addEntity ( character );
	}
	
	public Character getPlayer(int index)
	{
		return this.characters.get(index);
	}
	
	public final void update( int elapsedTime )
	{
	}
}
