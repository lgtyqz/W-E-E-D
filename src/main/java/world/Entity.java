package world;

public class Entity {
	private int[] m_position = {0, 0};
	private long timeLeft = (long)1e10; //That's ten seconds in nanoseconds
	public void setPosition(int[] p_position) {m_position = p_position; };
	public int[] getPosition() { return m_position; };
	public void displace(int x, int y) {
		m_position[0] += x;
		m_position[1] += y;
		timeLeft = (long)1e10; //Reset lifespan
	}
}