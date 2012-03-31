import pulpcore.Input;
import pulpcore.scene.Scene2D;

public class CharacterManager
{
	private Character	player1;
	private Character	player2;
	private ItemManager	itemManager;

	public final void load(Scene2D scene, EntityManager entityManager, ItemManager itemManager)
	{
		this.itemManager = itemManager;

		player1 = new Character ();
		player2 = new Character ();

		entityManager.addEntity ( player1 );
		entityManager.addEntity ( player2 );

		player1.setLocationOnTilemap ( 0, 0 );
		player1.setLocationOnTilemap ( TilemapManager.GRID_WIDTH - 1, TilemapManager.GRID_HEIGHT - 1 );
	}

	public Character getPlayer(int index)
	{
		if (index == 0) return player1;
		else if (index == 1) return player2;
		else throw new IndexOutOfBoundsException ( "Player index is invalid" );
	}

	public final void update(int elapsedTime)
	{
		if (Input.isDown ( Input.KEY_LEFT )) player1.moveLeft ();
		else if (Input.isDown ( Input.KEY_RIGHT )) player1.moveRight ();
		else if (Input.isDown ( Input.KEY_UP )) player1.moveTop ();
		else if (Input.isDown ( Input.KEY_DOWN )) player1.moveBottom ();
		else player1.moveStop ();

		if (Input.isDown ( Input.KEY_RIGHT_ALT )) player1.shoot ();

		if (Input.isDown ( Input.KEY_Q )) player2.moveLeft ();
		else if (Input.isDown ( Input.KEY_D )) player2.moveRight ();
		else if (Input.isDown ( Input.KEY_Z )) player2.moveTop ();
		else if (Input.isDown ( Input.KEY_S )) player2.moveBottom ();
		else player2.moveStop ();

		if (Input.isDown ( Input.KEY_LEFT_ALT )) player2.shoot ();

		if (Input.isDown ( Input.KEY_F5 )) player1.removeHealth ( 1 );
		if (Input.isDown ( Input.KEY_F6 )) player2.removeHealth ( 1 );

		player1.update ( elapsedTime );
		player2.update ( elapsedTime );

		itemManager.checkCollisionsWithCharacter ( player1 );
		itemManager.checkCollisionsWithCharacter ( player2 );
	}
}
