package world;
import static org.lwjgl.glfw.GLFW.*;

import graphics.Renderer;
public class Player extends Entity {
	/*
	 * Directions of player.
	 */
	public static final int facingLeft = 0;
	public static final int facingRight = 1;
	public static final int facingUp = 2;
	public static final int facingDown = 3;
	
	private int facing;
	
	public Player(int x, int y) {
		int[] newPos = {x, y};
		setPosition(newPos);
		facing = facingRight;
	}
	public void processKeyEvents(int key, World p_World) {
		switch(key) {
		case GLFW_KEY_RIGHT:
			displace(1, 0, p_World);
			facing = 1;
		break;
		case GLFW_KEY_LEFT:
			displace(-1, 0, p_World);
			facing = 0;
		break;
		case GLFW_KEY_UP:
			displace(0, -1, p_World);
			facing = 2;
		break;
		case GLFW_KEY_DOWN:
			displace(0, 1, p_World);
			facing = 3;
		break;
		case GLFW_KEY_Z: //Kaboom seed effect
			switch(facing) {
			case facingLeft:
				effectSeedKaboom(p_World, -2, 0);
			break;
			case facingRight:
				effectSeedKaboom(p_World, 2, 0);
			break;
			case facingUp:
				effectSeedKaboom(p_World, 0, -2);
			break;
			case facingDown:
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
	
	@Override
	public void draw(Renderer r) {
		//draw blue rounded rect, with a dot
		r.setColor(0f, 0.5f, 1f, 1f);
		// TODO: insert matrix transformation here (Please use TODO. It's pretty much a universal standard. ~ Michael)
		r.drawRoundedRectangle(5, 40, 40);
		r.setColor(1f, 1f, 1f, 1f);
		// [reset matrix transformation]
		// [make another one] <- based on facing
		r.drawCircle(5);
	}
	
	@Override
	public void update(World p_World) {
		
	}
}
