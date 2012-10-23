package mapeditor.main;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Util
{

	private static ArrayList<UnicodeFont> fonts = new ArrayList<UnicodeFont>();
	private static UnicodeFont currentFont;
	private static Color currentColor;

	@SuppressWarnings("unchecked")
	public static void useFont(String fontName, int style, int size, Color c)
	{

		boolean fontExists = false;
		for (UnicodeFont font : fonts)
		{
			if (font.getFont().getFontName().contains(fontName.replaceAll(" ", ""))
					&& font.getFont().getStyle() == style && font.getFont().getSize() == size)
			{
				fontExists = true;
				currentFont = font;
				if (currentColor != c)
				{
					currentFont.getEffects().add(new ColorEffect(c));
					try
					{
						currentFont.loadGlyphs();
					} catch (SlickException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		if (!fontExists)
		{
			UnicodeFont f = new UnicodeFont(new Font(fontName, style, size));
			f.addAsciiGlyphs();
			f.getEffects().add(new ColorEffect(c));
			try
			{
				f.loadGlyphs();
			} catch (SlickException e)
			{
				e.printStackTrace();
			}
			fonts.add(f);
			currentFont = f;
			currentColor = c;
		}
	}

	public static void write(String text, float x, float y)
	{
		currentFont.drawString(x, y, text);
		GL11.glDisable(GL11.GL_TEXTURE_2D); // slick.UnicodeFont.drawString
											// enables GL_TEXTURE_2D but doesn't
											// disables it
	}

	public static int getFontHeight()
	{
		return currentFont.getHeight("Q");
	}

	public static int getTextWidth(String str)
	{
		return currentFont.getWidth(str);
	}

	public static String[] tokenizeText(String text, int widthLimit, int maxLines)
	{
		ArrayList<String> tokens = new ArrayList<String>();

		while (text.contains(" "))
		{
			String token = text.substring(0, text.indexOf(' ') + 1);
			tokens.add(token);
			text = text.substring(text.indexOf(' ') + 1);
		}
		tokens.add(text);

		int lineLength = 0;
		ArrayList<String> lines2 = new ArrayList<String>();
		int tokenCounter = 0;
		int lineCounter = 0;

		while (tokenCounter < tokens.size())
		{
			String word = tokens.get(tokenCounter);
			int wordLength = getTextWidth(word) + 16;
			if (lineLength + wordLength > widthLimit)
			{
				if (lineCounter == maxLines - 1)
					break;
				lineLength = 0;
				lineCounter++;
			}
			if (lines2.size() <= lineCounter)
				lines2.add(word);
			else
				lines2.set(lineCounter, lines2.get(lineCounter) + word);
			lineLength += wordLength;
			tokenCounter++;
		}

		String tokenizedText[] = new String[lines2.size()];

		for (int i = 0; i < lines2.size(); i++)
		{
			if (lines2.get(i).charAt(lines2.get(i).length() - 1) == ' ')
				lines2.set(i, lines2.get(i).substring(0, lines2.get(i).length() - 1));
			tokenizedText[i] = lines2.get(i);
		}
		return tokenizedText;
	}

	public static Texture getTexture(String path)
	{
		try
		{
			return TextureLoader.getTexture("PNG", new FileInputStream(new File("data/" + path)));
		} catch (FileNotFoundException e)
		{
			System.out.println("File Not found: " + "data/" + path);
		} catch (IOException e)
		{
		}
		return null;
	}
	
	public static void render(Texture texture, int x, int y, int sizeX, int sizeY, float imageWidth, float imageHeight)
	{	
		glEnable(GL_TEXTURE_2D);
		texture.bind();
		glLoadIdentity();
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);
			glTexCoord2f(imageWidth/texture.getImageWidth(), 0);
			glVertex2f(sizeX, 0);
			glTexCoord2f(imageWidth/texture.getImageWidth(), imageHeight/texture.getImageHeight());
			glVertex2f(sizeX, sizeY);
			glTexCoord2f(0, imageHeight/texture.getImageHeight());
			glVertex2f(0, sizeY);
		glEnd();
		glLoadIdentity();
		glDisable(GL_TEXTURE_2D);
	}
	
	public static void render(Texture texture, int x, int y, int sizeX, int sizeY)
	{
		render(texture, x, y, sizeX, sizeY, sizeX, sizeY);
	}
	
	public static void renderQuad(int x, int y, int width, int height, double red, double green, double blue, double alpha)
	{
		glLoadIdentity();
		glTranslatef(x, y, 0);
		glColor4f((float)red, (float)green, (float)blue, (float)alpha);
		glBegin(GL_QUADS);
			glVertex2f(0, 0);
			glVertex2f(width, 0);
			glVertex2f(width, height);
			glVertex2f(0, height);
		glEnd();
		glColor4f(1, 1, 1, 1);
		glLoadIdentity();
	}
	
	public static void renderLine(int x1, int y1, int x2, int y2, int width)
	{
		glLoadIdentity();
		glLineWidth(width);
		glBegin(GL_LINES);
			glVertex2f(x1, y1);
			glVertex2f(x2, y2);
			glVertex2f(x1, y1);
			glVertex2f(x2, y2);
		glEnd();
		glLoadIdentity();
	}
	
	

}
