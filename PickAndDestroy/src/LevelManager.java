import pulpcore.scene.Scene2D;
import pulpcore.sprite.ImageSprite;


public class LevelManager
{
	private EntityManager entityManager;
	
	public final void load(Scene2D scene, EntityManager entityManager)
	{
		this.entityManager = entityManager;
		createLevel();
	}
	
	private void createLevel()
	{
		// sprites 2x2

		addSprite ( Wall.WALL, 2, 2, 2,2, "Silo2x2A.png", 0, 0 );
		addSprite ( Wall.WALL, 11, 11, 2,2, "Silo2x2B.png", 0,-5 );
		addSprite ( Wall.WALL, 2, 11, 2,2, "Silo2x2C.png", 0,-5 );
		addSprite ( Wall.WALL, 11, 2, 2,2, "Silo2x2D.png", 0,-5 );

		
		//sprites barre du haut
		addSprite ( Wall.WALL, 9, 2, 1,1, "Silo1x1B.png", -0, -10 );
		addSprite ( Wall.TABLE, 8, 2, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( Wall.TABLE, 7, 2, 1,1, "GeneratorAnim6F.png", 0,-5 );
		addSprite ( Wall.TABLE, 6, 2, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( Wall.WALL, 5, 2, 1,1, "Silo1x1A.png", -0, -10 );
		
		// sprites angle haut gauche

		addSprite ( Wall.TABLE, 4, 5, 2,1, "GdTableHz.png", 0,-5 );
		addSprite ( Wall.TABLE, 3, 6, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( Wall.WALL, 3, 5, 1,1, "TowerAnim3F.png", -0,-10 );

		// sprites angle bas gauche
		addSprite ( Wall.TABLE, 3, 8, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( Wall.TABLE, 4, 9, 2,1, "PtTableHz.png", 0,-5 );
		addSprite ( Wall.WALL, 3, 9, 1,1, "Silo1x1A.png", -0, -10 );

		
		// sprites angle haut droite
		addSprite ( Wall.WALL, 11, 5, 1,1, "Silo1x1A.png", -0,-10 );
		addSprite ( Wall.TABLE, 11, 6, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( Wall.TABLE, 9, 5, 2,1, "GdTableHz.png", 0,-5 );
		// sprites angle bas droite
		addSprite ( Wall.TABLE, 9, 9, 2,1, "GdTableHz.png", 0,-5 );
		addSprite ( Wall.TABLE, 11, 8, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( Wall.WALL, 11, 9, 1,1, "TowerAnim3F.png", -0,-10 );
		//sprites barre du bas
		addSprite ( Wall.WALL, 9, 12, 1,1, "Silo1x1A.png", -0,-10 );		
		addSprite ( Wall.TABLE, 8, 12, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( Wall.TABLE, 7, 12, 1,1, "GeneratorAnim6F.png", 0,-5 );
		addSprite ( Wall.TABLE, 6, 12, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( Wall.WALL, 5, 12, 1,1, "Silo1x1B.png", -0,-10 );
		
		// FX burn
		addSprite ( Wall.FX, 14, 8, 1,1, "FXBurn.png", 0,-5 );
		addSprite ( Wall.FX, 7, 1, 1,1, "FXBurn.png", 0,-5 );	
	}

	private void addSprite ( int type, int x, int y, int sx ,int sy, String name, int tx, int ty )
	{
		Wall wall = new Wall
		(
			type,
			name,
			TilemapManager.tileXToPixel(sx),
			TilemapManager.tileYToPixel(sy)
		);
		entityManager.addEntity ( wall );
		
		int px = TilemapManager.tileXToPixel(x);
		int py = TilemapManager.tileYToPixel(y);
		wall.setLocation ( px, py );

		double anchorX = -(double)tx / (double)wall.getSprite ().getImage ().getWidth ();
		double anchorY = -(double)ty / (double)wall.getSprite ().getImage ().getHeight ();
		wall.getSprite().setAnchor ( 0.5 + anchorX, 0.5 + anchorY );
	}
	
	public final void update( int elapsedTime )
	{
	}
}
