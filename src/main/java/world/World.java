package world;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import graphics.Renderer;
import network.RemoteWorldConnection;
import util.Util;

public class World
{
	private ArrayList<Chunk> m_Chunks;
	private ArrayList<Entity> m_Entities;
	private int[] cameraOffset = {0, 0};
	private Player focus;
	private RemoteWorldConnection m_Remote;
	private int m_Seed;
	
	public World()
	{
		m_Remote = null;
		m_Chunks = new ArrayList<Chunk>();
		m_Entities = new ArrayList<Entity>();
	}
	
	public void setSeed(String p_SeedStr)
	{
		m_Seed = p_SeedStr.hashCode();
	}
	
	public void setFocus(Player p_Focus)
	{
		focus = p_Focus;
	}
	
	/*
	 * This makes this world rely on a server for chunk data and
	 * entity updates.
	 */
	public void setRemote(RemoteWorldConnection p_Remote)
	{
		m_Remote = p_Remote;
	}
	
	public int[] getChunkOffsetFromTilePosition(int p_X, int p_Y) // mMMMmmm big names
	{
		int x = (int)(Math.floor((double)p_X/Chunk.RowTileCount)*Chunk.RowTileCount);
		int y = (int)(Math.floor((double)p_Y/Chunk.RowTileCount)*Chunk.RowTileCount);
		return new int[] { x, y };
	}
	
	public synchronized Chunk getChunk(int p_X, int p_Y)
	{
		int[] offset = getChunkOffsetFromTilePosition(p_X, p_Y);
		for (Chunk i : m_Chunks)
			if (i.getOffset()[0] == offset[0]
					&& i.getOffset()[1] == offset[1])
				return i;
		return null;
	}
	
	public synchronized Chunk setChunk(Chunk p_Chunk)
	{
		Chunk prevChunk = getChunk(p_Chunk.getOffset()[0], p_Chunk.getOffset()[1]);
		if (prevChunk != null)
			m_Chunks.remove(prevChunk);
		m_Chunks.add(p_Chunk);
		return prevChunk;
	}
	
	public synchronized Chunk ensureChunkExistence(int p_X, int p_Y)
	{
		Chunk chunk = getChunk(p_X, p_Y);
		if (chunk == null)
		{
			// Send a request to the server for a new chunk
			if (m_Remote != null)
				m_Remote.sendChunkRequest(p_X, p_Y);
			else
			{
				chunk = new Chunk();
				int[] offset = getChunkOffsetFromTilePosition(p_X, p_Y);
				chunk.setOffset(offset[0], offset[1]);
				//System.out.println("TECHNO GENERATION");
				chunk.generate(m_Seed);
				m_Chunks.add(chunk);
			}
		}
		return chunk;
	}
	
	public synchronized Tile getTile(int p_X, int p_Y)
	{
		Chunk chunk = ensureChunkExistence(p_X, p_Y);
		if (chunk != null)
			return chunk.getTile(p_X - chunk.getOffset()[0], p_Y - chunk.getOffset()[1]);
		return null;
	}
	
	public Tile setTile(int p_X, int p_Y, Tile p_Tile)
	{
		Chunk chunk = ensureChunkExistence(p_X, p_Y);
		if (m_Remote != null)
			m_Remote.sendChangedTile(p_X, p_Y, p_Tile);
		if (chunk != null)
			return chunk.setTile(p_X - chunk.getOffset()[0],
					p_Y - chunk.getOffset()[1], p_Tile);
		return null;
	}
	
	public Tile setTile(int p_X, int p_Y, int p_TileId)
	{
		return setTile(p_X, p_Y, (Tile)Chunk.createTileFromId(p_TileId));
	}
	
	public synchronized void draw(Renderer r, int width, int height) {
		cameraOffset[0] = focus.getPosition()[0] - (r.getWindow().getWidth()/25)/2;
		cameraOffset[1] = focus.getPosition()[1] - (r.getWindow().getHeight()/25)/2;
		for(Chunk i : m_Chunks) { i.draw(r, cameraOffset); }
		
		if (focus != null)
			focus.draw(r, cameraOffset);
		//TODO: adjust camera position
		for(Entity i : m_Entities) { i.draw(r, cameraOffset); }
	}
	
	public void clearChunks()
	{
		m_Chunks.clear();
	}
	
	/*
	 * Clear all chunks that are outside the specified circle
	 */
	public void clearChunks(int[] p_Center, int p_Radius)
	{
		for (int i = 0; i < m_Chunks.size(); i++)
			if (Util.distance(m_Chunks.get(i).getOffset(), p_Center) > p_Radius)
				m_Chunks.remove(i--);
	}
}
