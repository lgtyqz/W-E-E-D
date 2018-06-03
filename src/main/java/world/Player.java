package world;
import static org.lwjgl.glfw.GLFW.*;
public class Player extends Entity {
	int facing; //LRUD: left=0, right=1, up=2, down=3
	public Player(int x, int y) {
		int[] newPos = {x, y};
		setPosition(newPos);
		facing = 1;
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
			int[] p = getPosition();
			switch(facing) {
			case 0: //Left
				p_World.setTile(p[0] - 1, p[1], new WeedTile());
				p_World.setTile(p[0] - 2, p[1] - 1, new WeedTile());
				p_World.setTile(p[0] - 2, p[1] + 1, new WeedTile());
				p_World.setTile(p[0] - 3, p[1], new WeedTile());
			break;
			case 1: //Right
				p_World.setTile(p[0] + 1, p[1], new WeedTile());
				p_World.setTile(p[0] + 2, p[1] - 1, new WeedTile());
				p_World.setTile(p[0] + 2, p[1] + 1, new WeedTile());
				p_World.setTile(p[0] + 3, p[1], new WeedTile());
			break;
			case 2: //Up
				p_World.setTile(p[0], p[1] - 1, new WeedTile());
				p_World.setTile(p[0] - 1, p[1] - 2, new WeedTile());
				p_World.setTile(p[0] + 1, p[1] - 2, new WeedTile());
				p_World.setTile(p[0], p[1] - 3, new WeedTile());
			break;
			case 3: //Down
				p_World.setTile(p[0], p[1] + 1, new WeedTile());
				p_World.setTile(p[0] - 1, p[1] + 2, new WeedTile());
				p_World.setTile(p[0] + 1, p[1] + 2, new WeedTile());
				p_World.setTile(p[0], p[1] + 3, new WeedTile());
			break;
			}
		break;
		}
	}
}
