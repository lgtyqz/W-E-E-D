package world;

import java.util.Arrays;

/*
 * Chunks represent a section in the world.
 */
public class Chunk
{
	private Tile[] m_Tiles;
	private int[] m_Offset = { 0, 0 };
	
	/*
	 * Amount of tiles in one row (or one column).
	 */
	public static final int RowTileCount = 16;
	
	/*
	 *  Total amount of tiles a chunk will have.
	 */
	public static final int TileCount = RowTileCount * RowTileCount;
	
	/*
	 * Fill the entire chunk with empty tiles.
	 */
	public void fillBlankTiles()
	{
		m_Tiles = new EmptyTile[TileCount];
	}
	
	/*
	 * Replace a tile at local position.
	 */
	public Tile setTile(int p_X, int p_Y, Tile p_NewTile)
	{
		Tile oldTile = getTile(p_X, p_Y);
		m_Tiles[p_X + p_Y * RowTileCount] = p_NewTile;
		return oldTile;
	}

	/*
	 * Get a tile at local position.
	 */
	public Tile getTile(int p_X, int p_Y)
	{
		if (p_X < 0 || p_X >= 16 || p_Y < 0 || p_Y >= 16)
			throw new IllegalArgumentException("Invalid Tile Position");
		return m_Tiles[p_X + p_Y * RowTileCount];
	}
	
	public void setOffset(int p_X, int p_Y)
	{
		m_Offset[0] = p_X;
		m_Offset[1] = p_Y;
	}
	
	public int[] getOffset()
	{
		return Arrays.copyOf(m_Offset, m_Offset.length);
	}
}
