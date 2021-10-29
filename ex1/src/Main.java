
import java.io.*;
import java.io.PrinterWriter;
import java.lang.Math;
import java.util.HashMap;

import TokenNames;


import java_cup.runtime.Symbol;

public class Main{

    private static final int INT_UPPER_LIMIT = (int) Math.pow(2, 15);
    private static final HashMap<Integer, String> TokenNames = new HashMap<Integer, String>();

    public static void main(String argv[]){
        Lexer l;
        Symbol curr;
        FileReader file_reader;
        PrintWriter file_writer;
        String inputFilename = argv[0];
        String outputFilename = argv[1];

        init_TokenNames(); // initializing the token names dictionary;

        try
        {
            file_reader = new FileReader(inputFilename);

            file_writer = new PrintWriter(outputFilename);

            l = new Lexer(file_reader); // creating the lexer for the input file;

            // running on the input file till its end;
            for (curr = l.next_token(); curr != TokenNames.EOF; curr = l.next_token()){
                if (curr.sym == TokenNames.INT){
                    // checking that the number is legal;
                    if (!((curr.value >= 0) || (curr.value < INT_UPPER_LIMIT))){
                        // go to the catch block;

                        throw new Exception("INT out of range");
                    }
                }

                // else (if we caught an another error, we are going to the catch block, so there is no error here):
                print_to_File(file_writer, curr, l.getLine(), l.getTokenStartPosition());
            }

            l.yyclose(); // closing lexer's input file; similar file_reader.close(), but might make actions that are
                        // specific for the lexer;

            file_writer.close();
        }
        catch (Exception e){

            // print only "ERROR" to the output file, close the file, and print the exception tracer's message;

            // TODO

            e.printStackTrace();
        }
    }

    static private void print_to_File(PrintWriter file_writer, Symbol s, int line, int column){

        int token = s.sym;
        String tokenName = TokenNames.get(token)
        
        if (s.value != null){
            switch(tokenName)
            {
                case "STRING":
                case "ID":
                    file_writer.format("%s(%s)[%i,%i]", tokenName, (String) s.value, line, column);
                    break;
                
                case "INT":
                    file_writer.format("%s(%i)[%i,%i]", tokenName, (Integer) s.value, line, column);
            }
        } else{ 
            //s.value == NULL;

            file_writer.format("%s[%i,%i]", tokenName, line, column);
        }

        file_writer.print("\n"); // moving to the next line in the file;
    }

    static private void init_TokenNames(){
        String Names[] = new String[]{
            "EOF", "INT", "STRING", "ID", "LPAREN", "RPAREN", "LBRACK", "RBRACK", "LBRACE", "RBRACE", "NIL", "PLUS", "MINUS", 
            "TIMES", "DIVIDE", "COMMA", "DOT", "SEMICOLON", "ASSIGN", "EQ", "LT", "GT", "ARRAY", "CLASS", "EXTENDS", "RETURN",
            "WHILE", "IF", "NEW", "TYPE_INT", "TYPE_STRING"
        };

        for (int i=0; i < Names.length; i++){
            TokenNames.put(i, Names[i]);
        }
    }
}
