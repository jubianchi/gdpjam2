// Mouse over a moon for a clearer view. Click to change filters.
import pulpcore.Input;
import pulpcore.image.Colors;
import pulpcore.image.CoreFont;
import pulpcore.image.filter.*;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Label;
import pulpcore.sprite.Sprite;

public class Filters extends Scene2D {

    String[] moonNames = { "Io", "Europa", "Ganymede", "Callisto" };
    ImageSprite[] moons = new ImageSprite[4];
    Label[] labels = new Label[4];
    Filter whiteHot = new FilterChain(new ColorOverlay(Colors.WHITE), new Stroke(Colors.YELLOW, 10));
    double angle = 0;

    // Cycle through these filters
    Filter[] filters = { new Identity(), new HSBAdjust(-20, 64, -64), new Reflection(), new Thermal(), whiteHot };

    @Override
    public void load() {
        add(new ImageSprite("Galaxy.jpg", 0, 0));

        CoreFont font = CoreFont.getSystemFont().tint(Colors.WHITE);
        for (int i = 0; i < moons.length; i++) {
            labels[i] = new Label(font, moonNames[i], 0, 0);
            labels[i].setAnchor(0.5, 0);
            labels[i].setFilter(new Glow(0.25));
            
            moons[i] = new Moon(moonNames[i] + ".png", labels[i]);
            add(moons[i]);
            add(labels[i]);
        }

        positionMoons();
    }

    private void positionMoons() {
        for (int i = 0; i < moons.length; i++) {
            double moonsAngle = angle + i * (2 * Math.PI) / moons.length;
            double x = Math.cos(moonsAngle) * 200;
            double y = Math.sin(moonsAngle) * 100;
            double scale = 0.25 + (y + 100) / 266;
            double w = scale * moons[i].getImage().getWidth();
            double h = scale * moons[i].getImage().getHeight();
            
            moons[i].setLocation(x + 320, y + 240);
            moons[i].setSize(w, h);
            labels[i].setLocation(x + 320, y + 240 + h/2); 
        }
    }

    @Override
    public void update(int elapsedTime) {
        double dAngle = elapsedTime * Math.PI / 10000;
        angle = (angle + dAngle) % (2 * Math.PI);
        positionMoons();
    }

    public class Moon extends ImageSprite {

        Blur blur = new Blur();
        int filterIndex = 0;
        Label label;

        public Moon(String image, Label label) {
            super(image, 0, 0);
            this.label = label;
            this.label.alpha.set(0);
            setFilter(blur);
            setAnchor(0.5, 0.5);
            setCursor(Input.CURSOR_HAND);
        }

        @Override
        public void update(int elapsedTime) {
            super.update(elapsedTime);
            int blurGoal = isMouseOver() ? 0 : 10;
            if (!blur.radius.isAnimating() && blur.radius.get() != blurGoal) {
                int dur = 100 + blurGoal*20;
                blur.radius.animateTo(blurGoal, dur);
                label.alpha.animateTo(blurGoal == 0 ? 255 : 0, dur);
            }
            if (isMousePressed()) {
                filterIndex = (filterIndex + 1) % filters.length;
                setFilter(new FilterChain(filters[filterIndex], blur));
            }
        }
    }
}

