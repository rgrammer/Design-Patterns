package edu.jhu.apl.patterns_class.dom.replacement;

public interface NamedNodeMap extends NodeList
{
	public org.w3c.dom.Node getNamedItem(String name);

	public org.w3c.dom.Node setNamedItem(Node arg) throws org.w3c.dom.DOMException;

	public org.w3c.dom.Node removeNamedItem(String name) throws org.w3c.dom.DOMException;

	public org.w3c.dom.Node getNamedItemNS(String namespaceURI, String localName);
	public org.w3c.dom.Node setNamedItemNS(Node arg);
	public org.w3c.dom.Node removeNamedItemNS(String namespaceURI, String localName);
}
