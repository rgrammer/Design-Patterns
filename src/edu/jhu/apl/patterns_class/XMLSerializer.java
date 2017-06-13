package edu.jhu.apl.patterns_class;

import java.io.IOException;
import java.util.NoSuchElementException;

import edu.jhu.apl.patterns_class.dom.Document;
import edu.jhu.apl.patterns_class.dom.Document.*;

import edu.jhu.apl.patterns_class.dom.Node;
import edu.jhu.apl.patterns_class.dom.replacement.Attr;
import edu.jhu.apl.patterns_class.dom.replacement.Element;
import edu.jhu.apl.patterns_class.dom.replacement.Text;

public class XMLSerializer
{
	java.io.BufferedWriter writer = null;
	
	
	int			indentationLevel	= 0;

	public XMLSerializer(java.io.BufferedWriter writer) throws java.io.FileNotFoundException
	{
		this.writer = writer;
	}

	public void close() throws java.io.IOException
	{
		writer.close();
	}

	private void prettyIndentation() throws java.io.IOException
	{
		for (int i = 0; i < indentationLevel; i++)
			writer.write("\t");
	}
	
	

	//
	// Strategize Node data printing:
	public interface dataExtractionStrategy {
		public String dataExtractionAction(edu.jhu.apl.patterns_class.dom.replacement.Text node, java.io.BufferedWriter writer);
	}
	//Extract Data Concrete Strategy 1
	static class extractDocumentData implements dataExtractionStrategy {
		public String dataExtractionAction(edu.jhu.apl.patterns_class.dom.replacement.Text node, java.io.BufferedWriter writer)
		{		
			return node.getData();
		}
	}
	
	// Strategize whitespace insertion.
	public interface insertWhitespaceStrategy {
		public void insertSpaceAction(java.io.BufferedWriter writer) throws IOException;
		public void insertNewLineAction(java.io.BufferedWriter writer) throws IOException;
	}
	
	static class insertWhitespace implements insertWhitespaceStrategy{
		public void insertSpaceAction(java.io.BufferedWriter writer) throws IOException{
			writer.write(" ");
		}
		public void insertNewLineAction(java.io.BufferedWriter writer) throws IOException{
			writer.write("\n");
		}
	}
	
	 //template method for serialization algorithm
	abstract class SerializeTemplate{
		
		public final void visit(edu.jhu.apl.patterns_class.dom.replacement.Node node) throws IOException{
			extractDocumentData documentDataExtractor = new extractDocumentData();
			insertWhitespace whiteSpaceInserter = new insertWhitespace();
			if (node instanceof edu.jhu.apl.patterns_class.dom.Document)
			{
				addXMLDeclaration();
				recursiveCall(node, documentDataExtractor, whiteSpaceInserter);
			}
			else if (node instanceof edu.jhu.apl.patterns_class.dom.replacement.Element)
			{
				elementAction(node, documentDataExtractor, whiteSpaceInserter);
			}
			else if (node instanceof edu.jhu.apl.patterns_class.dom.replacement.Attr){
				attrAction(node);
			}
			else if (node instanceof edu.jhu.apl.patterns_class.dom.replacement.Text){
				textAction(node, documentDataExtractor, whiteSpaceInserter);
			}	
		}
		//default methods
		private void addXMLDeclaration() throws IOException{
			writer.write("<? xml version=\"1.0\" encoding=\"UTF-8\"?>");
		}
		private void attrAction(edu.jhu.apl.patterns_class.dom.replacement.Node node) throws IOException{
			writer.write(" " + ((edu.jhu.apl.patterns_class.dom.replacement.Attr )node).getName() + "=\"" +
					  ((edu.jhu.apl.patterns_class.dom.replacement.Attr )node).getValue() + "\"");
		}
		
		//methods to be implemented by subclasses
		public abstract void recursiveCall(edu.jhu.apl.patterns_class.dom.replacement.Node node, extractDocumentData documentDataExtractor, insertWhitespace whiteSpaceInserter) throws IOException;
		public abstract void elementAction(edu.jhu.apl.patterns_class.dom.replacement.Node node, extractDocumentData documentDataExtractor, insertWhitespace whiteSpaceInserter) throws IOException;
		public abstract void textAction(edu.jhu.apl.patterns_class.dom.replacement.Node node, extractDocumentData documentDataExtractor, insertWhitespace whiteSpaceInserter) throws IOException;
	}
	
	public class serializePretty extends SerializeTemplate{
		serializePretty serializePrettyRecursive = new serializePretty();
		@Override 
		public void recursiveCall(edu.jhu.apl.patterns_class.dom.replacement.Node node, extractDocumentData documentDataExtractor, insertWhitespace whiteSpaceInserter) throws IOException{
			whiteSpaceInserter.insertNewLineAction(writer);
			
			serializePrettyRecursive.visit((edu.jhu.apl.patterns_class.dom.replacement.Node) ((edu.jhu.apl.patterns_class.dom.replacement.Document )node).getDocumentElement());
		}
		
