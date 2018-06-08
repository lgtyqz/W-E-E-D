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


public class Main
{
	public static void main(String[] args)
	{
		boolean clientEnabled = false;
		boolean serverEnabled = false;
		String remoteIP = null;
		
		System.out.print("Args: ");
		for (String i : args)
			System.out.print(i + ",");
		System.out.println();
		
		if (args.length >= 1)
		{
			if (args[0].equals("client"))
			{
				clientEnabled = true;
				if (args.length >= 2)
					remoteIP = args[1];
			}
			if (args[0].equals("server"))
			{
				serverEnabled = true;
				if (args.length >= 2)
					remoteIP = args[1];
			}
			if (args[0].equals("singleplayer"))
				clientEnabled = serverEnabled = true;
		}
		
		Server server = null;
		
		if (serverEnabled)
		{
			server = new Server();
			server.run();
		}
		
		if (clientEnabled)
		{
			try {
				Client app = new Client();
				app.setIP(remoteIP);
				app.run();
				if (server != null)
					server.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
