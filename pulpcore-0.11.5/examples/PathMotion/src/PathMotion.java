// Shows how to animate a Sprite along a Path.
// Paths are defined with SVG path data.
import pulpcore.animation.Timeline;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.ImageSprite;
import pulpcore.image.Colors;
import pulpcore.math.Path;
import pulpcore.sprite.Sprite;

public class PathMotion extends Scene2D {

    // SVG path created with InkScape.
    // M, C, and L commands are supported.
    String svgPath = "M 181.42857,41.428571 C -13.491814,41.428571 -0.00016036284,503.14876 " +
            "178.57143,439.99999 C 319.88994,390.02513 331.55729,390.01741 465.71429,438.57143 " +
            "C 637.82839,500.86285 662.71309,41.428571 448.57142,41.428571 C 102.53673,41.428571 " +
            "105.24899,329.99999 324.28571,329.99999 C 534.47946,329.99999 539.74496,41.428571 " +
            "181.42857,41.428571";

    @Override
    public void load() {
        add(new FilledSprite(Colors.BLACK));

        Path path = new Path(svgPath);
        Timeline timeline = new Timeline();

        int count = 7;
        int dur = 20000;
        
        for (int i = 0; i < count; i++) {
            // Create a Man. Animate position and angle along the path.
            Sprite man = new ImageSprite("Man.png", 0, 0);
            man.setAnchor(0.5, 0.5);
            double position = (double)(i) / count;
            timeline.moveAndRotate(man, path, position, position + 1, dur);
            add(man);

            // Create a Spirit. Animate position (but not angle) along the path
            Sprite spirit = new ImageSprite("Spirit.png", 0, 0);
            spirit.setAnchor(0.5, 0.5);
            position = (i + 0.18) / count;
            timeline.move(spirit, path, position, position + 1, dur);
            add(spirit);
        }

        timeline.loopForever();
        addTimeline(timeline);
    }
}
