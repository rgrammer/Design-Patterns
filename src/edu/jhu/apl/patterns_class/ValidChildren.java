package edu.jhu.apl.patterns_class;

public class ValidChildren implements Observer // and Colleague
{
	private String				thisElement		= null;	// A value of null represents Document.
	private java.util.Vector<String>	validChildren		= new java.util.Vector<String>();
	private java.util.Vector<Boolean>	childIsAttribute	= new java.util.Vector<Boolean>();
	private boolean				_canHaveText		= false;
	private XMLValidator			mediator		= null;
	private boolean				active			= true;

	public ValidChildren(String thisElement, XMLValidator mediator)
	{
		this.thisElement	= thisElement;
		this.mediator		= mediator;
	}

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
			{
				if (mediator != null)
					mediator.validation(thisElement, child, isAttribute);

				return true;
			}

		return false;
	}

	public void activate()		{ active = true; }
	public void deactivate()	{ active = false; }

	//
	// For XML validation completeness, ValidChildren should also have methods for verifying the validity of attribute values
	// as commented in original version of XMLValidator.
	//

	public boolean	update(edu.jhu.apl.patterns_class.dom.replacement.Node container, short targetType, String target)
	{
		if (!active)
			return true;

		if (container == null && thisElement == null && targetType == org.w3c.dom.Node.ELEMENT_NODE)
		{
			return childIsValid(target, false);
		}
		else if (container != null && thisElement != null)
		{
			if (thisElement.compareTo(container.getNodeName()) == 0)
				switch(container.getNodeType())
				{
				case org.w3c.dom.Node.ELEMENT_NODE:
					switch(targetType)
					{
					case org.w3c.dom.Node.ELEMENT_NODE:
						return childIsValid(target, false);
					case org.w3c.dom.Node.ATTRIBUTE_NODE:
						return childIsValid(target, true);
					case org.w3c.dom.Node.TEXT_NODE:
						return canHaveText();
					}
					break;
				case org.w3c.dom.Node.ATTRIBUTE_NODE:
					// Per the block comment above, ValidChildren can't do this yet.
				}
		}

		return true;	// Default behavior if this ValidChildren is indifferent to the event.
	}
}
