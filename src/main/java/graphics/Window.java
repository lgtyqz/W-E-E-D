package graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.system.MemoryStack;

/*
 * Handles a glfw window.
 * Pass this to a Renderer to render things to it.
 */
public class Window
{
	private ArrayList<WindowEvent> m_Events = new ArrayList<WindowEvent>();
	private long m_WindowHandle = 0;
	
	/*
	 * Initialize the window.
	 */
	public void init(int p_Width, int p_Height, String p_Title)
	{
		System.out.print("Init window...");
		glfwInit();
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		m_WindowHandle = glfwCreateWindow(p_Width, p_Height, p_Title, NULL, NULL);
		
		glfwSetKeyCallback(m_WindowHandle, (window, key, scancode, action, mods) -> {
			if (window != m_WindowHandle)
				return;
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
				glfwSetWindowShouldClose(window, true);
			
			WindowEvent event = new WindowEvent();
			event.key = key;
			event.action = action;
			event.mods = mods;
			m_Events.add(event);
		});
		
		glfwSetMouseButtonCallback(m_WindowHandle, (window, button, action, mods) -> {
			if (window != m_WindowHandle)
				return;
			WindowEvent event = new WindowEvent();
			event.mouseButton = button;
			event.action = action;
			event.mods = mods;
			m_Events.add(event);
		});
		
		System.out.println("Done");
	}
	
	/*
	 * Enabled or disables v-sync for the current context.
	 */
	public void setVSync(boolean p_Enabled)
	{
		if (p_Enabled)
			glfwSwapInterval(1);
		else
			glfwSwapInterval(0);
	}
	
	/*
	 * Set the visibility of this window.
	 */
	public void setVisible(boolean p_Visible)
	{
		if (p_Visible)
			glfwShowWindow(m_WindowHandle);
		else
			glfwHideWindow(m_WindowHandle);
	}
	
	/*
	 * Checks if the current window is ready to close.
	 */
	public boolean closing()
	{
		return glfwWindowShouldClose(m_WindowHandle);
	}
	
	/*
	 * Poll the window's events.
	 * Returns an array of all the input events.
	 */
	public ArrayList<WindowEvent> updateEvents()
	{
		m_Events.clear();
		glfwPollEvents();
		return new ArrayList<WindowEvent>(m_Events);
	}
	
	/*
	 * Get the state of a key.
	 * Returns GLFW_PRESS or GLFW_RELEASE.
	 */
	public int getKey(int p_Key)
	{
		return glfwGetKey(m_WindowHandle, p_Key);
	}
	
	/*
	 * Update the window.
	 */
	public void update()
	{
		glfwSwapBuffers(m_WindowHandle);
	}
	
	/*
	 * Clear the window with black.
	 */
	public void clear()
	{
		clear(0f, 0f, 0f);
	}
	
	/*
	 * Clear the window with a color.
	 */
	public void clear(float p_R, float p_G, float p_B)
	{
		glClearColor(p_R, p_G, p_B, 0.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	/*
	 * Tells opengl to use this window for rendering.
	 * This is used internally.
	 */
	public void useGLContext()
	{
		glfwMakeContextCurrent(m_WindowHandle);
	}
	
	public int getWidth()
	{	try (MemoryStack stack = MemoryStack.stackPush())
		{
			IntBuffer width = stack.mallocInt(1);
			glfwGetFramebufferSize(m_WindowHandle, width, null);
			return width.get(0);
		}
	}
	
	public int getHeight()
	{	try (MemoryStack stack = MemoryStack.stackPush())
		{
			IntBuffer height = stack.mallocInt(1);
			glfwGetFramebufferSize(m_WindowHandle, null, height);
			return height.get(0);
		}
	}
}
