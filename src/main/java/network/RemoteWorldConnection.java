package network;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

import application.*;
import util.*;
import world.*;


/*
 * This class handles the communications between server and client World classes.
 */
public class RemoteWorldConnection implements Runnable 
{
	private World m_World;
	private Socket m_Socket;
	private BufferedReader m_BufReader;
	private Scanner m_Scanner;
	private PrintWriter m_Writer;
	private Thread m_Thread;
	private Clock m_SyncClock;
	
	//static ReentrantLock lock = new ReentrantLock();
	
	public RemoteWorldConnection(World p_World, Socket p_Socket)
	{
		m_SyncClock = new Clock();
		m_World = p_World;
		m_Socket = p_Socket;
		try {
			// The bufferreader will handle the input and allow us to check if there is any daya to read from the stream.
			// The scanner's next### and hasNext### methods block the thread and doesn't allow us to terminate it.
			m_BufReader = new BufferedReader(new InputStreamReader(p_Socket.getInputStream()));
			m_Scanner = new Scanner(m_BufReader);
			m_Writer = new PrintWriter(p_Socket.getOutputStream(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void makeChunkRequest(int p_X, int p_Y)
	{
		m_Writer.println(MessageUtil.REQUEST_CHUNK);
		m_Writer.println(p_X);
		m_Writer.println(p_Y);
		System.out.println("Requesting chunk (" + p_X + ", " + p_Y + ")");
	}
	
	public void sendChangedTile(int p_X, int p_Y, Tile p_Tile)
	{
		m_Writer.println(MessageUtil.TILE_CHANGED);
		m_Writer.println(p_X);
		m_Writer.println(p_Y);
		m_Writer.println(p_Tile.getId());
	}
	
	@Override
	public void run()
	{
		try {
			while(true)
			{
				// Wait until an int is available.
				// This will also terminate the thread if
				// the interrupt flag is set.
				try {
					while(!m_BufReader.ready())
					{
						System.out.println("Check");
						Thread.sleep(1000);
					}
				}catch(Exception e)
				{
					e.printStackTrace();
					return;
				}
				
				// Interpret the message
				int message = m_Scanner.nextInt();
				System.out.println("Recieved message: " + message);
				switch(message)
				{
				case MessageUtil.REQUEST_CHUNK:
				{
					//lock.lock();
					int[] pos = MessageUtil.readIntArr(m_Scanner, 2);
					
					m_Writer.println(MessageUtil.RECIEVED_CHUNK);
					Chunk chunk = m_World.ensureChunkExistence(pos[0], pos[1]);
					//chunk.generate(seed);
					MessageUtil.printChunk(m_Writer, chunk);
					//lock.unlock();
					break;
				}
				case MessageUtil.RECIEVED_CHUNK:
				{
					///lock.lock();
					Chunk chunk = MessageUtil.readChunk(m_Scanner);
					m_World.setChunk(chunk);
					//lock.unlock();
					break;
				}
				case MessageUtil.TILE_CHANGED:
				{
					//lock.lock();
					int[] pos = MessageUtil.readIntArr(m_Scanner, 2);
					int typeId = m_Scanner.nextInt();
					if (m_World.ensureChunkExistence(pos[0], pos[1]) != null)
						m_World.setTile(pos[0], pos[1], typeId);
					//lock.unlock();
					break;
				}
				case MessageUtil.SYNC_CLOCK:
				{
					m_SyncClock.setStartTime(m_Scanner.nextDouble());
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
		m_Thread = new Thread(this, m_ThreadName);
		m_Thread.start();
		return m_Thread;
	}
	
	public Thread getThread()
	{
		return m_Thread;
	}
	
	public Clock getSyncClock()
	{
		return new Clock(m_SyncClock);
	}
}
