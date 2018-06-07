package util;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Clock
{
	private double m_StartTime;
	
	public Clock()
	{
		restart();
	}
	
	public Clock(Clock p_Copy)
	{
		m_StartTime = p_Copy.m_StartTime;
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
	
	public void setStartTime(double p_Time)
	{
		m_StartTime = p_Time;
	}
	
	public double getStartTime()
	{
		return m_StartTime;
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
