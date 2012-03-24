// SceneGraph
// Mouse over a stem to make it sway. Click to generate a new tree.
// Shows how Scene2D internally uses a scene graph: Groups can have child Groups, and children
// inherit their parent's transform.
import pulpcore.animation.Color;
import static pulpcore.image.Colors.*;
import pulpcore.Input;
import pulpcore.math.CoreMath;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.Group;
import pulpcore.sprite.Sprite;
import pulpcore.Stage;

public class SceneGraph extends Scene2D {
    
    int maxDepth = 10;
    int startSize = 150;
    int trunkColor = rgb(0x3b2300);
    int leafColor = rgb(0x468207);
    
    @Override
    public void load() {
        add(new FilledSprite(BLACK));
        
        Group trunk = new Group(Stage.getWidth() / 2, Stage.getHeight());
        generateTree(trunk, 0);
        add(trunk);
    }
    
    @Override
    public void update(int elapsedTime) {
        if (Input.isMousePressed()) {
            getMainLayer().removeAll();
            load();
        }
    }
    
    private void generateTree(Group parent, int depth) {
        if (depth >= maxDepth) {
            return;
        }
        // Blend the color from trunk to stem
        Color color = new Color();
        color.animate(trunkColor, leafColor, maxDepth);
        color.update(depth);
        
        // Create this stem
        double w = (startSize/3) / (Math.pow(depth, 1.5)+1);
        double h = startSize / (Math.pow(depth, 0.9)+1);
        if (depth > 0) {
            h *= CoreMath.rand(0.8, 1.2);
        }
        Stem stem = new Stem(0, 0, w, h, color.get());
        stem.setAnchor(0.5, 1);
        parent.add(stem);
        
        // Add two child stems (using recursion)
        double x = w/4.0;
        double y = -h + w/6.0;
        
        // Left stem
        Group leftChild = new Group(-x, y);
        leftChild.angle.set(CoreMath.rand(-0.7, -0.3));
        generateTree(leftChild, depth + 1);
        parent.add(leftChild);
        parent.moveToBottom(leftChild);
        
        // Right stem
        Group rightChild = new Group(x, y);
        rightChild.angle.set(CoreMath.rand(0.3, 0.7));
        generateTree(rightChild, depth + 1);
        parent.add(rightChild);
        parent.moveToBottom(rightChild);
    }
    
    public static class Stem extends FilledSprite {
        
        double angleJitter;
        
        public Stem(double x, double y, double w, double h, int color) {
            super(x, y, w, h, color);
        }
        
        @Override
        public void update(int elapsedTime) {
            // Make the stem (and it's children) sway
            Group parent = getParent();
            if (Input.isMouseMoving() && !parent.angle.isAnimating() && isMouseOver()) {
                double maxAngleJitter = 1.5 / width.get();
                double newAngleJitter = CoreMath.rand(-maxAngleJitter, maxAngleJitter);
                double newAngle = parent.angle.get() - angleJitter + newAngleJitter;
                parent.angle.animateTo(newAngle, 100);
                angleJitter = newAngleJitter;
            }
        }
    }
}
