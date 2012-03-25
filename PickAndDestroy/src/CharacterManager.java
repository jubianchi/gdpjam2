import java.util.ArrayList;

import pulpcore.Stage;
import pulpcore.scene.Scene2D;


public class CharacterManager 
{
	private ArrayList<Character> characters;
	private EntityManager entityManager;
	private ItemManager itemManager;
	
	public final void load(Scene2D scene, EntityManager entityManager, ItemManager itemManager )
	{
		this.entityManager = entityManager;
		this.itemManager = itemManager;
		this.characters = new ArrayList<Character>();
		
		Character p1 = new Character();
        Character p2 = new Character();
        
        this.addCharacter(scene, p1);
        this.addCharacter(scene, p2);
	}
	
	public void addCharacter(Scene2D scene, Character character) {	
		switch(this.characters.size()) {
			case 1:
				character.setLocationOnTilemap ( TilemapManager.GRID_WIDTH-1, TilemapManager.GRID_HEIGHT-1 );
				break;
			case 2:
				character.setLocationOnTilemap ( 0, TilemapManager.GRID_HEIGHT-1 );
				break;
			case 3:
				character.setLocationOnTilemap ( TilemapManager.GRID_WIDTH-1, 0 );
				break;
			default:
				character.setLocationOnTilemap ( 0,0 );
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
		itemManager.checkCollisionsWithCharacter ( characters.get ( 0 ) );
		itemManager.checkCollisionsWithCharacter ( characters.get ( 1 ) );
	}
}
