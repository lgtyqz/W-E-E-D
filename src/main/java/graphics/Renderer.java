package graphics;


import static org.lwjgl.opengl.GL30.*;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;

public class Renderer
{
	private Window m_Window;
	
	private Shader m_FlatColorShader;
	private Shader m_TextureShader;
	
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
			"in vec4 in_UV;\r\n" + 
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
			"in vec2 pass_TextureCoord;\r\n" + 
			"out vec4 out_Color;\r\n" + 
			"void main(void) {\r\n" + 
			"	out_Color = pass_Color*tintColor*texture(textureDiffuse, pass_TextureCoord);\r\n" + 
			"}\r\n";
	
	private static final String strTextureVert =
			"#version 150 core\r\n" + 
			"uniform mat4 viewMatrix;\r\n" +
			"uniform mat4 transformMatrix;\r\n" +
			"in vec2 in_Position;\r\n" + 
			"in vec4 in_Color;\r\n" + 
			"in vec4 in_UV;\r\n" + 
			"out vec4 pass_Color;\r\n" + 
			"out vec2 pass_TextureCoord;\r\n" + 
			"void main(void)\r\n" + 
			"{\r\n" + 
			"	gl_Position = viewMatrix*transformMatrix*vec4(in_Position, 0.f, 1.f);\r\n" + 
			"	pass_Color = in_Color;\r\n" + 
			"	pass_TextureCoord = in_TextureCoord;\r\n" + 
			"}";
	
	/*
	 * These tell the draw method how to draw the vertices
	 */
	public static final int DrawTriangles = 0;
	public static final int DrawTriangleFan = 1;
	
	public Renderer()
	{
		loadShaders();
		
		// Setup the rectangle vertex array for later use.
		m_VABox = new VertexArray();
		m_VABox.add((new Vertex()).setPosition(1, 1));
		m_VABox.add((new Vertex()).setPosition(0, 1));
		m_VABox.add((new Vertex()).setPosition(0, 0));
		m_VABox.add((new Vertex()).setPosition(1, 1));
		m_VABox.add((new Vertex()).setPosition(1, 0));
		m_VABox.add((new Vertex()).setPosition(0, 0));
	}
	
	/*
	 * Set the current window to render to.
	 */
	public void setWindow(Window p_Window)
	{
		m_Window = p_Window;
	}
	
	/*
	 * Set the transform matrix for the next draw.
	 */
	public void setTransformMatrix(Matrix4f p_Mat)
	{
		m_FlatColorShader.setUniform("transformMatrix", p_Mat);
	}
	
	/*
	 * Set the tint color for the next draw.
	 */
	public void setColor(float p_R, float p_G, float p_B, float p_A)
	{
		m_FlatColorShader.setUniform("tintColor", new Vector4f(p_R, p_G, p_B, p_A));
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
		
		draw(va, DrawTriangleFan);
	}
	
	public void drawCircle(float p_Radius)
	{
		VertexArray circle = constructArc(p_Radius, 20, 0f, 360f);
		draw(circle, DrawTriangleFan);
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
	
	/*
	 * Scale the transform matrix to set the size of this rectangle.
	 */
	public void drawRectangle()
	{
		draw(m_VABox, DrawTriangles);
	}
	
	/*
	 * Draw a vertex array.
	 */
	public void draw(VertexArray p_VertexArray, int p_DrawType)
	{
		m_Window.useGLContext();
		
		// Update view. Use window size.
		Matrix4f view = new Matrix4f();
		// This makes everything work from top-left and in pixel coordinates
		view.ortho2D(0f, m_Window.getHeight(), m_Window.getWidth(), 0f);
		m_FlatColorShader.setUniform("viewMatrix", view);
		
		try {
			int vaoId = glGenVertexArrays();
			glBindVertexArray(vaoId);
			
			int vboId = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, p_VertexArray.getFloatBuffer(), GL_STATIC_DRAW);
			glVertexAttribPointer(Shader.PositionAttr, 2, GL_FLOAT, false, Vertex.totalSizeInBytes, 0);
			glVertexAttribPointer(Shader.ColorAttr, 4, GL_FLOAT, false, Vertex.totalSizeInBytes, Vertex.elementSizeInBytes * 4);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			
			glBindVertexArray(0);
			
			glUseProgram(m_FlatColorShader.getProgramGL());
			glBindVertexArray(vaoId);
			
			glEnableVertexAttribArray(Shader.PositionAttr);
			glEnableVertexAttribArray(Shader.ColorAttr);
			
			int drawType = GL_TRIANGLES;
			switch(p_DrawType)
			{
			case DrawTriangles:
				drawType = GL_TRIANGLES;
				break;
			case DrawTriangleFan:
				drawType = GL_TRIANGLE_FAN;
				break;
			}
			glDrawArrays(drawType, 0, p_VertexArray.size());
			
			glDisableVertexAttribArray(Shader.PositionAttr);
			glDisableVertexAttribArray(Shader.ColorAttr);
			
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
			
			resetDrawSettings();
		} catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
	}
	
	public void resetDrawSettings()
	{
		m_FlatColorShader.setUniform("transformMatrix", new Matrix4f());
		m_FlatColorShader.setUniform("tintColor", new Vector4f(1f, 1f, 1f, 1f));
	}
	
	private void loadShaders()
	{
		System.out.println("Compiling shaders...");
		m_FlatColorShader = new Shader(strFlatColorVert, strFlatColorFrag);
		
		resetDrawSettings();
		
		System.out.println("Done");
	}
	
	
}
