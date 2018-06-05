package world;

import org.joml.Matrix4f;

import graphics.Renderer;

public class Stone implements Tile {
	public static final int ID = 999;
	@Override
	public void onPlayer() {
	}

	@Override
	public boolean isObstacle() {
		return true;
	}

	@Override
	public int tileId() {
		return ID;
	}

	@Override
	public void draw(Renderer r) {
		//draw dark grey rounded rect
		r.setColor(0.4f, 0.4f, 0.4f, 1f);
		r.setTransformMatrix((new Matrix4f()).translate(100.f, 0, 0));
		r.drawRoundedRectangle(3, 25, 25);
	}

}
