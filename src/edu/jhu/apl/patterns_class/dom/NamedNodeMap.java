package edu.jhu.apl.patterns_class.dom;

public class NamedNodeMap extends NodeList implements edu.jhu.apl.patterns_class.dom.replacement.NamedNodeMap
{
	private edu.jhu.apl.patterns_class.dom.replacement.Document	document	= null;

	public NamedNodeMap(edu.jhu.apl.patterns_class.dom.replacement.Document document)
	{
		this.document	= document;
	}

	public org.w3c.dom.Node getNamedItem(String name)
	{
		for (java.util.ListIterator i = listIterator(0); i.hasNext();)
		{
			Node	node = (Node )i.next();

			if (node.getNodeName().compareTo(name) == 0)
				return node;
		}

		return null;
	}

	public org.w3c.dom.Node setNamedItem(edu.jhu.apl.patterns_class.dom.replacement.Node arg)
	  throws org.w3c.dom.DOMException
	{
		// TODO:  Check for readonly status:  NO_MODIFICATION_ALLOWED_ERR
		// TODO:  Check for map's parent type compatible with arg type:  HIERARCHY_REQUEST_ERR
		
		if (arg.getOwnerDocument() != document)
			throw new org.w3c.dom.DOMException(org.w3c.dom.DOMException.WRONG_DOCUMENT_ERR,
			  "Arg not created by this document.");

		if (arg instanceof Attr)
		{
			edu.jhu.apl.patterns_class.dom.replacement.Node parent	= (edu.jhu.apl.patterns_class.dom.replacement.Node) ((Attr )arg).getParentNode();

			if (parent != null && parent instanceof Element)
				throw new org.w3c.dom.DOMException(org.w3c.dom.DOMException.INUSE_ATTRIBUTE_ERR,
				  "Attribute in use by element. [" + ((Element )parent).getTagName() + "]");
		}

		Node	oldNode	= null;

		for (java.util.ListIterator i = listIterator(0); i.hasNext();)
		{
			oldNode =	(Node )i.next();

			if (oldNode.getNodeName().compareTo(arg.getNodeName()) == 0)
				break;
		}

		if (oldNode != null)
			remove(oldNode);

		addLast(arg);

		return oldNode;
	}

	public org.w3c.dom.Node removeNamedItem(String name) throws org.w3c.dom.DOMException
	{
		// TODO:  Check for readonly status:  NO_MODIFICATION_ALLOWED_ERR

		for (java.util.ListIterator i = listIterator(0); i.hasNext();)
		{
			Node	node	= (Node )i.next();

			if (node.getNodeName().compareTo(name) == 0)
				return node;
		}

		throw new org.w3c.dom.DOMException(org.w3c.dom.DOMException.NOT_FOUND_ERR, "Node not found.");
	}

	public org.w3c.dom.Node getNamedItemNS(String namespaceURI, String localName)
	  { return null; }
	public org.w3c.dom.Node setNamedItemNS(edu.jhu.apl.patterns_class.dom.replacement.Node arg)
	  { return null; }
	public org.w3c.dom.Node removeNamedItemNS(String namespaceURI, String localName)
	  { return null; }
}
