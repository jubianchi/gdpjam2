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
		addSprite ( 2, 2, 2,2, "2x2Obj_01.png", 0, 0 );
		addSprite ( 11, 11, 2,2, "2x2Obj_01.png", 0,-5 );
		addSprite ( 2, 11, 2,2, "2x2Obj_01.png", 0,-5 );
		addSprite ( 11, 2, 2,2, "2x2Obj_01.png", 0,-5 );
		
		//sprites barre du haut
		addSprite ( 5, 2, 1,1, "1x1ObjP2.png", -0, -10 );
		addSprite ( 6, 2, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 7, 2, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( 8, 2, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 9, 2, 1,1, "1x1ObjP2.png", -0, -10 );
		
		// sprites angle haut gauche
		addSprite ( 3, 5, 1,1, "1x1ObjP2.png", -0,-10 );
		addSprite ( 3, 6, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 4, 5, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( 5, 5, 1,1, "PtTableHz.png", 0,-5 );

		// sprites angle bas gauche
		addSprite ( 3, 8, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 3, 9, 1,1, "1x1ObjP2.png", -0, -10 );
		addSprite ( 4, 9, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( 5, 9, 1,1, "PtTableHz.png", 0,-5 );
		
		// sprites angle haut droite
		addSprite ( 9, 5, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 10, 5, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( 11, 5, 1,1, "1x1ObjP2.png", -0,-10 );
		addSprite ( 11, 6, 1,1, "PtTableHz.png", 0,-5 );

		// sprites angle bas droite
		
		addSprite ( 11, 8, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 9, 9, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( 10, 9, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 11, 9, 1,1, "1x1ObjP2.png", -0,-10 );
		
		//sprites barre du bas
		addSprite ( 5, 12, 1,1, "1x1ObjP2.png", -0,-10 );
		addSprite ( 6, 12, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 7, 12, 1,1, "PtTableVt.png", 0,-5 );
		addSprite ( 8, 12, 1,1, "PtTableHz.png", 0,-5 );
		addSprite ( 9, 12, 1,1, "1x1ObjP2.png", -0,-10 );
			
	}

	private void addSprite ( int x, int y, int sx ,int sy, String name, int tx, int ty )
	{
		Entity entity = new Entity
		(
			name,
			TilemapManager.tileXToPixel(sx),
			TilemapManager.tileYToPixel(sy)
		);
		entityManager.addEntity ( entity );
		
		int px = TilemapManager.tileXToPixel(x);
		int py = TilemapManager.tileYToPixel(y);
		entity.setLocation ( px, py );

		double anchorX = -(double)tx / (double)entity.getSprite ().getImage ().getWidth ();
		double anchorY = -(double)ty / (double)entity.getSprite ().getImage ().getHeight ();
		entity.getSprite().setAnchor ( 0.5 + anchorX, 0.5 + anchorY );
	}
	
	public final void update( int elapsedTime )
	{
	}
}
