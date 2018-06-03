package graphics;

public class WindowEvent 
{
	
	public WindowEvent()
	{
		key = -1;
		mouseButton = -1;
	}
	
	/*
	 * -1 if this is not a key event
	 */
	public int key;
	
	/*
	 * -1 if this is not a mouse event
	 */
	public int mouseButton;
	
	/*
	 * This will be one of the following:
	 * GLFW_RELEASE
	 * GLFW_PRESS
	 * GLFW_REPEAT
	 */
	public int action;
	
	/*
	 * Bits:
	 * GLFW_MOD_SHIFT
	 * GLFW_MOD_CONTROL
	 * GLFW_MOD_ALT
	 * GLFW_MOD_SUPER
	 * 
	 * Check for a bit by doing the following:
	 * if (event.mods & GLFW_MOD_SHIFT)
	 */
	public int mods;
}
