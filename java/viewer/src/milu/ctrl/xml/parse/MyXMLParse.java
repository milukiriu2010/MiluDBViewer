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
import org.w3c.dom.NamedNodeMap;
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
		
		List<String>  dataLst = new ArrayList<>();
		
		System.out.println( "===========================================" );
		
		// https://stackoverflow.com/questions/18576711/how-to-search-for-a-specific-element-in-an-xml-using-a-scanner-in-java?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression xPathExpr = xPath.compile( path );
		
		NodeList hitNodes = (NodeList)xPathExpr.evaluate( document, XPathConstants.NODESET );
		for ( int i = 0; i< hitNodes.getLength(); i++ )
		{
			Node hitNode = hitNodes.item(i);
			NamedNodeMap attributes = hitNode.getAttributes();
			System.out.println( "  Hit[NodeName]    =>" + hitNode.getNodeName() );
			System.out.println( "  Hit[NodeValue]   =>" + hitNode.getNodeValue() );
			System.out.println( "  Hit[BaseURI]     =>" + hitNode.getBaseURI() );
			System.out.println( "  Hit[LocalName]   =>" + hitNode.getLocalName() );
			System.out.println( "  Hit[NamespaceURI]=>" + hitNode.getNamespaceURI() );
			System.out.println( "  Hit[Prefix]      =>" + hitNode.getPrefix() );
			if ( attributes != null )
			{
				for ( int j = 0; j < attributes.getLength(); j++ )
				{
					Node nodeAttr = attributes.item(j);
					System.out.println( "  Hit[Attributes][" + nodeAttr.getNodeName() + "]=>" + nodeAttr.getNodeValue() );
				}
			}
			System.out.println( "  Hit[TextContent] =>" + hitNode.getTextContent() );
			System.out.println( "*************************************************");
			if ( hitNode.getNodeValue() != null )
			{
				dataLst.add( hitNode.getNodeValue() );
			}
			else
			{
				NodeList hitNodeTextLst = hitNode.getChildNodes();
				
				// check "text" as child
				if ( hitNodeTextLst.getLength() > 0 )
				{
					// /MiluDBViewer0.1.9/MiluDBViewer0.1.9.tar.gz
					// /MiluDBViewer0.1.9/MiluDBViewer_Setup0.1.9.exe
					// https://sourceforge.net/projects/miludbviewer/files/MiluDBViewer0.1.9/MiluDBViewer0.1.9.tar.gz/download
					// System.out.println( "  Hit Node Val:" + hitNodeTextLst.item(0).getNodeValue() );
					dataLst.add( hitNodeTextLst.item(0).getNodeValue() );
				}
			}
		}
		System.out.println( "===========================================" );
		
		return dataLst;
	}
	
	public Document str2doc( String strXML ) 
			throws ParserConfigurationException, IOException, SAXException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		// http://www.baeldung.com/convert-string-to-input-stream
		InputStream inStreamXML = new ByteArrayInputStream(strXML.getBytes());
		Document document = builder.parse(inStreamXML);
		return document;
	}

	public Node searchNode( Node node, String strPath )
		throws XPathExpressionException
	{
		// https://stackoverflow.com/questions/18576711/how-to-search-for-a-specific-element-in-an-xml-using-a-scanner-in-java?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression xPathExpr = xPath.compile( strPath );
		
		return (Node)xPathExpr.evaluate( node, XPathConstants.NODE );
	}

	public NodeList searchNodeList( Node node, String strPath )
		throws XPathExpressionException
	{
		// https://stackoverflow.com/questions/18576711/how-to-search-for-a-specific-element-in-an-xml-using-a-scanner-in-java?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression xPathExpr = xPath.compile( strPath );
		
		return (NodeList)xPathExpr.evaluate( node, XPathConstants.NODESET );
	}
	
	public String searchNodeText( Node node, String strPath )
			throws XPathExpressionException
		{
			// https://stackoverflow.com/questions/18576711/how-to-search-for-a-specific-element-in-an-xml-using-a-scanner-in-java?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression xPathExpr = xPath.compile( strPath );
			
			Node nodeHit = (Node)xPathExpr.evaluate( node, XPathConstants.NODE );
			/*
			System.out.println( "*************************************************");
			System.out.println( "  Hit[NodeName]    =>" + nodeHit.getNodeName() );
			System.out.println( "  Hit[NodeValue]   =>" + nodeHit.getNodeValue() );
			System.out.println( "  Hit[BaseURI]     =>" + nodeHit.getBaseURI() );
			System.out.println( "  Hit[LocalName]   =>" + nodeHit.getLocalName() );
			System.out.println( "  Hit[NamespaceURI]=>" + nodeHit.getNamespaceURI() );
			System.out.println( "  Hit[Prefix]      =>" + nodeHit.getPrefix() );
			System.out.println( "  Hit[TextContent] =>" + nodeHit.getTextContent() );
			*/
			return nodeHit.getNodeValue();
		}
}
