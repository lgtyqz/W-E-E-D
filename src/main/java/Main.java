import static org.lwjgl.glfw.GLFW.*;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import graphics.*;

public class Main
{
	public static void main(String[] args)
	{
		// Tell glfw to print errors to the console
		GLFWErrorCallback.createPrint(System.err).set();
		
		glfwInit();
		
		Window window = new Window();
		window.init(300, 300, "Pie is great");
		
		window.useGLContext();
		GL.createCapabilities();
		
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
		
		double lastTime = glfwGetTime(); // glfwGetTime returns seconds with high precision
		while (!window.closing())
		{
			final float currentTime = (float)glfwGetTime();
			final float delta = (float)(currentTime - lastTime);
			lastTime = currentTime;
			
			// Handle events here
			for (WindowEvent i : window.updateEvents())
			{
				if (i.key >= 0)
				{
					switch(i.key)
					{
					case GLFW_KEY_W:
						System.out.println("W Key Event " + currentTime);
						break;
					}
				}
			}
			window.clear();
			
			renderer.setColor(0f, 1f, 0f, 1f);
			renderer.setTransformMatrix((new Matrix4f()).translate(100.f, 100.f, 0).rotateZ((3.14f/3)*currentTime));
			renderer.drawRoundedRectangle(40, 100, 100);
			
			renderer.setColor(0.6f, 0.6f, 1f, 1f);
			renderer.setTransformMatrix((new Matrix4f()).translate(100.f, 0, 0));
			renderer.drawRoundedRectangle(20, 80, 100);
			
			renderer.setTransformMatrix((new Matrix4f()).translate(200.f, 2.f, 0).scale(10));
			renderer.drawRectangle();
			
			renderer.setTransformMatrix((new Matrix4f()).translate(2.f, 2.f, 0).scale(10));
			renderer.draw(vertsArr, Renderer.DrawTriangles);
			window.update();
		}
	}
}
