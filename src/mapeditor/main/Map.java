package mapeditor.main;

import static org.lwjgl.opengl.GL11.glColor4f;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import mapeditor.main.Tools.Tabs;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;

public class Map {

	private static Slot[][] matrix;
	private static Point renderOffset = new Point(0, 0);
	public static String NAME;
	
	public static void newMap(int width, int height)
	{
		matrix = new Slot[width][height];
		for(int i=0; i<matrix.length; i++)
			for(int j=0; j<matrix[0].length; j++)
				matrix[i][j] = new Slot(new Point(i*Main.TILE_SIZE, j*Main.TILE_SIZE));
	}
	
	public static void openMap(String mapID)
	{
		XMLParser parser = new XMLParser("map/" + mapID + ".xml");
		
		// Parse map info
		NAME = parser.getAttribute("Map", "name");
		Dimension size = new Dimension();
		size.setWidth(Integer.parseInt(parser.getAttribute("Map", "width")));
		size.setHeight(Integer.parseInt(parser.getAttribute("Map", "height")));
		matrix = new Slot[size.getWidth()][size.getHeight()];
		
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				matrix[i][j] = new Slot(new Point(i*Main.TILE_SIZE, j*Main.TILE_SIZE));
		
		// Parse tiles
		Queue<Integer> tileQueue = new LinkedList<Integer>();

		List<java.util.Map<String, String>> tiles = parser.getChildrenAttributes("Map/Tiles");
		for (java.util.Map<String, String> data : tiles)
		{
			int id = Integer.parseInt(data.get("id"));
			int amount = Integer.parseInt(data.get("amount"));
			System.out.println(id);
			for (int i = 0; i < amount; i++)
				tileQueue.add(id);
		}
		
		for (int j = 0; j < size.getHeight(); j++)
			for (int i = 0; i < size.getWidth(); i++)
				matrix[i][j].add(Tabs.Tile.getTextures().get(tileQueue.poll() - Tabs.Tile.id()), Tabs.Tile);
				
		// Parse all other entities
		String xmlElements[] = { "Portals", "Monsters", "NPCs", "Objects" };

		for (String xmlElement : xmlElements)
		{
			List<java.util.Map<String, String>> entities = parser.getChildrenAttributes("Map/" + xmlElement);
			for (java.util.Map<String, String> data : entities)
			{
				
				Point position = new Point(Integer.parseInt(data.get("x")), Integer.parseInt(data.get("y")));
				int id = Integer.parseInt(data.get("id"));
				
				for(Tabs tab: Tabs.values())
					if(tab.name().equalsIgnoreCase(xmlElement.substring(0, xmlElement.length()-1)))
						matrix[position.getX()][position.getY()].add(tab.getTextures().get(id - tab.id()), tab);
			}
		}
		
	}
	
	public static void render()
	{
		glColor4f(.4f, .4f, .4f, 1);
		for(int column = 1; column <= Math.min(Main.GRID_SIZE.getWidth()/Main.TILE_SIZE, getWidth()); column++)
			Util.renderLine(Main.TILE_SIZE*column, 0, Main.TILE_SIZE*column, Math.min(Main.GRID_SIZE.getHeight(), Map.getHeight()*Main.TILE_SIZE), 1);
		
		for(int row = 1; row <= Math.min(Main.GRID_SIZE.getHeight()/Main.TILE_SIZE, getHeight()); row++)
			Util.renderLine(0, Main.TILE_SIZE*row, Math.min(Main.GRID_SIZE.getWidth(), Map.getWidth()*Main.TILE_SIZE), Main.TILE_SIZE*row, 1);
		glColor4f(1, 1, 1, 1);
		
		int offx = getRenderOffset().getX();
		int offy = getRenderOffset().getY();
		
		for(int k=0; k<Tabs.values().length; k++)
		{
			for(int i=0; i<Math.min(Map.getWidth(), Main.GRID_SIZE.getWidth()/Main.TILE_SIZE); i++)
			{
				for(int j=0; j<Math.min(Map.getHeight(), Main.GRID_SIZE.getHeight()/Main.TILE_SIZE); j++)
				{
					matrix[i+offx][j+offy].render(Tabs.values()[k]);
				}
			}
		}
	}
	
	public static void keyboard()
	{
		if(Keyboard.getEventKeyState())
		{
			switch(Keyboard.getEventKey())
			{
			case Keyboard.KEY_UP:
				if(renderOffset.getY() > 0)
					renderOffset.setY(renderOffset.getY()-1);
				break;
			case Keyboard.KEY_RIGHT:
				if(renderOffset.getX() < matrix.length - Main.GRID_SIZE.getWidth()/Main.TILE_SIZE)
					renderOffset.setX(renderOffset.getX()+1);
				break;
			case Keyboard.KEY_DOWN:
				if(renderOffset.getY() < matrix[0].length - Main.GRID_SIZE.getHeight()/Main.TILE_SIZE)
					renderOffset.setY(renderOffset.getY()+1);
				break;
			case Keyboard.KEY_LEFT:
				if(renderOffset.getX() > 0)
					renderOffset.setX(renderOffset.getX()-1);
				break;
			
			}
		}
	}
	
	public static Slot get(int x, int y)
	{
		if(x >= 0 && x < matrix.length && y >= 0 && y < matrix[0].length)
			return matrix[x][y];
		return null;
	}
	
	public static Point getRenderOffset()
	{
		return renderOffset;
	}

	public static int getWidth()
	{
		return matrix.length;
	}
	
	public static int getHeight()
	{
		return matrix[0].length;
	}
	
}
