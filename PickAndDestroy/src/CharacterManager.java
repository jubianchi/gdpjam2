import pulpcore.Input;
import pulpcore.Stage;
import pulpcore.scene.Scene2D;

public class CharacterManager
{
	private Character				player1;
	private Character				player2;
	private ItemManager				itemManager;

	public static CharacterManager	shared;

	public final void load(Scene2D scene, EntityManager entityManager, ItemManager itemManager)
	{
		this.itemManager = itemManager;
		shared = this;

		player1 = new Character ( Character.DOWN );
		player2 = new Character ( Character.UP );

		entityManager.addEntity ( player1 );
		entityManager.addEntity ( player2 );

		player1.setLocationOnTilemap ( 1, 1 );
		player2.setLocationOnTilemap ( TilemapManager.GRID_WIDTH - 2, TilemapManager.GRID_HEIGHT - 2 );
	}

	public Character getPlayer(int index)
	{
		if (index == 0) return player1;
		else if (index == 1) return player2;
		else throw new IndexOutOfBoundsException ( "Player index is invalid" );
	}

	public final void update(int elapsedTime)
	{
		if (Input.isDown ( Input.KEY_Q )) player1.moveLeft ();
		else if (Input.isDown ( Input.KEY_D )) player1.moveRight ();
		else if (Input.isDown ( Input.KEY_Z )) player1.moveTop ();
		else if (Input.isDown ( Input.KEY_S )) player1.moveBottom ();
		else player1.moveStop ();
		if (PhaseManager.shared.isInBrutalPhase () && Input.isDown ( Input.KEY_LEFT_CONTROL )) player1.shoot ();

		if (Input.isDown ( Input.KEY_LEFT )) player2.moveLeft ();
		else if (Input.isDown ( Input.KEY_RIGHT )) player2.moveRight ();
		else if (Input.isDown ( Input.KEY_UP )) player2.moveTop ();
		else if (Input.isDown ( Input.KEY_DOWN )) player2.moveBottom ();
		else player2.moveStop ();
		if (PhaseManager.shared.isInBrutalPhase () && Input.isDown ( Input.KEY_RIGHT_CONTROL )) player2.shoot ();

		if (Input.isDown ( Input.KEY_F5 )) player1.hitBy ( 1 );
		if (Input.isDown ( Input.KEY_F6 )) player2.hitBy ( 1 );

		player1.update ( elapsedTime );
		player2.update ( elapsedTime );

		itemManager.checkCollisionsWithCharacter ( player1 );
		itemManager.checkCollisionsWithCharacter ( player2 );
	}

	public void postUpdate(int elapsedTime)
	{
		if (player1.getNbHealth () <= 0)
		{
			Stage.setScene ( new GameoverScene ( new String[] { "Game over", "Player 2 win !!" } ) );
		}
		else if (player2.getNbHealth () <= 0)
		{
			Stage.setScene ( new GameoverScene ( new String[] { "Game over", "Player 1 win !!" } ) );
		}
	}

	public void releaseResources()
	{
		player1.moveStop ();
		player2.moveStop ();
	}
}
