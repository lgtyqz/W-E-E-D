package world;

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
	public int getId() {
		return ID;
	}

	@Override
	public void draw(Renderer r, int[] cameraOffset) {
		//draw dark grey rounded rect
		r.setColor(0.4f, 0.4f, 0.4f, 1f);
		r.drawRoundedRectangle(3, 25, 25);
	}

}
