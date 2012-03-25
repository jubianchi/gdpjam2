import pulpcore.Input;


public class KeyManager {
	private Character player1;
	private Character player2;
	
	public KeyManager(Character player1, Character player2) {
		this.player1 = player1;
		this.player2 = player2;
	}
	
	public void update(int elapsedTime) {
		if(Input.isDown(Input.KEY_LEFT)) {	        
	        player1.moveLeft();	        
        } else if(Input.isDown(Input.KEY_RIGHT)) {        	
        	player1.moveRight();
        } else if(Input.isDown(Input.KEY_UP)) {        	
        	player1.moveTop();
        } else if(Input.isDown(Input.KEY_DOWN)) {        	
        	player1.moveBottom();
        } else {
        	player1.moveStop();
        }
		
		if(Input.isDown(Input.KEY_RIGHT_ALT)) {
			player1.shoot();
		}
		
		if(Input.isDown(Input.KEY_Q)) {	        
	        player2.moveLeft();
        } else if(Input.isDown(Input.KEY_D)) {        	
        	player2.moveRight();
        } else if(Input.isDown(Input.KEY_Z)) {        	
        	player2.moveTop();
        } else if(Input.isDown(Input.KEY_S)) {        	
        	player2.moveBottom();
        } else {
        	player2.moveStop();
        }
		
		if(Input.isDown(Input.KEY_LEFT_ALT)) {
			player1.shoot();
		}
		
		if(Input.isDown(Input.KEY_F5)) {
			player1.removeHealth(1);
		} else if(Input.isDown(Input.KEY_F6)) {
			player2.removeHealth(1);
		}
	}
}
