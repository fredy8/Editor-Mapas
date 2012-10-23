package mapeditor.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
				writer.write("\t</Tiles>\n");
				
				writer.write("\t<Portal>\n");
				writer.write("\t</Portal>\n");
				
				writer.write("\t<Monster>\n");
				writer.write("\t</Monster>\n");
				
				writer.write("\t<NPC>\n");
				writer.write("\t</NPC>\n");

				writer.write("\t<Object>\n");
				writer.write("\t</Object>\n");
			
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
