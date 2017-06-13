package edu.jhu.apl.patterns_class;

import edu.jhu.apl.patterns_class.dom.Attr;
import edu.jhu.apl.patterns_class.dom.Element;
import edu.jhu.apl.patterns_class.dom.Document;

public class CommandInterpreter
{
	private Command command;
	public void setCommand(Command command){
		this.command = command;
	}
	public void executeCommand(){
		command.execute();
	}
}

interface Command{
	public void execute();
}

class ParseFileCommand implements Command{

	XMLTokenizer xmltokenizer;
	String args[];
	public ParseFileCommand(XMLTokenizer xmltokenizer, String args[]){
		this.xmltokenizer = xmltokenizer;
		this.args = args;
	}
	public void execute() {
		xmltokenizer.main(args);
	}
}

class SerializeTreeCommand implements Command{

	XMLSerializer xmlserializer;
	String args[];
	public SerializeTreeCommand(XMLSerializer xmlserializer, String args[]){
		this.xmlserializer = xmlserializer;
		this.args = args;
	}
	public void execute() {
		xmlserializer.main(args);
	}
}

class SetAttributeCommand implements Command{
	String name;
	String value;
	Element element;
	public SetAttributeCommand(String name, String value, Element element){
		this.name = name;
		this.value = value;
		this.element = element;
	}
	public void execute() {
		element.setAttribute(name, value);
	}
}

class RemoveAttributeCommand implements Command{
	String name;
	Element element;
	public RemoveAttributeCommand(String name, Element element){
		this.name = name;
		this.element = element;
	}
	public void execute() {
		element.removeAttribute(name);
	}
}

class SetAttributeValueCommand implements Command{
	String value;
	Attr attr;
	public SetAttributeValueCommand(String value, Attr attr){
		this.value = value;
		this.attr = attr;
	}
	public void execute() {
		attr.setValue(value);
	}
}

class CreateElementCommand implements Command{
	String tagName;
	Document doc;
	public CreateElementCommand(String tagName, Document doc){
		this.tagName = tagName;
		this.doc = doc;
	}
	public void execute() {
		doc.createElement(tagName);
	}
}

class CreateTextNodeCommand implements Command{
	String data;
	Document doc;
	public CreateTextNodeCommand(String data, Document doc){
		this.data = data;
		this.doc = doc;
	}
	public void execute() {
		doc.createTextNode(data);
	}
}

class CreateAttrCommand implements Command{
	String name;
	Document doc;
	public CreateAttrCommand(String name, Document doc){
		this.name = name;
		this.doc = doc;
	}
	public void execute() {
		doc.createAttribute(name);
	}
}


