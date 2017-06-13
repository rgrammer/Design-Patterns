package edu.jhu.apl.patterns_class;


import java.util.Stack;

import edu.jhu.apl.patterns_class.XMLSerializer.serializePretty;
import edu.jhu.apl.patterns_class.XMLTokenizer.XMLToken;
import edu.jhu.apl.patterns_class.dom.Document;
import edu.jhu.apl.patterns_class.dom.Document.XMLSerializerFactory;

public class Director
{
	private static final int	BEFORE_PROLOG		= 1;
	private static final int	AFTER_PROLOG		= 2;
	private static final int	PARSING_ELEMENT		= 3;
	private static final int	IN_NONNULL_ELEMENT	= 4;
	private static final int	END			= 5;
	
	private int totalValue = 0;
	public class InterpreterContext {
	    public int addIntegers(int a, int b){
	        return a + b;
	    }
	    public int subtractIntegers(int a, int b){
	        return a - b;
	    }
	}
	
	public interface Expression {
	    int interpret(InterpreterContext ic);
	}
	
	public class AddIntegerExpression implements Expression { 
	    private int i; 
	    public AddIntegerExpression(int c){
	        this.i=c;
	    }
	    @Override
	    public int interpret(InterpreterContext ic) {
	        return ic.addIntegers(this.i, totalValue);
	    }
	}
	
	public class SubtractIntegerExpression implements Expression { 
	    private int i; 
	    public SubtractIntegerExpression(int c){
	        this.i=c;
	    }
	    @Override
	    public int interpret(InterpreterContext ic) {
	        return ic.subtractIntegers(this.i, totalValue);
	    }
	}
	
	public InterpreterContext ic;
	
	public int interpretCalculations(String str, int c){
        Expression exp=null;
        //create rules for expressions
        if(str.contains("+")){
            exp=new AddIntegerExpression(c);
        }else if(str.contains("-")){
            exp=new SubtractIntegerExpression(c);
        }else return c;
         
        return exp.interpret(ic);
    }

