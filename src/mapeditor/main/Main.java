package mapeditor.main;

import org.lwjgl.LWJGLException;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.Dimension;

import static org.lwjgl.opengl.GL11.*;


public class Main {
	
	public static final Dimension GRID_SIZE = new Dimension(640, 640);

	
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
			Tools.mouse();
			Tools.keyboard();
			
			glClear(GL_COLOR_BUFFER_BIT);
		
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
		new Main();
	}
	
}
