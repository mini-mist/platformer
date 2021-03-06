package net.halfbolt.platformer;

import com.badlogic.gdx.InputProcessor;
import net.halfbolt.platformer.render.Render;

public class InputHandler implements InputProcessor {
    private Render render;

    public InputHandler(Render render) {
        this.render = render;
    }

    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        render.touchDown(x, y, pointer);
        return false;
    }

    public boolean touchUp(int x, int y, int pointer, int button) {
        render.touchUp(x, y, pointer);
        return false;
    }

    public boolean touchDragged(int x, int y, int pointer) {
        render.touchDragged(x, y, pointer);
        return false;
    }

    public boolean mouseMoved(int x, int y) {
        return false;
    }

    public boolean scrolled(int amount) {
        return false;
    }
}