import static org.lwjgl.glfw.GLFW.*;

import java.io.*;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import application.*;

import org.lwjgl.*;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL11.*;

import graphics.*;
import world.*;
import util.Clock;

class ServerThread implements Runnable
{
	@Override
	public void run()
	{
		try {
			Application app = new Server();
			app.run();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

public class Main
{

	public static void main(String[] args)
	{
		// We'll just run the server and client in the same instance for now
		Thread server = new Thread(new ServerThread(), "pie");
		server.start();
		try {
			Application app = new Client();
			app.run();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
