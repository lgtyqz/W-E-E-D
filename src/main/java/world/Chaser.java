package world;
import org.joml.Matrix4f;

import graphics.Renderer;
public class Chaser extends Entity {
	public static final int ID = -1;
	@Override
	public void draw(Renderer r, int[] cameraOffset) {
		//draw red rounded rect
		r.setColor(1f, 0f, 0f, 1f);
		r.setTransformMatrix((new Matrix4f()).translate(
				25 * (getPosition()[0] - cameraOffset[0]),
				25 * (getPosition()[1] - cameraOffset[1]), 0));
		r.drawRoundedRectangle(3, 25, 25);
	}
	@Override
	public void update(World p_World) {
		//Go after one guy only, unless they're out of range
		if(getTarget().equals(null)) {
			setTarget(p_World);
		}
		hunt8Way(20.0, p_World);
	}
	public static int getID() { return ID; }
}