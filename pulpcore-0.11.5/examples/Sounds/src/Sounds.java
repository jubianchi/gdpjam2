// Sounds
// Plays sounds with level and pan animation.
// Try clicking near the ear and far from the ear.
import pulpcore.animation.Fixed;
import pulpcore.CoreSystem;
import static pulpcore.image.Colors.*;
import pulpcore.image.CoreImage;
import pulpcore.Input;
import pulpcore.scene.Scene2D;
import pulpcore.sound.Sound;
import pulpcore.sprite.Button;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.ImageSprite;
import pulpcore.Stage;

public class Sounds extends Scene2D {
    
    Sound boopSound, wooshSound;
    ImageSprite ear;
    Button muteButton;
    
    @Override
    public void load() {
        ear = new ImageSprite("ear.png", 300, 180);
        muteButton = new Button(CoreImage.load("mute.png").split(3,2), 600, 440, true);
        muteButton.setSelected(!CoreSystem.isMute());
        muteButton.setPixelLevelChecks(false);
        add(new FilledSprite(WHITE));
        add(ear);
        add(muteButton);
        setCursor(Input.CURSOR_CROSSHAIR);
        ear.setCursor(Input.CURSOR_HAND);
        
        boopSound = Sound.load("boop.wav");
        wooshSound = Sound.load("stereo.wav");
        wooshSound.play();
    }
    
    @Override
    public void update(int elapsedTime) {
        if (muteButton.isClicked()) {
            CoreSystem.setMute(!muteButton.isSelected());
        }
        if (ear.isMousePressed()) {
            // Animate from left to right speaker
            Fixed level = new Fixed(1);
            Fixed pan = new Fixed(-1);
            pan.animateTo(1, 500);
            
            wooshSound.play(level, pan);
        }
        if (Input.isMousePressed() && !ear.isMouseOver() && !muteButton.isMouseOver()) {
            // Set the pan of the sound based on the mouse position
            int x = Input.getMousePressX();
            double w = Stage.getWidth() / 2;
            Fixed level = new Fixed(1);
            Fixed pan = new Fixed((x - w) / w);
            
            boopSound.play(level, pan);
        }
    }
}
