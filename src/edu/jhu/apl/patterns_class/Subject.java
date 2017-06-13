package edu.jhu.apl.patterns_class;

public abstract class Subject
{
	private java.util.Vector<Observer>	observers	= new java.util.Vector<Observer>();

	public void	attach(Observer observer)
	{
		if (!observers.contains(observer))
			observers.add(observer);
	}

	public void	detach(Observer observer)
	{
		observers.remove(observer);
	}

	protected boolean notify(edu.jhu.apl.patterns_class.dom.replacement.Node container, short targetType, String target)
	{
		boolean	result	= true;

		for (java.util.Iterator<Observer> i = observers.iterator(); i.hasNext();)
			result &= i.next().update(container, targetType, target);

		return result;
	}
}