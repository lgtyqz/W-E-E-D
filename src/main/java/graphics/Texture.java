package graphics;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

/*
 * Loads and handles a texture for gl rendering.
 * Only PNG format is supported.
 * TODO: Finish
 */
public class Texture
{
	private ByteBuffer m_Buffer;
	private int m_TextureGL;
	
	private int m_Width, m_Height;
	
	public Texture(String p_FilePath) {
		loadResource(p_FilePath);
	}
	
	public boolean loadResource(String p_FilePath)
	{
		InputStream stream = ClassLoader.getSystemResourceAsStream(p_FilePath);
		if (stream == null)
			return false;
		return load(stream);
	}
	
	/*
	 * Load a texture from an input stream.
	 */
	public boolean load(InputStream p_Stream)
	{
		try {
			PNGDecoder decoder = new PNGDecoder(p_Stream);
			
			m_Width = decoder.getWidth();
			m_Height = decoder.getHeight();
			
			// Decode the png to a buffer
			m_Buffer = ByteBuffer.allocateDirect(decoder.getWidth()*decoder.getHeight()*4);
			decoder.decode(m_Buffer, decoder.getWidth()*4, Format.RGBA);
			m_Buffer.flip(); // OpenGL likes things flipped
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		m_TextureGL = glGenTextures();
		
		glActiveTexture(0);
		glBindTexture(GL_TEXTURE_2D, m_TextureGL);
		
		// Make sure opengl understands that the image is contained in contiguous 1 byte components
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, m_Width, m_Height, 0, 
		        GL_RGBA, GL_UNSIGNED_BYTE, m_Buffer);
		glGenerateMipmap(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        
		glBindTexture(GL_TEXTURE_2D, 0);
		
		return true;
	}
	
	public int getTextureGL()
	{
		return m_TextureGL;
	}
}
