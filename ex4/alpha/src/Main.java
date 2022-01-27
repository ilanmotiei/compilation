   
import java.io.*;
import java.io.PrintWriter;
import java_cup.runtime.Symbol;
import AST.*;
import IR.*;

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
			file_reader = new FileReader(inputFilename); // Initialize a file reader
			file_writer = new PrintWriter(outputFilename); // Initialize a file writer
			l = new Lexer(file_reader); // Initialize a new lexer
			p = new Parser(l, file_writer); // Initialize a new parser

			/***********************************/

			/* 3 ... 2 ... 1 ... Parse !!! */
			AST = (AST_PROGRAM) p.parse().value;

			/***********************************/

			/* Print the AST ... */
			AST_GRAPHVIZ.getInstance(); // Print the first line of the Graphwiz conf. file
			AST.PrintMe();
			AST_GRAPHVIZ.getInstance().finalizeFile(); // Finalize AST GRAPHIZ DOT file

			/***********************************/

			/* Semant the AST ... */
			
			try
			{
				AST.SemantMe();

				/* 
				Getting here means no semantic errors have been occured.
				In particular no lexical and syntactic errors have been occured.
				*/
					
				file_writer.print("OK");
			}
			catch (Exception e)
			{
				// A semantic error have been occured.

				System.out.println(e.getMessage());
				String line = e.getMessage().split(" : ")[1];
				String where = e.getMessage().split(" : ")[2];

				file_writer.print("ERROR" + "(" + line + ")");
			}

			/***********************************/
			IR.getInstance().AllocateRegisters();
			IR.getInstance().MIPSme();

			/* Close output file */
			file_writer.close();
    	}
			     
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}


