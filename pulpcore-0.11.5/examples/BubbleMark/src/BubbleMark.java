// PulpCore BubbleMark implementation. See http://www.bubblemark.com/
// - Cap Frame Rate: Caps the frame rate to the platform default (60 fps on Windows).
// - Pixel Snapping: Draws images at integer locations (increasing performance).
// - Dirty Rectangles: Only draws areas of the screen that have changed (increases performance 
//   when changed areas don't cover a large area of the screen).
import pulpcore.animation.Bool;
import pulpcore.animation.Fixed;
import pulpcore.image.Colors;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Label;
import pulpcore.Stage;

public class BubbleMark extends Scene2D {
    
    Ball[] balls = new Ball[16];
    Bool pixelSnapping = new Bool(false);
    Fixed frameRate = new Fixed();
    
    @Override
    public void load() {
        // Add background
        add(new FilledSprite(Colors.WHITE));
        
        // Add balls
        for (int i = 0; i < balls.length; i++) {
            BallSprite ballSprite = new BallSprite("ball.png");
            ballSprite.pixelSnapping.bindTo(pixelSnapping);
            add(ballSprite);
            balls[i] = ballSprite.ball;
        }
        
        // Add fps display
        Label frameRateLabel = new Label("%.1f fps", 5, 5);
        frameRateLabel.setFormatArg(frameRate);
        add(frameRateLabel);
    }
    
    @Override
    public synchronized void update(int elapsedTime) {
        // Check collisions
        for (int i = 0; i < balls.length; i++) {
            for (int j = i+1; j < balls.length; j++) {
                balls[i].doCollide(balls[j]);
            }
        }
            
        // Update fps display
        double fps = Stage.getActualFrameRate();
        if (fps >= 0) {
            frameRate.set(fps);
        }
    }
    
    // Methods called from JavaScript (invoke in the animation thread)
    
    public void setNumBalls(final int numBalls) {
        invokeLater(new Runnable() {
            public void run() {
                if (numBalls != balls.length) {
                    balls = new Ball[numBalls];
                    reload();
                }
            }
        });
    }
    
    public void setCapFrameRate(final boolean capFrameRate) {
        invokeLater(new Runnable() {
            public void run() {
                Stage.setFrameRate(capFrameRate ? Stage.DEFAULT_FPS : Stage.MAX_FPS);
            }
        });
    }
    
    public void setPixelSnapping(final boolean pixelSnapping) {
        invokeLater(new Runnable() {
            public void run() {
                BubbleMark.this.pixelSnapping.set(pixelSnapping);
            }
        });
    }
    
    // The Ball sprite, wrapper for Ball.java by rbair
    
    class BallSprite extends ImageSprite {
        
        Ball ball;
        
        public BallSprite(String image) {
            super(image, 0, 0);
            ball = new Ball();
            x.set(ball._x);
            y.set(ball._y);
        }
        
        @Override
        public void update(int elpasedTime) {
            super.update(elpasedTime);
            ball.move();
            x.set(ball._x);
            y.set(ball._y);
        }
    }
}
