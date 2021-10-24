
import java_cup.runtime.*;

%%

%class Lexer

%line
%column

%cupsym TokenNames

%cup



%{
	private Symbol symbol(int type, Object content) 	{   return new Symbol(type, content, yyline, yycolumn);   }
	private Symbol symbol(int type)				{   return new Symbol(type, yyline, yycolumn);   }
	public int getLine()					{	return yyline + 1;	 }
	public int getTokenStartPosition()			{	return yycolumn + 1;   }
%}


LETTER = [A-Za-z]
DIGIT = [0-9]
IDENTIFIER = {LETTER} ({DIGIT}|{LETTER})*
WHITE_SPACE = [\t\n ]+
IN_COMMENT_CHAR = "(" | ")" | "[" | "]" | "{" | "}" | [?!+-*/.;]
COMMENT = ("//" {IN_COMMENT_CHAR}* \n) | ("/*" {IN_COMMENT_CHAR}* "*/")
INTEGER = [1-9] {DIGIT}* | 0
STRING = " {LETTER}* "


%%


<YYINITIAL>{

	/* Keywords (Highest Priority) */
	
	"class"				{  	 return symbol(TokenNames.CLASS);    }
	"extends"			{  	 return symbol(TokenNames.EXTENDS);    }
	"nil"				{  	 return symbol(TokenNames.NIL);    }
	"return"			{  	 return symbol(TokenNames.RETURN);    }
	"array"				{  	 return symbol(TokenNames.ARRAY);    }
	"new"				{  	 return symbol(TokenNames.NEW);    }
	"while"				{  	 return symbol(TokenNames.WHILE);    }
	"if"				{  	 return symbol(TokenNames.IF);    }

	{IDENTIFIER}			{  	 return symbol(TokenNames.IDENTIFIER, yytext());    }
	{INTEGER}			{  	 return symbol(TokenNames.INTEGER, yytext());    }
	{STRING}			{  	 return symbol(TokenNames.STRING, yytext());    }
	
	/* Parenthesis, brackets and curly braces */
	
	"("					{  	 return symbol(TokenNames.LPAREN);    }
	")"					{  	 return symbol(TokenNames.RPAREN);    }
	"["					{  	 return symbol(TokenNames.LBRACK);    }
	"]"					{  	 return symbol(TokenNames.RBRACK);    }
	"{"					{  	 return symbol(TokenNames.LBRACE);    }
	"}"					{  	 return symbol(TokenNames.RBRACE);    }
	
	/* Operators */

	"+"					{  	 return symbol(TokenNames.PLUS);    }
	"-"					{  	 return symbol(TokenNames.MINUS);    }
	"*"					{  	 return symbol(TokenNames.TIMES);    }
	"/" 					{  	 return symbol(TokenNames.DIVIDE);    }
	"="					{  	 return symbol(TokenNames.EQ);    }
	"<"					{  	 return symbol(TokenNames.LT);    }
	">"					{  	 return symbol(TokenNames.GT);    }
	
	/* Seperators */
	
	","					{  	 return symbol(TokenNames.COMMA);    }
	"."					{  	 return symbol(TokenNames.DOT);    }
	";"					{  	 return symbol(TokenNames.SEMICOLON);    }
	":="					{  	 return symbol(TokenNames.ASSIGN);    }
	
	<<EOF>>            			{    return symbol(TokenNames.EOF);    }	
	
}
