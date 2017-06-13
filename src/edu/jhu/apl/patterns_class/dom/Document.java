package edu.jhu.apl.patterns_class.dom;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.Writer;

import org.w3c.dom.DOMException;

import edu.jhu.apl.patterns_class.XMLSerializer;
import edu.jhu.apl.patterns_class.XMLTokenizer;
import edu.jhu.apl.patterns_class.XMLValidator;

//Since Node is now wrapped by org.w3c.dom, this Document class acts as an adapter to the rest of the code
public class Document extends Node implements Cloneable
{
	public Document()
	{
		super(null, edu.jhu.apl.patterns_class.dom.Node.DOCUMENT_NODE);
		document	= this;
	}
	
	//DocumentFactoryMethod
	public static class DocumentFactory
	{
		public static Document getDocument(){
			return new edu.jhu.apl.patterns_class.dom.Document();
		}
	}
	
	//FileFactoryMethod
	public static class FileFactory
	{
		public static java.io.File getFile(String filename){
			return new java.io.File(filename);
		}
	}
	
	//XMLSerializerFactoryMethod
	public static class XMLSerializerFactory
	{
		public static XMLSerializer getXMLSerializer(java.io.BufferedWriter writer) throws FileNotFoundException{
			return new XMLSerializer(writer);
		}
	}
	
	//XMLTokenizerFactoryMethod
	public static class XMLTokenizerFactory
	{
		public static XMLTokenizer getXMLTokenizer(String arg) throws FileNotFoundException{
			return new XMLTokenizer(arg);
		}
	}
	
	//XMLValidatorFactoryMethod
	public static class XMLValidatorFactory
	{
		public static XMLValidator getXMLValidator() throws FileNotFoundException{
			return new XMLValidator();
		}
	}
	
	//BufferedWriterFactoryMethod
	public static class BufferedWriterFactory
	{
		public static java.io.BufferedWriter getBufferedWriter(java.io.File file){
			try {
				return new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(file)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
			
		}
	}
	
	//BufferedReaderFactoryMethod
	public static class BufferedReaderFactory
	{
		public static java.io.BufferedReader getBufferedReader(String filename){
			try {
				return new java.io.BufferedReader(new java.io.FileReader(filename));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
		}
	}
	
	
	//
	// Implemented Document members.
	//
	public org.w3c.dom.Element createElement(String tagName) throws org.w3c.dom.DOMException
	  //{return (org.w3c.dom.Element) new Element(tagName,this);}
		{return (org.w3c.dom.Element) new Element(tagName);}
	public org.w3c.dom.Text createTextNode(String data) //{ return (org.w3c.dom.Text) new Text(data, this); }
		{ return (org.w3c.dom.Text) new Text(data); }
	public org.w3c.dom.Attr createAttribute(String name) throws org.w3c.dom.DOMException
	  //{ return (org.w3c.dom.Attr) new Attr(name, this); }
		{ return (org.w3c.dom.Attr) new Attr(name); }
	public org.w3c.dom.Element getDocumentElement()
	{
		for (java.util.ListIterator i = ((NodeList )getChildNodes()).listIterator(0); i.hasNext();)
		{
			edu.jhu.apl.patterns_class.dom.replacement.Node	element =
			  (edu.jhu.apl.patterns_class.dom.replacement.Node )i.next();

			if (element instanceof edu.jhu.apl.patterns_class.dom.replacement.Element)
				return (org.w3c.dom.Element )element;
		}

		return null;
	}
	
	//Clone method to implement Prototype Pattern
	public Object clone(){
		Object clone = null;
		
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e){
			e.printStackTrace();
		}
		return clone;
	}

	//
	// Unimplemented Document members.
	//
	public org.w3c.dom.DOMImplementation getImplementation() { return null; }
	public org.w3c.dom.DocumentType getDoctype() { return null; }
	public org.w3c.dom.DocumentFragment createDocumentFragment() { return null; }
	public org.w3c.dom.Comment createComment(String data) { return null; }
	public org.w3c.dom.CDATASection createCDATASection(String data) throws org.w3c.dom.DOMException { return null; }
	public org.w3c.dom.ProcessingInstruction createProcessingInstruction(String target, String data)
	  throws org.w3c.dom.DOMException
	  { return null; }
	public org.w3c.dom.EntityReference createEntityReference(String name) throws org.w3c.dom.DOMException { return null; }
	public edu.jhu.apl.patterns_class.dom.replacement.Node
	  importNode(edu.jhu.apl.patterns_class.dom.replacement.Node importedNode, boolean deep) throws org.w3c.dom.DOMException
	  { return null; }
	public org.w3c.dom.Element createElementNS(String namespaceURI, String qualifiedName)
	  throws org.w3c.dom.DOMException
	  { return null; }
	public org.w3c.dom.Attr createAttributeNS(String namespaceURI, String qualifiedName)
	  throws org.w3c.dom.DOMException
	  { return null; }
	public org.w3c.dom.NodeList getElementsByTagNameNS(String namespaceURI, String localName)
	  { return null; }
	public org.w3c.dom.Element getElementById(String elementId) { return null; }
	public org.w3c.dom.Node cloneNode(boolean deep) { return null; }
	public edu.jhu.apl.patterns_class.dom.replacement.Node
	  renameNode(edu.jhu.apl.patterns_class.dom.replacement.Node n, String namespaceURI, String qualifiedName) { return null; }
	public void normalizeDocument() {}
	public org.w3c.dom.DOMConfiguration getDomConfig() { return null; }
	public edu.jhu.apl.patterns_class.dom.replacement.Node
	  adoptNode(edu.jhu.apl.patterns_class.dom.replacement.Node source) { return null; }
	public void setDocumentURI(String documentURI) {}
	public String getDocumentURI() { return null; }
	public void setStrictErrorChecking(boolean strictErrorChecking) {}
	public boolean getStrictErrorChecking() { return false; }
	public void setXmlVersion(String xmlVersion) {}
	public String getXmlVersion() { return null; }
	public void setXmlStandalone(boolean xmlStandalone) {}
	public boolean getXmlStandalone() { return false; }
	public String getXmlEncoding() { return null; }
	public String getInputEncoding() { return null; }
	
	public org.w3c.dom.NodeList getElementsByTagName(String tagname) {
		// TODO Auto-generated method stub
		return null;
	}
	public org.w3c.dom.Node importNode(org.w3c.dom.Node importedNode, boolean deep) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public org.w3c.dom.Node adoptNode(org.w3c.dom.Node source) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}
	public org.w3c.dom.Node renameNode(org.w3c.dom.Node n, String namespaceURI, String qualifiedName)
			throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}
}

//Event Handler implemented using CoR pattern
class Handler
{
    private static java.util.Random s_rn = new java.util.Random();
    private static int s_next = 1;
    private int m_id = s_next++;
    private Handler m_next;

    public void add(Handler next)
    {
        if (m_next == null)
          m_next = next;
        else
          m_next.add(next);
    }
    public void wrap_around(Handler root)
    {
        if (m_next == null)
          m_next = root;
        else
          m_next.wrap_around(root);
    }
    public void handle(int num)
    {
        if (s_rn.nextInt(4) != 0)
        {
            System.out.print(m_id + "-busy  ");
            m_next.handle(num);
        }
        else
          System.out.println(m_id + "-handled-" + num);
    }
}
