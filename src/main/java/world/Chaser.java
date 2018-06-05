package world;
import graphics.Renderer;
public class Chaser extends Entity {
	@Override
	public void draw(Renderer r) {
		//draw red rounded rect
		r.setColor(1f, 0f, 0f, 1f);
		// [insert matrix transformation here]
		r.drawRoundedRectangle(5, 40, 40);
	}
	@Override
	public void update(World p_World) {
		//Go after one guy only, unless they're out of range
		if(getTarget().equals(null)) {
			setTarget(p_World);
		}
		hunt8Way(20.0, p_World);
	}
}