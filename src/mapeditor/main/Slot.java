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
		return textures[type.id()];
	}

	public void addHover(Texture texture, Tabs type) {
		textures[type.id()] = texture;
	}
	
	public void render(Tabs type)
	{
		if(textures[type.id()] != null)
			Util.render(textures[type.id()], position.getX() - Map.getRenderOffset().getX()*Main.TILE_SIZE, position.getY() - Map.getRenderOffset().getY()*Main.TILE_SIZE, Main.TILE_SIZE, Main.TILE_SIZE, textures[type.id()].getTextureWidth(), textures[type.id()].getTextureHeight());
	}
	
	public void remove(Tabs type)
	{
		textures[type.id()] = null;
	}

	public void add(Texture texture, Tabs type) {
		textures[type.id()] = texture;
	}
	
	
}
