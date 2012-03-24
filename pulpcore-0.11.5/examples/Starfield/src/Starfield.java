// Starfield
// Scroll the mouse wheel or press the arrow keys to travel through the cosmos.
// Shows mouse wheel input, custom sprites, and motion blur.
import pulpcore.image.BlendMode;
import pulpcore.Input;
import pulpcore.math.CoreMath;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.ImageSprite;
import pulpcore.Stage;
import pulpcore.image.filter.MotionBlur;

public class Starfield extends Scene2D {
    
    int numStarImages = 3;
    int numStars = 50;
    int zoomTimeRemaining;
    int zoomX;
    int zoomY;
    double zoomSpeed;

    @Override
    public void load() {
        add(new ImageSprite("bg.jpg", 0, 0));
        
        for (int i = 0; i < numStars; i++) {
            int r = CoreMath.rand(numStarImages - 1);
            add(new Star("star" + r + ".png"));
        }
    }
    
    @Override
    public void update(int elapsedTime) {
        if (Input.getMouseWheelRotation() > 0) {
            zoom(Input.getMouseWheelX(), Input.getMouseWheelY(), -.02);
        }
        else if (Input.getMouseWheelRotation() < 0) {
            zoom(Input.getMouseWheelX(), Input.getMouseWheelY(), 0.75);
        }
        
        if (Input.isDown(Input.KEY_DOWN)) {
            zoom(Stage.getWidth() / 2, Stage.getHeight() / 2, -.02);
        }

        if (Input.isDown(Input.KEY_UP)) {
            zoom(Stage.getWidth() / 2, Stage.getHeight() / 2, 0.75);
        }
        
        zoomTimeRemaining -= elapsedTime;
    }
    
    void zoom(int x, int y, double speed) {
        zoomX = x;
        zoomY = y;
        zoomSpeed = speed;
        zoomTimeRemaining = 300;
    }
    
    class Star extends ImageSprite {

        MotionBlur motionBlur = new MotionBlur();
        double z;
        
        public Star(String image) {
            super(image, CoreMath.rand(0, Stage.getWidth()), CoreMath.rand(0, Stage.getHeight()));
            setZ(CoreMath.rand(1.0, 4.0));
            setBlendMode(BlendMode.Add());
            setFilter(motionBlur);
            motionBlur.distance.set(0);
        }
        
        @Override
        public void update(int elapsedTime) {
            super.update(elapsedTime);
            
            if (zoomTimeRemaining > 0) {
                double motionBlurDistance = zoomSpeed * z * z * 1.5;
                double speed = zoomSpeed * z * z / 3000;
                double dx = (x.get() - zoomX) * elapsedTime * speed;
                double dy = (y.get() - zoomY) * elapsedTime * speed;
                setZ(z + elapsedTime * speed);
                setLocation(x.get() + dx, y.get() + dy);
                
                if (x.get() < 0 || x.get() >= Stage.getWidth() ||
                    y.get() < 0 || y.get() >= Stage.getHeight())
                {
                    setZ(CoreMath.rand(1.0, 3.0));
                    setLocation(
                        zoomX + CoreMath.rand(-Stage.getWidth() / 10, Stage.getWidth() / 10),
                        zoomY + CoreMath.rand(-Stage.getHeight() / 10, Stage.getHeight() / 10));
                    motionBlur.distance.set(0);
                }
                else {
                    motionBlur.angle.set(Math.atan2(dy, dx));
                    if (zoomTimeRemaining <= 100) {
                        // Wind down the motion blur
                        if (!motionBlur.distance.isAnimating()) {
                            motionBlur.distance.animateTo(0, zoomTimeRemaining);
                        }
                    }
                    else {
                        motionBlur.distance.set(motionBlurDistance);
                    }
                }
            }
            else {
                motionBlur.distance.set(0);
            }
        }
        
        void setZ(double z) {
            this.z = z;
            setSize(z * 5, z * 5);
            alpha.set(z >= 2 ? 255 : (int)(255 * (z-1)));
        }
    }    
}
