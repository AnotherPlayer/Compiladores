package BackEnd;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Descenso recursivo ER -> AFN siguiendo el pseudocódigo de los apuntes
 * del 2.º parcial. Usa un analizador léxico simple que emite los mismos
 * números de token que aparecen en los apuntes.
 *
 * Tokens (números fijos):
 *   10: '|' (unión)
 *   20: '.' (concatenación). También se inserta de forma implícita cuando procede.
 *   30: '+' (cerradura positiva)
 *   40: '*' (cerradura de Kleene)
 *   50: '?' (opcional)
 *   60: '('
 *   70: ')'
 *   80: símbolo literal (un solo carácter)
 *   90: '['
 *   100: ']'
 *   110: '-'
 *   -1: fin de entrada
 */
public class RegexAFNBuilder {

    // Constantes de token
    private static final int TOK_OR = 10;
    private static final int TOK_CONCAT = 20;
    private static final int TOK_POS = 30;
    private static final int TOK_KLEENE = 40;
    private static final int TOK_OPT = 50;
    private static final int TOK_LPAREN = 60;
    private static final int TOK_RPAREN = 70;
    private static final int TOK_SYMBOL = 80;
    private static final int TOK_LBRACK = 90;
    private static final int TOK_RBRACK = 100;
    private static final int TOK_MINUS = 110;

    private RegexLexer lexer;

    public AFN construir(String er, int token) {
        lexer = new RegexLexer(er);
        AFN resultado = E();  // E -> T Ep

        // Debe consumirse toda la entrada
        int tk = lexer.yylex();
        if (tk != -1) {
            throw new IllegalArgumentException("Entrada no consumida completamente. Token inesperado: " + tk);
        }

        resultado.asignarToken(token);
        return resultado;
    }

    // E -> T Ep
    private AFN E() {
        AFN t = T();
        return Ep(t);
    }

    // Ep -> '|' T Ep | ε
    private AFN Ep(AFN f) {
        int tk = lexer.yylex();
        if (tk == TOK_OR) {
            AFN derecho = T();
            f.AFN_union(derecho);
            return Ep(f);
        }
        lexer.UndoToken();
        return f;
    }

    // T -> C Tp
    private AFN T() {
        AFN c = C();
        return Tp(c);
    }

    // Tp -> '.' C Tp | ε  (concatenación)
    private AFN Tp(AFN f) {
        int tk = lexer.yylex();
        if (tk == TOK_CONCAT) {
            AFN derecho = C();
            f.AFN_join(derecho);
            return Tp(f);
        }
        lexer.UndoToken();
        return f;
    }

    // C -> F Cp
    private AFN C() {
        AFN f = F();
        return Cp(f);
    }

    // Cp -> ('*'| '+' | '?') Cp | ε
    private AFN Cp(AFN f) {
        int tk = lexer.yylex();
        switch (tk) {
            case TOK_KLEENE:
                f.AFN_cerrKleene();
                return Cp(f);
            case TOK_POS:
                f.AFN_cerrPos();
                return Cp(f);
            case TOK_OPT:
                f.AFN_opcional();
                return Cp(f);
            default:
                lexer.UndoToken();
                return f;
        }
    }

    // F -> '(' E ')' | [a-b] | símbolo
    private AFN F() {
        int tk = lexer.yylex();
        switch (tk) {
            case TOK_LPAREN: {
                AFN interior = E();
                int cierre = lexer.yylex();
                if (cierre != TOK_RPAREN) {
                    throw new IllegalArgumentException("Se esperaba ')' en la expresión regular.");
                }
                return interior;
            }
            case TOK_LBRACK: {
                // Esperamos: símbolo 80, '-' 110, símbolo 80, ']'
                int tkInicio = lexer.yylex();
                if (tkInicio != TOK_SYMBOL) {
                    throw new IllegalArgumentException("Se esperaba símbolo inicial del rango.");
                }
                char a = lexer.getLexema().charAt(0);

                int guion = lexer.yylex();
                if (guion != TOK_MINUS) {
                    throw new IllegalArgumentException("Se esperaba '-' dentro del rango.");
                }

                int tkFin = lexer.yylex();
                if (tkFin != TOK_SYMBOL) {
                    throw new IllegalArgumentException("Se esperaba símbolo final del rango.");
                }
                char b = lexer.getLexema().charAt(0);

                int cierre = lexer.yylex();
                if (cierre != TOK_RBRACK) {
                    throw new IllegalArgumentException("Se esperaba ']' para cerrar el rango.");
                }

                AFN rango = new AFN();
                rango.doBasic(a, b);
                return rango;
            }
            case TOK_SYMBOL: {
                char c = lexer.getLexema().charAt(0);
                AFN basico = new AFN();
                basico.doBasic(c);
                return basico;
            }
            default:
                throw new IllegalArgumentException("Token inesperado en F(): " + tk);
        }
    }

