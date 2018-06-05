package world;

import java.util.ArrayList;
import graphics.Renderer;

public class World
{
	private ArrayList<Chunk> m_Chunks;
	private ArrayList<Entity> m_Entities;
	private int[] cameraOffset = {0, 0};
	private Player focus;
	/*
	 * TODO: Returns tile in world position.
	 */
	public World(Player p_Focus) {
		m_Chunks = new ArrayList<Chunk>();
		m_Entities = new ArrayList<Entity>();
		focus = p_Focus;
	}
	public void testInitialization() {
		
	}
	public Tile getTile(int p_X, int p_Y)
	{
		return null;
	}
	public Tile setTile(int p_X, int p_Y, Tile p_Tile) {
		return null;
	}
	public void draw(Renderer r, int width, int height) {
		for(Chunk i : m_Chunks) { i.draw(r, cameraOffset); }
		focus.draw(r, cameraOffset);
		//TODO: adjust camera position
		//cameraOffset[0] = focus.getPosition()[0];
		for(Entity i : m_Entities) { i.draw(r, cameraOffset); }
	}
}
