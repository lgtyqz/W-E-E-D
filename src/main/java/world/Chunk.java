package world;

import java.util.Arrays;

import org.joml.Matrix4f;

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
		fillBlankTiles();
		m_Entities = new ArrayList<Entity>();
	}
	
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
		if (p_NewTile == null)
			throw new IllegalArgumentException("Null tile");
		Tile oldTile = getTile(p_X, p_Y);
		m_Tiles[p_X + p_Y * RowTileCount] = p_NewTile;
		return oldTile;
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
	
	public Tile IDtoTile(int id) {
		Tile mail;
		switch(id) {
		case 0:
			mail =  new EmptyTile();
		break;
		case 1:
			mail =  new WeedTile();
		break;
		case 999:
			mail = new Stone();
		break;
		default:
			mail = new EmptyTile();
		}
		return mail;
	}
	
	public void draw(Renderer r, int[] cameraOffset) {
		for(int i = 0; i < RowTileCount; i++) {
			for(int j = 0; j < RowTileCount; j++) {
				r.setTransformMatrix((new Matrix4f()).translate(
						25 * (j - cameraOffset[0]), 25 * (i - cameraOffset[1]), 0));
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
