package world;
import static org.lwjgl.glfw.GLFW.*;

import graphics.Renderer;
public class Player extends Entity {
	/*
	 * Directions of player.
	 */
	public static final int FacingLeft = 0;
	public static final int FacingRight = 1;
	public static final int FacingUp = 2;
	public static final int FacingDown = 3;
	int facing; //LRUD: left=0, right=1, up=2, down=3
	public Player(int x, int y) {
		int[] newPos = {x, y};
		setPosition(newPos);
		facing = FacingRight;
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
	public void draw(Renderer r, World p_World) {
		//draw blue rounded rect, with a dot
		r.setColor(0f, 0.5f, 1f, 1f);
		// [insert matrix transformation here]
		r.drawRoundedRectangle(5, 40, 40);
		r.setColor(1f, 1f, 1f, 1f);
		// [reset matrix transformation]
		// [make another one]
		r.drawCircle(5);
	}
}
