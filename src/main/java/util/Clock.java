package util;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Clock
{
	private double m_StartTime;
	
	public Clock()
	{
		restart();
	}
	
	/*
	 * Get time from start in seconds.
	 */
	public float getElapse()
	{
		return (float)(glfwGetTime() - m_StartTime);
	}
	
	/*
	 * Restart clock.
	 * Returns elapse before restarting in seconds.
	 */
	public float restart()
	{
		double currentTime = glfwGetTime();
		float elapse = (float)(currentTime - m_StartTime);
		m_StartTime = currentTime;
		return elapse;
	}
}
