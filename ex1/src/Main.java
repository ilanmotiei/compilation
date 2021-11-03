
import java.io.*;
import java.io.PrintWriter;
import java.lang.Math;
import java.util.HashMap;

import java_cup.runtime.Symbol;

public class Main{

    private static final int INT_UPPER_LIMIT = (int) Math.pow(2, 15);
    private static final HashMap<Integer, String> TokenNameMap = new HashMap<Integer, String>();

    public static void main(String argv[]){
        Lexer l;
        Symbol curr;
        FileReader file_reader;
        PrintWriter file_writer;
        String inputFilename = argv[0];
        String outputFilename = argv[1];

        init_TokenNameMap(); // initializing the token names dictionary;

        try
        {
            file_reader = new FileReader(inputFilename);
            file_writer = new PrintWriter(outputFilename);
            l = new Lexer(file_reader); // creating the lexer for the input file;
        }
        catch (Exception e){

            return;
        }

        try
        {
            // running on the input file till its end;
            for (curr = l.next_token(); curr.sym != TokenNames.EOF; curr = l.next_token()){

                // check if it is a token of integer
                if (curr.sym == TokenNames.INT){
                    // checking that the number is legal;
                    if (!isVaildNumber(curr)){
                        // skip to the catch block;
                        throw new Error("INT out of range");
                    }
                }

                // else (if we caught an another error, we are going to the catch block, so there is no error here):
                print_to_File(file_writer, curr, l.getLine(), l.getTokenStartPosition());
            }

            l.yyclose(); // closing lexer's input file; similar to file_reader.close(), but might make actions that are
                        // specific for the lexer;

            file_writer.close();
        }
        catch (Throwable e){

            // clean the file, print only "ERROR" to the output file, close the file, and print the exception tracer's message;

            (new File(outputFilename)).delete();

            try{
                file_writer = new PrintWriter(outputFilename);
                file_writer.print("ERROR");
                file_writer.close();
                l.yyclose();
            }
            catch (Exception e1){
            }
        }
    }

    static private boolean isVaildNumber(Symbol curr)
    {

        try{
            if (((String) curr.value).contentEquals("ERROR")){
                return false;
            }
        }
        catch (Exception e){
            // curr.value class is Integer
            
            return ((int) curr.value >= 0) && ((int) curr.value < INT_UPPER_LIMIT);
        }

        // Dead code: just for compilation;

        return false;
    }

    static private void print_to_File(PrintWriter file_writer, Symbol s, int line, int column){

        int token = s.sym;
        String tokenName = TokenNameMap.get(token);
        
        if (s.value != null){
            switch(tokenName)
            {
                case "STRING":
                case "ID":
                    file_writer.format("%s(%s)[%d,%d]", tokenName, (String) s.value, line, column);
                    break;
                
                case "INT":
                    file_writer.format("%s(%d)[%d,%d]", tokenName, (int) s.value, line, column);
            }
        } else{ 
            //s.value == NULL;

            file_writer.format("%s[%d,%d]", tokenName, line, column);
        }

        file_writer.print("\n"); // moving to the next line in the file;
    }

    static private void init_TokenNameMap(){
        String Names[] = new String[]{
            "EOF", "INT", "STRING", "ID", "LPAREN", "RPAREN", "LBRACK", "RBRACK", "LBRACE", "RBRACE", "NIL", "PLUS", "MINUS", 
            "TIMES", "DIVIDE", "COMMA", "DOT", "SEMICOLON", "ASSIGN", "EQ", "LT", "GT", "ARRAY", "CLASS", "EXTENDS", "RETURN",
            "WHILE", "IF", "NEW", "TYPE_INT", "TYPE_STRING"
        };

        for (int i=0; i < Names.length; i++){
            TokenNameMap.put(i, Names[i]);
        }
    }
}
