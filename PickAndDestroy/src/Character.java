import java.util.List;

import pulpcore.Stage;
import pulpcore.math.Rect;
import pulpcore.sound.Playback;
import pulpcore.sound.Sound;


public class Character extends Entity
{	
	private int currentDirection = 0;
	
	private Sound stepSound;
	private Playback stepPlayback;

	private int healthPoint;
	private int ammoPoint;
	
	private String currentGun = "pistolet";
	
	private EntityManager entityManager;
	
	public Character ( EntityManager entityManager )
	{
		super ( "ecureuil/frise_face.png", 40, 30 );

		this.entityManager = entityManager;
		this.healthPoint = ConfigManager.gameModesConfig.getValue("startHealth");
		this.ammoPoint = ConfigManager.gameModesConfig.getValue("startAmmo");
		
		stepSound = Sound.load("bruit_de_pas.wav");
		stepPlayback = stepSound.play();
		stepPlayback.setPaused ( true );	
	}

	public void setImage ( String name )
	{
		getSprite().setImage(name);
		getSprite ().setAnchor ( 0.5, 0.65 );
	}
	
	public void getAmmo(int nb) 
	{
		int maxAmmo = ConfigManager.gameModesConfig.getValue("maxAmmo");
		
		if(ammoPoint + nb <= maxAmmo)
		{
			ammoPoint += nb;
		} 
		else 
		{
			ammoPoint = maxAmmo;
		}
	}
	
	public int getNbAmmo() {
		return ammoPoint;
	}
	
	public void getHealth(int nb) 
	{
		int maxHealth = ConfigManager.gameModesConfig.getValue("maxHealth");
		
		if(healthPoint + nb <= maxHealth)
		{
			healthPoint += nb;
		} 
		else 
		{
			healthPoint = maxHealth;
		}
	}
	
	public int getNbHealth() {
		return healthPoint;
	}
	
	public void removeAmmo(int nb) {
		ammoPoint -= nb;
	}
	
	public void removeHealth(int nb) {
		if(healthPoint - nb <= 0) {
			healthPoint = 0;
			
			//DIE MOTHER FUCKER!!!!
			Sound dieSound = Sound.load("rire+mort.wav");
			dieSound.play();
		} else {
			healthPoint -= nb;
		}
	}
	
	public void moveStop() 
	{
		switch(currentDirection)
		{
			case 0:
				setImage("ecureuil/fixe_dos.png");
				break;
			case 1:
				setImage("ecureuil/fixe_face.png");
				break;
			case 2:
				setImage("ecureuil/fixe_gauche.png");
				break;
			case 3:
				setImage("ecureuil/fixe_droite.png");
				break;				
		}
		
		stepPlayback.setPaused ( true );
		stepPlayback.rewind();
	}
	
	public void moveStart() 
	{
		if(stepPlayback.isPaused()) stepPlayback.setPaused ( false );
	}

	public void moveOfStep
	( 	int stepX, int stepY,
		int px1, int py1,
		int px2, int py2
	)
	{
		int px3 = ( px1 + px2 ) / 2;
		int py3 = ( py1 + py2 ) / 2;
		int colCount =
					( isWallAtPoint ( px1, py1 ) ? 1 : 0 )
				+	( isWallAtPoint ( px2, py2 ) ? 1 : 0 )
				+	( isWallAtPoint ( px3, py3 ) ? 1 : 0 );
		if ( colCount < 2 )
		{
			moveOf ( stepX, stepY );
		}
	}
	
