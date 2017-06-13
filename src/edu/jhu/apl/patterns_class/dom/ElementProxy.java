package edu.jhu.apl.patterns_class.dom;

import org.w3c.dom.TypeInfo;

import edu.jhu.apl.patterns_class.dom.Document;
import edu.jhu.apl.patterns_class.dom.Document.*;
import edu.jhu.apl.patterns_class.dom.replacement.Attr;
import edu.jhu.apl.patterns_class.dom.replacement.NodeList;

public class ElementProxy extends Node implements edu.jhu.apl.patterns_class.dom.replacement.Element
{
	private Element ProxifiedElement;

	ElementProxy(String tagName, Document document)
	{
		super(tagName, org.w3c.dom.Node.ELEMENT_NODE);
		this.document = document;
		ProxifiedElement = new Element(tagName);
		ProxifiedElement.setDocument(this.document);
	}

	public String getAttribute(String name) {
		return ProxifiedElement.getAttribute(name);
	}

	public Attr getAttributeNode(String name) {
		return ProxifiedElement.getAttributeNode(name);
	}

	public NodeList getElementsByTagName(String tagName) {
		return ProxifiedElement.getElementsByTagName(tagName);
	}

	public String getTagName() {
		return ProxifiedElement.getTagName();
	}

	public boolean hasAttribute(String name) {
		return ProxifiedElement.hasAttribute(name);
	}

	public void removeAttribute(String name) {
		ProxifiedElement.removeAttribute(name);
	}

	public Attr removeAttributeNode(Attr oldAttr) {
		return ProxifiedElement.removeAttributeNode(oldAttr);
	}

	public void setAttribute(String name, String value) {
		ProxifiedElement.setAttribute(name, value);
	}

	public Attr setAttributeNode(Attr newAttr) {
		return ProxifiedElement.setAttributeNode(newAttr);
	}

	//
	// Unimplemented Element members.
	//
	public edu.jhu.apl.patterns_class.dom.replacement.Attr getAttributeNodeNS(String namespaceURI, String localName)
	  { return null; }
	public String getAttributeNS(String namespaceURI, String localName) { return null; }
	public edu.jhu.apl.patterns_class.dom.replacement.NodeList getElementsByTagNameNS(String tagName) { return null; }
	public boolean hasAttributeNS(String namespaceURI, String localName) { return false; }
	public void removeAttributeNS(String namespaceURI, String localName) {}
	public edu.jhu.apl.patterns_class.dom.replacement.Attr
	  setAttributeNodeNS(edu.jhu.apl.patterns_class.dom.replacement.Attr newAttr) { return null; }
	public void setAttributeNS(String namespaceURI, String localName, String value) {}
	public edu.jhu.apl.patterns_class.dom.replacement.Attr
	  setAttributeNS(edu.jhu.apl.patterns_class.dom.replacement.Attr newAttr) { return null; }
	public edu.jhu.apl.patterns_class.dom.replacement.NodeList
	  getElementsByTagNameNS(String namespaceURI, String localName) { return null; }
	public void setIdAttributeNode(edu.jhu.apl.patterns_class.dom.replacement.Attr idAttr, boolean isId) {}
	public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) {}
	public void setIdAttribute(String name, boolean isId) {}
	public org.w3c.dom.TypeInfo getSchemaTypeInfo() { return null; }	
}

