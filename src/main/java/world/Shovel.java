package world;
import graphics.Renderer;

public class Shovel extends Entity {
	public static final int ID = 540231;
	@Override
	public void draw(Renderer r, int[] cameraOffset) {
		//TODO: draw shovel texture
	}
	@Override
	// We don't want shovels to die
	public void handleTimers(float change) {}
	public int getID() { return ID; }
}