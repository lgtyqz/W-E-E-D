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
 * TODO: It may be more reasonable to create 2 separate classes for server and client in the future.
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
	
	/*
	 * This is only relevent on the server.
	 * It keeps track of what player this connection is related to.
	 */
	private Player m_CurrentClientPlayer = new Player();
	
	//static ReentrantLock lock = new ReentrantLock();
	
	public RemoteWorldConnection(World p_World, Socket p_Socket)
	{
		m_SyncClock = new Clock();
		m_World = p_World;
		m_Socket = p_Socket;
		try {
			// The bufferreader will handle the input and allow us to check if there is any data to read from the stream.
			// The scanner's next### and hasNext### methods block the thread and doesn't allow us to terminate it.
			m_BufReader = new BufferedReader(new InputStreamReader(p_Socket.getInputStream()));
			m_Scanner = new Scanner(m_BufReader);
			m_Writer = new PrintWriter(p_Socket.getOutputStream(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendChunkRequest(int p_X, int p_Y)
	{
		m_Writer.println(MessageUtil.REQUEST_CHUNK);
		m_Writer.println(p_X);
		m_Writer.println(p_Y);
		//System.out.println("Requesting chunk (" + p_X + ", " + p_Y + ")");
	}
	
	public void sendChangedTile(int p_X, int p_Y, Tile p_Tile)
	{
		m_Writer.println(MessageUtil.TILE_CHANGED);
		m_Writer.println(p_X);
		m_Writer.println(p_Y);
		m_Writer.println(p_Tile.getId());
	}
	
	public void sendChunkUpdate(int p_X, int p_Y)
	{
		m_Writer.println(MessageUtil.RECIEVED_CHUNK);
		Chunk chunk = m_World.ensureChunkExistence(p_X, p_Y);
		MessageUtil.printChunk(m_Writer, chunk);
	}
	
	public void sendSyncClockRequest()
	{
		m_Writer.println(MessageUtil.REQUEST_SYNC_CLOCK);
	}
	
	public void sendPlayerUpdate(int p_X, int p_Y)
	{
		m_Writer.println(MessageUtil.PLAYER_UPDATE);
		m_Writer.println(p_X);
		m_Writer.println(p_Y);
	}
	
	public void sendPlayerListRequest()
	{
		m_Writer.println(MessageUtil.REQUEST_PLAYERS);
	}
	
	public void sendPlayers()
	{
		m_Writer.println(MessageUtil.RECIEVED_PLAYERS);
		m_Writer.println(m_World.getRemotePlayers().size());
		for (Player i : m_World.getRemotePlayers())
		{
			m_Writer.println(i.getPosition()[0]);
			m_Writer.println(i.getPosition()[1]);
		}
	}
	
	@Override
	public void run()
	{
		try {
			while(true)
			{
				// Wait until a value is available.
				// This will also terminate the thread if
				// the interrupt flag is set.
				try {
					while(!m_BufReader.ready())
						Thread.sleep(20);
				}catch(Exception e)
				{
					e.printStackTrace();
					return;
				}
				
				// Interpret the message
				int message = m_Scanner.nextInt();
				//System.out.println("Recieved message: " + message);
				switch(message)
				{
				case MessageUtil.REQUEST_CHUNK:
				{
					int[] pos = MessageUtil.readIntArr(m_Scanner, 2);
					sendChunkUpdate(pos[0], pos[1]);
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
				case MessageUtil.SYNC_CLOCK:
				{
					m_SyncClock.setStartTime(m_Scanner.nextDouble());
					break;
				}
				case MessageUtil.REQUEST_SYNC_CLOCK:
				{
					m_Writer.println(MessageUtil.SYNC_CLOCK);
					m_Writer.println(m_SyncClock.getStartTime());
					break;
				}
				case MessageUtil.PLAYER_UPDATE:
				{
					int[] pos = MessageUtil.readIntArr(m_Scanner, 2);
					m_CurrentClientPlayer.setPosition(pos);
					m_World.registerRemotePlayer(m_CurrentClientPlayer);
					break;
				}
				case MessageUtil.REQUEST_PLAYERS:
				{
					sendPlayers();
					break;
				}
				case MessageUtil.RECIEVED_PLAYERS:
				{
					m_World.getRemotePlayers().clear();
					int count = m_Scanner.nextInt();
					for (int i = 0; i < count; i++)
					{
						Player player = new Player();
						player.setPosition(new int[] {m_Scanner.nextInt(), m_Scanner.nextInt()});
						m_World.registerRemotePlayer(player);
					}
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
	
	public Clock getClock()
	{
		return new Clock(m_SyncClock);
	}
}
