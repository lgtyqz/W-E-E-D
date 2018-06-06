package world;

import java.util.Arrays;

import org.joml.Matrix4f;

import java.io.Serializable;
import java.util.ArrayList;
import graphics.Renderer;
/*
 * Chunks represent a section in the world.
 */
public class Chunk
{
	private Tile[] m_Tiles;
	private ArrayList<Entity> m_Entities; //For holding players & enemies
	private int[] m_Offset = { 0, 0 }; // To know which chunk it is
	
	/*
	 * Amount of tiles in one row (or one column).
	 */
	public static final int RowTileCount = 16;
	
	/*
	 *  Total amount of tiles a chunk will have.
	 */
	public static final int TileCount = RowTileCount * RowTileCount;
	
	public Chunk()
	{
		m_Tiles = new Tile[TileCount];
		fillEmptyTiles();
		m_Entities = new ArrayList<Entity>();
	}
	
	public void serverInitialization(Player focus) {
		//Step 1: Load chunk position from focus coords
		m_Offset = focus.getPosition();
	}
	
	/*
	 * Fill the entire chunk with empty tiles.
	 */
	public void fillEmptyTiles()
	{
		for (int i = 0; i < m_Tiles.length; i++)
			m_Tiles[i] = new EmptyTile();
	}
	
	/*
	 * Replace a tile at local position.
	 */
	public Tile setTile(int p_X, int p_Y, Tile p_NewTile)
	{
		if (p_NewTile == null)
			throw new IllegalArgumentException("Null tile");
		Tile oldTile = getTile(p_X, p_Y);
		m_Tiles[p_X + p_Y * RowTileCount] = p_NewTile;
		return oldTile;
	}
	
	/*
	 * Set a tile by id.
	 */
	public Tile setTile(int p_X, int p_Y, int p_NewTileID)
	{
		return setTile(p_X, p_Y, createTileFromId(p_NewTileID));
	}

	/*
	 * Get a tile at local position.
	 */
	public Tile getTile(int p_X, int p_Y)
	{
		if (p_X < 0 || p_X >= RowTileCount || p_Y < 0 || p_Y >= RowTileCount)
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
	
	private Tile createTileFromId(int p_Id)
	{
		switch(p_Id)
		{
		case EmptyTile.ID: return new EmptyTile();
		case WeedTile.ID:  return new WeedTile();
		case Stone.ID:     return new Stone();
		default:           return null;
		}
	}
	
	public void draw(Renderer r, int[] cameraOffset) {
		for(int i = 0; i < RowTileCount; i++) {
			for(int j = 0; j < RowTileCount; j++) {
				r.setTransformMatrix((new Matrix4f()).translate(
						25 * (j - cameraOffset[0] + m_Offset[0]),
						25 * (i - cameraOffset[1] + m_Offset[1]), 0));
				Tile tileAt = getTile(j, i);
				if(!(tileAt instanceof EmptyTile)) {
					tileAt.draw(r, cameraOffset);
				}
			}
		}
		for(int i = 0; i < m_Entities.size(); i++) {
			m_Entities.get(i).draw(r, cameraOffset);
		}
	}
}
