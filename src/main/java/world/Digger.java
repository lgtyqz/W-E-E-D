package world;
import org.joml.Matrix4f;

import graphics.Renderer;
public class Digger extends Entity {
	public static final int ID = -2;
	@Override
	public void draw(Renderer r, int[] cameraOffset) {
		//draw GREEN rounded rect
		r.setColor(0f, 1f, 0f, 1f);
		r.setTransformMatrix((new Matrix4f()).translate(
				25 * (getPosition()[0] - cameraOffset[0]),
				25 * (getPosition()[1] - cameraOffset[1]), 0));
		r.drawRoundedRectangle(5, 40, 40);
	}
	@Override
	public void update(World p_World) {
		// Not as hard-set as the Chaser, will go after the easiest target
		// Exploitable, however, as it's slow and two players can
		// mutually distract it.
		if(getMoveCount() % 2 == 0) {
			setTarget(p_World);
			huntOrtho(20.0, p_World);
		}else {
			for(int i = -1; i < 1; i++) {
				for(int j = -1; j < 1; j++) {
					dig(p_World, j, i);
				}
			}
			incrementMoves(); //to switch back to hunt mode
		}
	}
	public static int getID() { return ID; }
}