	public Director(String filename, DocumentBuilder builder)
	  throws java.io.FileNotFoundException, org.w3c.dom.DOMException, java.io.IOException
	{
		edu.jhu.apl.patterns_class.XMLTokenizer	tokenizer		= new XMLTokenizer(filename);
		int					documentLocation	= BEFORE_PROLOG;
		int					lastToken		= XMLToken.NULL;
		int					currentToken		= XMLToken.NULL;
		XMLToken				token			= null;
		
		//Interpreter Variables 
		boolean inOperator = false;
		boolean inValue = false;
		Stack operatorStack = new Stack();
		String currentOperator = "+";

		do
		{
			token		= tokenizer.getNextToken();
			currentToken	= token.getTokenType();

			switch(documentLocation)
			{
			case BEFORE_PROLOG:
				switch(lastToken)
				{
				case XMLToken.NULL:
					switch(currentToken)
					{
					case XMLToken.PROLOG_START:
						//builder.createProlog();
						builder.setState(builder.new BeforePrologState());
						builder.doAction(null);
						documentLocation	= AFTER_PROLOG;
						break;
					default:
					}
				default:
					// Shouldn't be able to get here.
				}
				break;
			case AFTER_PROLOG:
				switch(lastToken)
				{
				case XMLToken.PROLOG_START:
					switch(currentToken)
					{
					case XMLToken.PROLOG_IDENTIFIER:
						builder.setState(builder.new PrologIdentifierState());
						//builder.identifyProlog(token.getToken());
						builder.doAction(token.getToken());
						break;
					default:
					}
					break;
				case XMLToken.PROLOG_IDENTIFIER:
					switch(currentToken)
					{
					case XMLToken.ATTRIBUTE:
						builder.setState(builder.new AttributeState());
						builder.doAction(token.getToken());
						//builder.createAttribute(token.getToken());
						break;
					case XMLToken.PROLOG_END:
						//builder.endProlog();
						builder.setState(builder.new AfterPrologState());
						builder.doAction(null);
						documentLocation	= PARSING_ELEMENT;
						break;
					default:
					}
					break;
				case XMLToken.ATTRIBUTE:
					switch(currentToken)
					{
					case XMLToken.ATTRIBUTE_VALUE:
						builder.setState(builder.new AttributeValueState());
						builder.doAction(token.getToken());
						//builder.valueAttribute(token.getToken());
						break;
					default:
					}
					break;
				case XMLToken.ATTRIBUTE_VALUE:
					switch(currentToken)
					{
					case XMLToken.ATTRIBUTE:
						builder.setState(builder.new AttributeState());
						builder.doAction(token.getToken());
						//builder.createAttribute(token.getToken());
						break;
					case XMLToken.PROLOG_END:
						//builder.endProlog();
						builder.setState(builder.new AfterPrologState());
						builder.doAction(null);
						documentLocation	= PARSING_ELEMENT;
						break;
					default:
					}
					break;
				default:
				}
			case PARSING_ELEMENT:
				switch(lastToken)
				{
				case XMLToken.TAG_START:
					switch(currentToken)
					{
					case XMLToken.ELEMENT:
						builder.setState(builder.new CreateElementState());
						builder.doAction(token.getToken());
						//builder.createElement(token.getToken());
						
						//Interpreter implementation
						if(token.getToken() == "operation"){
							inOperator = true;
							inValue = false;
							operatorStack.push(token.getToken());
						}
						else if(token.getToken() == "value"){
							inValue = true;
							inOperator = false;
						}
						else
							inValue = false;
							inOperator = false;
						break;
					default:
					}
					break;
				case XMLToken.ELEMENT:
					switch(currentToken)
					{
					
					case XMLToken.ATTRIBUTE:
						builder.setState(builder.new AttributeState());
						builder.doAction(token.getToken());
						//builder.createAttribute(token.getToken());
						break;
					case XMLToken.TAG_END:
						documentLocation	= IN_NONNULL_ELEMENT;
						//builder.pushElement();
						builder.setState(builder.new InNonNullElementState());
						builder.doAction(null);
						break;
					case XMLToken.NULL_TAG_END:
						break;
					default:
					}
					break;
				case XMLToken.ATTRIBUTE:
					switch(currentToken)
					{
					case XMLToken.ATTRIBUTE_VALUE:
						builder.setState(builder.new AttributeValueState());
						builder.doAction(token.getToken());
						//builder.valueAttribute(token.getToken());
						break;
					default:
					}
					break;
				case XMLToken.ATTRIBUTE_VALUE:
					switch(currentToken)
					{
					case XMLToken.ATTRIBUTE:
						builder.setState(builder.new AttributeState());
						builder.doAction(token.getToken());
						//builder.createAttribute(token.getToken());
						break;
					case XMLToken.TAG_END:
						documentLocation	= IN_NONNULL_ELEMENT;
						//builder.pushElement();
						builder.setState(builder.new InNonNullElementState());
						builder.doAction(null);
						break;
					case XMLToken.NULL_TAG_END:
						break;
					default:
					}
					break;
				case XMLToken.PROLOG_END:
					switch(currentToken)
					{
					case XMLToken.TAG_START:
						// Actually create element when we read tag name.
						break;
					default:
					}
					break;
				case XMLToken.NULL_TAG_END:
					switch(currentToken)
					{
					case XMLToken.TAG_START:
						// Actually create element when we read tag name.
						break;
					case XMLToken.TAG_CLOSE_START:
						documentLocation	= IN_NONNULL_ELEMENT;
						break;
					default:
					}
					break;
				default:
				}
				break;
			case IN_NONNULL_ELEMENT:
				switch(lastToken)
				{
				case XMLToken.ELEMENT:
					switch(currentToken)
					{
					case XMLToken.TAG_END:
						if (!builder.popElement())
							documentLocation	= END;
						break;
					default:
					}
					break;
				case XMLToken.TAG_END:
					switch(currentToken)
					{
					case XMLToken.TAG_START:
						documentLocation	= PARSING_ELEMENT;
						// Actually create element when we read tag name.
						break;
					case XMLToken.VALUE:
						builder.setState(builder.new AddValueState());
						builder.doAction(token.getToken());
						//builder.addValue(token.getToken());
						
						//Do interpreter calculation
						if(inValue = true){
							currentOperator = operatorStack.pop().toString();
							interpretCalculations(currentOperator, Integer.parseInt(token.getToken()));
						}
						break;
					case XMLToken.TAG_CLOSE_START:
						break;
					default:
					}
					break;
				case XMLToken.VALUE:
					switch(currentToken)
					{
					case XMLToken.TAG_CLOSE_START:
						break;
					default:
					}
					break;
				case XMLToken.TAG_CLOSE_START:
					switch(currentToken)
					{
					case XMLToken.ELEMENT:
						builder.setState(builder.new ConfirmElementState());
						builder.doAction(token.getToken());
						//builder.confirmElement(token.getToken());
						break;
					default:
					}
					break;
				default:
				}
				break;
			case END:
				switch(currentToken)
				{
				case XMLToken.NULL:
					break;
				default:
				}
				break;
			default:
				// Shouldn't be able to get here.
			}

			lastToken	= currentToken;
		} while (currentToken != XMLToken.NULL);
		
		//Check to see if any operators left on the interpreter operator stack
		if(operatorStack.size() > 0){
			currentOperator = operatorStack.pop().toString();
			if(currentOperator == "-"){
				totalValue = totalValue * -1;
			}
		}
		System.out.println("Total interpreted value: " + totalValue);
	}

