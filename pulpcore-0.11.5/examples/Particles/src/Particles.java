// Particles
// Move your mouse around to see a trail of particles
import pulpcore.animation.Easing;
import pulpcore.animation.event.RemoveSpriteEvent;
import pulpcore.animation.Timeline;
import static pulpcore.image.Colors.*;
import pulpcore.image.BlendMode;
import pulpcore.image.CoreImage;
import pulpcore.Input;
import pulpcore.math.CoreMath;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.Group;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Sprite;
import pulpcore.Stage;

public class Particles extends Scene2D {
    
    Sprite background;
    CoreImage[] images;
    Group particleLayer;
    int lastX, lastY;
    boolean wasMouseInside;
    
    @Override
    public void load() {
        background = new FilledSprite(BLACK);
        images = CoreImage.load("particles.png").split(6, 1);
        
        particleLayer = new Group();
        // Particles should ignore mouse input
        particleLayer.enabled.set(false);
        // Particles look good with additive blending
        particleLayer.setBlendMode(BlendMode.Add());
        add(background);
        addLayer(particleLayer);
        
        makeParticles(320, 240, 320, 240, 50);
        
        setCursor(Input.CURSOR_OFF);
    }
    
    @Override
    public void update(int elapsedTime) {
        if (Input.isMouseInside()) {
            int x = Input.getMouseX();
            int y = Input.getMouseY();
            if (wasMouseInside) {
                int dist = (int)Math.sqrt((lastX - x) * (lastX - x) + (lastY - y) * (lastY - y)); 
                makeParticles(lastX, lastY, x, y, 2 + dist / 8);
            }
            lastX = x;
            lastY = y;
            wasMouseInside = true;
        }
        else {
            wasMouseInside = false;
        }
    }
    
    private void makeParticles(int x1, int y1, int x2, int y2, int numParticles) {
        
        Timeline timeline = new Timeline();
        
        for (int i = 0; i < numParticles; i++) {
            int size = CoreMath.rand(4, 48);
            int duration = (200 - size) * 3;
            int moveDistance = CoreMath.rand(4, 80 - size);
            double moveDirection = CoreMath.rand(0, 2*Math.PI);
            
            int startX = x1 + i * (x2 - x1) / numParticles;
            int startY = y1 + i * (y2 - y1) / numParticles;
            int goalX = startX + (int)(moveDistance * Math.cos(moveDirection));
            int goalY = startY + (int)(moveDistance * Math.sin(moveDirection));
            double startAngle = CoreMath.rand(0, 2*Math.PI);
            double endAngle = startAngle + CoreMath.rand(-2*Math.PI, 2*Math.PI);
            
            CoreImage image = images[CoreMath.rand(images.length - 1)];
            Sprite sprite = new ImageSprite(image, startX, startY);
            sprite.setAnchor(0.5, 0.5);
            sprite.setSize(size, size);
            particleLayer.add(sprite);
            
            timeline.animateTo(sprite.x, goalX, duration, Easing.REGULAR_OUT);
            timeline.animateTo(sprite.y, goalY, duration, Easing.REGULAR_OUT);
            timeline.animate(sprite.angle, startAngle, endAngle, duration);
            timeline.at(100).animateTo(sprite.alpha, 0, duration - 100, Easing.REGULAR_OUT);
            timeline.add(new RemoveSpriteEvent(particleLayer, sprite, duration));
        }
        
        addTimeline(timeline);
    }
}