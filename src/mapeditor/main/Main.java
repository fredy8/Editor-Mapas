package mapeditor.main;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.util.Scanner;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.Dimension;

public class Main {
	
	public static final Dimension GRID_SIZE;
	public static final int TILE_SIZE;
	
	//TODO map should be "infinite" (expanding) and should be written in xml depending on the max width/height with a tile
	//TODO render grabbed objects big
	//TODO no strong entity stacking
	//TODO render ids with entities in tools
	//TODO add mouse clicks instructions
	//TODO write tiles to xml
	
	static
	{
		//loads game configurations
		XMLParser parser = new XMLParser("game_config.xml");
				
		TILE_SIZE = Integer.parseInt(parser.getAttribute("Map", "tile_size").replaceAll("px", ""));
		int gridWidth = Integer.parseInt(parser.getAttribute("Map", "width"));
		int gridHeight = Integer.parseInt(parser.getAttribute("Map", "height"));
				
		GRID_SIZE = new Dimension(gridWidth*TILE_SIZE, gridHeight*TILE_SIZE);
	}
	
	public Main()
	{
		
		try {
			Display.setDisplayMode(new DisplayMode(GRID_SIZE.getWidth() + Tools.WIDTH, GRID_SIZE.getHeight()));
			Display.setTitle("Map Editor");
			Display.create();
		} catch (LWJGLException e) {
			System.out.println("Unable to create display");
			System.exit(1);
		}

		initGL();
		
		while (!Display.isCloseRequested())											
		{	
			glClear(GL_COLOR_BUFFER_BIT);
		
			Tools.mouse();
			while(Keyboard.next())
			{
				Tools.keyboard();
				Map.keyboard();
			}
			
			Map.render();
			Tools.render();

			Display.update(); 
			Display.sync(60); 
		}

		Display.destroy();
		System.exit(0);
	}

	private void initGL() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,GRID_SIZE.getWidth() + Tools.WIDTH, GRID_SIZE.getHeight(), 0, 1, -1); 
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void main(String args[])
	{
		
		Scanner in = new Scanner(System.in);
		
		System.out.println("Map Editor");
		
		char action;
		
		do{
			System.out.println("New or open?(n/o)");
			action = in.next().charAt(0);
		}while(action != 'n' && action != 'o');
		
		if(Character.toLowerCase(action) == 'n')
		{			
			System.out.println("Map name?");
			Map.NAME = in.nextLine();
			
			int mapWidth;
			do{
				System.out.println("Map width?");
				mapWidth = in.nextInt();
			}while(mapWidth <=0 && mapWidth > 100);
			
			int mapHeight;
			do{
				System.out.println("Map height?");
				mapHeight = in.nextInt();
			}while(mapHeight <=0 && mapHeight > 100);
			
			Map.newMap(mapWidth, mapHeight);
			
		}else if(Character.toLowerCase(action) == 'o')
		{
			int id;
			do{
				System.out.println("Map id?");
				id = in.nextInt();
			}while(id <=0 && id >= 256);
			
			Map.openMap(id);
		}
			
		new Main();
	}
	
}
