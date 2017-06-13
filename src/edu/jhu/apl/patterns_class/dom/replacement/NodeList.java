package edu.jhu.apl.patterns_class.dom.replacement;

public interface NodeList extends org.w3c.dom.NodeList
{
	public int	getLength();
	public org.w3c.dom.Node	item(int index);
}
