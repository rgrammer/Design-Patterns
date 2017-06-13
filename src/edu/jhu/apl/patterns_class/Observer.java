package edu.jhu.apl.patterns_class;

public interface Observer
{
	public boolean	update(edu.jhu.apl.patterns_class.dom.replacement.Node container, short targetType, String target);
}