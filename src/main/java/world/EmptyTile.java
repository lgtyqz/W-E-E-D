package world;

public class EmptyTile implements Tile
{
	@Override
	public void onPlayer()
	{
		// Does nothing...
	}

	@Override
	public boolean canPass()
	{
		return true;
	}
}
