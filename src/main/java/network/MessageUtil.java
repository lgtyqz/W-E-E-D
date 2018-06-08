package network;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.net.*;

import world.*;

class MessageUtil
{
	/*
	 * Sent from clientside
	 * Structure:
	 * int[2] Offset
	 */
	public static final int REQUEST_CHUNK = 0;
	/*
	 * Sent from serverside
	 * This is sent briefly after REQUEST_CHUNK.
	 * Structure:
	 * int[2] offset
	 * Chunkdata
	 * int entityCount
	 * entitydata
	 */
	public static final int RECIEVED_CHUNK = 1;
	
	/*
	 * Sent from clientside
	 * Structure:
	 * int[2] Position
	 * int Id
	 */
	public static final int TILE_CHANGED = 2;
	
	/*
	 * Sent from serverside
	 * Structure:
	 * double timestamp
	 */
	public static final int SYNC_CLOCK = 3;
	/*
	 * Sent from clientside
	 */
	public static final int REQUEST_SYNC_CLOCK = 4;
	
	/*
	 * Sent from clientside
	 */
	public static final int REQUEST_PLAYERS = 5;

	/*
	 * Sent from serverside
	 * Structure:
	 * int count
	 * PlayerList
	 *   int[] pos
	 */
	public static final int RECIEVED_PLAYERS = 6;
	
	/*
	 * Sent from clientside
	 * Structure:
	 * int[2] position
	 */
	public static final int PLAYER_UPDATE = 7;
	
	public static int[] readIntArr(Scanner p_Scanner, int p_Size)
	{
		int[] arr = new int[p_Size];
		for (int i = 0; i < p_Size; i++)
			arr[i] = p_Scanner.nextInt();
		return arr;
	}
	
	public static void printIntArr(PrintWriter p_Writer, int[] p_Arr)
	{
		for (int i = 0; i < p_Arr.length; i++)
			p_Writer.println(p_Arr[i]);
	}
	
	public static Chunk readChunk(Scanner p_Scanner)
	{
		Chunk chunk = new Chunk();
		
		// Read offset
		int[] offset = readIntArr(p_Scanner, 2);
		chunk.setOffset(offset[0], offset[1]);
		
		// Read all the tiles
		for (int x = 0; x < Chunk.RowTileCount; x++)
			for (int y = 0; y < Chunk.RowTileCount; y++)
				chunk.setTile(x, y, p_Scanner.nextInt());
		
		int numEntities = p_Scanner.nextInt();
		for (int i = 0; i < numEntities; i++) {
			int x = p_Scanner.nextInt();
			int y = p_Scanner.nextInt();
			int ID = p_Scanner.nextInt();
			
			chunk.spawnEntity((Entity)Chunk.createTileFromId(ID), x, y);
		}
		return chunk;
	}
	
	public static void printChunk(PrintWriter p_Writer, Chunk p_Chunk)
	{
		// Read offset
		printIntArr(p_Writer, p_Chunk.getOffset());
		
		// Read all the tiles
		for (int x = 0; x < Chunk.RowTileCount; x++)
			for (int y = 0; y < Chunk.RowTileCount; y++)
				p_Writer.println(p_Chunk.getTile(x, y).getId());
		
		ArrayList<Entity> entities = p_Chunk.getEntities();
		p_Writer.println(entities.size());
		//System.out.println("Entities in chunk: " + p_Chunk.getEntities().size());
		for(int e = 0; e < entities.size(); e++) {
			p_Writer.println(entities.get(e).getPosition()[0]);
			p_Writer.println(entities.get(e).getPosition()[1]);
			p_Writer.println(entities.get(e).getID());
			/*System.out.println(p_Chunk.getEntities().get(e).getPosition()[0]);
			System.out.println(p_Chunk.getEntities().get(e).getPosition()[1]);
			System.out.println(p_Chunk.getEntities().get(e).getID());
			*/
		}
	}
}