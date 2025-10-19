package compiler.lexical;

import compiler.syntax.sym;
import compiler.lexical.Token;
import es.uned.lsi.compiler.lexical.ScannerIF;
import es.uned.lsi.compiler.lexical.LexicalError;
import es.uned.lsi.compiler.lexical.LexicalErrorManager;

// incluir aqui, si es necesario otras importaciones

%%
 
%public
%class Scanner
%char
%line
%column
%cup
%unicode


%implements ScannerIF
%scanerror LexicalError

// incluir aqui, si es necesario otras directivas
%ignorecase

%{
  LexicalErrorManager lexicalErrorManager = new LexicalErrorManager ();
  //private int commentCount = 0;
  
  private Token newToken(int type, String lexema) {
	  Token token = new Token (type);
      token.setLine (yyline + 1);
      token.setColumn (yycolumn + 1);
      token.setLexema (lexema);
	  return token;
  }

  private Token newToken(int type) {
	  return newToken(type, yytext ());
  }

  private void error(String text) {
		LexicalError error = new LexicalError ();
		error.setLine (yyline + 1);
		error.setColumn (yycolumn + 1);
		error.setLexema (yytext ());
		lexicalErrorManager.lexicalError (text+" "+error);
  }
  private void errorFatal(String text) {
		LexicalError error = new LexicalError ();
		error.setLine (yyline + 1);
		error.setColumn (yycolumn + 1);
		error.setLexema (yytext ());
		lexicalErrorManager.lexicalFatalError (text+" "+error);
  }
  private void errorCadenaSinCerrar() {
		LexicalError error = new LexicalError ();
		error.setLine (yyline + 1);
		error.setColumn (yytext().length() + yycolumn + 1);
		error.setLexema (yytext ());
		lexicalErrorManager.lexicalFatalError ("Cadena sin cerrar... "+error);
  }

  private int comentarios_abiertos=0;
%}

%eof{
	if(comentarios_abiertos>0) {
		errorFatal("Se han quedado comentarios sin cerrar.");
	}

%eof}
  

ESPACIO_BLANCO=[ \t\r\n\f]
fin = "fin"{ESPACIO_BLANCO}

//PALABRAS RESERVADAS (antes de conocer ignore case)
//BEGIN=[Bb][Ee][Gg][Ii][Nn]
//BOOLEAN=[Bb][Oo][Oo][Ll][Ee][Aa][Nn]
//CONST=[Cc][Oo][Nn][Ss][Tt]
//ELSE=[Ee][Ll][Ss][Ee]
//END=[Ee][Nn][Dd]
//FALSE=[Ff][Aa][Ll][Ss][Ee]
//FUNCTION=[Ff][Uu][Nn][Cc][Tt][Ii][Oo][Nn]
//IF=[Ii][Ff]
//IN=[Ii][Nn]
//INTEGER=[Ii][Nn][Tt][Ee][Gg][Ee][Rr]
//OF=[Oo][Ff]
//OR=[Oo][Rr]
//PROCEDURE=[Pp][Rr][Oo][Cc][Ee][Dd][Uu][Rr][Ee]
//PROGRAM=[Pp][Rr][Oo][Gg][Rr][Aa][Mm]
//REPEAT=[Rr][Ee][Pp][Ee][Aa][Tt]
//SET=[Ss][Ee][Tt]
//THEN=[Tt][Hh][Ee][Nn]
//TRUE=[Tt][Rr][Uu][Ee]
//TYPE=[Tt][Yy][Pp][Ee]
//UNTIL=[Uu][Nn][Tt][Ii][Ll]
//VAR=[Vv][Aa][Rr]
//WRITE=[Ww][Rr][Ii][Tt][Ee]

// AUXILIARES
DIGITO=[0-9]
DIGITO_SIN_CERO=[1-9]
CERO=0
LETRA=[a-zA-Z]
ALPHANUMERICO={LETRA}|{DIGITO}

