package edu.jhu.apl.patterns_class;

import java.io.FileNotFoundException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import edu.jhu.apl.patterns_class.XMLSerializer.serializePretty;
import edu.jhu.apl.patterns_class.dom.Document;
import edu.jhu.apl.patterns_class.dom.Document.*;
import edu.jhu.apl.patterns_class.dom.replacement.Attr;
import edu.jhu.apl.patterns_class.dom.replacement.Text;

public class XMLValidator
{
	
	//VectorCreator
	private static abstract class VectorCreator{
		public static java.util.Vector<ValidChildren> createVector(){
			java.util.Vector<ValidChildren>	schema = vectorFactory();
			return schema;
		}
	}
	//VectorConcreteCreator
	private static class VectorConcreteCreator extends VectorCreator{
		protected java.util.Vector<ValidChildren> vectorFactory(){
			return new java.util.Vector<ValidChildren>();
		}
	}
	//VectorFactoryMethod
	private static java.util.Vector<ValidChildren> vectorFactory()
	{
		java.util.Vector<ValidChildren>	schema	= new java.util.Vector<ValidChildren>();
		return schema;
	
	}
	
	//ValidChildrenCreator
	private static abstract class ValidChildrenCreator{
		public static ValidChildren createValidChildren(String element){
			ValidChildren	schemaElement = validChildrenFactory(element);
			return schemaElement;
		}
	}
	//ValidChildrenConcreteCreator
	private static class ValidChildrenConcreteCreator extends ValidChildrenCreator{
		protected ValidChildren validChildrenFactory(String element){
			return new ValidChildren(element, null);
		}
	}
	//ValidChildrenFactoryMethod
	private static ValidChildren validChildrenFactory(String element)
	{
		ValidChildren	schemaElement	= new ValidChildren(element, null);
		return schemaElement;
	
	}
	
	//VectorConcreteProduct
	VectorCreator vectorCreator = new VectorConcreteCreator();
	//VectorProduct
	java.util.Vector<ValidChildren>	schema = VectorCreator.createVector();
	//private java.util.Vector<ValidChildren>	schema	= vectorFactory();

	//
	// Supercedes any existing description for this element.
	//
	public ValidChildren addSchemaElement(String element)
	{
		ValidChildren	schemaElement	= findSchemaElement(element);

		if (schemaElement != null)
			schema.remove(schemaElement);
		//ValidChildrenConcreteProduct
		ValidChildrenCreator validChildrenCreator = new ValidChildrenConcreteCreator();
		//ValidChildrenProduct
		ValidChildren	schemaElement1 = ValidChildrenCreator.createValidChildren(element);
		schema.add(schemaElement1);
		return schemaElement1;
	}

	public ValidChildren findSchemaElement(String element)
	{
		for (int i = 0; i < schema.size(); i++)
			if ((schema.elementAt(i).getThisElement() == null && element == null) ||
			 (schema.elementAt(i).getThisElement()!=null && schema.elementAt(i).getThisElement().compareTo(element)==0))
				return schema.elementAt(i);

		return null;
	}
	
