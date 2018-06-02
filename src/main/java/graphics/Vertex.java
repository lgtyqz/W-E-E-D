package graphics;
import java.util.Arrays;

public class Vertex
{
	public float[] position = new float[] { 0f, 0f, 0f, 1f };
	public float[] UV       = new float[] { 0f, 0f };
	public float[] color    = new float[] { 1f, 1f, 1f, 1f };
	
	public static final int elementCount = 8;
	public static final int elementSizeInBytes = 4;
	public static final int totalSizeInBytes = elementCount * elementSizeInBytes;
	
	/*
	 * Default constructor.
	 */
	public Vertex() {}
	
	/*
	 * Copy constructor.
	 */
	public Vertex(Vertex p_Copy)
	{
		position = p_Copy.getPosition();
		color = p_Copy.getColor();
	}
	
	/*
	 * Set position of vertex.
	 */
	public Vertex setPosition(float p_X, float p_Y)
	{
		position[0] = p_X;
		position[1] = p_Y;
		return this;
	}
	
	/*
	 * Get position of vertex.
	 */
	public float[] getPosition()
	{
		return Arrays.copyOf(position, 2);
	}
	
	/*
	 * Set texture UV position of vertex.
	 */
	public Vertex setUV(float p_U, float p_V)
	{
		UV[0] = p_U;
		UV[1] = p_V;
		return this;
	}
	
	/*
	 * Get texture UV position of vertex. Returns a copy of the UV array.
	 */
	public float[] getUV()
	{
		return Arrays.copyOf(UV, UV.length);
	}
	
	/*
	 * Set color of vertex. Alpha remains unchanged.
	 */
	public Vertex setColor(float p_R, float p_G, float p_B)
	{
		color[0] = p_R;
		color[1] = p_G;
		color[2] = p_B;
		return this;
	}
	
	/*
	 * Set color of vertex.
	 */
	public Vertex setColor(float p_R, float p_G, float p_B, float p_A)
	{
		color[0] = p_R;
		color[1] = p_G;
		color[2] = p_B;
		color[3] = p_A;
		return this;
	}
	
	/*
	 * Get color of vertex. Returns a copy of the color array.
	 */
	public float[] getColor()
	{
		return Arrays.copyOf(color, color.length);
	}
}
