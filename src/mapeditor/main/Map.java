package mapeditor.main;

public class Map {

	private static Slot[][] matrix = new Slot[30][30];
	
	static
	{
		//TODO init Slot array by reading map size
		
	}
	
	public static void render()
	{
		for(int column = 1; column <= Main.GRID_SIZE.getWidth()/32; column++)
			Util.renderLine(32*column, 0, 32*column, Main.GRID_SIZE.getHeight(), 1);
		
		for(int row = 1; row <= Main.GRID_SIZE.getHeight()/32; row++)
			Util.renderLine(0, 32*row, Main.GRID_SIZE.getWidth(), 32*row, 1);
		
	}
	
	public Slot get(int x, int y)
	{
		if(x >= 0 && x < matrix.length && y >= 0 && y < matrix[0].length)
			return matrix[x][y];
		return null;
	}
	
}
