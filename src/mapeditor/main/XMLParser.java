package mapeditor.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser
{

	private Document doc;

	public XMLParser(String fileName)
	{
		try
		{
			File xmlFile = new File("data/" + fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xmlFile);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setDocument(String fileName)
	{
		try
		{
			File xmlFile = new File("data/" + fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xmlFile);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * <br>
	 * <b>getAttribute</b> <br>
	 * <p>
	 * <tt>public String getAttribute(String uri, String attr)</tt>
	 * </p>
	 * Returns the value of the attribute <i>attr</i> of the element in the xml
	 * specified by the uri. Example: getAttribute("html/head/style", "type") <br>
	 * <br>
	 */
	public String getAttribute(String uri, String attr)
	{

		String nodePath[] = uri.contains("/") ? uri.substring(uri.indexOf("/") + 1).split("/") : new String[0];

		Element root = doc.getDocumentElement();

		Element targetNode = root;
		for (int i = 0; i < nodePath.length; i++)
		{
			targetNode = (Element) targetNode.getElementsByTagName(nodePath[i]).item(0);
		}

		return targetNode.getAttribute(attr);

	}

	/**
	 * 
	 * <br>
	 * <b>getAttributes</b> <br>
	 * <p>
	 * <tt>public Map<String, String> getAttributes(String uri)</tt>
	 * </p>
	 * Returns a map of all attributes and their values for an element specified
	 * by the uri. <br>
	 * <br>
	 */
	public Map<String, String> getAttributes(String uri)
	{
		String nodePath[] = uri.contains("/") ? uri.substring(uri.indexOf("/") + 1).split("/") : new String[0];
		Element root = doc.getDocumentElement();

		Element targetNode = root;
		for (int i = 0; i < nodePath.length; i++)
		{
			targetNode = (Element) targetNode.getElementsByTagName(nodePath[i]).item(0);
		}

		Map<String, String> attributes = new HashMap<String, String>();

		NamedNodeMap attrs = targetNode.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++)
		{
			attributes.put(attrs.item(i).getNodeName(), attrs.item(i).getNodeValue());
		}

		return attributes;
	}

	/**
	 * 
	 * <br>
	 * <b>getAttributes</b> <br>
	 * <p>
	 * <tt>public Map<String, String> getAttributes(String uri)</tt>
	 * </p>
	 * Returns a list of maps of all attributes and their values for all
	 * children of an element specified by the uri. <br>
	 * <br>
	 */
	public List<Map<String, String>> getChildrenAttributes(String uri)
	{
		String nodePath[] = uri.contains("/") ? uri.substring(uri.indexOf("/") + 1).split("/") : new String[0];
		Element root = doc.getDocumentElement();

		Element targetNode = root;
		for (int i = 0; i < nodePath.length; i++)
			targetNode = (Element) targetNode.getElementsByTagName(nodePath[i]).item(0);

		NodeList children = targetNode.getChildNodes();
		List<Map<String, String>> attributes = new ArrayList<Map<String, String>>();

		for (int i = 0; i < children.getLength(); i++)
		{
			Node child = children.item(i);
			NamedNodeMap attrs = child.getAttributes();

			attributes.add(new HashMap<String, String>());

			if (attrs != null)
			{
				for (int j = 0; j < attrs.getLength(); j++)
				{
					Node attribute = attrs.item(j);
					attributes.get(i).put(attribute.getNodeName(), attribute.getNodeValue());
				}
			}
		}

		attributes.removeAll(Collections.singleton(new HashMap<String, String>()));

		return attributes;
	}

}
