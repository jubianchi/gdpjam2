// Easings 
// Click to view various easings.

import pulpcore.Input;
import pulpcore.animation.Easing;
import pulpcore.animation.Timeline;
import static pulpcore.image.Colors.*;
import pulpcore.image.CoreFont;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Label;
import pulpcore.sprite.Sprite;

public class Easings extends Scene2D {
    
    // Ease t from 0 to 1.
    Easing customEasing = new Easing() {
        @Override
        protected double ease(double t) {
            return Math.sin(t*2*Math.PI)/3+t;
        }   
    };
        
    Easing[] easings = { 
        Easing.NONE,
        Easing.REGULAR_IN, Easing.REGULAR_OUT, Easing.REGULAR_IN_OUT,
        Easing.STRONG_IN, Easing.STRONG_OUT, Easing.STRONG_IN_OUT,
        Easing.BACK_IN, Easing.BACK_OUT, Easing.BACK_IN_OUT,
        Easing.ELASTIC_IN, Easing.ELASTIC_OUT, Easing.ELASTIC_IN_OUT,
        customEasing
    };
    
    String[] easingNames = { 
        "Easing.NONE",
        "Easing.REGULAR_IN", "Easing.REGULAR_OUT", "Easing.REGULAR_IN_OUT",
        "Easing.STRONG_IN", "Easing.STRONG_OUT", "Easing.STRONG_IN_OUT",
        "Easing.BACK_IN", "Easing.BACK_OUT", "Easing.BACK_IN_OUT",
        "Easing.ELASTIC_IN", "Easing.ELASTIC_OUT", "Easing.ELASTIC_IN_OUT",
        "customEasing"
    };
    
    Sprite icon;
    Label label;
    Timeline timeline;
    int easingIndex;
    
    @Override
    public void load() {
        CoreFont font = CoreFont.getSystemFont().tint(WHITE);
        icon = new ImageSprite("earth.png", 140, 240);
        icon.setAnchor(0.5, 0.5);
        label = new Label(font, "", 320, 20);
        label.setAnchor(0.5, 0);
        
        add(new ImageSprite("background.png", 0, 0));
        add(icon);
        add(label);
        setCursor(Input.CURSOR_HAND);
        
        setEasing(0);
    }
    
    private void setEasing(int index) {
        easingIndex = index;
        Easing easing = easings[index];
        label.setText(easingNames[index]);
        
        int dur = 1000;
        int delay = 500;
        timeline = new Timeline();
        timeline.move(icon, 140,240, 500,240, dur, easing);
        timeline.after(delay).move(icon, 500,240, 140,240, dur, easing);
        timeline.loopForever(delay);
    }
    
    @Override
    public void update(int elapsedTime) {
        timeline.update(elapsedTime);
        
        if (Input.isMousePressed()) {
            setEasing((easingIndex + 1) % easings.length);
        }
    }
}