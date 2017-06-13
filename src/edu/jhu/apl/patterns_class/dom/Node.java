package edu.jhu.apl.patterns_class.dom;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;

import edu.jhu.apl.patterns_class.XMLSerializer;
import edu.jhu.apl.patterns_class.Visitor;
import edu.jhu.apl.patterns_class.XMLSerializer.serializeMinimal;

//Since Node is now wrapped by org.w3c.dom, this Node class acts as an adapter to the rest of the code
public class Node implements edu.jhu.apl.patterns_class.dom.replacement.Node, org.w3c.dom.Node
{
	private String		name		= null;
	private String		value		= null;
	private short		nodeType	= -1;
	private org.w3c.dom.Node		parent		= null;
	private NodeList	nodes		= null;
	protected edu.jhu.apl.patterns_class.dom.Document	document	= null;

	Node(String name, short type)
	{
		this.name	= name;
		nodeType	= type;
		nodes		= new NodeList();
	}

	void setParent(org.w3c.dom.Node parent)							{ this.parent = parent; }

	//
	// Implemented Interface Members
	//
	public String getNodeName()							{ return name; }
	public String getNodeValue() throws org.w3c.dom.DOMException			{ return value; }
	public void setNodeValue(String nodeValue) throws org.w3c.dom.DOMException	{ value = nodeValue; }
	public short getNodeType()							{ return nodeType; }
	public org.w3c.dom.Node getParentNode()		{ return parent; }
	public edu.jhu.apl.patterns_class.dom.replacement.NodeList getChildNodes()	{ return nodes; }
	public org.w3c.dom.Node getFirstChild()
	  {return (org.w3c.dom.Node)nodes.getFirst();}
	public org.w3c.dom.Node getLastChild()
	  {return (org.w3c.dom.Node )nodes.getLast();}
	public org.w3c.dom.Node getPreviousSibling()
	  { return (org.w3c.dom.Node )getSibling(-1);}
	public org.w3c.dom.Node getNextSibling()
	  { return (org.w3c.dom.Node )getSibling(1); }
	public org.w3c.dom.Document getOwnerDocument()	{ return (org.w3c.dom.Document) document; }
	public edu.jhu.apl.patterns_class.dom.replacement.Node
	  insertBefore(edu.jhu.apl.patterns_class.dom.replacement.Node newChild,
	  edu.jhu.apl.patterns_class.dom.replacement.Node refChild) throws org.w3c.dom.DOMException
	{
		// TODO:  Do readonly checks on this node and current parent of newChild.  NO_MODIFICATION_ALLOWED_ERR
		// TODO:  Exclude child types not permitted for this element here.  HIERARCHY_REQUEST_ERR

		if (newChild.getOwnerDocument() != getOwnerDocument())
			throw new org.w3c.dom.DOMException(org.w3c.dom.DOMException.WRONG_DOCUMENT_ERR,
			  "New Child is not a part of this document.");

		if (newChild.getParentNode() != null)
			newChild.getParentNode().removeChild((org.w3c.dom.Node) newChild);

		if (refChild == null)
		{
			nodes.addLast(newChild);
			((Node )newChild).setParent(this);
			return newChild;
		}

		int index	= nodes.indexOf(refChild);

		if (index == -1)
			throw new org.w3c.dom.DOMException(org.w3c.dom.DOMException.NOT_FOUND_ERR,
			  "Reference Child is not a child of this node.");

		nodes.add(index, newChild);
		((Node )newChild).setParent(this);

		return newChild;
	}
	public edu.jhu.apl.patterns_class.dom.replacement.Node
	  replaceChild(edu.jhu.apl.patterns_class.dom.replacement.Node newChild,
	  edu.jhu.apl.patterns_class.dom.replacement.Node oldChild) throws org.w3c.dom.DOMException
	{
		// TODO:  Do readonly checks on this node and current parent of newChild.  NO_MODIFICATION_ALLOWED_ERR
		// TODO:  Exclude child types not permitted for this element here.  HIERARCHY_REQUEST_ERR

		if (newChild.getOwnerDocument() != getOwnerDocument())
			throw new org.w3c.dom.DOMException(org.w3c.dom.DOMException.WRONG_DOCUMENT_ERR,
			  "New Child is not a part of this document.");

		if (newChild.getParentNode() != null)
			newChild.getParentNode().removeChild((org.w3c.dom.Node) newChild);

		int index	= nodes.indexOf(oldChild);

		if (index == -1)
			throw new org.w3c.dom.DOMException(org.w3c.dom.DOMException.NOT_FOUND_ERR,
			  "Old Child is not a child of this node.");

		nodes.add(index, newChild);
		((Node )newChild).setParent(this);
		((Node )nodes.get(index + 1)).setParent(null);
		nodes.remove(index + 1);

		return oldChild;
	}
	public edu.jhu.apl.patterns_class.dom.replacement.Node removeChild(edu.jhu.apl.patterns_class.dom.replacement.Node oldChild)
	  throws org.w3c.dom.DOMException
	{
		// TODO:  Do readonly checks on this node.  NO_MODIFICATION_ALLOWED_ERR

		int index	= nodes.indexOf(oldChild);

		if (index == -1)
			throw new org.w3c.dom.DOMException(org.w3c.dom.DOMException.NOT_FOUND_ERR,
			  "Old Child is not a child of this node.");

		((Node )nodes.get(index)).setParent(null);
		nodes.remove(index);

		return oldChild;
	}
	public edu.jhu.apl.patterns_class.dom.replacement.Node appendChild(edu.jhu.apl.patterns_class.dom.replacement.Node newChild)
	  throws org.w3c.dom.DOMException
	{
		// TODO:  Do readonly checks on this node and current parent of newChild.  NO_MODIFICATION_ALLOWED_ERR
		// TODO:  Exclude child types not permitted for this element here.  HIERARCHY_REQUEST_ERR

		if (newChild.getOwnerDocument() != getOwnerDocument())
			throw new org.w3c.dom.DOMException(org.w3c.dom.DOMException.WRONG_DOCUMENT_ERR,
			  "New Child is not a part of this document.");

		if (newChild.getParentNode() != null)
			newChild.getParentNode().removeChild((org.w3c.dom.Node) newChild);

		nodes.addLast(newChild);
		((Node )newChild).setParent(this);

		return newChild;
	}
	public boolean hasChildNodes()					{ return nodes.size() > 0; }
	public String getLocalName()					{ return name; }

