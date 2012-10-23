package mapeditor.main;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;
import static org.lwjgl.opengl.GL11.*;

public class Tools {
	
	//TODO read object's size and render as that size when grabbed

	public static final int WIDTH = 200, TAB_WIDTH = 25, TAB_HEIGHT = 90, COLUMNS = 4;
	public static List<Texture> textures = new ArrayList<Texture>();
	private static int selectedTab = 0;
	private static Texture grabbedTexture;
	
	static
	{
		Util.useFont("Arial", Font.BOLD, 12, Color.BLACK);
	}
	
	public static void render()
	{
		
		//renders the tool pane in the left
		Util.renderQuad(Main.GRID_SIZE.getWidth(), 0, WIDTH, Main.GRID_SIZE.getHeight(), .4, .4, .4, 1);
		
		//renders the grabbed texture
				if(grabbedTexture != null)
				{		
					int x = Mouse.getX();
					int y = Main.GRID_SIZE.getHeight() - Mouse.getY() + 1;
					if(x < Main.GRID_SIZE.getWidth() && y < Main.GRID_SIZE.getHeight())
					{
						Util.render(grabbedTexture, (int)(x/32+.5)*32, (int)(y/32+.5)*32, 32, 32, grabbedTexture.getTextureWidth(), grabbedTexture.getTextureHeight());
					}else
					{
						glColor4f(1, 1, 1, .5f);
						Util.render(grabbedTexture, x, y, 32, 32, grabbedTexture.getTextureWidth(), grabbedTexture.getTextureHeight());
						glColor4f(1, 1, 1, 1f);
					}		
				}
		
		//render the tabs of the tool pane
		for(int i=0; i<Tabs.values().length; i++)
		{
			//renders the tab background
			float grayTone = selectedTab == i ? .4f : .6f;
			Util.renderQuad(Main.GRID_SIZE.getWidth()-TAB_WIDTH, 30+92*i, TAB_WIDTH, TAB_HEIGHT, grayTone, grayTone, grayTone, 1);
		
			//writes text on the tabs
			for(int j=0; j<Tabs.values()[i].name().length(); j++)
			{
				//TODO vertical center alignment
				String tabName = String.valueOf(Tabs.values()[i].name().charAt(j));
				Util.write(tabName, Main.GRID_SIZE.getWidth()-Util.getTextWidth(tabName)/2-13, 30 + 93*i + Util.getFontHeight()*j);
			}
			
			//render the entity textures
			if(selectedTab == i)
			{
				for(int j=0; j<Tabs.values()[i].getTextures().size() ; j++)
				{
					Texture texture = Tabs.values()[i].getTextures().get(j);
					Util.render(texture, Main.GRID_SIZE.getWidth() + j%COLUMNS*35 + 25, j/COLUMNS*35 + 70, 32, 32, texture.getTextureWidth(), texture.getTextureHeight());
				}
			}
		}
		
		Util.write(Tabs.values()[selectedTab].name(), Main.GRID_SIZE.getWidth() + 70, 20); //TODO center aligned with Util.getTextWidth
		

	}
	
	public static void mouse()
	{
		while(Mouse.next())
		{
			if(!Mouse.getEventButtonState()) 
			{
					if (Mouse.getDX() == 0 && Mouse.getDY() == 0) //on mouse release
				{
					
					int x = Mouse.getX();
					int y = Main.GRID_SIZE.getHeight() - Mouse.getY() + 1;
					
					//Checks for tab click
					for(int i=0; i<Tabs.values().length; i++) //TODO can be simplified to O(1)
					{
						Point tabPos = new Point(Main.GRID_SIZE.getWidth()-TAB_WIDTH, 30+92*i);
						
						if(x >= tabPos.getX() && x <= tabPos.getX() + TAB_WIDTH &&
						   y >= tabPos.getY() && y <= tabPos.getY() + TAB_HEIGHT) //inside ith tab
						{
							selectedTab = i;
						}
					}
					
					//check for texture click
					for(int i=0; i<Tabs.values()[selectedTab].getTextures().size(); i++) //TODO can be simplified to O(1)
					{
						
						Point texturePos = new Point(Main.GRID_SIZE.getWidth() + i%COLUMNS*35 + 25, i/COLUMNS*35 + 70);
						
						if(x >= texturePos.getX() && x <= texturePos.getX() + 32 &&
						   y >= texturePos.getY() && y <= texturePos.getY() + 32) //click on the texture
						{
							grabbedTexture = Tabs.values()[selectedTab].getTextures().get(i);
						}
					}
				}
			}
		}
	}
	
	enum Tabs{
		
		Tile(0), Portal(1), Monster(2), NPC(3), Object(4);
		
		private List<Texture> textures = new ArrayList<Texture>();
		private int id;
		
		private Tabs(int id)
		{		
			this.id = id;
			
			File folder = new File("data/" + name().toLowerCase() + "/");
						
			File ids[] = folder.listFiles();
									
			for(File folderID: ids)
			{
				if(!folderID.getName().contains(".")){
					textures.add(Util.getTexture(name().toLowerCase() + "/" + folderID.getName()
								+ (this.name().equals("Monster") ? "/front.png" : "/texture.png")));
				}
			}
		}
		
		public List<Texture> getTextures()
		{
			return textures;
		}

		public int id() {
			return id;
		}
		
	}

	public static void keyboard() {
		while(Keyboard.next())
		{
			if(Keyboard.getEventKeyState())
			{
				if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
				{
					grabbedTexture = null;
				}
			}
		}
	}
	
}
