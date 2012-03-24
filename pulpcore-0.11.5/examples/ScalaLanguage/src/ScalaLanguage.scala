// Drag the blue block around
// Shows how to use functions, traits, and animation in Scala.
import pulpcore.animation.Easing._
import pulpcore.image.Colors
import pulpcore.scene.Scene2D
import pulpcore.sprite._
import pulpcore.Input
import PulpCore._

class ScalaLanguage extends Scene2D {
  
    override def load = {
        add(new FilledSprite(Colors.BLACK))

        // Function binding
        val leftPaddle = new ImageSprite("Block1.png", 0, 0)
        leftPaddle.setAnchor(Sprite.CENTER)
        leftPaddle.x.bindTo(() => Math.pow(leftPaddle.y - 240, 2) / 1000 + 64)
        leftPaddle.y.bindTo(() => Input.getMouseY * 0.73 + 64)
        add(leftPaddle)

        // Timeline animation
        val rightPaddle = new ImageSprite("Block2.png", 575, 0)
        rightPaddle.setAnchor(Sprite.CENTER)
        val timelineY = Timeline(
            rightPaddle.y -> (64 to 416 dur 1500),
            rightPaddle.y -> (416 to 64 dur 1500 delay 1500))
        timelineY.loopForever()
        addTimeline(timelineY)
        add(rightPaddle)

        // Traits
        val middleBlock = new ImageSprite("Block3.png", 320, 0) with Draggable
        middleBlock.setAnchor(Sprite.SOUTH)
        add(middleBlock)

        // Simple animation
        middleBlock.y := 0 to 480 dur 1000 ease REGULAR_IN
    }
}

trait Draggable extends Sprite {
    var dragging = false
    var deltaX = 0.0
    var deltaY = 0.0

    setCursor(Input.CURSOR_MOVE)

    override def update(elapsedTime:Int) = {
        super.update(elapsedTime)

        // Check if the mouse was pressed on this Sprite,
        // and this Sprite is the top-most Sprite under the mouse.
        if (super.isMousePressed && super.isPick(Input.getMouseX, Input.getMouseY)) {
            dragging = true
            deltaX = Input.getMouseX - this.x.get
            deltaY = Input.getMouseY - this.y.get
            getParent.moveToTop(this)
        }
        // Check if the mouse was released anywhere in the Scene.
        if (Input.isMouseReleased) {
            dragging = false
        }
        if (dragging && Input.isMouseMoving) {
            setLocation(Input.getMouseX-deltaX, Input.getMouseY-deltaY)
        }
    }
}

