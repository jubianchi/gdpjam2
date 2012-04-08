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
	
	private int currentDirection;
	
	private Sound stepSound;
	private Playback stepPlayback;

	private int healthPoint;
	private int ammoPoint;
	
	private Gun currentGun = new Gun(Gun.GUN,  "pistolet.png", 40, 30);
	
	public String spriteSet = "ecureuil";
	
	public Character ( int direction )
	{
		super ( "ecureuil/frise_face.png", 40, 30 );
	
		this.currentDirection = direction;
		this.healthPoint = ConfigManager.gameModesConfig.getValue("startHealth");
		this.ammoPoint = ConfigManager.gameModesConfig.getValue("startAmmo");
		
		stepSound = Sound.load("bruit_de_pas.wav");
		
		moveStop ();
	}

	public int getDirection() {
		return this.currentDirection;
	}
	
	public void setImage(String name)
	{
		getSprite ().setImage ( name );
		getSprite ().setAnchor ( 0.5, 0.65 );
	}
	
	public String getSpriteSetArme() {
		return this.currentGun.getSpriteName() + "/";
	}
	
	public void addAmmo(int nb) 
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

	public void addHealth(int nb)
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

	public void hitBy ( int hitPoints )
	{
		Sound.load("degats.wav").play ();
		healthPoint = Math.max ( healthPoint - hitPoints, 0 );
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

		if ( stepPlayback != null )
		{
			stepPlayback.setPaused ( true );
		}
	}

	public void moveStart()
	{
		if ( stepPlayback == null )
			stepPlayback = stepSound.loop ();
		else
			stepPlayback.setPaused ( false );
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

			if (!isWallOrTableAtRect ( r ))
			{
				moveOf ( 0, -step );
			}
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

			if (!isWallOrTableAtRect ( r ))
			{
				moveOf ( 0, +step );
			}
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

			if (!isWallOrTableAtRect ( r ))
			{
				moveOf ( -step, 0 );
			}
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

			if (!isWallOrTableAtRect ( r ))
			{
				moveOf ( +step, 0 );
			}
		}
	}

	public void addGun(Gun item)
	{
		this.currentGun = item;		
		addAmmo(3);
		
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
