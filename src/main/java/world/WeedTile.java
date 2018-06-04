package world;
import org.joml.Matrix4f;

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
	public int tileId() { return ID; }
	
	@Override
	public void draw(Renderer r) {
		//draw dark green rounded rect
		r.setColor(0f, 0.25f, 0f, 1f);
		r.setTransformMatrix((new Matrix4f()).translate(100.f, 0, 0));
		r.drawRoundedRectangle(3, 25, 25);
	}
}
