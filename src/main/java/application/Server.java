package application;

import java.io.*;
import java.net.*;

import graphics.Renderer;
import graphics.Window;
import network.*;
import world.*;

import java.util.ArrayList;
import java.util.Scanner;


public class Server implements Application
{
	private ServerSocket m_ServerSocket;
	private ArrayList<RemoteWorldConnection> m_ClientThreads;
	private World m_World;
	
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Server()
	{
		m_ClientThreads = new ArrayList<RemoteWorldConnection>();
		m_World = new World();
		m_World.setSeed("pie is great");
	}

	@Override
	public void run()
	{
		Thread serverThread = new Thread(new ClientAcceptingThread(this), "ClientAcceptingThread");
		serverThread.start();
	}
}
