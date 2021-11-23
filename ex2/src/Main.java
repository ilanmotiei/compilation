   
import java.io.*;
import java.io.PrintWriter;
import java_cup.runtime.Symbol;
import AST.*;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		Symbol s;
		AST_PROGRAM AST;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		
		try
		{
			// Initialize a file reader
			file_reader = new FileReader(inputFilename);

			// Initialize a file writer
			file_writer = new PrintWriter(outputFilename);
			
			// Initialize a new lexer
			l = new Lexer(file_reader);
			
			// Initialize a new parser
			p = new Parser(l, file_writer);

			// 3 ... 2 ... 1 ... Parse !!!
			AST = (AST_PROGRAM) p.parse().value;
			
			// ---------- getting here means no error occured ----------

			/* --------------------------------------------------------- */
			/*               PRINT "OK" TO THE OUTPUT FILE               */
			/* --------------------------------------------------------- */
			
			file_writer.print("OK");
			
			// Close output file
			file_writer.close();
			l.yyclose();

			// -----------------------------------------------------------------------
			// PRINT THE AST VISUALIZATION TO THE TERMINAL AND TO THE DESTNIATION FILE 
			//                                                           
			//                  	REMOVE BEFORE SUBMIT!!!!                
			// -----------------------------------------------------------------------

			/*

			// Print the first line of the Graphwiz conf. file
			AST_GRAPHVIZ.getInstance();

			// Print the AST
			AST.PrintMe();
			
			// Finalize AST GRAPHIZ DOT file
			AST_GRAPHVIZ.getInstance().finalizeFile();

			*/
    	}
			     
		catch (Exception e)
		{

			/* --------------------------------------------------------- */
			/*            PRINT "ERROR(${LOC}) TO THE OUTPUT FILE        */
			/* --------------------------------------------------------- */

			e.printStackTrace();
		}
	}
}


