package network;
import java.io.*;
import java.net.*;
import java.util.Scanner;

import application.*;
import world.*;


/*
 * This class handles the communications between server and client World classes.
 */
public class RemoteWorldConnection implements Runnable 
{
	private World m_World;
	private Socket m_Socket;
	private Scanner m_Scanner;
	private PrintWriter m_Writer;
	private int m_RunAs;
	private Thread m_Thread;
	
	public RemoteWorldConnection(World p_World, Socket p_Socket)
	{
		m_World = p_World;
		m_Socket = p_Socket;
		try {
			m_Scanner = new Scanner(p_Socket.getInputStream());
			m_Writer = new PrintWriter(p_Socket.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void makeChunkRequest(int p_X, int p_Y)
	{
		m_Writer.print(MessageUtil.REQUEST_CHUNK);
		m_Writer.print(p_X);
		m_Writer.print(p_Y);
	}
	
	public void sendChangedTile(int p_X, int p_Y, Tile p_Tile)
	{
		m_Writer.print(MessageUtil.TILE_CHANGED);
		m_Writer.print(p_X);
		m_Writer.print(p_Y);
		m_Writer.print(p_Tile.getId());
	}
	
	@Override
	public void run()
	{
		try {
			while(true)
			{
				int message = m_Scanner.nextInt();
				switch(message)
				{
				case MessageUtil.REQUEST_CHUNK:
				{
					int[] pos = MessageUtil.readIntArr(m_Scanner, 2);
					
					m_Writer.print(MessageUtil.RECIEVED_CHUNK);
					Chunk chunk = m_World.ensureChunkExistence(pos[0], pos[1]);
					MessageUtil.printChunk(m_Writer, chunk);
					break;
				}
				case MessageUtil.RECIEVED_CHUNK:
				{
					Chunk chunk = MessageUtil.readChunk(m_Scanner);
					m_World.setChunk(chunk);
					break;
				}
				case MessageUtil.TILE_CHANGED:
				{
					int[] pos = MessageUtil.readIntArr(m_Scanner, 2);
					int typeId = m_Scanner.nextInt();
					if (m_World.ensureChunkExistence(pos[0], pos[1]) != null)
						m_World.setTile(pos[0], pos[1], typeId);
					break;
				}
				
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Thread start(String m_ThreadName)
	{
		return m_Thread = new Thread(this, m_ThreadName);
	}
	
	public Thread getThread()
	{
		return m_Thread;
	}
}
