package application;

import java.io.*;
import java.net.*;

import graphics.Renderer;
import graphics.Window;
import network.*;
import util.Clock;
import world.*;

import java.util.ArrayList;
import java.util.Scanner;


public class Server implements Application
{
	private ServerSocket m_ServerSocket;
	private ArrayList<RemoteWorldConnection> m_ClientThreads;
	private World m_World;
	private Thread m_ClientAcceptingThread;
	private Thread m_WorldUpdateThread;
	
	class ClientAcceptingThread implements Runnable
	{
		private Server m_Server;
		public ClientAcceptingThread(Server p_Server)
		{
			m_Server = p_Server;
		}
		
		@Override
		public void run()
		{
			try {
				m_ServerSocket = new ServerSocket(1000);
				while(true)
				{
					// Accept any new client connections
					Socket socket = m_ServerSocket.accept();
					
					System.out.println("New client: " + socket);
					RemoteWorldConnection remote = new RemoteWorldConnection(m_Server.m_World, socket);
					remote.start(Integer.toString(m_ClientThreads.size()));
					m_ClientThreads.add(remote);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class WorldUpdateThread implements Runnable
	{
		private World m_World;
		public WorldUpdateThread(World p_World)
		{
			m_World = p_World;
		}
		
		@Override
		public void run()
		{
			try {
				do
				{
					Thread.sleep(250);
					m_World.update();
					//System.out.println("pie in the cow");
				} while(true);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Server()
	{
		m_ClientThreads = new ArrayList<RemoteWorldConnection>();
		m_World = new World();
		m_World.setSeed("dcu7x");
	}
	
	public void close()
	{
		// End all the world connections
		for (RemoteWorldConnection i : m_ClientThreads)
			i.getThread().interrupt();
		try {
			// Causes ClientAcceptingThread to throw an exception and terminate.
			m_ServerSocket.close();
			m_WorldUpdateThread.interrupt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		m_ClientAcceptingThread = new Thread(new ClientAcceptingThread(this), "ClientAcceptingThread");
		m_ClientAcceptingThread.start();
		
		m_WorldUpdateThread = new Thread(new WorldUpdateThread(m_World), "WorldUpdateThread");
		m_WorldUpdateThread.start();
	}
}