    /**
     * Analizador léxico simple para ER que emite los tokens numéricos de los apuntes
     * y soporta UndoToken(). Inserta tokens de concatenación (20) de forma implícita
     * cuando detecta que un factor puede seguir a otro.
     */
    private static class RegexLexer {
        private final String input;
        private int idx;
        private int ultimoToken;
        private String lexema;
        private final Deque<Integer> pilaTokens = new ArrayDeque<>();

        RegexLexer(String er) {
            this.input = er;
            this.idx = 0;
            this.ultimoToken = -1;
            this.lexema = "";
        }

        int yylex() {
            // Si hay token deshecho, úsalo primero
            if (!pilaTokens.isEmpty()) {
                int tk = pilaTokens.pop();
                ultimoToken = tk;
                return tk;
            }

            // Consumir espacios
            while (idx < input.length() && Character.isWhitespace(input.charAt(idx))) {
                idx++;
            }

            // Fin de cadena
            if (idx >= input.length()) {
                ultimoToken = -1;
                lexema = "";
                return -1;
            }

            char c = input.charAt(idx);

            // Inserción implícita de concatenación cuando corresponde
            if (debeInsertarConcat(c, ultimoToken)) {
                ultimoToken = TOK_CONCAT;
                lexema = ".";
                return TOK_CONCAT;
            }

            idx++; // Consumimos el carácter actual
            switch (c) {
                case '|':
                    ultimoToken = TOK_OR;
                    lexema = "|";
                    return TOK_OR;
                case '.':
                    ultimoToken = TOK_CONCAT;
                    lexema = ".";
                    return TOK_CONCAT;
                case '+':
                    ultimoToken = TOK_POS;
                    lexema = "+";
                    return TOK_POS;
                case '*':
                    ultimoToken = TOK_KLEENE;
                    lexema = "*";
                    return TOK_KLEENE;
                case '?':
                    ultimoToken = TOK_OPT;
                    lexema = "?";
                    return TOK_OPT;
                case '(':
                    ultimoToken = TOK_LPAREN;
                    lexema = "(";
                    return TOK_LPAREN;
                case ')':
                    ultimoToken = TOK_RPAREN;
                    lexema = ")";
                    return TOK_RPAREN;
                case '[':
                    ultimoToken = TOK_LBRACK;
                    lexema = "[";
                    return TOK_LBRACK;
                case ']':
                    ultimoToken = TOK_RBRACK;
                    lexema = "]";
                    return TOK_RBRACK;
                case '-':
                    ultimoToken = TOK_MINUS;
                    lexema = "-";
                    return TOK_MINUS;
                default:
                    // Cualquier otro carácter es un símbolo literal
                    ultimoToken = TOK_SYMBOL;
                    lexema = String.valueOf(c);
                    return TOK_SYMBOL;
            }
        }

        void UndoToken() {
            if (ultimoToken != -1) {
                pilaTokens.push(ultimoToken);
                // No modificamos lexema; se actualizará en la próxima lectura real
                ultimoToken = -1;
            }
        }

        String getLexema() {
            return lexema;
        }

        // Determina si debe insertarse un token de concatenación implícita antes de 'c'
        private boolean debeInsertarConcat(char actual, int tokPrevio) {
            if (tokPrevio == -1) return false;

            boolean previoPuedeTerminar =
                    tokPrevio == TOK_SYMBOL ||
                    tokPrevio == TOK_RPAREN ||
                    tokPrevio == TOK_RBRACK ||
                    tokPrevio == TOK_POS ||
                    tokPrevio == TOK_KLEENE ||
                    tokPrevio == TOK_OPT;

            boolean actualPuedeIniciar = !(actual == '|' || actual == ')' || actual == '*' || actual == '+' || actual == '?' );

            return previoPuedeTerminar && actualPuedeIniciar;
        }
    }
}
