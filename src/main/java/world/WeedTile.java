package world;

import graphics.Renderer;

public class WeedTile implements Tile {
	public static final int ID = 1;
	@Override
	public void onPlayer() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isObstacle() {
		//No walking through the weeds
		return true;
	}
	public int getId() { return ID; }
	
	@Override
	public void draw(Renderer r, int[] cameraOffset) {
		//draw dark green rounded rect
		r.setColor(0f, 0.25f, 0f, 1f);
		r.drawRoundedRectangle(3, 25, 25);
	}
}
