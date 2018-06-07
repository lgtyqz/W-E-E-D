package world;
import graphics.Renderer;
import graphics.Texture;
import org.joml.Matrix4f;

public class Shovel extends Entity {
	public static final int ID = 540231;
	//Texture shovelSprite = new Texture("images/shovel.png");
	@Override
	public void draw(Renderer r, int[] cameraOffset) {
		/*r.setTransformMatrix((new Matrix4f()).translate(
				getPosition()[0] - cameraOffset[0],
				getPosition()[1] - cameraOffset[1], 0).scale(25));
		r.drawImage(shovelSprite);*/
		
		//For now until the above image-drawing code works
		r.setColor(1f, 1f, 0f, 1f);
		r.setTransformMatrix((new Matrix4f()).translate(
				25 * (getPosition()[0] - cameraOffset[0]) + 5,
				25 * (getPosition()[1] - cameraOffset[1]) + 5, 0));
		r.drawRoundedRectangle(5, 15, 15);
	}
	@Override
	// We don't want shovels to die
	public void handleTimers(float change) {}
	public int getID() { return ID; }
}