	public void validation(String thisElement, String child, boolean isAttribute)
	{
		//
		// Some arbitrary decision algorithm.  In this case, halt validation when we encounter attribute2.
		//
		if (isAttribute && thisElement.compareTo("element") == 0 && child.compareTo("attribute2") == 0)
			for (java.util.Iterator<ValidChildren> i = schema.iterator(); i.hasNext();)
				i.next().deactivate();
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
	
	//send and receive methods for mediator implementation
	public void send(String msg){
		
	}
	
	public void receive(String msg){
		
	}

	//
	// Optional for schema implementation:
	//
	// public static boolean canValue(edu.jhu.apl.patterns_class.dom.replacement.Attribute attribute, String value)
	// {
	// }

	public static void main(String args[])
	{
		if (args.length < 1)
		{
			System.out.println("No output filenames provided.");
			System.exit(0);
		}

		//
		// Create tree of this document:
		// <? xml version="1.0" encoding="UTF-8"?>
		// <document>
		//   <element attribute="attribute value"/>
		//   <element/>
		//   <element attribute="attribute value" attribute2="attribute2 value">
		//     Element Value
		//   </element>
		//   <element>
		//   </element>
		// </document>
		//
		// Schema for this document:
		// document contains:  element
		// element contains:  element
		// element contains attributes:  attribute, attribute2
		//
		//XMLValidatorProduct
		XMLValidator xmlValidator = null;
		try {
			xmlValidator = XMLValidatorFactory.getXMLValidator();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//XMLValidator	xmlValidator	= xmlValidatorFactory();
		ValidChildren	schemaElement	= xmlValidator.addSchemaElement(null);
		schemaElement.addValidChild("document", false);
		schemaElement	= xmlValidator.addSchemaElement("document");
		schemaElement.addValidChild("element", false);
		schemaElement	= xmlValidator.addSchemaElement("element");
		schemaElement.addValidChild("element", false);
		schemaElement.addValidChild("attribute", true);
		schemaElement.addValidChild("attribute2", true);
		schemaElement.setCanHaveText(true);
		//DocumentProduct
		Document document = DocumentFactory.getDocument();
		//edu.jhu.apl.patterns_class.dom.replacement.Document	document	= documentFactory();
		Element	root		= null;
		Element	child		= null;
		edu.jhu.apl.patterns_class.dom.replacement.Attr		attr		= null;

		if (xmlValidator.canRootElement("document"))
		{
			root	= document.createElement("document");
			document.appendChild(root);
		}
		else
		{
			System.out.println("Attempted invalid schema operation.");
			System.exit(0);
		}

		if (xmlValidator.canAddElement((edu.jhu.apl.patterns_class.dom.replacement.Element) root, "element"))
		{
			child	= document.createElement("element");

			if (xmlValidator.canAddAttribute((edu.jhu.apl.patterns_class.dom.replacement.Element) child, "attribute"))
			{
				attr	= (Attr) document.createAttribute("attribute");
				attr.setValue("attribute value");
				child.setAttributeNode((org.w3c.dom.Attr) attr);
			}
			else
			{
				System.out.println("Attempted invalid schema operation.");
				System.exit(0);
			}

			root.appendChild(child);
		}
		else
		{
			System.out.println("Attempted invalid schema operation.");
			System.exit(0);
		}

		if (xmlValidator.canAddElement((edu.jhu.apl.patterns_class.dom.replacement.Element) root, "element"))
		{
			child	= document.createElement("element");
			root.appendChild(child);
		}
		else
		{
			System.out.println("Attempted invalid schema operation.");
			System.exit(0);
		}

		if (xmlValidator.canAddElement((edu.jhu.apl.patterns_class.dom.replacement.Element) root, "element"))
		{
			child	= document.createElement("element");

			if (xmlValidator.canAddAttribute((edu.jhu.apl.patterns_class.dom.replacement.Element) child, "attribute"))
				child.setAttribute("attribute", "attribute value");
			else
			{
				System.out.println("Attempted invalid schema operation.");
				System.exit(0);
			}

			if (xmlValidator.canAddAttribute((edu.jhu.apl.patterns_class.dom.replacement.Element) child, "attribute2"))
				child.setAttribute("attribute2", "attribute2 value");
			else
			{
				System.out.println("Attempted invalid schema operation.");
				System.exit(0);
			}

			if (xmlValidator.canAddText((edu.jhu.apl.patterns_class.dom.replacement.Element) child))
			{
				edu.jhu.apl.patterns_class.dom.replacement.Text text = (Text) document.createTextNode("Element Value");
				child.appendChild((Node) text);
			}
			else
			{
				System.out.println("Attempted invalid schema operation.");
				System.exit(0);
			}

			root.appendChild(child);
		}
		else
		{
			System.out.println("Attempted invalid schema operation.");
			System.exit(0);
		}

		if (xmlValidator.canAddElement((edu.jhu.apl.patterns_class.dom.replacement.Element) root, "element"))
		{
			child	= document.createElement("element");
			root.appendChild(child);
		}
		else
		{
			System.out.println("Attempted invalid schema operation.");
			System.exit(0);
		}

		//
		// Serialize
		//
		try
		{
			//XMLSerializerProduct
			java.io.BufferedWriter writer = null;
			XMLSerializer	xmlSerializer	= XMLSerializerFactory.getXMLSerializer(writer);
			serializePretty prettySerializer = xmlSerializer.new serializePretty();
			//XMLSerializer	xmlSerializer	= xmlSerializerFactory(args[0]);
			prettySerializer.visit(document);
			xmlSerializer.close();
		}
		catch (java.io.IOException e)
		{
			System.out.println("Error writing file.");
			e.printStackTrace();
		}
	}
	
}

//XML Validator Memento
class XMLValidatorMemento {
	   private java.util.Vector<ValidChildren> state;

	   public XMLValidatorMemento(java.util.Vector<ValidChildren> stateToSave) { state = stateToSave; }
	   public java.util.Vector<ValidChildren> getSavedState() { return state; }
}

class XMLValidatorOriginator {
   private java.util.Vector<ValidChildren> state;

   public void set(java.util.Vector<ValidChildren> state) { 
       this.state = state; 
   }

   public XMLValidatorMemento saveToMemento() { 
       return new XMLValidatorMemento(state); 
   }
   public void restoreFromMemento(XMLValidatorMemento m) {
       state = m.getSavedState(); 
   }
}   

class XMLValidatorCaretaker {
   private java.util.Vector<XMLValidatorMemento> savedStates = new java.util.Vector<XMLValidatorMemento>();

   public void addMemento(XMLValidatorMemento m) { savedStates.add(m); }
   public XMLValidatorMemento getMemento(int index) { return savedStates.get(index); }
}   


/*class ValidChildren
{
	//StringVectorCreator
	private static abstract class StringVectorCreator{
		public static java.util.Vector<String> createStringVector(){
			java.util.Vector<String>	validChildren = stringVectorFactory();
			return validChildren;
		}
	}
	//StringVectorConcreteCreator
	private static class StringVectorConcreteCreator extends StringVectorCreator{
		protected java.util.Vector<String> stringVectorFactory(){
			return new java.util.Vector<String>();
		}
	}
	//StringVectorFactoryMethod
	private static java.util.Vector<String> stringVectorFactory()
	{
		java.util.Vector<String>	validChildren	= new java.util.Vector<String>();
		return validChildren;
	
	}
	
	
	//BooleanVectorCreator
	private static abstract class BooleanVectorCreator{
		public static java.util.Vector<Boolean> createBooleanVector(){
			java.util.Vector<Boolean>	childIsAttribute = booleanVectorFactory();
			return childIsAttribute;
		}
	}
	//BooleanVectorConcreteCreator
	private static class BooleanVectorConcreteCreator extends BooleanVectorCreator{
		protected java.util.Vector<Boolean> booleanVectorFactory(){
			return new java.util.Vector<Boolean>();
		}
	}
	//BooleanVectorFactoryMethod
	private static java.util.Vector<Boolean> booleanVectorFactory()
	{
		java.util.Vector<Boolean>	childIsAttribute	= new java.util.Vector<Boolean>();
		return childIsAttribute;
	
	}
	
	private String				thisElement		= null;	// A value of null represents Document.
	private java.util.Vector<String>	validChildren		= StringVectorCreator.createStringVector();
	private java.util.Vector<Boolean>	childIsAttribute	= BooleanVectorCreator.createBooleanVector();
	private boolean				_canHaveText		= false;

	public ValidChildren(String thisElement)		{ this.thisElement = thisElement; }

	public String	getThisElement()			{ return thisElement; }
	public boolean	canHaveText()				{ return _canHaveText; }
	public void	setCanHaveText(boolean _canHaveText)	{ this._canHaveText = _canHaveText; }

	public void	addValidChild(String child, boolean isAttribute)
	{
		if (childIsValid(child, isAttribute))
			return;

		validChildren.add(child);
		childIsAttribute.add(new Boolean(isAttribute));
	}

	public boolean	childIsValid(String child, boolean isAttribute)
	{
		for (int i = 0; i < validChildren.size(); i++)
			if (childIsAttribute.elementAt(i).booleanValue() == isAttribute &&
			  validChildren.elementAt(i).compareTo(child) == 0)
				return true;

		return false;
	}
}*/
