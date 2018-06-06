package application;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.glfwInit;

import org.lwjgl.glfw.*;

import graphics.*;
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
		braveNewWorld.setFocus(mustafa);
		
		Renderer renderer = new Renderer();
		renderer.setWindow(window);
		
		Clock timeClock = new Clock();
		Clock deltaClock = new Clock();
		while (!window.closing())
		{
			final float delta = deltaClock.restart();
			
			// Handle events here
			for (WindowEvent i : window.updateEvents())
			{
				if (i.key >= 0)
				{
					switch(i.key)
					{
					case GLFW_KEY_W:
						System.out.println("W Key Event " + timeClock.getElapse());
						break;
					}
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
