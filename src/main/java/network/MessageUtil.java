package network;

import java.util.Scanner;
import java.io.*;
import java.net.*;

import world.*;

class MessageUtil
{
	/*
	 * Structure:
	 * int[2] Offset
	 */
	public static final int REQUEST_CHUNK = 0;
	/*
	 * This is sent briefly after REQUEST_CHUNK
	 * Structure:
	 * int[2] offset
	 * Chunkdata
	 */
	public static final int RECIEVED_CHUNK = 1;
	
	/*
	 * Structure:
	 * int[2] Position
	 * int Id
	 */
	public static final int TILE_CHANGED = 2;
	
	/*
	 * Structure:
	 * double timestamp
	 */
	public static final int SYNC_CLOCK = 3;
	
	public static final int REQUEST_SYNC_CLOCK = 3;
	
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
	}
}