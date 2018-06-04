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
	 * Set elapse time in seconds.
	 * Returns previous elapse.
	 */
	public float setElapse(float p_Seconds)
	{
		double currentTime = glfwGetTime();
		float elapse = (float)(currentTime - m_StartTime);
		m_StartTime = currentTime - p_Seconds;
		return elapse;
	}
	
	/*
	 * Restart clock.
	 * Returns the previous elapse in seconds.
	 */
	public float restart()
	{
		double currentTime = glfwGetTime();
		float elapse = (float)(currentTime - m_StartTime);
		m_StartTime = currentTime;
		return elapse;
	}
}
