package milu.ctrl.xml.parse;

import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

// http://www.suzushin7.jp/entry/2017/10/14/how-to-get-xml-contents-by-using-dom-in-java
public class MyXMLParse 
{
	public List<String> analyze( String strXML, String path ) 
			throws ParserConfigurationException, IOException, SAXException, XPathExpressionException
	{
		//  rss/channel/item/title
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		// http://www.baeldung.com/convert-string-to-input-stream
		InputStream inStreamXML = new ByteArrayInputStream(strXML.getBytes());
		Document document = builder.parse(inStreamXML);
		/*
		Element root = document.getDocumentElement();

		System.out.println( "===========================================" );
		System.out.println( "Root:" + root.getTagName() );
		NodeList childNodes = root.getChildNodes();
		for ( int i = 0; i< childNodes.getLength(); i++ )
		{
			Node node = childNodes.item(i);
			System.out.println( "  Node:" + node.getNodeName() );
		}
		*/
		
		List<String>  pathLst = new ArrayList<>();
		
		System.out.println( "===========================================" );
		
		// https://stackoverflow.com/questions/18576711/how-to-search-for-a-specific-element-in-an-xml-using-a-scanner-in-java?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression xPathExpr = xPath.compile( path );
		
		NodeList hitNodes = (NodeList)xPathExpr.evaluate( document, XPathConstants.NODESET );
		for ( int i = 0; i< hitNodes.getLength(); i++ )
		{
			Node hitNode = hitNodes.item(i);
			// System.out.println( "  Hit Node Tag:" + hitNode.getNodeName() );
			NodeList hitNodeTextLst = hitNode.getChildNodes();
			
			if ( hitNodeTextLst.getLength() > 0 )
			{
				// /MiluDBViewer0.1.9/MiluDBViewer0.1.9.tar.gz
				// /MiluDBViewer0.1.9/MiluDBViewer_Setup0.1.9.exe
				// https://sourceforge.net/projects/miludbviewer/files/MiluDBViewer0.1.9/MiluDBViewer0.1.9.tar.gz/download
				// System.out.println( "  Hit Node Val:" + hitNodeTextLst.item(0).getNodeValue() );
				pathLst.add( hitNodeTextLst.item(0).getNodeValue() );
			}
		}
		System.out.println( "===========================================" );
		
		
		return pathLst;
	}
}
