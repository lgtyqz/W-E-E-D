package world;

public class WeedTile implements Tile {

	@Override
	public void onPlayer() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isObstacle() {
		//No walking through the weeds
		return true;
	}

}
