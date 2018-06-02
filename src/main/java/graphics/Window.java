package graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
	private long m_WindowHandle = 0;
	
	public void init(int p_Width, int p_Height, String p_Title)
	{
		glfwInit();
		m_WindowHandle = glfwCreateWindow(p_Width, p_Height, p_Title, NULL, NULL);
		glfwSetKeyCallback(m_WindowHandle, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
				glfwSetWindowShouldClose(window, true);
		});
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
	 */
	public void updateEvents()
	{
		glfwPollEvents();
	}
	
	/*
	 * Update the window.
	 */
	public void update()
	{
		glfwSwapBuffers(m_WindowHandle);
	}
	
	public void clear()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void useGLContext()
	{
		glfwMakeContextCurrent(m_WindowHandle);
	}
}
