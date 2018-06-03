package graphics;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.joml.*;

/*
 * This compiles a shader for gl to use.
 * Mostly used internally in the renderer.
 * You probably don't need to use this for anything else.
 */
public class Shader
{
	private int m_ProgramGL;
	private int m_VertexShaderGL;
	private int m_FragmentShaderGL;
	
	/*
	 * Locations of the shader attributes.
	 */
	public static final int PositionAttr = 0;
	public static final int UVAttr = 1;
	public static final int ColorAttr = 2;
	
	
	public Shader() {}
	
	public Shader(String p_VertexShader, String p_FragmentShader)
	{
		load(p_VertexShader, p_FragmentShader);
	}
	
	public boolean load(String p_VertexShader, String p_FragmentShader)
	{
		m_VertexShaderGL = compileShader(p_VertexShader, ARBVertexShader.GL_VERTEX_SHADER_ARB);
		m_FragmentShaderGL = compileShader(p_FragmentShader, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
		
		m_ProgramGL = glCreateProgram();
		glAttachShader(m_ProgramGL, m_VertexShaderGL);
		glAttachShader(m_ProgramGL, m_FragmentShaderGL);
		
		// Bind attributes
		glBindAttribLocation(m_ProgramGL, PositionAttr, "in_Position");
		glBindAttribLocation(m_ProgramGL, UVAttr, "in_UV");
		glBindAttribLocation(m_ProgramGL, ColorAttr, "in_Color");
		
		glLinkProgram(m_ProgramGL);
		if (ARBShaderObjects.glGetObjectParameteriARB(m_ProgramGL, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL_FALSE)
		{
			System.err.println(getLogInfo(m_ProgramGL));
			return false;
		}
		
		glValidateProgram(m_ProgramGL);
        if (ARBShaderObjects.glGetObjectParameteriARB(m_ProgramGL, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE)
        {
            System.err.println(getLogInfo(m_ProgramGL));
            return false;
        }
		return true;
	}
	
	public void setUniform(String p_Name, Matrix4f p_Val)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(4*4);
		p_Val.get(buffer);
		int location = glGetUniformLocation(m_ProgramGL, p_Name);
		if (location < 0)
		{
			System.out.println("Failed to get uniform");
			return;
		}
		glUseProgram(m_ProgramGL);
		glUniformMatrix4fv(location, false, buffer);
		glUseProgram(0);
	}
	
	public void setUniform(String p_Name, Vector4f p_Val)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
		p_Val.get(buffer);
		int location = glGetUniformLocation(m_ProgramGL, p_Name);
		if (location < 0)
		{
			System.out.println("Failed to get uniform");
			return;
		}
		glUseProgram(m_ProgramGL);
		glUniform4fv(location, buffer);
		glUseProgram(0);
	}
	
	public int getProgramGL()
	{
		return m_ProgramGL;
	}
	
	private int compileShader(String p_Source, int p_ShaderType)
	{
		int shaderObj = 0;
		shaderObj = glCreateShader(p_ShaderType);
		glShaderSource(shaderObj, p_Source);
		glCompileShader(shaderObj);
		if (glGetShaderi(shaderObj, GL_COMPILE_STATUS) == GL_FALSE)
			throw new RuntimeException("Error compiling shader: " + getLogInfo(shaderObj));
		return shaderObj;
	}
	
	private static String getLogInfo(int p_ShaderObject)
	{
		return ARBShaderObjects.glGetInfoLogARB(p_ShaderObject, ARBShaderObjects.glGetObjectParameteriARB(p_ShaderObject, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
	
}
