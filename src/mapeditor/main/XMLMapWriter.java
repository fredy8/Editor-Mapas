package mapeditor.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mapeditor.main.Tools.Tabs;

public class XMLMapWriter {

	BufferedWriter writer;
	
	public XMLMapWriter(String path)
	{
		try {
			File file = new File(path);
			if(!file.exists())
			{
				file.createNewFile();
			}
			writer = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void dumpFile()
	{		
		try {
			writer.write("<Map name=\"" + Map.NAME + "\" width=\"" + Map.getWidth() + "\" height=\"" + Map.getHeight() + "\">\n");
		
				writer.write("\t<Tiles>\n");
				
				int tileID = Tabs.Tile.id() + Tabs.values()[0].getTextures().indexOf(Map.get(0, 0).get(Tabs.values()[0]));
				List<Integer> idAmount = new ArrayList<Integer>();
				
				idAmount.add(tileID);
				idAmount.add(0);
				
				for(int i=0; i<Map.getWidth(); i++)
				{
					for(int j=0; j<Map.getHeight(); j++)
					{
						if(Tabs.Tile.id() + Tabs.values()[0].getTextures().indexOf(Map.get(j, i).get(Tabs.values()[0])) == tileID)
							idAmount.set(idAmount.size() - 1, idAmount.get(idAmount.size() - 1) + 1);
						else
						{
							tileID = Tabs.Tile.id() + Tabs.values()[0].getTextures().indexOf(Map.get(i, j).get(Tabs.values()[0]));
							idAmount.add(tileID);
							idAmount.add(1);
						}
					}
				}
				
				for(int i=0; i<idAmount.size(); i+=2)
					writer.write("\t\t<Tile id=\"" + idAmount.get(i) + "\" amount=\"" + idAmount.get(i+1) + "\" />\n");
								
				writer.write("\t</Tiles>\n");
				
				for(int i=1; i<Tabs.values().length; i++)
				{
					writer.write("\t<" + Tabs.values()[i].name() + "s>\n");
					for(int x=0; x<Map.getWidth(); x++)
						for(int y=0; y<Map.getHeight(); y++)
							if(Tabs.values()[i].getTextures().contains(Map.get(x, y).get(Tabs.values()[i])))
									writer.write("\t\t<" + Tabs.values()[i].name() +" id=\"" + (Tabs.values()[i].id()+Tabs.values()[i].getTextures().indexOf(Map.get(x, y).get(Tabs.values()[i]))) + "\" x=\"" + x + "\" y=\"" + y + "\" />\n");						
					writer.write("\t</" + Tabs.values()[i].name() + "s>\n");
				}
			
			writer.write("</Map>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
