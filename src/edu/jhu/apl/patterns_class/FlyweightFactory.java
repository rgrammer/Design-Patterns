package edu.jhu.apl.patterns_class;

import java.util.HashMap;

import edu.jhu.apl.patterns_class.dom.Attr;
import edu.jhu.apl.patterns_class.dom.Document;
import edu.jhu.apl.patterns_class.dom.Element;
import edu.jhu.apl.patterns_class.dom.Node;
import edu.jhu.apl.patterns_class.dom.Text;

//Creates flyweights of type Element, Text, or Attribute. Only the intrinsic values are passed in during creation
class FlyweightFactory {

	private static final HashMap nodes = new HashMap();

	public static Node getFlyweight(String value, short type) {
		Node concreteNode = (Node) nodes.get(type);

		if (concreteNode == null) {
			if (type == org.w3c.dom.Node.ELEMENT_NODE) {
				concreteNode = new Element(value);
			} 
			else if (type == org.w3c.dom.Node.TEXT_NODE) {
				concreteNode = new Text(value);
			}
			else if (type == org.w3c.dom.Node.ATTRIBUTE_NODE) {
				concreteNode = new Attr(value);
			}
			nodes.put(type, concreteNode);
		}
		return concreteNode;
	}
}