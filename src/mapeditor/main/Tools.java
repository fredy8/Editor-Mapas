package mapeditor.main;

import static org.lwjgl.opengl.GL11.glColor4f;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

public class Tools {
	
	//TODO read object's size and render as that size when grabbed

	public static final int WIDTH = 200, TAB_WIDTH = 25, TAB_HEIGHT = 90, COLUMNS = (int) ((WIDTH*.85)/(Main.TILE_SIZE+4)+.5);
	public static List<Texture> textures = new ArrayList<Texture>();
	private static int selectedTab = 0;
	private static Texture grabbedTexture;
	private static Button save;
	private static long lastPressF, lastPressC, lastPressD;
	
	static
	{
		Util.useFont("Arial", Font.BOLD, 12, Color.BLACK);
		
		save = new Button("Save", new Point(Main.GRID_SIZE.getWidth() + WIDTH/4, Main.GRID_SIZE.getHeight() - 100), new Runnable(){
			public void run()
			{
				XMLMapWriter writer = new XMLMapWriter("test.xml");
				writer.dumpFile();
				writer.close();
			}
		});
		
	}
	
	public static void render()
	{
		//renders the tool pane in the left
		Util.renderQuad(Main.GRID_SIZE.getWidth(), 0, WIDTH, Main.GRID_SIZE.getHeight(), .4, .4, .4, 1);
		
		//writes instructions
		Util.write("Double-click:", Main.GRID_SIZE.getWidth() + 10, Main.GRID_SIZE.getHeight() - 200);
		Util.write("F - Fill all tiles.", Main.GRID_SIZE.getWidth() + 20, Main.GRID_SIZE.getHeight() - 180);
		Util.write("C - Clear all tiles.", Main.GRID_SIZE.getWidth() + 20, Main.GRID_SIZE.getHeight() - 160);
		Util.write("D - Fill all empty tiles.", Main.GRID_SIZE.getWidth() + 20, Main.GRID_SIZE.getHeight() - 140);
		
		//renders the save/open button
		save.render();
		
		//renders the grabbed texture
		if (grabbedTexture != null) {
			int x = Mouse.getX();
			int y = Main.GRID_SIZE.getHeight() - Mouse.getY() + 1;
			if (x < Math.min(Main.GRID_SIZE.getWidth(), Map.getWidth()*Main.TILE_SIZE) && y < Math.min(Main.GRID_SIZE.getHeight(), Map.getHeight()*Main.TILE_SIZE)) {
				Util.render(grabbedTexture, (int) (x / Main.TILE_SIZE + .5) * Main.TILE_SIZE, (int) (y / Main.TILE_SIZE + .5)
						* Main.TILE_SIZE, Main.TILE_SIZE, Main.TILE_SIZE, grabbedTexture.getTextureWidth(), grabbedTexture.getTextureHeight());
			} else {
				glColor4f(1, 1, 1, .5f);
				Util.render(grabbedTexture, x, y, Main.TILE_SIZE,
						Main.TILE_SIZE, grabbedTexture.getTextureWidth(),
						grabbedTexture.getTextureHeight());
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
					Util.render(texture, Main.GRID_SIZE.getWidth() + j%COLUMNS*(Main.TILE_SIZE + 4) + 25, j/COLUMNS*(Main.TILE_SIZE+4) + 70, Main.TILE_SIZE, Main.TILE_SIZE, texture.getTextureWidth(), texture.getTextureHeight());
				}
			}
		}
		
