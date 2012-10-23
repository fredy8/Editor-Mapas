package mapeditor.main;

import mapeditor.main.Tools.Tabs;

import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

public class Slot {

	private Point position;
	private Texture[] textures = new Texture[5];
	private boolean[] locks = new boolean[5]; //lock means the texture is set, not rendered by hover

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
		Util.render(textures[type.id()], position.getX(), position.getY(), 32, 32);
	}
	
	public void removeHover()
	{
		for(int i=0; i<textures.length; i++)
		{
			if(locks[i] != true)
			{
				textures[i] = null;
			}
		}
	}
	
	public void lock(Tabs type)
	{
		locks[type.id()] = true;
	}
	
	public void remove(Tabs type)
	{
		textures[type.id()] = null;
		locks[type.id()] = false;
	}
	
	
}