//CONSTANTES LITERALES
ENTERO={CERO} | {DIGITO_SIN_CERO}{DIGITO}* // Tambien {DIGITO} | {DIGITO_SIN_CERO}{DIGITO}+
//LOGICA= "true"|"false"
CADENA_SIN_CERRAR="\""[^\"\n]*
CADENA="\"".*"\"" /* CADENA="\""[^\n]*"\"" Son equivalentes*/

ID={LETRA}{ALPHANUMERICO}*	//ID={LETRA}[a-zA-Z0-9]* 
		
%state COMENTARIO_BLOQUE, COMENTARIO_LINEA

%%

<YYINITIAL> 
{
	/************************
	 * PALABRAS RESERVADAS	*
	 ************************/
	// LO PONGO ANTES QUE LOS IDENTIFICADORES PARA QUE LO DETECTE COMO PALABRA RESERVADA Y NO COMO ID
	"begin"				{ return newToken(sym.BEGIN); 		}
	"boolean"			{ return newToken(sym.BOOLEAN); 	}
	"const"				{ return newToken(sym.CONST); 		}
	"else"				{ return newToken(sym.ELSE); 		}
	"end"				{ return newToken(sym.END); 		}
	"false"				{ return newToken(sym.FALSE); 		}
	"function"			{ return newToken(sym.FUNCTION); 	}
	"if"				{ return newToken(sym.IF); 			}
	"in"				{ return newToken(sym.IN); 			}
	"integer"			{ return newToken(sym.INTEGER); 	}
	"of"				{ return newToken(sym.OF); 			}
	"or"				{ return newToken(sym.OR); 			}
	"procedure"			{ return newToken(sym.PROCEDURE); 	}
	"program"			{ return newToken(sym.PROGRAM); 	}
	"repeat"			{ return newToken(sym.REPEAT); 		}
	"set"				{ return newToken(sym.SET); 		}
	"then"				{ return newToken(sym.THEN); 		}
	"true"				{ return newToken(sym.TRUE); 		}
	"type"				{ return newToken(sym.TYPE); 		}
	"until"				{ return newToken(sym.UNTIL); 		}
	"var"				{ return newToken(sym.VAR); 		}
	"write"				{ return newToken(sym.WRITE); 		}
	
	/****************
	 * COMENTARIOS	*
	 ****************/
	"//"				{ yybegin(COMENTARIO_LINEA);							}
	"{"					{ comentarios_abiertos++; yybegin(COMENTARIO_BLOQUE);	}
	"}"					{ errorFatal("Cierre de comentario sin apertura previa.");	}
	
	/****************************
	 * 	CONSTANTES LITERALES 	*
	 ****************************/
	{ENTERO}			{ return newToken(sym.INT);/*System.out.println("Numero encontrado " + yytext());*/ 	}
//	{LOGICA}			{ return newToken(sym.BOOL);/*System.out.println("Logica encontrado " + yytext());*/ 	}
	{CADENA}			{ String lexema = yytext (); return newToken(sym.STRING, lexema.substring(1,lexema.length()-1)); /*System.out.println("Cadena encontrada: " + yytext());*/ }
	{CADENA_SIN_CERRAR}	{ errorCadenaSinCerrar();																}
	//.*"\""				{ errorFatal("Cadena sin cerrar...");														}
//	"linea_columna"		{ System.out.println("yyline(): " + yyline() +" yychar(): "+ yychar());					}
	/********************
	 * DELIMITADORES	*
	 ********************/
	// "				{Se detecta con las cadenas		}
	"("					{ return newToken(sym.LPAREN); 	}
	")"					{ return newToken(sym.RPAREN); 	}
	"["					{ return newToken(sym.LBRACK); 	}
	"]"					{ return newToken(sym.RBRACK); 	}
	// "{}" y "//"		{los delimitadores de comentarios los trato en los comentarios}
	","					{ return newToken(sym.COMMA); 	}
	".."				{ return newToken(sym.DOTDOT); 	}
	"."					{ return newToken(sym.DOT); 	}
	";"					{ return newToken(sym.SEMICOLON); 	}
	":"					{ return newToken(sym.COLON); 	}
	// "=" 				{se procesan con los operadores	}
	
	/****************
	 * OPERADORES	*
	 ****************/
	// ARITMETICOS
    "+"					{ return newToken(sym.PLUS);	}
    "-"					{ return newToken(sym.MINUS);	}
    "*"					{ return newToken(sym.STAR);	}
    // RELACIONALES
    ">"					{ return newToken(sym.GT); 	}
    "<>"				{ return newToken(sym.NE); 	}
    // LÓGICOS
    //"or"				{ return newToken(sym.OR); 	}
    // DE ASIGNACIÓN
    ":="				{ return newToken(sym.ASSIGN); 			}
    "="					{ return newToken(sym.EQUALS); 	}
    // ESPECIALES
    //"in"				{ return newToken(sym.IN); }

    
	/********************
	 * IDENTIFICADORES	*
	 ********************/
    // Los identificadores al final para que no reconozca las palabras reservadas como identificadores,
    // ya que encajan en el patrón de identificador.
	{ID}				{ return newToken(sym.ID); }
	
	
	/*********
	 * OTROS *
	 *********/
	
	{ESPACIO_BLANCO}	{				}

	{fin} 				{				}
    
    // error en caso de coincidir con ningún patrón
	[^]					{ errorFatal("Token no reconocido..."); 	}
//                        {
//                           LexicalError error = new LexicalError ();
//                           error.setLine (yyline + 1);
//                           error.setColumn (yycolumn + 1);
//                           error.setLexema (yytext ());
//                           lexicalErrorManager.lexicalError (error);
//                        }
    
}

<COMENTARIO_LINEA>
{
	"\r\n"|"\r"|"\n"	{ yybegin(YYINITIAL); 	}
	[^]					{						}
}
<COMENTARIO_BLOQUE>
{
	"{"					{ comentarios_abiertos++; 		}
	"}"					{	comentarios_abiertos--;
							if(comentarios_abiertos == 0)
								yybegin(YYINITIAL); 	}
	[^]					{								}
}

                         


