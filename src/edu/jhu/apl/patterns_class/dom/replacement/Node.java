package edu.jhu.apl.patterns_class.dom.replacement;

import edu.jhu.apl.patterns_class.XMLSerializer;
import edu.jhu.apl.patterns_class.Visitor;

//import org.w3c.dom.NodeList;

//import edu.jhu.apl.patterns_class.dom.Document;

//Node extends org.w3c.dom.Node so the user can expect a recognizable structure
public interface Node
{
	//
	// Implemented Interface Members
	//
	public String	getNodeName();
	public String	getNodeValue() throws org.w3c.dom.DOMException;
	public void	setNodeValue(String nodeValue) throws org.w3c.dom.DOMException;
	public short	getNodeType();
	public org.w3c.dom.Node	getParentNode();
	public NodeList	getChildNodes();
	public org.w3c.dom.Node	getFirstChild();
	public org.w3c.dom.Node	getLastChild();
	public org.w3c.dom.Node	getPreviousSibling();
	public org.w3c.dom.Node	getNextSibling();
	public org.w3c.dom.Document	getOwnerDocument();
	public Node	insertBefore(Node newChild, Node refChild) throws org.w3c.dom.DOMException;
	public Node	replaceChild(Node newChild, Node oldChild) throws org.w3c.dom.DOMException;
	public Node	removeChild(Node oldChild) throws org.w3c.dom.DOMException;
	public Node	appendChild(Node newChild) throws org.w3c.dom.DOMException;
	public boolean	hasChildNodes();
	public String	getLocalName();
	public void accept(Visitor v);
	
	//
	// Unimplemented Interface Members
	//
	public void normalize();
	public boolean isSupported(String feature, String version);
	public String getNamespaceURI();
	public String getPrefix();
	public void setPrefix(String prefix) throws org.w3c.dom.DOMException;
	public org.w3c.dom.Node cloneNode(boolean deep);
	public boolean hasAttributes();
	public org.w3c.dom.NamedNodeMap getAttributes();
	public Object getUserData(String key);
	public Object setUserData(String key, Object data, org.w3c.dom.UserDataHandler handler);
	public Object getFeature(String feature, String version);
	public boolean isEqualNode(Node arg);
	public String lookupNamespaceURI(String prefix);
	public boolean isDefaultNamespace(String namespaceURI);
	public String lookupPrefix(String namespaceURI);
	public boolean isSameNode(Node other);
	public void setTextContent(String textContent);
	public String getTextContent();
	public short compareDocumentPosition(Node other);
	public String getBaseURI();

	
	
}