	//Accept method for visitor pattern
	public void accept(Visitor v){
		v.visit(this);
	}
	
	//
	// Unimplemented Interface Members
	//
	public void normalize() {}
	public boolean isSupported(String feature, String version)					{ return false; }
	public String getNamespaceURI()									{ return null; }
	public String getPrefix()									{ return null; }
	public void setPrefix(String prefix) throws org.w3c.dom.DOMException				{}
	public org.w3c.dom.Node cloneNode(boolean deep)			{ return null; }
	public boolean hasAttributes()									{ return false; }
	public NamedNodeMap getAttributes()			{ return null; }
	public Object getUserData(String key)								{ return null; }
	public Object setUserData(String key, Object data, org.w3c.dom.UserDataHandler handler)		{ return null; }
	public Object getFeature(String feature, String version)					{ return null; }
	public boolean isEqualNode(edu.jhu.apl.patterns_class.dom.replacement.Node arg)			{ return false; }
	public String lookupNamespaceURI(String prefix)							{ return null; }
	public boolean isDefaultNamespace(String namespaceURI)						{ return false; }
	public String lookupPrefix(String namespaceURI)							{ return null; }
	public boolean isSameNode(edu.jhu.apl.patterns_class.dom.replacement.Node other)		{ return false; }
	public void setTextContent(String textContent)							{}
	public String getTextContent()									{ return null; }
	public short compareDocumentPosition(edu.jhu.apl.patterns_class.dom.replacement.Node other)	{ return (short )0; }
	public String getBaseURI()									{ return null; }
	public org.w3c.dom.Node insertBefore(org.w3c.dom.Node newChild, org.w3c.dom.Node refChild) throws DOMException {
		return null;
	}
	public org.w3c.dom.Node replaceChild(org.w3c.dom.Node newChild, org.w3c.dom.Node oldChild) throws DOMException {
		return null;
	}
	public org.w3c.dom.Node removeChild(org.w3c.dom.Node oldChild) throws DOMException {
		return null;
	}
	public org.w3c.dom.Node appendChild(org.w3c.dom.Node newChild) throws DOMException {
		return null;
	}
	public short compareDocumentPosition(org.w3c.dom.Node other) throws DOMException {
		return 0;
	}
	public boolean isSameNode(org.w3c.dom.Node other) {
		return false;
	}
	public boolean isEqualNode(org.w3c.dom.Node arg) {
		return false;
	}
	//
	// Class Members
	//
	private Node getSibling(int direction)
	{
		if (parent == null)
			return null;

		java.util.LinkedList	siblings	= (java.util.LinkedList )parent.getChildNodes();

		try
		{
			return (Node )siblings.get(siblings.indexOf(this) + direction);
		}
		catch (java.lang.IndexOutOfBoundsException e)
		{
			return null;
		}
	}
	
	
	//Iterator Class
	public class Iterator
	{
		private XMLSerializer serializer;
		serializeMinimal minimalSerializer = serializer.new serializeMinimal();
		private Node node;
		private java.util.ListIterator itr;
		private edu.jhu.apl.patterns_class.dom.replacement.Node s_current;
		
		public Iterator(Node node2){
			node = node2;
			
		}
		public void first(edu.jhu.apl.patterns_class.dom.replacement.Node node) throws IOException{
			itr = ((edu.jhu.apl.patterns_class.dom.NodeList )node.getAttributes()).listIterator(0);
			next();
		}
		public void next() throws IOException {
			try {
				s_current = (edu.jhu.apl.patterns_class.dom.replacement.Node) itr.next();
				minimalSerializer.visit(s_current);
			}
			catch (NoSuchElementException ex){
				s_current = null;
			}
		}
		public boolean is_done(){
			return s_current == null;
		}
		public Object current_item(){
			return s_current;
		}
		
	}
	
	//Iterator Factory
	public Iterator create_iterator(){
		return new Iterator(this);
	}

	

}
 

