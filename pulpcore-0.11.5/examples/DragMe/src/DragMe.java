// Drag the blocks around
import pulpcore.Input;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Sprite;
import pulpcore.image.Colors;

public class DragMe extends Scene2D {

    @Override
    public void load() {
        add(new FilledSprite(Colors.BLACK));
        add(new DraggableSprite("Block1.png", 0, 0));
        add(new DraggableSprite("Block2.png", 510, 0));
        add(new DraggableSprite("Block3.png", 510, 350));
        add(new DraggableSprite("Block4.png", 0, 350));
    }

    public static class DraggableSprite extends ImageSprite {

        private boolean dragging = false;
        private double deltaX = 0.0;
        private double deltaY = 0.0;

        public DraggableSprite(String image, int x, int y) {
            super(image, x, y);
            alpha.set(200); // So you can see which one is on top
        }

        @Override
        public void update(int elapsedTime) {
            super.update(elapsedTime);

            // Check if the mouse was pressed on this Sprite,
            // and this Sprite is the top-most Sprite under the mouse.
            if (super.isMousePressed() && super.isPick(Input.getMouseX(), Input.getMouseY())) {
                dragging = true;
                deltaX = Input.getMouseX() - this.x.get();
                deltaY = Input.getMouseY() - this.y.get();
                getParent().moveToTop(this);
            }
            // Check if the mouse was released anywhere in the Scene.
            if (Input.isMouseReleased()) {
                dragging = false;
            }
            if (dragging && Input.isMouseMoving()) {
                setLocation(Input.getMouseX() - deltaX, Input.getMouseY() - deltaY);
            }
        }
    }
}
