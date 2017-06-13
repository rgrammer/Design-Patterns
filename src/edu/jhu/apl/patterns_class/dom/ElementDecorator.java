package edu.jhu.apl.patterns_class.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

import edu.jhu.apl.patterns_class.Visitor;
import edu.jhu.apl.patterns_class.dom.Document;
import edu.jhu.apl.patterns_class.dom.Document.*;
import edu.jhu.apl.patterns_class.dom.ElementDecorator.ValidChildrenFactory;
import edu.jhu.apl.patterns_class.dom.ElementDecorator.VectorFactory;
import edu.jhu.apl.patterns_class.dom.replacement.Attr;
import edu.jhu.apl.patterns_class.dom.replacement.NamedNodeMap;
import edu.jhu.apl.patterns_class.dom.replacement.Node;
import edu.jhu.apl.patterns_class.dom.replacement.NodeList;

public class ElementDecorator implements edu.jhu.apl.patterns_class.dom.replacement.Element{
	
	protected edu.jhu.apl.patterns_class.dom.Element decoratedElement;
	
	public ElementDecorator(edu.jhu.apl.patterns_class.dom.Element decoratedElement){
		this.decoratedElement = decoratedElement;
	}
	
	//FileFactoryMethod
	public static class ValidChildrenFactory
	{
		public static ValidChildren getValidChildren(String element){
			ValidChildren	schemaElement	= new ValidChildren(element);
			return schemaElement;
		}
	}
	
	//FileFactoryMethod
	public static class VectorFactory
	{
		public static java.util.Vector<ValidChildren> getVector(){
			java.util.Vector<ValidChildren>	schema	= new java.util.Vector<ValidChildren>();
			return schema;
		}
	}
	
	//VectorProduct
	java.util.Vector<ValidChildren>	schema = VectorFactory.getVector();
	
	public ValidChildren addSchemaElement(String element)
	{
		ValidChildren	schemaElement	= findSchemaElement(element);

		if (schemaElement != null)
			schema.remove(schemaElement);

		//ValidChildrenProduct
		schemaElement = ValidChildrenFactory.getValidChildren(element);
		return schemaElement;
	}
	
	public ValidChildren findSchemaElement(String element)
	{
		for (int i = 0; i < schema.size(); i++)
			if ((schema.elementAt(i).getThisElement() == null && element == null) ||
			 (schema.elementAt(i).getThisElement()!=null && schema.elementAt(i).getThisElement().compareTo(element)==0))
				return schema.elementAt(i);

		return null;
	}
	
	public boolean canRootElement(String newElement)
	{
		return canAddElement(null, newElement);
	}
	
	public boolean canAddElement(edu.jhu.apl.patterns_class.dom.replacement.Element element, String newElement)
	{
		ValidChildren	schemaElement	= findSchemaElement(element == null ? null : element.getTagName());

		return schemaElement == null ? true : schemaElement.childIsValid(newElement, false);
	}
	
	public boolean canAddText(edu.jhu.apl.patterns_class.dom.replacement.Element element)
	{
		ValidChildren	schemaElement	= findSchemaElement(element.getTagName());

		return schemaElement == null ? true : schemaElement.canHaveText();
	}

	public boolean canAddAttribute(edu.jhu.apl.patterns_class.dom.replacement.Element element, String newAttribute)
	{
		ValidChildren	schemaElement	= findSchemaElement(element.getTagName());

		return schemaElement == null ? true : schemaElement.childIsValid(newAttribute, true);
	}

	//Unimplemented methods
	public String getNodeName() { return null; }
	public String getNodeValue() throws DOMException { return null; }
	public void setNodeValue(String nodeValue) throws DOMException {	}
	public short getNodeType() { return 0; }
	public org.w3c.dom.Node getParentNode() { return null; }
	public NodeList getChildNodes() { return null; }
	public org.w3c.dom.Node getFirstChild() { return null; }
	public org.w3c.dom.Node getLastChild() { return null; }
	public org.w3c.dom.Node getPreviousSibling() { return null; }
	public org.w3c.dom.Node getNextSibling() { return null; }
	public org.w3c.dom.Document getOwnerDocument() { return null;}
	public Node insertBefore(Node newChild, Node refChild) throws DOMException { return null; }
	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {return null; }
	public Node removeChild(Node oldChild) throws DOMException { return null; }
	public Node appendChild(Node newChild) throws DOMException { return null; }
	public boolean hasChildNodes() { return false; }
	public String getLocalName() { return null;}
	public void normalize() {}
	public boolean isSupported(String feature, String version) {return false;}
	public String getNamespaceURI() {return null;}
	public String getPrefix() {return null;}
	public void setPrefix(String prefix) throws DOMException {}
	public org.w3c.dom.Node cloneNode(boolean deep) {return null;}
	public Object getUserData(String key) {return null;}
	public Object setUserData(String key, Object data, UserDataHandler handler) {return null;}
	public Object getFeature(String feature, String version) {return null;}
	public boolean isEqualNode(Node arg) {return false;}
	public String lookupNamespaceURI(String prefix) {return null;}
	public boolean isDefaultNamespace(String namespaceURI) {return false;}
	public String lookupPrefix(String namespaceURI) {return null;}
	public boolean isSameNode(Node other) {return false;}
	public void setTextContent(String textContent) {}
	public String getTextContent() {return null;}
	public short compareDocumentPosition(Node other) {return 0;}
	public String getBaseURI() {return null;}
	public String getAttribute(String name) {return null;}
	public Attr getAttributeNode(String name) {return null;}
	public NodeList getElementsByTagName(String tagName) {return null;}
	public String getTagName() {return null;}
	public boolean hasAttribute(String name) {return false;}
	public void removeAttribute(String name) {}
	public Attr removeAttributeNode(Attr oldAttr) {return null;}
	public void setAttribute(String name, String value) {}
	public Attr setAttributeNode(Attr newAttr) {return null;}
	public Attr getAttributeNodeNS(String namespaceURI, String localName) {return null;}
	public String getAttributeNS(String namespaceURI, String localName) {return null;}
	public NodeList getElementsByTagNameNS(String tagName) {return null;}
	public boolean hasAttributeNS(String namespaceURI, String localName) {return false;}
	public void removeAttributeNS(String namespaceURI, String localName) {}
	public Attr setAttributeNodeNS(Attr newAttr) {return null;}
	public void setAttributeNS(String namespaceURI, String localName, String value) {}
	public Attr setAttributeNS(Attr newAttr) {return null;}
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {return null;}
	public void setIdAttributeNode(Attr idAttr, boolean isId) {	}
	public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) {	}
	public void setIdAttribute(String name, boolean isId) {}
	public TypeInfo getSchemaTypeInfo() {return null;}
	public boolean hasAttributes() {return false;}
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
	public org.w3c.dom.NamedNodeMap getAttributes() {
		return null;
	}

	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		
	}
}