	public boolean isWallAtPoint ( int x, int y )
	{
		List<Entity> list = entityManager.getCollidingEntities ( x, y );
		for ( int i = 0; i < list.size (); i++ )
		{
			Entity e = list.get(i);
			if ( e.getClass() == Wall.class )
			{
				Wall w = (Wall)e;
				if ( 
						( w.getType () == Wall.TABLE ) 
					||	( w.getType () == Wall.WALL )
					)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean isWallAtRect(Rect r)
	{
		List<Entity> list = entityManager.getCollidingEntities ( r );
		for ( int i = 0; i < list.size (); i++ )
		{
			Entity e = list.get(i);
			if ( e.getClass() == Wall.class )
			{
				Wall w = (Wall)e;
				if ( 
						( w.getType () == Wall.TABLE ) 
					||	( w.getType () == Wall.WALL )
					)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void moveTop() {					
		moveStart();
		
		if(currentDirection != 0) setImage("ecureuil/frise_dos.png");
		currentDirection = 0;
		
		int limit = getRect().height / 2;
		int step = ConfigManager.gameModesConfig.getValue("characterMoveStep");
		
		if ( getRect().y - step > limit )
		{
			Rect r = new Rect ( getRect () );
			r.width -= 4;
			r.x += 2;
			r.height = 20;
			r.y -= step;
			
			if ( ! isWallAtRect ( r ) )
			{ moveOf ( 0, -step ); }
			
			/*
			boolean collide = false;
			collide |= isWallAtPoint ( getRect ().x, getRect ().y );
			collide |= isWallAtPoint ( getRect ().x + getRect ().width, getRect ().y );

			if ( ! collide )
			{
				moveOf ( 0, -step );
			}
			*/
			/*
			moveOfStep
			(
				0, -step,
				getRect ().x, getRect ().y,
				getRect ().x + getRect ().width, getRect ().y
			);*/
		}
	}

	public void moveBottom() {					
		moveStart();
		
		if(currentDirection != 1) getSprite ().setImage("ecureuil/frise_face.png");
		currentDirection = 1;
		
		int limit = (Stage.getHeight() - getRect().height / 2);
		int step = ConfigManager.gameModesConfig.getValue("characterMoveStep");
		
		if ( getRect().y + step < limit )
		{
			Rect r = new Rect ( getRect () );
			r.width -= 4;
			r.x += 2;
			r.y += r.height - 20 + step;
			r.height = 20;
			
			if ( ! isWallAtRect ( r ) )
			{ moveOf ( 0, +step ); }
			
			/*boolean collide = false;
			collide |= isWallAtPoint ( getRect ().x, getRect ().y + getRect ().height );
			collide |= isWallAtPoint ( getRect ().x + getRect ().width, getRect ().y + getRect ().height );

			if ( ! collide )
			{
				moveOf ( 0, +step );
			}
			*/
		}
	}
	
	public void moveLeft() {
		moveStart();
		
		if(currentDirection != 2) setImage("ecureuil/frise_gauche.png");
		currentDirection = 2;
		
		int limit = getRect().width / 2;
		int step = ConfigManager.gameModesConfig.getValue("characterMoveStep");
		
		if ( getRect().x - step > limit )
		{
			Rect r = new Rect ( getRect () );
			r.height -= 4;
			r.y += 2;
			r.width = 20;
			r.x -= step;
			
			if ( ! isWallAtRect ( r ) )
			{ moveOf ( -step, 0 ); }
			
			/*
			boolean collide = false;
			collide |= isWallAtPoint ( getRect ().x, getRect ().y );
			collide |= isWallAtPoint ( getRect ().x, getRect ().y + getRect ().height );

			if ( ! collide )
			{
				moveOf ( -step, 0 );
			}*/
		}
	}
	
	public void moveRight()
	{
		moveStart();
		
		if(currentDirection != 3) setImage("ecureuil/frise_droite.png");
		currentDirection = 3;
		
		int limit = (Stage.getWidth() - getRect().width / 2);
		int step = ConfigManager.gameModesConfig.getValue("characterMoveStep");
		
		if ( getRect().x + step < limit )
		{
			Rect r = new Rect ( getRect () );
			r.height -= 4;
			r.y += 2;
			r.x += r.width - 20 + step;
			r.width = 20;
			
			if ( ! isWallAtRect ( r ) )
			{ moveOf ( +step, 0 ); }
			
			/*
			boolean collide = false;
			collide |= isWallAtPoint ( getRect ().x + getRect ().width, getRect ().y );
			collide |= isWallAtPoint ( getRect ().x + getRect ().width, getRect ().y + getRect ().height );

			if ( ! collide )
			{
				moveOf ( +step, 0 );
			}
			*/
		}
	}

	public void addBullets()
	{
		getAmmo(1);
	}

	public void addGun()
	{
		
	}

	public void addHeart()
	{
		getHealth(1);
	}
}
