package world;
import graphics.Renderer;
/*
 * Represents a single tile.
 */
public interface Tile
{
	/*
	 * Called when the player moves to this tile.
	 */
	public void onPlayer(/* TODO: Add params for player modification */);
	
	/*
	 * Player will be unable to move to this tile if this returns true.
	 */
	public boolean isObstacle();
	public int getId();
	public void draw(Renderer r, int[] cameraOffset);
}
