package mapeditor.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
			writer.write("<Map>\n");
		
				writer.write("\t<Tiles>\n");
				//TODO
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
