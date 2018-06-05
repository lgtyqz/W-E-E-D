package graphics;



import org.joml.Matrix4f;
import org.joml.Rectanglef;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;

public class Renderer
{
	private Window m_Window;
	
	private Shader m_FlatColorShader;
	private Shader m_TextureShader;
	
	private Vector4f m_FillColor;
	private Matrix4f m_TransformMatrix;
	
	private VertexArray m_VABox;
	
	private static final String strFlatColorFrag =
			"#version 150 core\r\n" + 
			"uniform vec4 tintColor;\r\n" +
			"in vec4 pass_Color;\r\n" + 
			"out vec4 out_Color;\r\n" + 
			"void main(void) {\r\n" + 
			"	out_Color = pass_Color*tintColor;\r\n" + 
			"}\r\n";
	
	private static final String strFlatColorVert =
			"#version 150 core\r\n" + 
			"uniform mat4 viewMatrix;\r\n" +
			"uniform mat4 transformMatrix;\r\n" +
			"in vec2 in_Position;\r\n" + 
			"in vec4 in_Color;\r\n" + 
			"in vec2 in_UV;\r\n" + 
			"out vec4 pass_Color;\r\n" + 
			"void main(void)\r\n" + 
			"{\r\n" + 
			"	gl_Position = viewMatrix*transformMatrix*vec4(in_Position, 0.f, 1.f);\r\n" + 
			"	pass_Color = in_Color;\r\n" + 
			"}";
	
	private static final String strTextureFrag =
			"#version 150 core\r\n" +
			"uniform sampler2D textureDiffuse;\r\n" +
			"uniform vec4 tintColor;\r\n" +
			"in vec4 pass_Color;\r\n" + 
			"in vec2 pass_UV;\r\n" + 
			"out vec4 out_Color;\r\n" + 
			"void main(void) {\r\n" + 
			"	out_Color = pass_Color*tintColor*texture(textureDiffuse, pass_UV);\r\n" + 
			"}\r\n";
	
	private static final String strTextureVert =
			"#version 150 core\r\n" + 
			"uniform mat4 viewMatrix;\r\n" +
			"uniform mat4 transformMatrix;\r\n" +
			"in vec2 in_Position;\r\n" + 
			"in vec4 in_Color;\r\n" + 
			"in vec2 in_UV;\r\n" + 
			"out vec4 pass_Color;\r\n" + 
			"out vec2 pass_UV;\r\n" + 
			"void main(void)\r\n" + 
			"{\r\n" + 
			"	gl_Position = viewMatrix*transformMatrix*vec4(in_Position, 0.f, 1.f);\r\n" + 
			"	pass_Color = in_Color;\r\n" + 
			"	pass_UV = in_UV;\r\n" + 
			"}";
	
	/*
	 * These tell the draw method how to draw the vertices
	 */
	public static final int DRAW_TRIANGLES = GL_TRIANGLES;
	public static final int DRAW_TRIANGLE_FAN = GL_TRIANGLE_FAN;
	public static final int DRAW_LINE_STRIP = GL_LINE_STRIP;
	
	public Renderer()
	{
		m_FillColor = new Vector4f(1f, 1f, 1f, 1f);
		m_TransformMatrix = new Matrix4f();
		
		// Setup the rectangle vertex array for later use.
		m_VABox = new VertexArray();
		m_VABox.add((new Vertex()).setPosition(1, 1));
		m_VABox.add((new Vertex()).setPosition(0, 1));
		m_VABox.add((new Vertex()).setPosition(0, 0));
		m_VABox.add((new Vertex()).setPosition(1, 1));
		m_VABox.add((new Vertex()).setPosition(1, 0));
		m_VABox.add((new Vertex()).setPosition(0, 0));
		
		loadShaders();
	}
	
	/*
	 * Set the current window to render to.
	 */
	public void setWindow(Window p_Window)
	{
		if (p_Window == null)
			throw new NullPointerException();
		m_Window = p_Window;
	}
	
	/*
	 * Set the transform matrix for the next draw.
	 * Note: This is reset after every draw.
	 */
	public void setTransformMatrix(Matrix4f p_Mat)
	{
		if (p_Mat == null)
			throw new NullPointerException();
		m_TransformMatrix.set(p_Mat);
	}
	
	/*
	 * Set the tint color for the next draw.
	 * Note: This is reset after every draw.
	 */
	public void setColor(float p_R, float p_G, float p_B, float p_A)
	{
		m_FillColor.set(p_R, p_G, p_B, p_A);
	}
	
	/*
	 * Set the tint color for the next draw.
	 * Note: This is reset after every draw.
	 */
	public void setColor(Vector4f p_Color)
	{
		if (p_Color == null)
			throw new NullPointerException();
		m_FillColor.set(p_Color);
	}
	
	/*
	 * Draw a rounded rectangle.
	 */
	public void drawRoundedRectangle(float p_Radius, float p_Width, float p_Height)
	{
		VertexArray topleftCorner = constructArc(p_Radius, 5, 180f, 90f);
		VertexArray toprightCorner = constructArc(p_Radius, 5, 270f, 90f);
		VertexArray bottomleftCorner = constructArc(p_Radius, 5, 90f, 90f);
		VertexArray bottomrightCorner = constructArc(p_Radius, 5, 0f, 90f);
		
		topleftCorner.move(p_Radius, p_Radius);
		toprightCorner.move(p_Width - p_Radius, p_Radius);
		bottomleftCorner.move(p_Radius, p_Height - p_Radius);
		bottomrightCorner.move(p_Width - p_Radius, p_Height - p_Radius);
		
		VertexArray va = new VertexArray();
		va.add(topleftCorner);
		va.add(toprightCorner);
		va.add(bottomrightCorner);
		va.add(bottomleftCorner);
		
		draw(va, DRAW_TRIANGLE_FAN);
	}
	
