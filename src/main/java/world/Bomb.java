package world;
import graphics.Renderer;

public class Bomb extends Entity {
	private float bombTimer = 3;
	@Override
	public void draw(Renderer r, int[] cameraOffset) {
		//TODO: draw bomb texture
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
}
