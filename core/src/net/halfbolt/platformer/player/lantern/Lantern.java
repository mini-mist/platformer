package net.halfbolt.platformer.player.lantern;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import net.halfbolt.platformer.player.Player;
import net.halfbolt.platformer.world.LevelManager;
import net.halfbolt.platformer.world.tilemap.tile.Tile;

public class Lantern {
    private Player player;
    private LevelManager w;
    private Body body;
    private BodyDef bodyDef;

    public Lantern(LevelManager w, Player player) {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(player.getPos());

        body = w.getWorld().createBody(bodyDef);
        body.setLinearDamping(8f);
        body.setAngularDamping(5f);

        CircleShape circle = new CircleShape();
        circle.setRadius(0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.filter.categoryBits = player.getBits();
        fixtureDef.filter.maskBits = Tile.tileBits;

        body.createFixture(fixtureDef);

        circle.dispose();

        PointLight light = new PointLight(w.getRender().getLights(), 300, Color.ORANGE, 15, 10, 10);
        light.attachToBody(body);
        light.setSoft(false);
        this.player = player;
        this.w = w;

    }

    public void update() {
        //System.out.println(body.getPosition());
        Vector2 delta = w.getRender().getGui().getControl().getLanternDelta(getPos());
        Vector3 screenPos3 = w.getRender().getCamera().project(new Vector3(getPos().x, getPos().y, 0));
        Vector2 screenPos = new Vector2(screenPos3.x, screenPos3.y);
        if (w.getRender().getGui().getControl().bowButton != null) {
            if (!w.getRender().getGui().getControl().bowButton.inBounds(w.getRender().getGui().touchPos) && !w.getRender().getGui().getControl().moveTouchpad.inBounds(w.getRender().getGui().touchPos)) {
                if (screenPos.x < 0) {
                    delta.set(new Vector2(20f, 0));
                }
                if (screenPos.x > Gdx.graphics.getWidth()) {
                    delta.set(new Vector2(-20f, 0));
                }
                if (screenPos.y < 0) {
                    delta.set(new Vector2(0, -20f));
                }
                if (screenPos.y > Gdx.graphics.getHeight()) {
                    delta.set(new Vector2(0, 20f));
                }
            } else {
                delta = new Vector2();
            }
        } else {
            if (screenPos.x < 0) {
                delta.set(new Vector2(20f, 0));
            }
            if (screenPos.x > Gdx.graphics.getWidth()) {
                delta.set(new Vector2(-20f, 0));
            }
            if (screenPos.y < 0) {
                delta.set(new Vector2(0, -20f));
            }
            if (screenPos.y > Gdx.graphics.getHeight()) {
                delta.set(new Vector2(0, 20f));
            }
        }
        //stop lantern from going off screen
        if (screenPos.x < Gdx.graphics.getWidth() / 8f) {
            delta.add(new Vector2(20f, 0));
        }
        if (screenPos.x > Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 8f) {
            delta.add(new Vector2(-20f, 0));
        }
        if (screenPos.y < Gdx.graphics.getHeight() / 8f) {
            delta.add(new Vector2(0, -20f));
        }
        if (screenPos.y > Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 8f) {
            delta.add(new Vector2(0, 20f));
        }
        //apply force
        body.applyForceToCenter(delta, true);
    }

    public void lanternFlash () {
        //TODO: make lantern flash when it stuns an enemy
    }

    public void render() {
    }

    public void dispose() {
    }

    public Vector2 getPos() {
        return body.getPosition();
    }

    public void resetPos() {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(player.getPos());

        body = w.getWorld().createBody(bodyDef);
        body.setLinearDamping(8f);
        body.setAngularDamping(5f);

        CircleShape circle = new CircleShape();
        circle.setRadius(0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.filter.categoryBits = Player.playerBits;
        fixtureDef.filter.maskBits = Tile.tileBits;

        body.createFixture(fixtureDef);

        circle.dispose();
    }
}
