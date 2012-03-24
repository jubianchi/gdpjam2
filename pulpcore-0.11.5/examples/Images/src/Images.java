// Images
// Shows frame-based animated images and pixel-level collision tests.
// Use the arrow keys to move the player.
import java.util.Iterator;
import pulpcore.animation.event.AddSpriteEvent;
import pulpcore.animation.event.RemoveSpriteEvent;
import pulpcore.animation.Timeline;
import pulpcore.image.AnimatedImage;
import pulpcore.image.CoreFont;
import pulpcore.image.CoreImage;
import pulpcore.Input;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.Group;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Label;
import pulpcore.sprite.Sprite;
import pulpcore.Stage;
import static pulpcore.image.Colors.*;
import static pulpcore.math.CoreMath.rand;

public class Images extends Scene2D {
    
    // Speed (pixels per millisecond)
    double playerSpeed = 0.1;
    double gravitySpeed = 0.15;
    CoreImage playerLeftImage, playerRightImage;
    ImageSprite player;
    Group stars;
    Label clickMessage;
    
    double getRandX(Sprite star) {
        return rand(star.width.get()/2, Stage.getWidth() - star.width.get());
    }
    
    double getRandY(Sprite star) {
        return rand(star.height.get()/2, Stage.getHeight() - star.height.get());
    }
    
    @Override
    public void load() {
        // Set up some random stars
        stars = new Group();
        for (int i = 0; i < 16; i++) {
            ImageSprite star = new ImageSprite("star.png", 0, 0);
            star.setLocation(getRandX(star), getRandY(star));
            star.angle.set(rand(Math.PI*2));
            stars.add(star);
        }
        add(new FilledSprite(BLACK));
        add(stars);
        
        // Load the player
        playerRightImage = CoreImage.load("player.png");
        playerLeftImage = playerRightImage.mirror();
        player = new ImageSprite(playerRightImage, Stage.getWidth()/2, 0);
        add(player);
        
        // Show keyboard message
        clickMessage = new Label(CoreFont.getSystemFont().tint(WHITE),
            "Click to start", Stage.getWidth()/2, Stage.getHeight()/2);
        clickMessage.setAnchor(0.5, 0.5);
        add(clickMessage);
    }
    
    @Override
    public void update(int elapsedTime) {
        clickMessage.visible.set(!Input.hasKeyboardFocus());
        
        // Gets keys (allow left and right to be held simultaneously)
        int direction = 0;
        if (Input.isDown(Input.KEY_LEFT)) {
            direction -= 1;
        }
        if (Input.isDown(Input.KEY_RIGHT)) {
            direction += 1;
        }
        
        if (direction == 0) {
            // Stop the player animation
            AnimatedImage image = (AnimatedImage)player.getImage();
            image.setFrame(0);
            // Set the animation time to right before the next frame, so the animation
            // changes frames immediately when the player moves.
            image.update(image.getDuration(0) - 1);
        }
        else {
            // Move the player in the x direction
            double dx = playerSpeed * direction;
            double newX = player.x.get() + dx * elapsedTime;
            double halfWidth = player.width.get()/2;
            if (newX < -halfWidth) {
                newX = Stage.getWidth() + halfWidth;
            }
            else if (newX > Stage.getWidth() + halfWidth) {
                newX = -halfWidth;
            }
            player.x.set(newX);
            player.setImage(direction < 0 ? playerLeftImage : playerRightImage);
        }
        
        // Apply gravity
        double newY = player.y.get() + gravitySpeed * elapsedTime;
        if (newY >= Stage.getHeight() + player.height.get()) {
            newY = -player.height.get();
        }
        player.y.set(newY);
        
        // Check collisions
        Iterator i = stars.iterator();
        while (i.hasNext()) {
            Sprite star = (Sprite)i.next();
            if (star.intersects(player)) {
                // Move the star to another layer so it's not checked for collisions
                stars.remove(star);
                getMainLayer().add(star);
                
                // Hide the star
                Timeline t = new Timeline();
                t.scaleTo(star, 128, 128, 200);
                t.animateTo(star.alpha, 0, 200);
                addTimeline(t);
                
                // Show the star again 15 seconds later
                t = new Timeline(15000);
                t.setLocation(star, getRandX(star), getRandY(star));
                t.set(star.angle, rand(Math.PI*2));
                t.animate(star.alpha, 0, 255, 200);
                t.scale(star, 2, 2, 64, 64, 200);
                t.addEvent(new RemoveSpriteEvent(getMainLayer(), star, 200));
                t.addEvent(new AddSpriteEvent(stars, star, 200));
                addTimeline(t);
            }
        }
    }
}