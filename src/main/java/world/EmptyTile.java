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
	public int tileId() { return ID; };
	@Override
	public void draw(Renderer r, int[] cameraOffset) {}
}
