package world;
import org.joml.Matrix4f;

import graphics.Renderer;

public class Bomb extends Entity {
	public static final int ID = 8008;
	private float bombTimer = 3;
	@Override
	public void draw(Renderer r, int[] cameraOffset) {
		r.setColor(1f, 1f, 0f, 1f);
		r.setTransformMatrix((new Matrix4f()).translate(
				25 * (getPosition()[0] - cameraOffset[0]),
				25 * (getPosition()[1] - cameraOffset[1]), 0));
		r.drawRoundedRectangle(3, 25, 25);
	}
	@Override
	public void handleTimers(float change) {
		bombTimer -= change;
	}
	@Override
	public void update(World p_World) {
		//TODO: handleTimers, include change parameter
		if(bombTimer <= 0) {
			explode(p_World);
			kill();
		}
	}
	public void explode(World p_World) {
		//TODO: loop through entities, kill all in blast radius
		//Dig everything in 5x5
		for(int i = -2; i < 2; i++) {
			for(int j = -2; j < 2; j++) {
				dig(p_World, j, i);
			}
		}
	}
	public int getID() { return ID; }
}
