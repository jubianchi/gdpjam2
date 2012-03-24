// Text
// Shows word wrapping, animated text, accent chars, and font tinting.
import pulpcore.animation.Easing;
import pulpcore.animation.Timeline;
import static pulpcore.image.Colors.*;
import pulpcore.image.CoreFont;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.Label;
import pulpcore.sprite.Sprite;
import pulpcore.Stage;
import pulpcore.image.Colors;
import pulpcore.image.filter.DropShadow;
import pulpcore.image.filter.Filter;
import pulpcore.image.filter.FilterChain;
import pulpcore.image.filter.Stroke;
import pulpcore.util.StringUtil;

public class Text extends Scene2D {
    
    String[] messageText = 
        { "Welcome", "to", "PulpCore", " ", "AKA", "\u201cpu\u0306lp ko\u0302r\u201d" };
    String backgroundText =
        "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Curabitur laoreet " +
        "augue quis turpis. In sem. Nam ac justo. Sed lacinia vulputate justo. Integer " +
        "semper placerat enim. Proin fermentum feugiat augue. Sed placerat libero non " +
        "orci. Donec faucibus, velit eget hendrerit semper, nibh augue gravida purus, id " +
        "fermentum lorem leo accumsan dui. Vestibulum laoreet enim eu mi posuere " +
        "vulputate. Pellentesque malesuada lacinia nulla. Nullam ut sem ac tellus " +
        "tincidunt pellentesque. Donec eget felis ac elit scelerisque posuere. Quisque " +
        "malesuada. Aliquam erat volutpat. Pellentesque aliquet felis vulputate enim. " +
        "Morbi odio. Morbi nec massa a sapien aliquam semper. Phasellus eget tellus.";
    
    @Override
    public void load() {
        add(new FilledSprite(WHITE));
        
        // Add word-wrapped background text
        int x = 50;
        int y = 45;
        int startTime = 0;
        int maxWidth = Stage.getWidth() - x*2;
        CoreFont bgFont = CoreFont.load("serif.font.png").tint(gray(128));
        String[] textLines = StringUtil.wordWrap(backgroundText, bgFont, maxWidth);
        for (String line : textLines) {
            // Add the sprite
            Label label = new Label(bgFont, line, x, y);
            add(label);
            y += label.height.get() + 5;
            
            // Animate (typing effect)
            int numChars = line.length();
            label.numDisplayChars.set(0);
            label.numDisplayChars.animateTo(numChars, 30*numChars, Easing.NONE, startTime);
            startTime += 30*numChars + 100;
        }

        // Create a white stroke + drop shadow filter for the messages
        DropShadow dropShadow = new DropShadow(0, 3);
        dropShadow.radius.set(2);
        Stroke stroke = new Stroke(Colors.WHITE, 2);
        Filter filter = new FilterChain(stroke, dropShadow);

        // Add messages (play in a loop)
        Timeline timeline = new Timeline();
        x = Stage.getWidth() / 2;
        y = Stage.getHeight() / 2;
        startTime = 1000;
        CoreFont messageFont = CoreFont.load("complex.font.png");
        for (String line : messageText) {
            // Add the sprite
            int labelWidth = -1; // auto
            int labelHeight = 10;
            Label label = new Label(messageFont, line, x, y, labelWidth, labelHeight);
            label.setAnchor(0.5, 0.5);
            label.alpha.set(0);
            label.setFilter(filter);
            add(label);
            
            // Animate (zoom)
            timeline.at(startTime).animate(label.alpha, 0, 255, 500);
            timeline.at(startTime).animate(label.height, 10, 102, 1500, Easing.STRONG_OUT);
            timeline.at(startTime).animate(label.angle, -0.1, 0.1, 2000);
            timeline.at(startTime + 1750).animate(label.alpha, 255, 0, 250);
            startTime += 2000;
        }
        timeline.loopForever();
        addTimeline(timeline);
    }
}
