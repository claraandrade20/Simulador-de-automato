%%
%class Lexer
%unicode
%line
%column
%type Token

%{
  private Token novo(Token.Tipo tipo) {
    return new Token(tipo, yytext(), yyline + 1, yycolumn + 1);
  }
%}

BRANCO = [ \t\r\n]+

%%

"("      { return novo(Token.Tipo.ABRE); }
")"      { return novo(Token.Tipo.FECHA); }
{BRANCO} { }
.        { return novo(Token.Tipo.ERRO); }