		@Override
		public void elementAction(edu.jhu.apl.patterns_class.dom.replacement.Node node, extractDocumentData documentDataExtractor, insertWhitespace whiteSpaceInserter) throws IOException{
			prettyIndentation();
			//writer.write("<" + ((edu.jhu.apl.patterns_class.dom.replacement.Element )node).getTagName());

			int	attrCount	= 0;

			/*for (java.util.ListIterator i =
			  ((edu.jhu.apl.patterns_class.dom.NodeList )node.getAttributes()).listIterator(0);
			  i.hasNext();)
			{
				edu.jhu.apl.patterns_class.dom.replacement.Node	attr =
				  (edu.jhu.apl.patterns_class.dom.replacement.Node )i.next();

				serializePretty(attr);
				attrCount++;
			}*/
			
			//Create Iterator
			Iterator it1 = IteratorFactory(node);
			//Iterate until end of list
			for (it1.first(node, "Pretty"); !it1.is_done(); it1.next("Pretty")){
				it1.next("Pretty");
			}
			

			if (attrCount > 0)
				//writer.write(" ");
				whiteSpaceInserter.insertSpaceAction(writer);

			if (!((edu.jhu.apl.patterns_class.dom.NodeList )node.getChildNodes()).listIterator(0).hasNext())
			{
				writer.write("/>");
				//writer.write("\n");
				whiteSpaceInserter.insertNewLineAction(writer);
			}
			else
			{
				writer.write(">");
				//writer.write("\n");
				whiteSpaceInserter.insertNewLineAction(writer);
				indentationLevel++;

				for (java.util.ListIterator i =
				  ((edu.jhu.apl.patterns_class.dom.NodeList )node.getChildNodes()).listIterator(0);
				  i.hasNext();)
				{
					edu.jhu.apl.patterns_class.dom.replacement.Node	child =
					  (edu.jhu.apl.patterns_class.dom.replacement.Node )i.next();

					if (child instanceof edu.jhu.apl.patterns_class.dom.replacement.Element ||
					  child instanceof edu.jhu.apl.patterns_class.dom.replacement.Text)
						serializePrettyRecursive.visit(child);
				}

				indentationLevel--;
				prettyIndentation();
				writer.write("</" + ((edu.jhu.apl.patterns_class.dom.replacement.Element )node).getTagName() + ">");
				//writer.write("\n");
				whiteSpaceInserter.insertNewLineAction(writer);
			}
		}
		
		@Override
		public void textAction(edu.jhu.apl.patterns_class.dom.replacement.Node node, extractDocumentData documentDataExtractor, insertWhitespace whiteSpaceInserter) throws IOException{
			prettyIndentation();
			//extractDocumentData documentData = new extractDocumentData();
			documentDataExtractor.dataExtractionAction((edu.jhu.apl.patterns_class.dom.replacement.Text )node, writer);
			//writer.write(((edu.jhu.apl.patterns_class.dom.replacement.Text )node).getData());
			whiteSpaceInserter.insertNewLineAction(writer);
			//writer.write("\n");
		}
	}
	
	public class serializeMinimal extends SerializeTemplate{
		serializeMinimal serializeMinimalRecursive = new serializeMinimal();
		@Override
		public void recursiveCall(edu.jhu.apl.patterns_class.dom.replacement.Node node, extractDocumentData documentDataExtractor, insertWhitespace whiteSpaceInserter) throws IOException{
			serializeMinimalRecursive.visit((edu.jhu.apl.patterns_class.dom.replacement.Node) ((edu.jhu.apl.patterns_class.dom.replacement.Document )node).getDocumentElement());
		}
		@Override
		public void elementAction(edu.jhu.apl.patterns_class.dom.replacement.Node node, extractDocumentData documentDataExtractor, insertWhitespace whiteSpaceInserter) throws IOException{
			//writer.write("<" + ((edu.jhu.apl.patterns_class.dom.replacement.Element )node).getTagName());

			/*for (java.util.ListIterator i =
			  ((edu.jhu.apl.patterns_class.dom.NodeList )node.getAttributes()).listIterator(0);
			  i.hasNext();)
			{
				edu.jhu.apl.patterns_class.dom.replacement.Node	attr =
				  (edu.jhu.apl.patterns_class.dom.replacement.Node )i.next();

				serializeMinimal(attr);
			}*/
			
			//Create Iterator
			Iterator it2 = IteratorFactory(node);
			//Iterate until end of list
			for (it2.first(node, "Minimal"); !it2.is_done(); it2.next("Minimal")){
				it2.next("Minimal");
			}

			if (!((edu.jhu.apl.patterns_class.dom.NodeList )node.getChildNodes()).listIterator(0).hasNext()){
				//writer.write("/>");
			}
			else
			{
				writer.write(">");

				for (java.util.ListIterator i =
				  ((edu.jhu.apl.patterns_class.dom.NodeList )node.getChildNodes()).listIterator(0);
				  i.hasNext();)
				{
					edu.jhu.apl.patterns_class.dom.replacement.Node	child =
					  (edu.jhu.apl.patterns_class.dom.replacement.Node )i.next();

					if (child instanceof edu.jhu.apl.patterns_class.dom.replacement.Element ||
					  child instanceof edu.jhu.apl.patterns_class.dom.replacement.Text)
						serializeMinimalRecursive.visit(child);
				}
				writer.write("</" + ((edu.jhu.apl.patterns_class.dom.replacement.Element )node).getTagName() + ">");
			}
		}
		
