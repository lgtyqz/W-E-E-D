import static org.lwjgl.glfw.GLFW.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.*;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL11.*;

import graphics.*;
import util.Clock;

public class Main
{
	public static void main(String[] args)
	{
		// Tell glfw to print errors to the console
		GLFWErrorCallback.createPrint(System.err).set();
		
		glfwInit();
		
		Window window = new Window();
		window.init(300, 300, "Pie is great");
		
		Renderer renderer = new Renderer();
		renderer.setWindow(window);
		
		System.out.print("Setting up verts...");
		VertexArray vertsArr = new VertexArray();
		vertsArr.add((new Vertex()).setPosition(1, 1));
		vertsArr.add((new Vertex()).setPosition(0, 1));
		vertsArr.add((new Vertex()).setPosition(0, 0));
		vertsArr.add((new Vertex()).setPosition(1, 1));
		vertsArr.add((new Vertex()).setPosition(1, 0));
		vertsArr.add((new Vertex()).setPosition(0, 0));
		System.out.println("Done");
		
		System.out.print("Running loop...");
		
		Texture texture = new Texture();
		texture.loadResource("images/shovel.png");
		
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
			
			renderer.setColor(window.getCursorPosition()[0] > 100 ? 0f : 1f, 1f, 0f, 1f);
			renderer.setTransformMatrix((new Matrix4f()).translate(100.f, 100.f, 0).rotateZ((3.14f/3)*timeClock.getElapse()));
			renderer.drawRoundedRectangle(40, 100, 100);
			
			renderer.setColor(0.6f, 0.6f, 1f, 1f);
			renderer.setTransformMatrix((new Matrix4f()).translate(100.f, 0, 0));
			renderer.drawRoundedRectangle(20, 80, 100);
			
			renderer.setTransformMatrix((new Matrix4f()).translate(200.f, 2.f, 0).scale(10));
			renderer.drawRectangle();
			
			renderer.setTransformMatrix((new Matrix4f()).translate(2.f, 2.f, 0).scale(10));
			renderer.draw(vertsArr, Renderer.DRAW_TRIANGLES);
			
			renderer.setTransformMatrix((new Matrix4f()).translate(200.f, 200.f, 0));
			renderer.drawCircle(100);
			renderer.setTransformMatrix((new Matrix4f()).translate(2.f, 200.f, 0).scale(50));
			renderer.drawImage(texture, null);
			window.update();
		}
	}
}