	public static void main(String[] args)
	{
		if (args.length < 2)
		{
			System.out.println("No output filenames provided.");
			System.exit(0);
		}

		Document	document = new edu.jhu.apl.patterns_class.dom.Document();
		edu.jhu.apl.patterns_class.DocumentBuilder			builder	 = new DocumentBuilder(document);
	
		//
		// Schema for this document:
		// document contains:  element
		// element contains:  element
		// element contains attributes:  attribute, attribute2
		//
		XMLValidator	xmlValidator	= new XMLValidator();
		ValidChildren	schemaElement	= xmlValidator.addSchemaElement(null);
		schemaElement.addValidChild("document", false);
		schemaElement	= xmlValidator.addSchemaElement("document");
		schemaElement.addValidChild("element", false);
		schemaElement	= xmlValidator.addSchemaElement("element");
		schemaElement.addValidChild("element", false);
		schemaElement.addValidChild("attribute", true);
		schemaElement.addValidChild("attribute2", true);
		schemaElement.setCanHaveText(true);

		try
		{
			edu.jhu.apl.patterns_class.Director		director =
			  new edu.jhu.apl.patterns_class.Director(args[0], builder);
			XMLSerializer	xmlSerializer	=
			  new XMLSerializer(new java.io.BufferedWriter(new java.io.OutputStreamWriter(
			  new java.io.FileOutputStream(new java.io.File(args[1])))));
			
			serializePretty prettySerializer = xmlSerializer.new serializePretty();
			//XMLSerializer	xmlSerializer	= xmlSerializerFactory(args[0]);
			prettySerializer.visit(builder.getDocument());
			xmlSerializer.close();
		}
		catch (java.io.FileNotFoundException e)
		{
			System.out.println("Exception:  " + e);
			e.printStackTrace();
		}
		catch (java.io.IOException e)
		{
			System.out.println("Exception:  " + e);
			e.printStackTrace();
		}
		catch (org.w3c.dom.DOMException e)
		{
			System.out.println("Exception:  " + e);
			e.printStackTrace();
		}
	}
}