		@Override
		public void textAction(edu.jhu.apl.patterns_class.dom.replacement.Node node, extractDocumentData documentDataExtractor, insertWhitespace whiteSpaceInserter){
			//writer.write(((edu.jhu.apl.patterns_class.dom.replacement.Text )node).getData());
			documentDataExtractor.dataExtractionAction((edu.jhu.apl.patterns_class.dom.replacement.Text )node, writer);
		}
	}
	
	//Visit method for Visitor Pattern
	public void visit(Node node){
		
	}
	
	

	public static void main(String args[])
	{
		if (args.length < 2)
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
		//Get new document 
		Document document = DocumentFactory.getDocument();
		
		//edu.jhu.apl.patterns_class.dom.Document document	= documentFactory();
		edu.jhu.apl.patterns_class.dom.replacement.Element	root		= (Element) document.createElement("document");
		document.appendChild(root);

		edu.jhu.apl.patterns_class.dom.replacement.Element	child		= (Element) document.createElement("element");
		edu.jhu.apl.patterns_class.dom.replacement.Attr		attr		= (Attr) document.createAttribute("attribute");
		attr.setValue("attribute value");
		child.setAttributeNode(attr);
		root.appendChild(child);

		child	= (Element) document.createElement("element");
		root.appendChild(child);

		child	= (Element) document.createElement("element");
		child.setAttribute("attribute", "attribute value");
		child.setAttribute("attribute2", "attribute2 value");
		edu.jhu.apl.patterns_class.dom.replacement.Text		text		= (Text) document.createTextNode("Element Value");
		child.appendChild(text);
		root.appendChild(child);

		child	= (Element) document.createElement("element");
		root.appendChild(child);

		//
		// Serialize
		//
		try
		{
			//XMLSerializerProduct
			java.io.BufferedWriter writer = null;
			XMLSerializer	xmlSerializer	= XMLSerializerFactory.getXMLSerializer(writer);
			serializePretty prettySerializer = xmlSerializer.new serializePretty();
			serializeMinimal minimalSerializer = xmlSerializer.new serializeMinimal();
			//Initilize extractDocumentData and whiteSpaceInserter instances
			extractDocumentData documentDataExtractor = new extractDocumentData();
			insertWhitespace whiteSpaceInserter = new insertWhitespace();
			//xmlSerializer.serializePretty(document, documentDataExtractor, whiteSpaceInserter);
			prettySerializer.visit(document);
			xmlSerializer.close();
			//XMLSerializerProduct
			xmlSerializer	= XMLSerializerFactory.getXMLSerializer(writer);
			//xmlSerializer.serializeMinimal(document, documentDataExtractor);
			minimalSerializer.visit(document);
			xmlSerializer.close();
		}
		catch (java.io.IOException e)
		{
			System.out.println("Error writing file.");
			e.printStackTrace();
		}
	}
	
	//Iterator class
	public class Iterator
	{
		private edu.jhu.apl.patterns_class.dom.replacement.Node node;
		private java.util.ListIterator itr;
		private edu.jhu.apl.patterns_class.dom.replacement.Node node_current;
		serializePretty serializePrettyRecursive = new serializePretty();
		serializeMinimal serializeMinimalRecursive = new serializeMinimal();
		
		public Iterator(edu.jhu.apl.patterns_class.dom.replacement.Node sr){
			node = sr;
		}
		public void first(edu.jhu.apl.patterns_class.dom.replacement.Node node, String serializationType) throws IOException{
			itr = ((edu.jhu.apl.patterns_class.dom.NodeList )node.getAttributes()).listIterator(0);
			next(serializationType);
		}
		public void next(String serializationType) throws IOException {
			try {
				node_current = (edu.jhu.apl.patterns_class.dom.replacement.Node) itr.next();

				if(serializationType == "Pretty"){
					serializePrettyRecursive.visit(node_current);
				}
				else if (serializationType == "Minimal"){
					serializeMinimalRecursive.visit(node_current);
				}
			}
			catch (NoSuchElementException ex){
				node_current = null;
			}
		}
		public boolean is_done(){
			return node_current == null;
		}
		public edu.jhu.apl.patterns_class.dom.replacement.Node current_item(){
			return node_current;
		}
		
	}
	//Iterator Factory
	public Iterator IteratorFactory(edu.jhu.apl.patterns_class.dom.replacement.Node node){
		Iterator it1 = new Iterator(node);
		return it1;
	}

	
}
