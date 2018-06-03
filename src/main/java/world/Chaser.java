package world;
import graphics.Renderer;
public class Chaser extends Entity {
	//whatever player is currently being targeted by the creature
	@Override
	public void draw(Renderer r, World p_World) {
		//draw red rounded rect
		r.setColor(1f, 0f, 0f, 1f);
		// [insert matrix transformation here]
		r.drawRoundedRectangle(5, 40, 40);
		setTarget(p_World);
		hunt8Way(20.0, p_World);
	}
}