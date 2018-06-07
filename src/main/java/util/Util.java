package util;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Util 
{
	public static String readStreamAsString(InputStream p_Stream)
	{
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(p_Stream, "UTF-8"));
			
			String line;
			while((line = reader.readLine()) != null)
				builder.append(line).append('\n');
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
	
	// Moved it over here so it can be used in other places
	public static double distance(int[] pos1, int[] pos2) {
		return Math.sqrt(
				Math.pow(pos1[0] - pos2[0], 2) + 
				Math.pow(pos1[1] - pos2[1], 2)
			);
	}
}
