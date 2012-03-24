// Compositing
// Shows image tinting, additive blending (colors blend to white) and
// and multiplicative blending (colors blend to black).
import pulpcore.animation.BindFunction;
import pulpcore.animation.Easing;
import pulpcore.animation.event.TimelineEvent;
import pulpcore.animation.Timeline;
import pulpcore.image.CoreImage;
import pulpcore.image.BlendMode;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.Group;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Sprite;
import pulpcore.Stage;
import pulpcore.image.filter.Blur;
import static pulpcore.image.Colors.*;
import static pulpcore.math.CoreMath.rand;

public class Compositing extends Scene2D {
    
    BlendMode nextBlendMode = BlendMode.Add();
    int prevBackgroundColor = BLACK;
    int border = 100;
    int particles = 4;
    int moves = 4;
    
    @Override 
    public void load() {
        // Add background and set up blend mode
        FilledSprite background;
        Group particleLayer = new Group();
        if (nextBlendMode == BlendMode.Add()) {
            background = new FilledSprite(prevBackgroundColor);
            background.fillColor.animateTo(BLACK, 500);
            particleLayer.setBlendMode(nextBlendMode);
            prevBackgroundColor = BLACK;
            nextBlendMode = BlendMode.Multiply();
        }
        else {
            background = new FilledSprite(prevBackgroundColor);
            background.fillColor.animateTo(WHITE, 500);
            particleLayer.setBlendMode(nextBlendMode);
            prevBackgroundColor = WHITE;
            nextBlendMode = BlendMode.Add();
        }
        add(background);
        particleLayer.alpha.set(0);
        addLayer(particleLayer);
        
        // Add particles
        for (int i = 0; i < particles; i++) {
            int[] x = new int[moves];
            int[] y = new int[moves];
            for (int j = 0; j < moves; j++) {
                x[j] = rand(border, Stage.getWidth() - border*2);
                y[j] = rand(border, Stage.getHeight() - border*2);
            }
            
            Timeline moveTimeline = new Timeline();
            Sprite sprite = makeParticle();
            particleLayer.add(sprite);
            
            int startTime = 0;
            for (int j = 0; j < moves; j++) {
                int lastX = x[j];
                int lastY = y[j];
                int newX = x[(j+1)%moves];
                int newY = y[(j+1)%moves];
                
                int dx = newX - lastX;
                int dy = newY - lastY;
                int moveDur = (int)(Math.sqrt(dx * dx + dy * dy) * 50); 
                moveTimeline.at(startTime).move(sprite, lastX, lastY, newX, newY, moveDur, 
                    Easing.ELASTIC_IN_OUT);
                startTime += moveDur;
            }
            moveTimeline.loopForever();
            addTimeline(moveTimeline);
        
            // Create 3 mirror objects
            Sprite mirror = makeParticle();
            mirror.x.bindTo(mirrorX(sprite));
            mirror.y.bindTo(mirrorY(sprite));
            particleLayer.add(mirror);
            
            mirror = makeParticle();
            mirror.x.bindTo(mirrorX(sprite));
            mirror.y.bindTo(sprite.y);
            particleLayer.add(mirror);
            
            mirror = makeParticle();
            mirror.x.bindTo(sprite.x);
            mirror.y.bindTo(mirrorY(sprite));
            particleLayer.add(mirror);
        }
        
        // Transition to the next blend mode
        int changeTime = 30000;
        Timeline timeline = new Timeline();
        timeline.at(500).animate(particleLayer.alpha, 0, 255, 2000);
        timeline.at(changeTime-2500).animate(particleLayer.alpha, 255, 0, 2000);
        timeline.add(new TimelineEvent(changeTime) {
            public void run() {
                reload();
            }
        });
        addTimeline(timeline);
    }
    
    Sprite makeParticle() {
        CoreImage image = CoreImage.load("particle.png");
        int color = hue(rand(0, 255));
        final ImageSprite sprite = new ImageSprite(image.tint(color), 0, 0);
        sprite.setAnchor(0.5, 0.5);
        sprite.pixelSnapping.set(true);
        Blur blur = new Blur(8);
        blur.quality.set(1);
        blur.radius.bindTo(new BindFunction() {
            public Number f() {
                int w = Stage.getWidth()/2;
                double f = 1 - Math.abs(w - sprite.x.get()) / w;
                if (f < 0.10) {
                    return 0;
                }
                else {
                    return 16 * (f-0.10);
                }
            }
        });
        sprite.setFilter(blur);
        return sprite;
    }
    
    BindFunction mirrorX(final Sprite source) {
        return new BindFunction() {
            public Number f() {
                return (Stage.getWidth() - source.x.get());
            }
        };
    }
    
    BindFunction mirrorY(final Sprite source) {
        return new BindFunction() {
            public Number f() {
                return (Stage.getHeight() - source.y.get());
            }
        };
    }
   
}
