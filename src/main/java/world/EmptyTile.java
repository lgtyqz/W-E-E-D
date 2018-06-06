package world;
import graphics.Renderer;
public class EmptyTile implements Tile
{
	public static final int ID = 0;
	@Override
	public void onPlayer()
	{
		// Does nothing...
		// TODO: Makes it do something
	}

	@Override
	public boolean isObstacle()
	{
		return false;
	}
	@Override
	public int getId() { return ID; };
	@Override
	public void draw(Renderer r, int[] cameraOffset)
	{
		r.setColor(0.6f, 0.6f, 0.6f, 1f);
		r.drawRoundedRectangle(3, 25, 25);
	}
}
