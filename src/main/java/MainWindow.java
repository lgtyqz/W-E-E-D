
import graphics.*;

import org.joml.Matrix4f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;

/*
 * TODO: Wrap most of the window fucntionality in the window class
 */
public class MainWindow
{
	public MainWindow()
	{
		GLFWErrorCallback.createPrint(System.err).set();
	}
	
	public void init()
	{
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
		
		//InputStream frag = getClass().getResourceAsStream("/shaders/basicShader.frag");
		//InputStream vert = getClass().getResourceAsStream("/shaders/basicShader.vert");
		String fragStr = "#version 150 core\r\n" + 
				//"uniform sampler2D texture_diffuse;\r\n" + 
				"in vec4 pass_Color;\r\n" + 
				"in vec2 pass_TextureCoord;\r\n" + 
				"out vec4 out_Color;\r\n" + 
				"void main(void) {\r\n" + 
				"	out_Color = pass_Color;\r\n" + 
				//"	out_Color = texture(texture_diffuse, pass_TextureCoord);\r\n" + 
				"}\r\n";
		
		String vertStr = "#version 150 core\r\n" +
				"uniform mat4 projectionMatrix;\r\n" +
				//"uniform mat4 viewMatrix;\r\n" +
				//"uniform mat4 modelMatrix;\r\n" +
				"in vec2 in_Position;\r\n" + 
				"in vec2 in_TextureCoord;\r\n" + 
				"in vec4 in_Color;\r\n" + 
				"out vec4 pass_Color;\r\n" + 
				"out vec2 pass_TextureCoord;\r\n" + 
				"void main(void)\r\n" + 
				"{\r\n" + 
				"	gl_Position = projectionMatrix * vec4(in_Position, 0.f, 1.f);\r\n" + 
				"	pass_Color = in_Color;\r\n" + 
				"	pass_TextureCoord = in_TextureCoord;\r\n" + 
				"}";

		System.out.print("Running loop...");
		while (!window.closing())
		{
			window.updateEvents();
			window.clear();
			
			renderer.setColor(0f, 1f, 0f, 1f);
			renderer.setTransformMatrix((new Matrix4f()).translate(100.f, 100.f, 0));
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
