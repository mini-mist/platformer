package net.halfbolt.platformer.player.controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Touchpad {
    private SpriteBatch batch;
    private Texture backgroundTex;
    private Texture knobTex;
    private Vector2 knobPos;
    private Vector2 pos;
    private final Vector2 startingPos;
    private double size;
    private float knobSize;
    private final boolean fixedPos = false;
    private Rectangle boundingBox;
    private int cursor = -1;

    public Touchpad(SpriteBatch batch, String backgroundPath, String knobPath, Vector2 pos, double size, float knobSize, Rectangle boundingBox) {
        this.batch = batch;
        this.backgroundTex = new Texture(backgroundPath);
        this.knobTex = new Texture(knobPath);
        this.pos = pos.cpy();
        this.knobPos = pos.cpy();
        this.startingPos = pos.cpy();
        this.size = size;
        this.knobSize = knobSize;
        this.boundingBox = boundingBox;
    }

    public void render() {
        //System.out.println(knobPos.dst(startingPos) > 10);
        batch.begin();
        batch.draw(backgroundTex,
                (float) (pos.x - size / 2), (float) (pos.y - size / 2),
                (float) size, (float) size,
                0, 0,
                backgroundTex.getWidth(), backgroundTex.getHeight(),
                false, true);
        batch.draw(knobTex,
                knobPos.x, knobPos.y,
                knobSize, knobSize,
                0, 0,
                knobTex.getWidth(), knobTex.getHeight(),
                false, true);
        batch.end();
    }

    public double getX() {
        return (knobPos.x - pos.x) / size * 2;
    }

    public double getY() {
        return (knobPos.y - pos.y) / size * 2;
    }

    public void touchDragged(int x, int y, int cursor) {
        if (this.cursor != cursor) {
            return;
        }
        double angle = Math.atan((x - pos.x) / (y - pos.y));
        double dist = Math.sqrt(Math.pow(x - pos.x, 2) + Math.pow(y - pos.y, 2));
        if (dist < size / 2) {
            knobPos.x = x;
            knobPos.y = y;
        } else {
            if (y < pos.y) {
                knobPos.x = (float) (Math.sin(angle + Math.PI) * size / 2 + pos.x);
                knobPos.y = (float) (Math.cos(angle + Math.PI) * size / 2 + pos.y);
            } else {
                knobPos.x = (float) (Math.sin(angle) * size / 2 + pos.x);
                knobPos.y = (float) (Math.cos(angle) * size / 2 + pos.y);
            }
        }
    }

    public void touchDown(int x, int y, int cursor) {
        if (this.cursor != -1) {
            return;
        }
        if (!boundingBox.contains(new Vector2(x, y))) {
            return;
        }
        System.out.println("Touch down, with cursor " + cursor + ", and x " + x + ", y " + y);
        this.cursor = cursor;
        if (!fixedPos) {
            pos.x = x;
            pos.y = y;
        }
    }

    public void touchUp(int x, int y, int cursor) {
        if (this.cursor == cursor) {
            this.cursor = -1;
        } else {
            return;
        }
        pos = startingPos.cpy();
        knobPos = pos.cpy();
    }

    public boolean isPressed () {
        return (knobPos.dst(startingPos) > 10);
    }

    //for bow button, check if player is auto-aiming or not
    public boolean autoAim () {
        return (knobPos.dst(pos) < 20);
    }

    public void dispose() {
        backgroundTex.dispose();
        knobTex.dispose();
    }

    public int getCursor() {
        return cursor;
    }
}