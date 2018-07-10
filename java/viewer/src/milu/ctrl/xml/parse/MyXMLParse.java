package milu.ctrl.xml.parse;

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
