package world;
import static org.lwjgl.glfw.GLFW.*;
public class Player extends Entity {
	
	/*
	 * Directions of player.
	 */
	// If there are magic numbers, such as the ones for the facing direction (0-3),
	// use constants to give these numbers meaning. ~ Michael
	public static final int FacingLeft = 0;
	public static final int FacingUp = 1;
	public static final int FacingRight = 2;
	public static final int FacingDown = 3;
	
	private int facing;
	
	
	public Player(int x, int y) {
		int[] newPos = {x, y};
		setPosition(newPos);
		facing = FacingRight;
	}
	public void processKeyEvents(int key, World p_World) {
		switch(key) {
		case GLFW_KEY_RIGHT:
			displace(1, 0);
		break;
		case GLFW_KEY_LEFT:
			displace(-1, 0);
		break;
		case GLFW_KEY_UP:
			displace(0, -1);
		break;
		case GLFW_KEY_DOWN:
			displace(0, 1);
		break;
		case GLFW_KEY_Z: //Kaboom seed effect
			
			// Remember, duplication like there was before can always be refactored
			// to make it more convenient and self explanatory. 
			// Plus, you can even reuse the kaboom effect in other places as well!
			// ~ Michael
			switch(facing) {
			case FacingLeft:
				effectSeedKaboom(p_World, -2, 0);
			break;
			case FacingRight:
				effectSeedKaboom(p_World, 2, 0);
			break;
			case FacingUp:
				effectSeedKaboom(p_World, 0, -2);
			break;
			case FacingDown:
				effectSeedKaboom(p_World, 0, 2);
			break;
			}
		break;
		}
	}
	
	/*
	 * Applies a kaboom effect relative to the player.
	 */
	private void effectSeedKaboom(World p_World, int p_OffsetX, int p_OffsetY)
	{
		int[] p = getPosition();
		p[0] += p_OffsetX;
		p[1] += p_OffsetY;
		p_World.setTile(p[0] - 1, p[1], new WeedTile()); // Left
		p_World.setTile(p[0] + 1, p[1], new WeedTile()); // Right
		p_World.setTile(p[0], p[1] - 1, new WeedTile()); // Up
		p_World.setTile(p[0], p[1] + 1, new WeedTile()); // Bottom
	}
}
