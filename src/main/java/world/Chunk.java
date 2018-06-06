package world;

import java.util.Arrays;
import java.util.Random;
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
	public void spawnEntity(Entity e, int x, int y) {
		int[] newPosition = {
			m_Offset[0] + x,
			m_Offset[1] + y
		};
		if(e != null) {
			e.setPosition(newPosition);
			m_Entities.add(e);
		}
	}
	
	public void generate(int seed) {
		Random r = new Random();
		r.setSeed(seed);
		//Step 1: Load chunk position from focus coords
		//m_Offset[0] = focus.getPosition()[0] - RowTileCount/2;
		//m_Offset[1] = focus.getPosition()[1] - RowTileCount/2; //TODO: translation
		for(int i = 0; i < RowTileCount; i++) {
			for(int j = 0; j < RowTileCount; j++) {
				double result = Math.min(Math.floor(
						ImprovedNoise.noise((m_Offset[0] + j)/32.0,
									(m_Offset[1] + i)/32.0, seed) * 128 + 128),
						255);
				System.out.println(result);
				if(result <= 140 && result > 100) {
					//Lower-level enemy spawn area
					if(result % 10 == 0) {
						//Destroyable Obstacle
						setTile(j, i, new WeedTile());
					}else {
						//Spawn enemy
						double rand = r.nextDouble();
						Entity crip = null;
						if(rand < 0.015) {
							crip = new Chaser();
						}else if(rand < 0.02) {
							crip = new Digger();
						}
						spawnEntity(crip, j, i);
					}
				}else if(result > 70) {
					//Slightly more dangerous area
					if(result % 6 == 0) {
						setTile(j, i, new WeedTile());
						if(result % 4 == 0) {
							setTile(j, i, new Stone());
						}
					}else {
						//Spawn enemy or shovel
						double rand = r.nextDouble();
						Entity crip = null;
						if(rand < 0.01) {
							crip = new Chaser();
						}else if(rand < 0.02) {
							crip = new Digger();
						}else if(rand < 0.035) {
							crip = new Shovel();
						}
						spawnEntity(crip, j, i);
					}
				}else {
					//Rewards zone
					double rand = r.nextDouble();
					Entity crip = null;
					if(rand < 0.05) {
						crip = new Shovel();
					}else if(rand < 0.06) {
						crip = new Bomb();
					}else if(rand < 0.063) {
						//TODO: Replace with harder enemy
						crip = new Chaser();
					}
					spawnEntity(crip, j, i);
				}
			}
		}
		return;
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
	public Tile setTile(int p_X, int p_Y, Object p_NewTile)
	{
		if (p_NewTile == null)
			throw new IllegalArgumentException("Null tile");
		if(p_NewTile instanceof Tile) {
			Tile oldTile = getTile(p_X, p_Y);
			m_Tiles[p_X + p_Y * RowTileCount] = (Tile)p_NewTile;
			return oldTile;
		}else if(p_NewTile instanceof Entity) {
			Entity newEntity = (Entity)p_NewTile;
			int[] newPos = {
				m_Offset[0] + p_X,
				m_Offset[1] + p_Y
			};
			newEntity.setPosition(newPos);
			return getTile(p_X, p_Y);
		}else {
			throw new IllegalArgumentException("Neither tile nor entity");
		}
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
	
	private Object createTileFromId(int p_Id)
	{
		switch(p_Id)
		{
		case EmptyTile.ID: 	return new EmptyTile();	//0
		case WeedTile.ID: 	return new WeedTile();	//1
		case Stone.ID:     	return new Stone();		//999
		case Chaser.ID:		return new Chaser();	//-1
		case Digger.ID:		return new Digger();	//-2
		case Shovel.ID:		return new Shovel();	//540231, which is shovel in 1337speak
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
