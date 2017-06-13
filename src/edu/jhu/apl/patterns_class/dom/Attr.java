package edu.jhu.apl.patterns_class.dom;

public class Attr extends Node implements edu.jhu.apl.patterns_class.dom.replacement.Attr
{
	public Attr(String tagName)
	{
		super(tagName, org.w3c.dom.Node.ATTRIBUTE_NODE);
		
	}

	Attr(String tagName, String value, Document document)
	{
		super(tagName, org.w3c.dom.Node.ATTRIBUTE_NODE);
		this.document	= document;
		setValue(value);
	}
	
	//Because of flyweight, document now has to be set since that is an extrinsic value
	public void setDocument(Document document){
		this.document	= document;
	}

	//
	// Implemented Attr members.
	//
	public String getName()
	{
		return getNodeName();
	}
	public String getValue()
	{
		return getNodeValue();
	}
	public void setValue(String value)
	{
		// TODO:  Check for readonly status.  NO_MODIFICATION_ALLOWED_ERR

		setNodeValue(value);
	}
	public edu.jhu.apl.patterns_class.dom.replacement.Element getOwnerElement()
	{
		return (Element )getParentNode();
	}

	//
	// Unimplemented Attr members.
	//
	public boolean getSpecified()	{ return true; }
	public boolean isId()		{ return false; }
	public org.w3c.dom.TypeInfo getSchemaTypeInfo() { return null; }
	
	
	//Attr is a leaf so it cannot have children so the following must return null or false
	public edu.jhu.apl.patterns_class.dom.replacement.NodeList getChildNodes()	{ return null; }
	public org.w3c.dom.Node getFirstChild()
	  {return null;}
	public org.w3c.dom.Node getLastChild()
	  {return null;}
	public edu.jhu.apl.patterns_class.dom.replacement.Node
	  insertBefore(edu.jhu.apl.patterns_class.dom.replacement.Node newChild,
	  edu.jhu.apl.patterns_class.dom.replacement.Node refChild) throws org.w3c.dom.DOMException
	{ return null; }
	public edu.jhu.apl.patterns_class.dom.replacement.Node
	  replaceChild(edu.jhu.apl.patterns_class.dom.replacement.Node newChild,
	  edu.jhu.apl.patterns_class.dom.replacement.Node oldChild) throws org.w3c.dom.DOMException
	{ return null; }
	public edu.jhu.apl.patterns_class.dom.replacement.Node removeChild(edu.jhu.apl.patterns_class.dom.replacement.Node oldChild)
	  throws org.w3c.dom.DOMException
	{ return null; }
	public edu.jhu.apl.patterns_class.dom.replacement.Node appendChild(edu.jhu.apl.patterns_class.dom.replacement.Node newChild)
	  throws org.w3c.dom.DOMException
	{ return null; }
	public boolean hasChildNodes()					{ return false; }
}
