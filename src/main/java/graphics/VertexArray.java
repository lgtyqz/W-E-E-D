package graphics;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

public class VertexArray
{
	private ArrayList<Vertex> m_Vertices = new ArrayList<Vertex>();
	private boolean m_FloatBufferNeedsUpdate = true;
	private FloatBuffer m_FloatBuffer;
	
	public boolean add(Vertex p_Vertex)
	{
		m_FloatBufferNeedsUpdate = true;
		return m_Vertices.add(new Vertex(p_Vertex));
	}
	
	public boolean add(VertexArray p_Append)
	{
		m_FloatBufferNeedsUpdate = true;
		return m_Vertices.addAll(p_Append.m_Vertices);
	}
	
	public Vertex remove(int p_Index)
	{
		m_FloatBufferNeedsUpdate = true;
		return m_Vertices.remove(p_Index);
	}
	
	public int size()
	{
		return m_Vertices.size();
	}
	
	public Vertex set(int p_Index, Vertex p_Vertex)
	{
		m_FloatBufferNeedsUpdate = true;
		return m_Vertices.set(p_Index, new Vertex(p_Vertex));
	}
	
	public Vertex get(int p_Index)
	{
		m_FloatBufferNeedsUpdate = true;
		return new Vertex(m_Vertices.get(p_Index));
	}
	
	/*
	 * Generates and returns a float buffer for opengl to use
	 */
	public FloatBuffer getFloatBuffer()
	{
		if (m_FloatBufferNeedsUpdate || m_FloatBuffer == null)
			updateFloatBuffer();
		return m_FloatBuffer;
	}
	
	private void updateFloatBuffer()
	{
		m_FloatBuffer = BufferUtils.createFloatBuffer(m_Vertices.size() * Vertex.elementCount);
		for (Vertex i : m_Vertices)
		{
			m_FloatBuffer.put(i.position);
			m_FloatBuffer.put(i.UV);
			m_FloatBuffer.put(i.color);
		}
		m_FloatBuffer.flip(); // Opengl expects the data to be flipped
		m_FloatBufferNeedsUpdate = false;
	}
}
