package edu.jhu.apl.patterns_class.dom;

//Event Handler implemented using CoR pattern
public abstract class CoRHandler {

	CoRHandler successor;

	public void setSuccessor(CoRHandler successor) {
		this.successor = successor;
	}

	public abstract void handleRequest(HandlerTypeEnum request);

}

enum HandlerTypeEnum {
	type1, type2;
}

class Type1Handler extends CoRHandler {

	public void handleRequest(HandlerTypeEnum request) {
		if (request == HandlerTypeEnum.type1) {
			System.out.println("Type1Handler handles " + request);
		} else {
			System.out.println("Type1Handler doesn't handle " + request);
			if (successor != null) {
				successor.handleRequest(request);
			}
		}
	}

}

class Type2Handler extends CoRHandler {

	public void handleRequest(HandlerTypeEnum request) {
		if (request == HandlerTypeEnum.type2) {
			System.out.println("Type2Handler handles " + request);
		} else {
			System.out.println("Type2Handler doesn't handle " + request);
			if (successor != null) {
				successor.handleRequest(request);
			}
		}
	}

}