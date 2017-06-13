package edu.jhu.apl.patterns_class;

import java.io.BufferedWriter;
import java.io.Console;
import java.util.Arrays;
import edu.jhu.apl.patterns_class.dom.Document;

import java.io.IOException;
import java.io.Writer;

public class Facade
{
	public static void main (String args[]) throws IOException {
		Console c = System.console();
	    if (c == null) {
	        System.err.println("No console.");
	        System.exit(1);
	    }
	    else{
	    	String userSelection = "A";
	    	String [] validOperations = {"Q", "P", "B", "PS", "MS", "T"};
	    	System.out.println("Welcome!");
	    	while(userSelection != "Q"){
		    	String[] facadeMenu = {
		    		"The operations below are available for you to choose from, click the corresponding key to execute:",
		    		"P: Parse XML File",
		    		"B: Build Document",
		    		"S: Serialize XML File",
		    		"T: Tokenize XML File",
		    		"Q: Quit"};	
				for (String s : facadeMenu)
		    		System.out.println(s);
				userSelection = c.readLine("Enter your selection: ");
				if(!Arrays.asList(validOperations).contains(userSelection)){
					System.out.println("Invalid selection, choose again");
				}
				else{
					if(userSelection == "P"){
						String filename = userSelection = c.readLine("Enter a file name: ");
						DocumentBuilder docBuilder = new DocumentBuilder();
						docBuilder.parse(filename);
						System.out.println("File has been parsed!");
					}
					else if(userSelection == "B"){
						String builderUserSelection = "Z";
						String userInput = "";
						while(builderUserSelection != "F"){
							String[] builderMenu = {
						    		"The operations below are available for you to choose from while building a doc, click the corresponding key to execute:",
						    		"E: Create Element",
						    		"A: Create Attribute",
						    		"V: Add Value",
						    		"F: Finish Building"};
							builderUserSelection = c.readLine("Enter your selection: ");
							System.out.println(builderMenu);
							Document doc = new Document();
							DocumentBuilder docBuilder = new DocumentBuilder(doc);
							if(builderUserSelection == "E"){
								userInput = c.readLine("Enter tag name: ");
								docBuilder.createElement(userInput);
							}
							else if(builderUserSelection == "A"){
								userInput = c.readLine("Enter attribute value: ");
								docBuilder.createAttribute(userInput);
							}
							else if(builderUserSelection == "V")
								userInput = c.readLine("Enter value: ");
								docBuilder.addValue(userInput);
						}
						System.out.println("Finished Building!");
					}		
					else if(userSelection == "S"){
						String filename = userSelection = c.readLine("Enter a file name: ");
						String outputfilename = userSelection = c.readLine("Enter an output file name: ");
						Writer writer = null;
						BufferedWriter bufferedWriter = new BufferedWriter(writer);
						XMLSerializer serializer = new XMLSerializer(bufferedWriter);
						String[] serializerArgs = new String[2];
						serializerArgs[0] = filename;
						serializerArgs[1] = outputfilename;
						serializer.main(serializerArgs);
						System.out.println("File has been serialized!");
					}
					else if(userSelection == "T"){
						String filename = userSelection = c.readLine("Enter a file name: ");
						XMLTokenizer tokenizer = new XMLTokenizer(filename); 
						String[] tokenizerArgs = new String[1];
						tokenizerArgs[0] = filename;
						tokenizer.main(tokenizerArgs);
						System.out.println("File has been tokenized!");
					}
				}
			
	    	}
	    }
    }
}

