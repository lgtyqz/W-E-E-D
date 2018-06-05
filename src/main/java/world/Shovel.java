package world;
import graphics.Renderer;

public class Shovel extends Entity {
	@Override
	public void draw(Renderer r) {
		//TODO: draw shovel texture
	}
	@Override
	// We don't want shovels to die
	public void handleTimers(float change) {}
}