package world;
import graphics.Renderer;

public class Entity {
	private int[] m_position = {0, 0};
	private float timeLeft = 10f; //That's ten seconds in nanoseconds
	private float refractoryPeriod = 0.4f;
	private float moveTimer = 0;
	private boolean dead = false;
	private int moveCount = 0;
	private Player target;
	
	public void setPosition(int[] p_position) {m_position = p_position; }
	public int[] getPosition() { return m_position; };
	public float getTimer() { return moveTimer; }
	public void setTimer(long p_moveTimer) { moveTimer = p_moveTimer; }
	public boolean isAlive() { return dead; }
	public void kill() { dead = true; }
	public int getMoveCount() { return moveCount; }
	public void setMoveCount(int p_NewCount) { moveCount = p_NewCount; }
	public void incrementMoves() { moveCount++; }
	public Player getTarget() { return target; }
	
	public void displace(int x, int y, World p_World) {
		Tile tile = p_World.getTile(m_position[0] + x, m_position[1] + y);
		if(tile != null && !tile.isObstacle())
		{
			//Spawn weed
			p_World.setTile(m_position[0], m_position[1], new WeedTile());
			m_position[0] += x;
			m_position[1] += y;
			timeLeft = (long)1e10; //Reset lifespan
			moveTimer = 0; //Commence delay
		}
		else
			System.out.println(tile == null);
	}
	public void handleTimers(float change) {
		if(moveTimer < refractoryPeriod) {
			moveTimer += change;
			moveTimer = Math.min(moveTimer, refractoryPeriod); //hard cap
		}
		timeLeft -= change;
		if(timeLeft <= 0) {
			kill();
		}
	}
	public void draw(Renderer r, int[] cameraOffset) {
		//draw stuff here
		//handleTimers();
	}
	public void update(World p_World) {
		
	}
	public double distance(int[] pos1, int[] pos2) {
		return Math.sqrt(
				Math.pow(pos1[0] - pos2[0], 2) + 
				Math.pow(pos1[1] - pos2[1], 2)
			);
	}
	public void hunt8Way(double minDist, World p_World) {
		//minDist is minimum seeing distance
		//Make sure it's targeting SOMETHING and that it's good to move
		if(target != null) {
			// Relative coords of closest tile to player
			int[] minSquares = {-1, -1};
			minDist = 20; //Minimum sight range
			//Figure out which square is the closest to the player
			for(int i = -1; i <= 1; i++) {
				for(int j = -1; j <= 1; j++) {
					int[] newPosition = {getPosition()[0] + j,
										getPosition()[1] + i};
					if(distance(target.getPosition(), newPosition) 
							< minDist) {
						minDist = distance(target.getPosition(), newPosition);
						minSquares[0] = j;
						minSquares[1] = i;
					}
				}
			}
			if(minDist < 20) {
				displace(minSquares[0], minSquares[1], p_World);
			}else {
				target = null;
			}
		}
	}
	public void huntOrtho(double minDist, World p_World) {
		//minDist is minimum seeing distance
		//Make sure it's targeting SOMETHING and that it's good to move
		if(target != null) {
			// Relative coords of closest tile to player
			int[] minSquares = {-1, -1};
			minDist = 20; //Minimum sight range
			//Figure out which square is the closest to the player
			for(int i = -1; i <= 1; i++) {
				for(int j = -1; j <= 1; j++) {
					if(i == 0 || j == 0) {
						int[] newPosition = {getPosition()[0] + j,
											getPosition()[1] + i};
						if(distance(target.getPosition(), newPosition) 
								< minDist) {
							minDist = distance(target.getPosition(),
									newPosition);
							minSquares[0] = j;
							minSquares[1] = i;
						}
					}
				}
			}
			if(minDist < 20) {
				displace(minSquares[0], minSquares[1], p_World);
			}else {
				target = null;
			}
		}
	}
	public boolean dig(World p_World, int p_dX, int p_dY) {
		if(p_World.getTile(getPosition()[0] + p_dX,
							getPosition()[1] + p_dY).getId() == 1){
			//Is it a weed?
			p_World.setTile(getPosition()[0] + p_dX,
					getPosition()[1] + p_dY, new EmptyTile());
			//Kill it with F I R E
			//TODO: spawn drawn particles
			return true;
		}
		return false;
	}
	public void setTarget(World p_World) {
		//something about going through all the players available and picking
		//the closest one if they're in range
	}
}