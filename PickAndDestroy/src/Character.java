import pulpcore.Stage;
import pulpcore.math.Rect;
import pulpcore.sound.Playback;
import pulpcore.sound.Sound;

public class Character extends Entity
{	
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	
	private int currentDirection = UP;
	
	private Sound stepSound;
	private Playback stepPlayback;

	private int healthPoint;
	private int ammoPoint;
	
	private Gun currentGun = new Gun(Gun.GUN,  "pistolet.png", 40, 30);
	private EntityManager entityManager;
	
	public String spriteSet = "ecureuil";
	
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

	public void setImage(String name)
	{
		getSprite ().setImage ( name );
		getSprite ().setAnchor ( 0.5, 0.65 );
	}
	
	public String getSpriteSetArme() {
		return this.currentGun.getSpriteName() + "/";
	}
	
	public void getAmmo(int nb) 
	{
		int maxAmmo = ConfigManager.gameModesConfig.getValue ( "maxAmmo" );

		if (ammoPoint + nb <= maxAmmo)
		{
			ammoPoint += nb;
		}
		else
		{
			ammoPoint = maxAmmo;
		}
	}

	public int getNbAmmo()
	{
		return ammoPoint;
	}

	public void getHealth(int nb)
	{
		int maxHealth = ConfigManager.gameModesConfig.getValue ( "maxHealth" );

		if (healthPoint + nb <= maxHealth)
		{
			healthPoint += nb;
		}
		else
		{
			healthPoint = maxHealth;
		}
	}

	public int getNbHealth()
	{
		return healthPoint;
	}

	public void removeAmmo(int nb)
	{
		ammoPoint -= nb;
	}

	public void removeHealth(int nb)
	{
		if (healthPoint - nb <= 0)
		{
			healthPoint = 0;

			// DIE MOTHER FUCKER!!!!
			Sound dieSound = Sound.load ( "rire+mort.wav" );
			dieSound.play ();
		}
		else
		{
			healthPoint -= nb;
		}
	}

	public void moveStop()
	{
		switch (currentDirection)
		{
			case UP:
				setImage(spriteSet + "/" + this.getSpriteSetArme() + "fixe_dos.png");
				break;
			case DOWN:
				setImage(spriteSet + "/" + this.getSpriteSetArme() + "fixe_face.png");
				break;
			case LEFT:
				setImage(spriteSet + "/" + this.getSpriteSetArme() + "fixe_gauche.png");
				break;
			case RIGHT:
				setImage(spriteSet + "/" + this.getSpriteSetArme() + "fixe_droite.png");
				break;				
		}

		stepPlayback.setPaused ( true );
		stepPlayback.rewind ();
	}

	public void moveStart()
	{
		if (stepPlayback.isPaused ()) stepPlayback.setPaused ( false );
	}
	
	public void moveTop() {					
		moveStart();
		
		if(currentDirection != UP) setImage(spriteSet + "/" + this.getSpriteSetArme() + "frise_dos.png");
		currentDirection = UP;

		int limit = getRect ().height / 2;
		int step = ConfigManager.gameModesConfig.getValue ( "characterMoveStep" );

		if (getRect ().y - step > limit)
		{
			Rect r = new Rect ( getRect () );
			r.width -= 4;
			r.x += 2;
			r.height = 20;
			r.y -= step;

			if (!isWallAtRect ( r ))
			{
				moveOf ( 0, -step );
			}

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
		
		if(currentDirection != DOWN) getSprite ().setImage(spriteSet + "/" + this.getSpriteSetArme() + "frise_face.png");
		currentDirection = DOWN;

		int limit = (Stage.getHeight () - getRect ().height / 2);
		int step = ConfigManager.gameModesConfig.getValue ( "characterMoveStep" );

		if (getRect ().y + step < limit)
		{
			Rect r = new Rect ( getRect () );
			r.width -= 4;
			r.x += 2;
			r.y += r.height - 20 + step;
			r.height = 20;

			if (!isWallAtRect ( r ))
			{
				moveOf ( 0, +step );
			}

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
		
		if(currentDirection != LEFT) setImage(spriteSet + "/" + this.getSpriteSetArme() + "frise_gauche.png");
		currentDirection = LEFT;

		int limit = getRect ().width / 2;
		int step = ConfigManager.gameModesConfig.getValue ( "characterMoveStep" );

		if (getRect ().x - step > limit)
		{
			Rect r = new Rect ( getRect () );
			r.height -= 4;
			r.y += 2;
			r.width = 20;
			r.x -= step;

			if (!isWallAtRect ( r ))
			{
				moveOf ( -step, 0 );
			}

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
		
		if(currentDirection != RIGHT) setImage(spriteSet + "/" + this.getSpriteSetArme() + "frise_droite.png");
		currentDirection = RIGHT;

		int limit = (Stage.getWidth () - getRect ().width / 2);
		int step = ConfigManager.gameModesConfig.getValue ( "characterMoveStep" );

		if (getRect ().x + step < limit)
		{
			Rect r = new Rect ( getRect () );
			r.height -= 4;
			r.y += 2;
			r.x += r.width - 20 + step;
			r.width = 20;

			if (!isWallAtRect ( r ))
			{
				moveOf ( +step, 0 );
			}

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
		getAmmo ( 1 );
	}

	public void addGun(Gun item)
	{
		this.currentGun = item;		
		getAmmo(3);
		
		switch(currentDirection)
		{
			case UP:
				setImage(spriteSet + "/" + this.getSpriteSetArme() + "frise_dos.png");
				break;
			case DOWN:
				setImage(spriteSet + "/" + this.getSpriteSetArme() + "frise_face.png");
				break;
			case LEFT:
				setImage(spriteSet + "/" + this.getSpriteSetArme() + "frise_gauche.png");
				break;
			case RIGHT:
				setImage(spriteSet + "/" + this.getSpriteSetArme() + "frise_droite.png");
				break;				
		}
	}

	private int	delayForNextShoot	= 0;

	public void update(int elapsedTime)
	{
		delayForNextShoot -= elapsedTime;
	}

	public void shoot()
	{
		if (this.getNbAmmo () > 0)
		{
			if (delayForNextShoot <= 0)
			{
				this.currentGun.shoot ( this );
				delayForNextShoot = this.currentGun.getNextShootDelay ();
			}
		}
	}

	public void addHeart()
	{
		getHealth ( 1 );
	}

	public final void setShootLocation(Shoot shoot)
	{
		switch (currentDirection)
		{
			case UP:
				shoot.setLocation ( getRect ().x + (getRect ().width / 2) - (shoot.getRect ().width / 2), getRect ().y - (shoot.getRect ().height / 2) );
				break;
			case DOWN:
				shoot.setLocation ( getRect ().x + (getRect ().width / 2) - (shoot.getRect ().width / 2), getRect ().y + getRect ().height - (shoot.getRect ().height / 2) );
				break;
			case LEFT:
				shoot.setLocation ( getRect ().x - (shoot.getRect ().width / 2), getRect ().y + (getRect ().height / 2) - (shoot.getRect ().height / 2) );
				break;
			case RIGHT:
				shoot.setLocation ( getRect ().x + getRect ().width - (shoot.getRect ().width / 2), getRect ().y + (getRect ().height / 2) - (shoot.getRect ().height / 2) );
				break;
		}
	}
}
