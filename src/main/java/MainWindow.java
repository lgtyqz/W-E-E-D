
import rendering.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.*;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
//import javax.swing.Timer;
//import java.awt.event.*;

/*
 * TODO: Wrap most of the window fucntionality in the window class
 */
public class MainWindow //implements ActionListener
{
	//Timer timer;
	public MainWindow()
	{
		GLFWErrorCallback.createPrint(System.err).set();
		//Timer timer = new Timer(20, this);
	}
	
	public void init()
	{
		long window;
		glfwInit();
		long firstTime = System.nanoTime();
		window = glfwCreateWindow(300, 300, "pie", NULL, NULL);
		
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);
		GL.createCapabilities();
		glClearColor(1.0f, 0.7f, 0.7f, 0.0f);
		
		VertexArray vertsArr = new VertexArray();
		vertsArr.add((new Vertex()).setPosition(-1, -1));
		vertsArr.add((new Vertex()).setPosition(-1, 1));
		vertsArr.add((new Vertex()).setPosition(1, 1));
		vertsArr.add((new Vertex()).setPosition(1, 1));
		vertsArr.add((new Vertex()).setPosition(1, -1));
		vertsArr.add((new Vertex()).setPosition(-1, -1));
		
		
		int vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
		
		int vboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, vertsArr.getFloatBuffer(), GL_STATIC_DRAW);
		glVertexAttribPointer(0, 4, GL_FLOAT, false, Vertex.totalSizeInBytes, 0);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, Vertex.totalSizeInBytes, Vertex.elementSizeInBytes * 4);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
		
		float delta = 0;
		while (!glfwWindowShouldClose(window))
		{
			long lastTime = System.nanoTime();
			delta = lastTime - firstTime;
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			
			glBindVertexArray(vaoId);
			if(delta % 4e7 < 2e7)
			{
				glColor3f(0f, 0f, 1f);
			}
			else
			{
				glColor3f(1f, 0f, 0f);
			}
			
			
			glEnableVertexAttribArray(0);
			
			glDrawArrays(GL_TRIANGLES, 0, vertsArr.size());
			
			glDisableVertexAttribArray(0);
			glBindVertexArray(0);

			glfwSwapBuffers(window); // swap the color buffers
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}
}
