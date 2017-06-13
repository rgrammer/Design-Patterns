package edu.jhu.apl.patterns_class;

import java.io.IOException;
import edu.jhu.apl.patterns_class.dom.Document;
import edu.jhu.apl.patterns_class.dom.Node;

abstract class AbstractDocumentFactory
{
	abstract void addElement(edu.jhu.apl.patterns_class.dom.replacement.Element element, String tagName);
	abstract void addTextNode(edu.jhu.apl.patterns_class.dom.replacement.Element element, String data);
	abstract void addAttribute(edu.jhu.apl.patterns_class.dom.replacement.Element element, String name, String value);
}
