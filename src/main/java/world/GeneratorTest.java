package world;

public class GeneratorTest {
	static int seed = "pornography".hashCode();
	public static int getTile(int seed, double x, double y) {
		return 3 + (int)Math.round(5 * (ImprovedNoise.noise(x, y, seed)));
	}
	public static void main(String[] args) {
		System.out.println(seed);
		for(int i = 0; i < 20; i++) {
			String s = "";
			for(int j = 0; j < 20; j++) {
				s += getTile(seed, (double)(i)/2, (double)(j)/2);
				s += " ";
			}
			System.out.println(s);
		}
	}
}