		Util.write(Tabs.values()[selectedTab].name(), Main.GRID_SIZE.getWidth() + 70, 20); //TODO center aligned with Util.getTextWidth
		
	}
	
	public static void mouse()
	{
		while(Mouse.next())
		{
			int x = Mouse.getX();
			int y = Main.GRID_SIZE.getHeight() - Mouse.getY() + 1;
			
			save.mouse();
			
			if(Mouse.getEventButtonState()) 
			{
				if (Mouse.getDX() == 0 && Mouse.getDY() == 0) //on mouse release
				{
					
					//Checks for tab click
					for(int i=0; i<Tabs.values().length; i++) //TODO can be simplified to O(1)
					{
						Point tabPos = new Point(Main.GRID_SIZE.getWidth()-TAB_WIDTH, 30+92*i);
						
						if(x >= tabPos.getX() && x <= tabPos.getX() + TAB_WIDTH &&
						   y >= tabPos.getY() && y <= tabPos.getY() + TAB_HEIGHT) //inside ith tab
						{
							selectedTab = i;
							grabbedTexture = null;
							return;
						}
					}
					
					//check for texture click
					for(int i=0; i<Tabs.values()[selectedTab].getTextures().size(); i++) //TODO can be simplified to O(1)
					{
						
						Point texturePos = new Point(Main.GRID_SIZE.getWidth() + i%COLUMNS*(Main.TILE_SIZE+4) + 25, i/COLUMNS*(Main.TILE_SIZE+4) + 70);
						
						if(x >= texturePos.getX() && x <= texturePos.getX() + Main.TILE_SIZE &&
						   y >= texturePos.getY() && y <= texturePos.getY() + Main.TILE_SIZE) //click on the texture
						{
							grabbedTexture = Tabs.values()[selectedTab].getTextures().get(i);
						}
					}
				}
			}
			
			if(x < Main.GRID_SIZE.getWidth() && y < Main.GRID_SIZE.getHeight())
			{
				if(grabbedTexture != null)
				{
					if(Mouse.isButtonDown(0) && (selectedTab == 0 ? true : (Mouse.getDX() == 0 && Mouse.getDY() == 0)))
					{
						Slot slot = Map.get((int)(x/Main.TILE_SIZE+.5) + Map.getRenderOffset().getX(), (int)(y/Main.TILE_SIZE+.5) + Map.getRenderOffset().getY());
						if(slot != null)
							slot.add(grabbedTexture, Tabs.values()[selectedTab]);
					}else if((Mouse.isButtonDown(1) && (selectedTab == 0 ? true : (Mouse.getDX() == 0 && Mouse.getDY() == 0))))
					{
						Slot slot = Map.get((int)(x/Main.TILE_SIZE+.5) + Map.getRenderOffset().getX(), (int)(y/Main.TILE_SIZE+.5) + Map.getRenderOffset().getY());
						if(slot != null)
							slot.remove(Tabs.values()[selectedTab]);
					}
				}
			}
		}
	}
	
	public static void keyboard() {
		
		if(Keyboard.getEventKeyState())
		{
			switch(Keyboard.getEventKey()){
			case Keyboard.KEY_ESCAPE:
				grabbedTexture = null;
				break;
			case Keyboard.KEY_F:
				if(System.currentTimeMillis() < lastPressF + 250 && selectedTab == 0)
				{
					for(int i=0; i<Map.getWidth(); i++)
					{
						for(int j=0; j<Map.getHeight(); j++)
						{
							if(grabbedTexture != null)
							Map.get(i, j).add(grabbedTexture, Tabs.values()[selectedTab]);
						}
					}
				}else
				{
					lastPressF = System.currentTimeMillis();
				}
				break;	
			case Keyboard.KEY_C:
			if(System.currentTimeMillis() < lastPressC + 250)
			{
				for(int i=0; i<Map.getWidth(); i++)
				{
					for(int j=0; j<Map.getHeight(); j++)
					{
						Map.get(i, j).remove(Tabs.values()[selectedTab]);
					}
				}
			}else
			{
				lastPressC = System.currentTimeMillis();
			}
			break;	
			case Keyboard.KEY_D:
				if(System.currentTimeMillis() < lastPressD + 250 && selectedTab == 0)
				{
					for(int i=0; i<Map.getWidth(); i++)
					{
						for(int j=0; j<Map.getHeight(); j++)
						{
							if(grabbedTexture != null)
								if(Map.get(i, j) == null)
									Map.get(i, j).add(grabbedTexture, Tabs.values()[selectedTab]);
						}
					}
				}else
				{
					lastPressD = System.currentTimeMillis();
				}
				break;	

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
					textures.add(Util.getTexture("data/" + name().toLowerCase() + "/" + folderID.getName()
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
	
}
