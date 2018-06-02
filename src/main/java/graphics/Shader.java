package graphics;

import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;

public class Shader
{
	private int m_ProgramGL;
	private int m_VertexShaderGL;
	private int m_FragmentShaderGL;
	
	public Shader() {}
	
	public Shader(String p_VertexShader, String p_FragmentShader)
	{
		load(p_VertexShader, p_FragmentShader);
	}
	
	public boolean load(String p_VertexShader, String p_FragmentShader)
	{
		m_VertexShaderGL = compileShader(p_VertexShader, ARBVertexShader.GL_VERTEX_SHADER_ARB);
		m_FragmentShaderGL = compileShader(p_FragmentShader, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
		
		m_ProgramGL = ARBShaderObjects.glCreateProgramObjectARB();
		ARBShaderObjects.glAttachObjectARB(m_ProgramGL, m_VertexShaderGL);
		ARBShaderObjects.glAttachObjectARB(m_ProgramGL, m_FragmentShaderGL);
		
		ARBShaderObjects.glLinkProgramARB(m_ProgramGL);
		if (ARBShaderObjects.glGetObjectParameteriARB(m_ProgramGL, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL_FALSE)
		{
			System.err.println(getLogInfo(m_ProgramGL));
			return false;
		}
		
		ARBShaderObjects.glValidateProgramARB(m_ProgramGL);
        if (ARBShaderObjects.glGetObjectParameteriARB(m_ProgramGL, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE)
        {
            System.err.println(getLogInfo(m_ProgramGL));
            return false;
        }
		return true;
	}
	
	public int getProgramGL()
	{
		return m_ProgramGL;
	}
	
	private int compileShader(String p_Source, int p_ShaderType)
	{
		int shaderObj = 0;
		shaderObj = ARBShaderObjects.glCreateShaderObjectARB(p_ShaderType);
		ARBShaderObjects.glShaderSourceARB(shaderObj, p_Source);
		ARBShaderObjects.glCompileShaderARB(shaderObj);
		if (ARBShaderObjects.glGetObjectParameteriARB(shaderObj, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE)
			throw new RuntimeException("Error compiling shader: " + getLogInfo(shaderObj));
		return shaderObj;
	}
	
	private String getLogInfo(int p_ShaderObject)
	{
		return ARBShaderObjects.glGetInfoLogARB(p_ShaderObject, ARBShaderObjects.glGetObjectParameteriARB(p_ShaderObject, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
	
}
