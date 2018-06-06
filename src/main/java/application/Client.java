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
	
	@Override
	public void run()
	{
		int gameState = GAMEPLAY;
		
		// Tell glfw to print errors to the console
		GLFWErrorCallback.createPrint(System.err).set();
		
		glfwInit();
		
		Window window = new Window();
		window.init(400, 400, "Pie in the horse");
		
		Player mustafa = new Player(8, 8);
		World braveNewWorld = new World();
		
		Socket socket = null;
		try {
			socket = new Socket("localhost", 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RemoteWorldConnection remote = new RemoteWorldConnection(braveNewWorld, socket);
		remote.start("Client Remote Thread");
		braveNewWorld.setRemote(remote);
		
		braveNewWorld.setFocus(mustafa);
		braveNewWorld.ensureChunkExistence(0, 0);
		//braveNewWorld.ensureChunkExistence(Chunk.RowTileCount + 1, 0);
		
		Renderer renderer = new Renderer();
		renderer.setWindow(window);
		
		Clock timeClock = new Clock();
		Clock deltaClock = new Clock();
		Clock moveClock = new Clock();
		while (!window.closing())
		{
			final float delta = deltaClock.restart();
			
			// Handle events here
			for (WindowEvent i : window.updateEvents())
			{
				if (i.key >= 0 && timeClock.getElapse() > 0.3)
				{
					mustafa.processKeyEvents(i.key, braveNewWorld);
					braveNewWorld.ensureChunkExistence(mustafa.getPosition()[0], mustafa.getPosition()[1]);
					timeClock.restart();
				}
			}
			
			window.clear();
			
			if(gameState == GAMEPLAY) {
				braveNewWorld.draw(renderer, window.getWidth(), window.getHeight());
			}
			
			window.update();
		}
	}
}
