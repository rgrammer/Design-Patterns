package edu.jhu.apl.patterns_class.dom;

public class NodeList extends java.util.LinkedList<edu.jhu.apl.patterns_class.dom.replacement.Node>
  implements edu.jhu.apl.patterns_class.dom.replacement.NodeList
{
	public int		getLength()	{ return size(); }
	public org.w3c.dom.Node	item(int index)
	  { return (org.w3c.dom.Node )get(index); }
}
