
import java_cup.runtime.*;

%%

%class Lexer

%line
%column

%cupsym TokenNames

%cup



%{
	private Symbol symbol(int type, Object content) 	{   return new Symbol(type, yyline, yycolumn, content);   	}
	private Symbol symbol(int type)						{   return new Symbol(type, yyline, yycolumn);   			}
	public int getLine()								{	return yyline + 1;	 									}
	public int getTokenStartPosition()					{	return yycolumn + 1;   									}
%}


/* Basic Types Definitions */
LineTerminator = \r|\n|\r\n
WHITE_SPACE = {LineTerminator} | [ \t\f]
LETTER = [a-zA-Z]
DIGIT = [0-9]

// might be incorrect:
IN_COMMENT_CHAR = "(" | ")" | "[" | "]" | "{" | "}" | [?!+*/;] | "." | "-" |{LETTER}|{DIGIT}|[ \t\f] 
/* excluding  {WHITE_SPACE} */

/* Tokens Definitions */
ID = {LETTER}({DIGIT}|{LETTER})*
INTEGER = {DIGIT}+

// might be incorrect:
COMMENT = ("//"{IN_COMMENT_CHAR}*{LineTerminator}) | ("/*"({IN_COMMENT_CHAR} | {WHITE_SPACE})*"*/")
STRING = \"{LETTER}*\"


%%


<YYINITIAL>{

	/* Keywords (Highest Priority) */
	
	"class"				{  	 return symbol(TokenNames.CLASS);    			}
	"extends"			{  	 return symbol(TokenNames.EXTENDS);    			}
	"nil"				{  	 return symbol(TokenNames.NIL);    				}
	"return"		{  	 return symbol(TokenNames.RETURN);    			}
	"array"				{  	 return symbol(TokenNames.ARRAY);    			}
	"new"				{  	 return symbol(TokenNames.NEW);    				}
	"while"				{  	 return symbol(TokenNames.WHILE);    			}
	"if"				{  	 return symbol(TokenNames.IF);    				}
	"int"				{    return symbol(TokenNames.TYPE_INT);		    }
	"string" 			{	 return symbol(TokenNames.TYPE_STRING);		    }

	/* Tokens holding a corresponding value */

	{ID}				{  	return symbol(TokenNames.ID, new String(yytext()));    			}
	
	{INTEGER}			{  	

							String got = new String(yytext());
							if ((got.length() != 1) && (got.charAt(0) == '0')){
								return symbol(TokenNames.INT, new String("ERROR"));
							}

							// else:

							return symbol(TokenNames.INT, new Integer(yytext()));
						}

	{STRING}			{  	 return symbol(TokenNames.STRING, yytext());    					}

	/* Comment */
	{COMMENT}			{ /* do nothing */}
	
	/* Parenthesis, brackets and curly braces */
	
	"("					{  	 return symbol(TokenNames.LPAREN);    }
	")"					{  	 return symbol(TokenNames.RPAREN);    }
	"["					{  	 return symbol(TokenNames.LBRACK);    }
	"]"					{  	 return symbol(TokenNames.RBRACK);    }
	"{"					{  	 return symbol(TokenNames.LBRACE);    }
	"}"					{  	 return symbol(TokenNames.RBRACE);    }
	
	/* Operators */

	"+"					{  	 return symbol(TokenNames.PLUS);    	}
	"-"					{  	 return symbol(TokenNames.MINUS);    	}
	"*"					{  	 return symbol(TokenNames.TIMES);    	}
	"/" 				{  	 return symbol(TokenNames.DIVIDE);  	}
	"="					{  	 return symbol(TokenNames.EQ);    		}
	"<"					{  	 return symbol(TokenNames.LT);    		}
	">"					{  	 return symbol(TokenNames.GT);    		}
	
	/* Seperators */
	
	","					{  	 return symbol(TokenNames.COMMA);    	}
	"."					{  	 return symbol(TokenNames.DOT);    		}
	";"					{  	 return symbol(TokenNames.SEMICOLON);   }
	":="				{  	 return symbol(TokenNames.ASSIGN);    	}
	
	/* EOF */
	<<EOF>>            	{    return symbol(TokenNames.EOF);    		}	

	/* Tokens we are igonoring */
	// basic: 
	{WHITE_SPACE} 		{ }
	{IN_COMMENT_CHAR}	{ }
	{LineTerminator}	{ }		
}
