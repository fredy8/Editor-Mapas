package mapeditor.main;

import mapeditor.main.Tools.Tabs;

import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

public class Slot {

	private Point position;
	private Texture[] textures = new Texture[5];

	public Slot(Point position)
	{
		this.position = position;
	}
	
	public Texture get(Tabs type)
	{
		return textures[type.index()];
	}

	public void render(Tabs type)
	{
		if(textures[type.index()] != null)
		{
				Util.render(textures[type.index()],
							position.getX() - Map.getRenderOffset().getX()*Main.TILE_SIZE, //x
							position.getY() - Map.getRenderOffset().getY()*Main.TILE_SIZE, //y
							type.getSize(type.getTextures().indexOf(textures[type.index()])).getWidth()*Main.TILE_SIZE, //width
							type.getSize(type.getTextures().indexOf(textures[type.index()])).getHeight()*Main.TILE_SIZE, //height
							textures[type.index()].getTextureWidth(), //texture width
							textures[type.index()].getTextureHeight()); //texture height
			}
	}
	
	public void remove(Tabs type)
	{
		textures[type.index()] = null;
	}

	public void add(Texture texture, Tabs type) {
		textures[type.index()] = texture;
	}
	
	
}
