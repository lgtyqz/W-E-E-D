package RenderSystem;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
	private long m_WindowHandle = 0;
	
	public void init(int p_Width, int p_Height, String p_Title)
	{
		glfwInit();
		m_WindowHandle = glfwCreateWindow(p_Width, p_Height, p_Title, NULL, NULL);
		glfwSetKeyCallback(m_WindowHandle, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true);
		});
	}
	
	public void setVisible(boolean p_Visible)
	{
		if (p_Visible)
			glfwShowWindow(m_WindowHandle);
		else
			glfwHideWindow(m_WindowHandle);
	}
	
	public boolean closing()
	{
		return glfwWindowShouldClose(m_WindowHandle);
	}
}
