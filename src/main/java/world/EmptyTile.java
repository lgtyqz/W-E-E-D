package world;

public class EmptyTile implements Tile
{
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
	public int tileId() { return 0; };
}
