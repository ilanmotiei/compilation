   
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
			p = new Parser(l);

			// 3 ... 2 ... 1 ... Parse !!!
			AST = (AST_PROGRAM) p.parse().value;
			
			/* --------------------------------------------------------- */
			/*               PRINT "OK" TO THE OUTPUT FILE!!!            */
			/* --------------------------------------------------------- */

			// TODO
			
			// Close output file
			file_writer.close();

			/* --------------------------------------------------------- */
			/*    PRINT THE AST VISUALIZATION TO THE DESTNIATION FILE    */
			/* --------------------------------------------------------- */
			
			// Finalize AST GRAPHIZ DOT file
			AST_GRAPHVIZ.getInstance().finalizeFile();

			/* --------------------------------------------------------- */
			/*                  REMOVE BEFORE SUBMIT!!!!                 */
			/* --------------------------------------------------------- */

			// TODO

			// Print the AST
			AST.PrintMe();
    	}
			     
		catch (Exception e)
		{

			/* --------------------------------------------------------- */
			/*          PRINT "ERROR(${LOC}) TO THE OUTPUT FILE!!!       */
			/* --------------------------------------------------------- */
		
			//TODO



			e.printStackTrace();
		}
	}
}


