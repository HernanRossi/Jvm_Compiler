
/*
 * Compiler.java
 *
 * A starting place for the unamed language compiler for CSC 435/535
 *
 */

import org.antlr.runtime.*;
import java.io.*;
import java.util.*;
import AST.*;
import Semantic.*;
import IntRep.*;

public class Compiler {

	public static String inputFileName;
	
	public static void main (String[] args) throws Exception {
		ANTLRInputStream input;

		if (args.length == 0 ) {
			System.out.println("Usage: Test filename.ul");
			return;
		}
		else {
			input = new ANTLRInputStream(new FileInputStream(args[0]));
		}
		inputFileName = args[0];
		String[] inputString = inputFileName.split("/");
		int inputLength = inputString.length;
		inputFileName = inputString[inputLength -1];
		String[] tempinputString = inputFileName.split("\\.");
		inputFileName = tempinputString[0];
		
		// The name of the grammar here is "ulNoActions",
		// so ANTLR generates ulNoActionsLexer and ulNoActionsParser
		ulNoActionsLexer lexer = new ulNoActionsLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ulNoActionsParser parser = new ulNoActionsParser(tokens);

		try {
			Program p = parser.program();	
			TypeVisitor tv= new TypeCheckVisitor();	
			p.accept(tv);
			TempVisitor tmpv = new IntermedRepVisitor();
			tmpv.setOutputFileName(inputFileName);
			p.accept(tmpv);
			// Visitor v = new PrintVisitor();
			// p.accept(v);
		}
		catch (RecognitionException e )	{
			// A lexical or parsing error occured.
			// ANTLR will have already printed information on the
			// console due to code added to the grammar.  So there is
			// nothing to do here.
		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
