package mapeditor.main;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;


public class Tools {

	public static final int WIDTH = 200, TAB_WIDTH = 25, TAB_HEIGHT = 90;
	
	public static List<Texture> textures = new ArrayList<Texture>();
	
	private static int selectedTab = 0;
	
	static
	{
		Util.useFont("Arial", Font.BOLD, 12, Color.BLACK);
	}
	
	public static void render()
	{
	
		//renders the tool pane in the left
		Util.renderQuad(Main.MAP_SIZE.getWidth(), 0, WIDTH, Main.MAP_SIZE.getHeight(), .4, .4, .4, 1);
		
		//render the tabs of the tool pane
		for(int i=0; i<Tabs.values().length; i++)
		{
			//renders the tab background
			float grayTone = selectedTab == i ? .4f : .6f;
			Util.renderQuad(Main.MAP_SIZE.getWidth()-TAB_WIDTH, 30+92*i, TAB_WIDTH, TAB_HEIGHT, grayTone, grayTone, grayTone, 1);
		
			//writes text on the tabs
			for(int j=0; j<Tabs.values()[i].name().length(); j++)
			{
				//TODO vertical center
				String tabName = String.valueOf(Tabs.values()[i].name().charAt(j));
				Util.write(tabName, Main.MAP_SIZE.getWidth()-Util.getTextWidth(tabName)/2-13, 30 + 93*i + Util.getFontHeight()*j);
			}
			
			int columns = 4;
			for(int j=0; j<Tabs.values()[i].getTextures().size() ; j++)
			{
				Util.render(Tabs.values()[i].getTextures().get(j), Main.MAP_SIZE.getWidth() + j%columns*35 + 25, j/columns*35 + 70, 32, 32);
			}
		}
		
		Util.write(Tabs.values()[selectedTab].name(), Main.MAP_SIZE.getWidth() + 70, 20); //TODO center aligned with Util.getTextWidth
				
				
	}
	
	public static void mouse()
	{
		while(Mouse.next())
		{
			if(!Mouse.getEventButtonState() && Mouse.getDX() == 0 && Mouse.getDY() == 0) //on mouse release
			{
				for(int i=0; i<Tabs.values().length; i++)
				{
					Point tabPos = new Point(Main.MAP_SIZE.getWidth()-TAB_WIDTH, 30+92*i);
					
					int x = Mouse.getX();
					int y = Main.MAP_SIZE.getHeight() - Mouse.getY() + 1;
					
					if(x >= tabPos.getX() && x <= tabPos.getX() + TAB_WIDTH &&
					   y >= tabPos.getY() && y <= tabPos.getY() + TAB_HEIGHT) //inside ith tab
					{
						selectedTab = i;
					}
					
				}
			}
		}
	}
	
	enum Tabs{
		
		Tile();
		
		private List<Texture> textures = new ArrayList<Texture>();
		
		private Tabs()
		{
						
			System.out.println(name().toLowerCase());
			
			File folder = new File("data/" + name().toLowerCase() + "/");
						
			File ids[] = folder.listFiles();
									
			for(File folderID: ids)
			{
				if(!folderID.getName().startsWith(".")){
					textures.add(Util.getTexture(name().toLowerCase() + "/" + folderID.getName() + "/texture.png"));
				}
			}

		}
		
		public List<Texture> getTextures()
		{
			return textures;
		}
		
	}
	
}