	/*
	 * Draw a Circle.
	 */
	public void drawCircle(float p_Radius)
	{
		VertexArray circle = constructArc(p_Radius, 20, 0f, 360f);
		draw(circle, DRAW_TRIANGLE_FAN);
	}
	
	/*
	 * Constructs a vertex array that represents a part of a circle.
	 */
	private static VertexArray constructArc(float p_Radius, int p_Slices, float p_FromAngle, float p_Angle)
	{
		VertexArray va = new VertexArray();
		for (int i = 0; i < p_Slices + 1; i++)
		{
			double rads = Math.toRadians(p_FromAngle + (float)i*(p_Angle/p_Slices));
			float c = (float)Math.cos(rads);
			float s = (float)Math.sin(rads);
			float x = p_Radius*c;
			float y = p_Radius*s;
			va.add((new Vertex()).setPosition(x, y));
		}
		return va;
	}
	
	public void drawImage(Texture p_Texture, Rectanglef p_SubTexture)
	{
		VertexArray imageVA = new VertexArray();
		imageVA.add((new Vertex()).setPosition(1, 1).setUV(1, 1));
		imageVA.add((new Vertex()).setPosition(0, 1).setUV(0, 1));
		imageVA.add((new Vertex()).setPosition(0, 0).setUV(0, 0));
		imageVA.add((new Vertex()).setPosition(1, 1).setUV(1, 1));
		imageVA.add((new Vertex()).setPosition(1, 0).setUV(1, 0));
		imageVA.add((new Vertex()).setPosition(0, 0).setUV(0, 0));
		draw(imageVA, DRAW_TRIANGLES, p_Texture, true);
	}
	
	/*
	 * Scale the transform matrix to set the size of this rectangle.
	 */
	public void drawRectangle()
	{
		draw(m_VABox, DRAW_TRIANGLES);
	}
	
	
	public void draw(VertexArray p_VertexArray, int p_DrawType)
	{
		draw(p_VertexArray, p_DrawType, null, true);
	}
	
	/*
	 * Draw a vertex array.
	 */
	private void draw(VertexArray p_VertexArray, int p_DrawType, Texture p_Texture, boolean p_ResetDrawSettings)
	{
		m_Window.useGLContext();
		
		final Shader currentShader = p_Texture == null ? m_FlatColorShader : m_TextureShader;
		
		// Update the view. Use window size.
		// Makes everything work from top-left and in pixel coordinates
		Matrix4f view = new Matrix4f();
		view.ortho2D(0f, m_Window.getHeight(), m_Window.getWidth(), 0f);
		
		// Update the shader's parameters
		currentShader.setUniform("viewMatrix", view);
		currentShader.setUniform("tintColor", m_FillColor);
		currentShader.setUniform("transformMatrix", m_TransformMatrix);
		
		try {
			int vaoId = glGenVertexArrays();
			glBindVertexArray(vaoId);
			
			int vboId = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, p_VertexArray.getFloatBuffer(), GL_STATIC_DRAW);
			glVertexAttribPointer(Shader.POSITION_ATTR, 2, GL_FLOAT, false, Vertex.totalSizeInBytes, 0);
			glVertexAttribPointer(Shader.UV_ATTR, 2, GL_FLOAT, false, Vertex.totalSizeInBytes, Vertex.elementSizeInBytes * 2);
			glVertexAttribPointer(Shader.COLOR_ATTR, 4, GL_FLOAT, false, Vertex.totalSizeInBytes, Vertex.elementSizeInBytes * 4);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			
			glBindVertexArray(0);
			
			glUseProgram(currentShader.getProgramGL());
			
			if (p_Texture != null)
			{
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, p_Texture.getTextureGL());
			}
			
			glBindVertexArray(vaoId);
			
			glEnableVertexAttribArray(Shader.POSITION_ATTR);
			glEnableVertexAttribArray(Shader.UV_ATTR);
			glEnableVertexAttribArray(Shader.COLOR_ATTR);
			
			glDrawArrays(p_DrawType, 0, p_VertexArray.size());
			
			glDisableVertexAttribArray(Shader.POSITION_ATTR);
			glDisableVertexAttribArray(Shader.UV_ATTR);
			glDisableVertexAttribArray(Shader.COLOR_ATTR);
			
			glBindVertexArray(0);
			glUseProgram(0);
			
			// Delete stuff
			
			glBindVertexArray(vaoId);
			
			// Delete buffers
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glDeleteBuffers(vboId);
			 
			// Delete vertex array
			glBindVertexArray(0);
			glDeleteVertexArrays(vaoId);
			
			if (p_ResetDrawSettings)
				resetDrawSettings();
		} catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
	}
	
	public void resetDrawSettings()
	{
		m_FillColor.set(1f, 1f, 1f, 1f);
		m_TransformMatrix.identity();
	}
	
	private void loadShaders()
	{
		System.out.println("Compiling shaders...");
		m_FlatColorShader = new Shader(strFlatColorVert, strFlatColorFrag);
		m_TextureShader = new Shader(strTextureVert, strTextureFrag);
		
		resetDrawSettings();
		
		System.out.println("Done");
	}
	
	
}
