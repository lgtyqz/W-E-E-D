package world;

import java.util.ArrayList;
import graphics.Renderer;
import network.RemoteWorldConnection;

public class World
{
	private ArrayList<Chunk> m_Chunks;
	private ArrayList<Entity> m_Entities;
	private int[] cameraOffset = {0, 0};
	private Player focus;
	private RemoteWorldConnection m_Remote;
	
	/*
	 * TODO: Returns tile in world position.
	 */
	public World() {
		m_Remote = null;
		m_Chunks = new ArrayList<Chunk>();
		m_Entities = new ArrayList<Entity>();
	}
	
	public void setFocus(Player p_Focus)
	{
		focus = p_Focus;
	}
	
	public void setRemote(RemoteWorldConnection p_Remote)
	{
		m_Remote = p_Remote;
	}
	
	public void testInitialization() {
		
	}
	
	public int[] getChunkOffsetFromTilePosition(int p_X, int p_Y) // mMMMmmm big names
	{
		int x = (int)(Math.floor((double)p_X/Chunk.RowTileCount)*Chunk.RowTileCount);
		int y = (int)(Math.floor((double)p_Y/Chunk.RowTileCount)*Chunk.RowTileCount);
		return new int[] { x, y };
	}
	
	public Chunk getChunk(int p_X, int p_Y)
	{
		int[] offset = getChunkOffsetFromTilePosition(p_X, p_Y);
		for (Chunk i : m_Chunks)
			if (i.getOffset()[0] == offset[0]
					&& i.getOffset()[1] == offset[1])
				return i;
		return null;
	}
	
	public Chunk setChunk(Chunk p_Chunk)
	{
		Chunk prevChunk = getChunk(p_Chunk.getOffset()[0], p_Chunk.getOffset()[1]);
		if (prevChunk != null)
			m_Chunks.remove(prevChunk);
		m_Chunks.add(p_Chunk);
		return prevChunk;
	}
	
	public Chunk ensureChunkExistence(int p_X, int p_Y)
	{
		Chunk chunk = getChunk(p_X, p_Y);
		if (chunk == null)
		{
			if (m_Remote != null)
				m_Remote.makeChunkRequest(p_X, p_Y);
			else
			{
				// TODO: Add world generation here.
				chunk = new Chunk();
				int[] offset = getChunkOffsetFromTilePosition(p_X, p_Y);
				chunk.setOffset(offset[0], offset[1]);
				m_Chunks.add(chunk);
			}
		}
		return chunk;
	}
	
	public Tile getTile(int p_X, int p_Y)
	{
		return null;
	}
	public Tile setTile(int p_X, int p_Y, Tile p_Tile)
	{
		return null;
	}
	public Tile setTile(int p_X, int p_Y, int p_TileId)
	{
		return null;
	}
	public void draw(Renderer r, int width, int height) {
		for(Chunk i : m_Chunks) { i.draw(r, cameraOffset); }
		
		if (focus != null)
			focus.draw(r, cameraOffset);
		//TODO: adjust camera position
		//cameraOffset[0] = focus.getPosition()[0];
		for(Entity i : m_Entities) { i.draw(r, cameraOffset); }
	}
}
