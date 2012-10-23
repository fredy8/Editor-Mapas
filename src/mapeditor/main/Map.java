package mapeditor.main;

import mapeditor.main.Tools.Tabs;

import org.lwjgl.util.Point;

public class Map {

	private static Slot[][] matrix = new Slot[30][30];
	
	static
	{
		//TODO init Slot array by reading map size
		for(int row = 0; row < matrix.length; row++)
		{
			for(int column = 0; column < matrix[0].length; column++)
			{
				matrix[row][column] = new Slot(new Point(32*row, 32*column));
			}
		}
	}
	
	public static void render()
	{
		for(int column = 1; column <= Main.GRID_SIZE.getWidth()/32; column++)
			Util.renderLine(32*column, 0, 32*column, Main.GRID_SIZE.getHeight(), 1);
		
		for(int row = 1; row <= Main.GRID_SIZE.getHeight()/32; row++)
			Util.renderLine(0, 32*row, Main.GRID_SIZE.getWidth(), 32*row, 1);
		
		for(int k=0; k<Tabs.values().length; k++)
		{
			for(int i=0; i<matrix.length; i++)
			{
				for(int j=0; j<matrix[0].length; j++)
				{
					matrix[i][j].render(Tabs.values()[k]);
				}
			}
		}
	}
	
	public static Slot get(int x, int y)
	{
		if(x >= 0 && x < matrix.length && y >= 0 && y < matrix[0].length)
			return matrix[x][y];
		return null;
	}
	
}
