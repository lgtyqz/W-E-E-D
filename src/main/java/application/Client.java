package application;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.glfwInit;

import java.io.IOException;
import java.net.*;

import org.lwjgl.glfw.*;

import graphics.*;
import network.*;
import util.Clock;
import world.*;

public class Client implements Application
{
	static final int MAINMENU = 0;
	static final int SPAWNING = 1;
	static final int GAMEPLAY = 2;
	
	private String m_RemoteIP;
	
	public void setIP(String p_RemoteIP)
	{
		m_RemoteIP = p_RemoteIP;
	}
	
	
	@Override
	public void run()
	{
		int gameState = GAMEPLAY;
		
		// Tell glfw to print errors to the console
		GLFWErrorCallback.createPrint(System.err).set();
		
		glfwInit();
		
		Window window = new Window();
		window.init(800, 600, "Pie in the horse");
		
		Shovel.shovelSprite = new Texture("images/shovel.png");
		
		Player mustafa = new Player(14, 10);
		World braveNewWorld = new World();
		
		Socket socket = null;
		try {
			System.out.println("Connecting to " + m_RemoteIP);
			socket = new Socket(m_RemoteIP == null ? "localhost" : m_RemoteIP, 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RemoteWorldConnection remote = new RemoteWorldConnection(braveNewWorld, socket);
		remote.start("Client Remote Thread");
		//remote.sendSyncClockRequest();
		
		braveNewWorld.setRemote(remote);
		
		braveNewWorld.setFocus(mustafa);
		braveNewWorld.ensureChunkExistence(0, 0);
		
		Renderer renderer = new Renderer();
		renderer.setWindow(window);
		
		Clock timeClock = new Clock();
		Clock moveClock = new Clock();
		Clock chunkRefreshClock = new Clock();
		while (!window.closing())
		{
			// Let's clear the chunks periodically for performance reasons
			if (chunkRefreshClock.getElapse() >= 5)
			{
				System.out.println("Clearing useless chunks");
				braveNewWorld.clearChunks(mustafa.getPosition(), Chunk.RowTileCount*2);
				chunkRefreshClock.restart();
			}
			
			// Handle events here
			for (WindowEvent i : window.updateEvents())
			{
				if (i.key >= 0 && timeClock.getElapse() > 0.25)
				{
					mustafa.processKeyEvents(i.key, braveNewWorld);
					updateChunks(braveNewWorld, mustafa);
					timeClock.restart();
				}
			}
			
			
			window.clear();
			
			if(gameState == GAMEPLAY) {
				braveNewWorld.draw(renderer, window.getWidth(), window.getHeight());
				
			}
			
			window.update();
		}
		
		// Terminate the remote thread
		remote.getThread().interrupt();
	}
	
	/*
	 * Updates the chunks around the player
	 */
	private static void updateChunks(World p_World, Player p_Player)
	{
		p_World.ensureChunkExistence(p_Player.getPosition()[0], p_Player.getPosition()[1]);
		p_World.ensureChunkExistence(p_Player.getPosition()[0], p_Player.getPosition()[1]);
		p_World.ensureChunkExistence(p_Player.getPosition()[0] + Chunk.RowTileCount, p_Player.getPosition()[1]);
		p_World.ensureChunkExistence(p_Player.getPosition()[0] - Chunk.RowTileCount, p_Player.getPosition()[1]);
		p_World.ensureChunkExistence(p_Player.getPosition()[0], p_Player.getPosition()[1] + Chunk.RowTileCount);
		p_World.ensureChunkExistence(p_Player.getPosition()[0], p_Player.getPosition()[1] - Chunk.RowTileCount);
		p_World.ensureChunkExistence(p_Player.getPosition()[0] + Chunk.RowTileCount, p_Player.getPosition()[1] + Chunk.RowTileCount);
		p_World.ensureChunkExistence(p_Player.getPosition()[0] - Chunk.RowTileCount, p_Player.getPosition()[1] - Chunk.RowTileCount);
		p_World.ensureChunkExistence(p_Player.getPosition()[0] + Chunk.RowTileCount, p_Player.getPosition()[1] - Chunk.RowTileCount);
		p_World.ensureChunkExistence(p_Player.getPosition()[0] - Chunk.RowTileCount, p_Player.getPosition()[1] + Chunk.RowTileCount);
	}
}
