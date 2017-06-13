package edu.jhu.apl.patterns_class;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.jhu.apl.patterns_class.XMLTokenizer.XMLToken;
import edu.jhu.apl.patterns_class.dom.Document;
import edu.jhu.apl.patterns_class.dom.replacement.Attr;
import edu.jhu.apl.patterns_class.dom.replacement.Element;
import edu.jhu.apl.patterns_class.dom.replacement.Node;
import edu.jhu.apl.patterns_class.dom.replacement.Text;

public class DocumentBuilder extends Subject
{
	//Code to make this a Singleton
	private static DocumentBuilder instance = null;
	public DocumentBuilder() {
		//Exists only to defeat instantiation
	}
	public static DocumentBuilder getInstance(){
		if(instance == null){
			instance = new DocumentBuilder();
		}
		return instance;
	}
	
	private edu.jhu.apl.patterns_class.dom.replacement.Document				factory		= null;
	private java.util.ArrayDeque<edu.jhu.apl.patterns_class.dom.replacement.Element>	elementStack	=
			  new java.util.ArrayDeque<edu.jhu.apl.patterns_class.dom.replacement.Element>();
	private edu.jhu.apl.patterns_class.dom.replacement.Element				currentElement	= null;
	private edu.jhu.apl.patterns_class.dom.replacement.Attr					currentAttr	= null;
	
	public DocumentBuilder(Document document)
	{
		this.factory	= (edu.jhu.apl.patterns_class.dom.replacement.Document) document;
	}

	public edu.jhu.apl.patterns_class.dom.replacement.Document getDocument()	// aka getResult
	{
		return factory;
	}
	
	//Variable to hold valid state of doc
	private boolean docIsValid = true;
	XMLValidator validator = new XMLValidator();
	

	//State pattern
	public interface State {
	      public void doAction(DocumentBuilder context, String token);
	}
	
	  private State currentState;

	public void setState(State state) {
		currentState = state;
	}

	public void doAction(String token) {
		currentState.doAction(this, token);
	}
  
  class BeforePrologState implements State {
	  public void doAction(DocumentBuilder context, String token) {
	    context.createProlog();
	  }
	}
	public class AfterPrologState implements State {
	  public void doAction(DocumentBuilder context, String token) {
		 context.endProlog();
	  }
	}
	
	public class ParsingElementState implements State {
		  public void doAction(DocumentBuilder context, String token) {
		    context.pushElement();
		  }
		}
	
	public class AttributeState implements State {
		  public void doAction(DocumentBuilder context, String token) {
		    context.createAttribute(token);
		  }
		}
	
	public class CreateElementState implements State {
		  public void doAction(DocumentBuilder context, String token) {
		    context.createElement(token);
		  }
		}
	
	public class ConfirmElementState implements State {
		  public void doAction(DocumentBuilder context, String token) {
		    context.confirmElement(token);
		  }
		}
	
	public class AddValueState implements State {
		  public void doAction(DocumentBuilder context, String token) {
		    context.addValue(token);
		  }
		}
	
	public class AttributeValueState implements State {
		  public void doAction(DocumentBuilder context, String token) {
		    context.valueAttribute(token);
		  }
		}
	
	public class PrologIdentifierState implements State {
		  public void doAction(DocumentBuilder context, String token) {
		    context.identifyProlog(token);
		  }
		}
	
	public class InNonNullElementState implements State {
		  public void doAction(DocumentBuilder context, String token) {
		    context.popElement();
		  }
		}
	
	public void parse(String fileName) throws IOException{
		DocumentBuilder docBuilder = new DocumentBuilder();
		Director director = new Director(fileName, docBuilder);
		String fileList[] = new String[1];
		fileList[0] = fileName;
		director.main(fileList);
	}
	
	private edu.jhu.apl.patterns_class.dom.replacement.Element newElement;
	Document doc = new Document();
	
	public void addValue(String text) throws org.w3c.dom.DOMException
	{
		String	trimmed	= text.trim();
		if (!notify(elementStack.peek(), org.w3c.dom.Node.TEXT_NODE, trimmed))
			throw new org.w3c.dom.DOMException(org.w3c.dom.DOMException.VALIDATION_ERR,
			  "Invalid TEXT node '" + trimmed + "' for element " + elementStack.peek().getTagName() + ".");
		elementStack.peek().appendChild((Node) factory.createTextNode(trimmed));
	}
	
	public void confirmElement(String tag) throws org.w3c.dom.DOMException
	{
		// Throw an exception if tag.trim() != currentElement.getTagName()
	}
	
	public void createAttribute(String attribute) throws org.w3c.dom.DOMException
	{
		String	trimmed	= attribute.trim();
		trimmed	= trimmed.substring(0, trimmed.length() - 1);
		if (currentElement != null)	// Remove this check and let Observer handle null if we handle prolog attributes.
			if (!notify(currentElement, org.w3c.dom.Node.ATTRIBUTE_NODE, trimmed))
				throw new org.w3c.dom.DOMException(org.w3c.dom.DOMException.VALIDATION_ERR,
				  "Invalid ATTRIBUTE node '" + trimmed + "' for element " + currentElement.getTagName() + ".");
		currentAttr	= (Attr) factory.createAttribute(trimmed);
	}
	
	public void createElement(String tag) throws org.w3c.dom.DOMException
	{
		String	trimmed	= tag.trim();
		if (!notify(elementStack.peek(), org.w3c.dom.Node.ELEMENT_NODE, trimmed))
			throw new org.w3c.dom.DOMException(org.w3c.dom.DOMException.VALIDATION_ERR,
			  "Invalid ELEMENT node '" + trimmed + "' for element " + elementStack.peek().getTagName() + ".");
		currentElement	= (Element) factory.createElement(trimmed);

		if (elementStack.peek() == null)	// This is the root element.
			factory.appendChild(currentElement);
		else
			elementStack.peek().appendChild(currentElement);
	}
	
	
	public void createProlog() throws org.w3c.dom.DOMException
	{
		// null method in this implementation
	}

	public void endProlog() throws org.w3c.dom.DOMException
	{
		// null method in this implementation
	}

	public void identifyProlog(String id) throws org.w3c.dom.DOMException
	{
		// null method in this implementation
	}
	
	public boolean popElement() throws org.w3c.dom.DOMException
	{
		currentElement	= elementStack.pop();
		return elementStack.size() > 0;
	}

	public void pushElement() throws org.w3c.dom.DOMException
	{
		elementStack.push(currentElement);
		currentElement	= null;
	}

	public void valueAttribute(String value) throws org.w3c.dom.DOMException
	{
		String	trimmed	= value.trim();
		trimmed	= trimmed.substring(1, trimmed.length() - 1);
		if (!notify(currentAttr, org.w3c.dom.Node.ATTRIBUTE_NODE, trimmed))
			throw new org.w3c.dom.DOMException(org.w3c.dom.DOMException.VALIDATION_ERR,
			  "Invalid ATTRIBUTE VALUE '" + trimmed + "' for attribute " + currentAttr.getName() + ".");
		currentAttr.setValue(trimmed);

		if (currentElement != null)	// Discard prolog attributes.  This implementation currently doesn't have
						// anything to do with them.
			currentElement.setAttributeNode(currentAttr);
	}
}
