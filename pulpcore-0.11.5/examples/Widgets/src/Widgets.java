// Widgets
// Shows UI Components
import pulpcore.animation.Easing;
import pulpcore.image.CoreFont;
import pulpcore.image.CoreImage;
import pulpcore.Input;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.Button;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.Group;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Label;
import pulpcore.sprite.Slider;
import pulpcore.sprite.Sprite;
import pulpcore.sprite.StretchableSprite;
import pulpcore.sprite.TextField;
import pulpcore.Stage;
import static pulpcore.image.Colors.*;

public class Widgets extends Scene2D {
    
    Label answer;
    TextField textField;
    TextField passwordField;
    Button okButton;
    Button checkbox;
    Group form;
    
    @Override
    public void load() {
        CoreFont font = CoreFont.getSystemFont().tint(WHITE);
        
        // Create the form fields
        Label label = new Label(font, "Name: ", 225, 50);
        label.setAnchor(1, 0.5);
        
        textField = new TextField("Suzy", 230, 50, 150, font.getHeight());
        textField.setAnchor(0, 0.5);
        textField.setFocus(true);
        
        Label label2 = new Label(font, "Secret Password: ", 225, 90);
        label2.setAnchor(1, 0.5);
        
        passwordField = new TextField(230, 90, 150, font.getHeight());
        passwordField.setPasswordMode(true);
        passwordField.setAnchor(0, 0.5);
        
        Slider slider = new Slider("slider.png", "slider-thumb.png", 225, 130);
        slider.setAnchor(0, 0.5);
        Label label3 = new Label(font, "Value: %d ", 225, 130);
        label3.setFormatArg(slider.value);
        label3.setAnchor(1, 0.5);
        
        CoreImage checkboxImage = CoreImage.load("checkbox.png");
        checkbox = Button.createLabeledToggleButton(checkboxImage.split(3,2), font,
            "I'm feeling slanted", 225, 180, 30, 12, 0.0, 0.5, false);
        checkbox.setCursor(Input.CURSOR_DEFAULT);
        checkbox.setPixelLevelChecks(false);
        checkbox.setAnchor(0, 0.5);
        
        CoreImage buttonImage = CoreImage.load("button.png");
        okButton = new Button(buttonImage.split(3), 225, 210);
        okButton.setAnchor(0.5, 0);
        okButton.setKeyBinding(Input.KEY_ENTER);
        
        answer = new Label(font, "", 225, 285);
        answer.setAnchor(0.5, 0.5);
        
        // Add the form fields to a group
        form = new Group(Stage.getWidth() / 2, Stage.getHeight() / 2, 480, 320);
        form.setAnchor(0.5, 0.5);
        form.add(new StretchableSprite("border.9.png", 0, 0, 480, 320));
        form.add(label);
        form.add(createTextFieldBackground(textField));
        form.add(textField);
        form.add(label2);
        form.add(createTextFieldBackground(passwordField));
        form.add(passwordField);
        form.add(label3);
        form.add(slider);
        form.add(okButton);
        form.add(checkbox);
        form.add(answer);
        
        // Add background and form to the scene
        add(new FilledSprite(BLACK));
        addLayer(form);
    }
    
    public Sprite createTextFieldBackground(TextField field) {
        field.selectionColor.set(rgb(0x1d5ef2));
        ImageSprite background = new ImageSprite("textfield.png", field.x.get()-5, field.y.get());
        background.setAnchor(0, 0.5);
        return background;
    }
    
    @Override
    public void update(int elapsedTime) {
        if (checkbox.isClicked()) {
            double newAngle = checkbox.isSelected() ? Math.PI/16 : 0;
            form.angle.animateTo(newAngle, 500, Easing.ELASTIC_OUT);
        }
        if (okButton.isClicked()) {
            answer.setText("Hello, " + textField.getText() + "!");
            double w = answer.width.get();
            double h = answer.height.get();
            answer.scale(w*.1, h*.1, w, h, 200, Easing.BACK_OUT);
        }
        if (Input.isPressed(Input.KEY_TAB)) {
            if (textField.hasFocus()) {
                textField.setFocus(false);
                passwordField.setFocus(true);
            }
            else {
                textField.setFocus(true);
                passwordField.setFocus(false);
            }
        }
    }
}