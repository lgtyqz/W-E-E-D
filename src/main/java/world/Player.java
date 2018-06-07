package world;
import static org.lwjgl.glfw.GLFW.*;

import org.joml.Matrix4f;
import java.util.Map;
import java.util.HashMap;

import graphics.Renderer;
public class Player extends Entity {
	public static final int ID = Integer.MAX_VALUE;
	/*
	 * Directions of player.
	 */
	public static final int facingLeft = 0;
	public static final int facingRight = 1;
	public static final int facingUp = 2;
	public static final int facingDown = 3;
	/*
	 * Item names in hashmap
	 */
	public static final String shovels = "shovels";
	public static final String bombs = "bombs";
	public static final String freezes = "freezes";
	static final int[][] specialSauce = {
			{-1, 0}, {1, 0}, {0, 1}, {0, -1}
	};
	
	private int facing;
	Map<String, Integer> items = new HashMap<String, Integer>();
	
	public Player() {
		facing = facingRight;
		items.put(shovels, 0);
		items.put(bombs, 0);
		items.put(freezes, 0);
	}
	
	public Player(int x, int y) {
		int[] newPos = {x, y};
		setPosition(newPos);
		facing = facingRight;
	}
	public void processKeyEvents(int key, World p_World) {
		switch(key) {
		case GLFW_KEY_RIGHT:
			System.out.println("Move Right");
			displace(1, 0, p_World);
			facing = 1;
		break;
		case GLFW_KEY_LEFT:
			System.out.println("Move Left");
			displace(-1, 0, p_World);
			facing = 0;
		break;
		case GLFW_KEY_UP:
			System.out.println("Move Up");
			displace(0, -1, p_World);
			facing = 2;
		break;
		case GLFW_KEY_DOWN:
			System.out.println("Move Down");
			displace(0, 1, p_World);
			facing = 3;
		break;
		case GLFW_KEY_Z: //Kaboom seed effect
			System.out.println("Kaboom Seed!");
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
		case GLFW_KEY_X:
			System.out.println("Shovel");
			switch(facing) {
			case facingLeft:
				if(dig(p_World, -1, 0) && items.get(shovels) > 0) {
					items.put(shovels, items.get(shovels) - 1);
				}
			break;
			case facingRight:
				if(dig(p_World, 1, 0) && items.get(shovels) > 0) {
					items.put(shovels, items.get(shovels) - 1);
				}
			break;
			case facingUp:
				if(dig(p_World, 0, -1) && items.get(shovels) > 0) {
					items.put(shovels, items.get(shovels) - 1);
				}
			break;
			case facingDown:
				if(dig(p_World, 0, 1) && items.get(shovels) > 0) {
					items.put(shovels, items.get(shovels) - 1);
				}
			break;
			}
		}
	}
	
	/*
	 * Applies a kaboom effect relative to the player.
	 */
	private void effectSeedKaboom(World p_World, int p_OffsetX, int p_OffsetY)
	{
		int[] p = getPosition();
		int[] q = {0, 0};
		//q makes it so that the player doesn't jump when spraying the seed
		q[0] = p[0] + p_OffsetX;
		q[1] = p[1] + p_OffsetY;
		for(int i = 0; i < specialSauce.length; i++) {
			if(p_World.getTile(q[0] + specialSauce[i][0],
								q[1] + specialSauce[i][1]).getId() == 0) {
				p_World.setTile(q[0] + specialSauce[i][0],
								q[1] + specialSauce[i][1], new WeedTile());
			}
		}
	}
	
	@Override
	public void draw(Renderer r, int[] cameraOffset) {
		//draw blue rounded rect, with a dot
		r.setColor(0f, 0.5f, 1f, 1f);
		r.setTransformMatrix((new Matrix4f()).translate(
				25 * (getPosition()[0] - cameraOffset[0]),
				25 * (getPosition()[1] - cameraOffset[1]), 0));
		r.drawRoundedRectangle(3, 25, 25);
		/*r.setColor(1f, 1f, 1f, 1f);
		r.setTransformMatrix((new Matrix4f()).translate(
				25 * (getPosition()[0] - cameraOffset[0]),
				25 * (getPosition()[1] - cameraOffset[1]), 0));
		// [make another one] <- based on facing
		r.drawCircle(5);*/
	}
	
	@Override
	public void update(World p_World) {
		//TODO: Go through entities, if touching enemy, kill the player
		//TODO: Loop through entities, if touching item, pick up that item
		
	}
	public int getID() { return ID; }
}
