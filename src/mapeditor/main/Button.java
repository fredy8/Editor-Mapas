package mapeditor.main;

import java.awt.Dimension;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;

public class Button {

	private String text;
	private boolean enabled = true;
	private Point position;
	private static final int BORDER_WIDTH = 5;
	private Dimension size;
	private Runnable action;
	
	public Button(String text, Point position, Runnable action)
	{
		this.text = text;
		this.position = position;
		this.action = action;
		size = new Dimension(Util.getTextWidth(text) + BORDER_WIDTH*2, Util.getFontHeight() + BORDER_WIDTH*2);
	}
	
	public void render()
	{
		
		Util.renderQuad(position.getX(), position.getY(), getWidth(), getHeight(), .3, .3, .3, 1);
		Util.write(text, position.getX()+BORDER_WIDTH, position.getY() + BORDER_WIDTH);
	}
	
	public int getWidth()
	{
		return (int) size.getWidth();
	}
	
	public int getHeight()
	{
		return (int) size.getHeight();
	}
	
	public void mouse()
	{
		if(Mouse.getEventButtonState() && isEnabled())
		{
			int x = Mouse.getX();
			int y = Main.GRID_SIZE.getHeight() - Mouse.getY() + 1;
			if(x > position.getX() && x < position.getX() + getWidth() &&
			   y > position.getY() && y < position.getY() + getHeight())
			{
				action.run();
			}
		}
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
}
