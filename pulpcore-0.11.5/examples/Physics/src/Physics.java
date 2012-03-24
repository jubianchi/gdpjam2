// Click to add a ball. Press R to reset
//
// Physics example using JBox2D, the Java port of Erin Catto's Box2D.
// www.jbox2d.org
// www.box2d.org
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import pulpcore.Input;
import pulpcore.animation.BindFunction;
import pulpcore.image.Colors;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Sprite;

public class Physics extends Scene2D {
    // JBox2D recommends objects should be in size from 0.1 to 10.
    // If the world is 1/10th the size of the stage, objects can be as small as 1x1 pixels and
    // as large as 100x100 pixels. Adjust the scale for your own needs.
    private static final float WORLD_TO_STAGE_SCALE = 10;

    private World world;
    private float time;
    private float dt = 1 / 60f;
    private float ballRadius = 31 / WORLD_TO_STAGE_SCALE;

    @Override
    public void load() {
        add(new FilledSprite(Colors.BLACK));

        world = new World(new AABB(new Vec2(0,0), new Vec2(64,48)),
            new Vec2(0.0f, 40), true);
        time = 0;

        createBridge("BridgePiece.png");

        // Add a ball
        Sprite ball = new ImageSprite("Ball.png", 0, 0);
        Body ballBody = createBallBody(new Vec2(32, 8), ballRadius);
        bindSpriteToBody(ball, ballBody);
        ball.setTag(ballBody);
        add(ball);
    }

    @Override
    public void update(int elapsedTime) {
        // Update Physics.
        // This can be improved, see http://gafferongames.com/game-physics/fix-your-timestep/
        time += (elapsedTime / 1000f);
        while (time >= dt) {
            world.step(dt, 10);
            time -= dt;
        }

        if (Input.isPressed(Input.KEY_R)) {
            reload();
        }
        if (Input.isMousePressed()) {
            int mouseX = Input.getMousePressX();
            int mouseY = Input.getMousePressY();
            Sprite s = getMainLayer().pick(mouseX, mouseY);
            if (s != null && s.getTag() instanceof Body) {
                // Remove an old ball
                s.removeFromParent();
                world.destroyBody((Body)s.getTag());
            }
            else {
                float x = mouseX / WORLD_TO_STAGE_SCALE;
                float y = mouseY / WORLD_TO_STAGE_SCALE;

                // Check if the position in valid
                if (world.inRange(new AABB(new Vec2(x-1,y-1), new Vec2(x+1, y+1)))) {
                    // Create a new ball and bind the view (Sprite) to the data (Body)
                    Sprite ball = new ImageSprite("Ball.png", 0, 0);
                    Body ballBody = createBallBody(new Vec2(x, y), ballRadius);
                    bindSpriteToBody(ball, ballBody);
                    ball.setTag(ballBody);
                    add(ball);
                }
            }
        }
    }

    private void bindSpriteToBody(Sprite sprite, final Body body) {
        sprite.setAnchor(0.5, 0.5);
        sprite.x.bindTo(new BindFunction() {
            public Number f() {
                return body.getPosition().x * WORLD_TO_STAGE_SCALE;
            }
        });
        sprite.y.bindTo(new BindFunction() {
            public Number f() {
                return body.getPosition().y * WORLD_TO_STAGE_SCALE;
            }
        });
        sprite.angle.bindTo(new BindFunction() {
            public Number f() {
                return body.getAngle();
            }
        });
    }

    private void addSpriteForBody(String imageName, Body body) {
        Sprite sprite = new ImageSprite(imageName, 0, 0);
        bindSpriteToBody(sprite, body);
        add(sprite);
    }

    private Body createBallBody(Vec2 pos, float radius) {
        CircleDef cd = new CircleDef();
        cd.radius = radius;
        cd.density = 2.0f;
        cd.friction = 0.2f;
        BodyDef bd = new BodyDef();
        bd.position.set(pos);
        Body body = world.createBody(bd);
        body.createShape(cd);
        body.setMassFromShapes();
        return body;
    }

    private void createBridge(String imageName) {

        PolygonDef sd = new PolygonDef();
        sd.setAsBox(0.6f, 0.2f);
        sd.density = 60.0f;
        sd.friction = 0.2f;

        RevoluteJointDef jd = new RevoluteJointDef();
        int numPlanks = 40;

        Body prevBody = null;
        Body firstBody = null;
        for (int i = 0; i < numPlanks; i++) {
            BodyDef bd = new BodyDef();
            bd.position.set(3f + 1.5f * i, 32f);
            Body body = world.createBody(bd);
            body.createShape(sd);

            addSpriteForBody(imageName, body);

            if (prevBody == null) {
                firstBody = body;
            }
            else {
                if (i != numPlanks - 1) {
                    body.setMassFromShapes();
                }
                Vec2 anchor = new Vec2(2.4f + 1.5f * i, 32f);
                jd.initialize(prevBody, body, anchor);
                world.createJoint(jd);
            }

            prevBody = body;
        }

        Vec2 anchor = new Vec2(2.4f + 1.5f * numPlanks, 32f);
        jd.initialize(prevBody, firstBody, anchor);
        world.createJoint(jd);
    }
}
