package BackEnd;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public class AnalizadorLexico {

    public static final int YYEOF = SimbEspeciales.FIN;
    public static final int YYERROR = SimbEspeciales.ERROR;
    public static final int TOKEN_IDENTIFIER = 257;
    public static final int TOKEN_INTEGER_LITERAL = 258;
    public static final int TOKEN_FLOAT_LITERAL = 259;
    public static final int TOKEN_STRING_LITERAL = 260;
    public static final int TOKEN_CHAR_LITERAL = 261;
    public static final int TOKEN_AND_AND = 270;
    public static final int TOKEN_OR_OR = 271;
    public static final int TOKEN_EQUAL_EQUAL = 272;
    public static final int TOKEN_NOT_EQUAL = 273;
    public static final int TOKEN_GREATER_EQUAL = 274;
    public static final int TOKEN_LESS_EQUAL = 275;
    public static final int TOKEN_INCREMENT = 276;
    public static final int TOKEN_DECREMENT = 277;
    public static final int TOKEN_ARROW = 278;

    public static final int TOKEN_IF = 300;
    public static final int TOKEN_ELSE = 301;
    public static final int TOKEN_WHILE = 302;
    public static final int TOKEN_FOR = 303;
    public static final int TOKEN_DO = 304;
    public static final int TOKEN_SWITCH = 305;
    public static final int TOKEN_CASE = 306;
    public static final int TOKEN_DEFAULT = 307;
    public static final int TOKEN_BREAK = 308;
    public static final int TOKEN_CONTINUE = 309;
    public static final int TOKEN_RETURN = 310;
    public static final int TOKEN_INT = 311;
    public static final int TOKEN_FLOAT = 312;
    public static final int TOKEN_DOUBLE = 313;
    public static final int TOKEN_CHAR = 314;
    public static final int TOKEN_VOID = 315;
    public static final int TOKEN_BOOL = 316;
    public static final int TOKEN_STRING = 317;
    public static final int TOKEN_CLASS = 318;
    public static final int TOKEN_STRUCT = 319;
    public static final int TOKEN_TRUE = 320;
    public static final int TOKEN_FALSE = 321;
    public static final int TOKEN_CONST = 322;

    private final PushbackReader reader;
    private final ArrayDeque<int[]> history;
    private final StringBuilder lexemeBuilder;
    private final int[] asciiTokens;
    private final Map<String,Integer> reservedWords;

    private String lastLexeme;
    private int lastToken;
    private boolean reachedEOF;
    private int line;
    private int column;

    public AnalizadorLexico(String input){
        this(new StringReader(input != null ? input : ""));
    }

    public AnalizadorLexico(Reader reader){
        this.reader = new PushbackReader(reader, 8);
        this.history = new ArrayDeque<int[]>();
        this.lexemeBuilder = new StringBuilder();
        this.asciiTokens = new int[256];
        this.reservedWords = new HashMap<String,Integer>();
        this.lastLexeme = "";
        this.lastToken = YYEOF;
        this.reachedEOF = false;
        this.line = 1;
        this.column = 0;
        initAsciiTokenTable();
        initReservedWords();
    }

    public String yytext(){
        return lastLexeme;
    }

    public int yylastToken(){
        return lastToken;
    }

    public int getLine(){
        return line;
    }

    public int getColumn(){
        return column;
    }

    public void close() throws IOException{
        reader.close();
    }

    public int yylex(){
        if(reachedEOF){
            lastLexeme = "";
            lastToken = YYEOF;
            return YYEOF;
        }

        lexemeBuilder.setLength(0);

        try{
            int ch;
            while(true){
                ch = readChar();
                if(ch == -1){
                    reachedEOF = true;
                    lastLexeme = "";
                    lastToken = YYEOF;
                    return YYEOF;
                }
                if(Character.isWhitespace(ch)){
                    commit();
                    continue;
                }
                if(ch == '/'){
                    int next = readChar();
                    if(next == '/'){
                        commit(); // consume '/'
                        commit(); // consume second '/'
                        consumeLineComment();
                        continue;
                    }
                    if(next == '*'){
                        commit(); // consume '/'
                        commit(); // consume '*'
                        if(!consumeBlockComment()){
                            lastLexeme = "/*";
                            lastToken = YYERROR;
                            return YYERROR;
                        }
                        continue;
                    }
                    unreadChar(next);
                    unreadChar(ch);
                    return scanSingleOrAsciiToken();
                }
                if(ch == '#'){
                    commit();
                    consumeLineComment();
                    continue;
                }
                break;
            }

            if(Character.isLetter(ch) || ch == '_'){
                unreadChar(ch);
                return scanIdentifierOrReserved();
            }

            if(Character.isDigit(ch)){
                unreadChar(ch);
                return scanNumberLiteral();
            }

            switch(ch){
                case '"':
                    unreadChar(ch);
                    return scanStringLiteral();
                case '\'':
                    unreadChar(ch);
                    return scanCharLiteral();
                case '+':
                    unreadChar(ch);
                    return scanPlus();
                case '-':
                    unreadChar(ch);
                    return scanMinus();
                case '&':
                    unreadChar(ch);
                    return scanAmpersand();
                case '|':
                    unreadChar(ch);
                    return scanPipe();
                case '=':
                    unreadChar(ch);
                    return scanEquals();
                case '!':
                    unreadChar(ch);
                    return scanExclamation();
                case '>':
                    unreadChar(ch);
                    return scanGreater();
                case '<':
                    unreadChar(ch);
                    return scanLess();
            }

            unreadChar(ch);
            return scanSingleOrAsciiToken();

        }catch(IOException e){
            lastLexeme = "";
            lastToken = YYERROR;
            return YYERROR;
        }
    }

    private void initAsciiTokenTable(){
        for(int i=0;i<asciiTokens.length;i++){
            asciiTokens[i] = SimbEspeciales.ERROR;
        }
        for(int i=32;i<=126;i++){
            asciiTokens[i] = i;
        }
        setAsciiToken('\n', SimbEspeciales.OMITIR);
        setAsciiToken('\r', SimbEspeciales.OMITIR);
        setAsciiToken('\t', SimbEspeciales.OMITIR);
        setAsciiToken(' ', SimbEspeciales.OMITIR);
    }

    private void initReservedWords(){
        reservedWords.put("if", TOKEN_IF);
        reservedWords.put("else", TOKEN_ELSE);
        reservedWords.put("while", TOKEN_WHILE);
        reservedWords.put("for", TOKEN_FOR);
        reservedWords.put("do", TOKEN_DO);
        reservedWords.put("switch", TOKEN_SWITCH);
        reservedWords.put("case", TOKEN_CASE);
        reservedWords.put("default", TOKEN_DEFAULT);
        reservedWords.put("break", TOKEN_BREAK);
        reservedWords.put("continue", TOKEN_CONTINUE);
        reservedWords.put("return", TOKEN_RETURN);
        reservedWords.put("int", TOKEN_INT);
        reservedWords.put("float", TOKEN_FLOAT);
        reservedWords.put("double", TOKEN_DOUBLE);
        reservedWords.put("char", TOKEN_CHAR);
        reservedWords.put("void", TOKEN_VOID);
        reservedWords.put("bool", TOKEN_BOOL);
        reservedWords.put("boolean", TOKEN_BOOL);
        reservedWords.put("string", TOKEN_STRING);
        reservedWords.put("class", TOKEN_CLASS);
        reservedWords.put("struct", TOKEN_STRUCT);
        reservedWords.put("true", TOKEN_TRUE);
        reservedWords.put("false", TOKEN_FALSE);
        reservedWords.put("const", TOKEN_CONST);
    }

    private void setAsciiToken(char symbol,int token){
        asciiTokens[symbol & 0xFF] = token;
    }

    private int scanIdentifierOrReserved() throws IOException{
        lexemeBuilder.setLength(0);
        int ch;
        while((ch = readChar()) != -1){
            if(Character.isLetterOrDigit(ch) || ch == '_'){
                lexemeBuilder.append((char)ch);
                commit();
            }else{
                unreadChar(ch);
                break;
            }
        }
        lastLexeme = lexemeBuilder.toString();
        Integer token = reservedWords.get(lastLexeme);
        if(token != null){
            lastToken = token;
            return token;
        }
        lastToken = TOKEN_IDENTIFIER;
        return TOKEN_IDENTIFIER;
    }

    private int scanNumberLiteral() throws IOException{
        lexemeBuilder.setLength(0);
        boolean hasDot = false;
        boolean hasExponent = false;
        int ch;
        while((ch = readChar()) != -1){
            if(Character.isDigit(ch)){
                lexemeBuilder.append((char)ch);
                commit();
                continue;
            }
            if(ch == '.' && !hasDot){
                hasDot = true;
                lexemeBuilder.append((char)ch);
                commit();
                continue;
            }
            if((ch == 'e' || ch == 'E') && !hasExponent){
                hasExponent = true;
                lexemeBuilder.append((char)ch);
                commit();
                int next = readChar();
                if(next == '+' || next == '-'){
                    lexemeBuilder.append((char)next);
                    commit();
                }else{
                    unreadChar(next);
                }
                continue;
            }
            unreadChar(ch);
            break;
        }
        lastLexeme = lexemeBuilder.toString();
        if(hasDot || hasExponent){
            lastToken = TOKEN_FLOAT_LITERAL;
            return TOKEN_FLOAT_LITERAL;
        }
        lastToken = TOKEN_INTEGER_LITERAL;
        return TOKEN_INTEGER_LITERAL;
    }

    private int scanStringLiteral() throws IOException{
        lexemeBuilder.setLength(0);
        int ch = readChar(); // opening quote
        lexemeBuilder.append((char)ch);
        commit();
        boolean closed = false;
        while((ch = readChar()) != -1){
            lexemeBuilder.append((char)ch);
            commit();
            if(ch == '\\'){
                int escaped = readChar();
                if(escaped == -1){
                    lastLexeme = lexemeBuilder.toString();
                    lastToken = YYERROR;
                    return YYERROR;
                }
                lexemeBuilder.append((char)escaped);
                commit();
                continue;
            }
            if(ch == '"'){
                closed = true;
                break;
            }
            if(ch == '\n'){
                break;
            }
        }
        lastLexeme = lexemeBuilder.toString();
        if(!closed){
            lastToken = YYERROR;
            return YYERROR;
        }
        lastToken = TOKEN_STRING_LITERAL;
        return TOKEN_STRING_LITERAL;
    }

    private int scanCharLiteral() throws IOException{
        lexemeBuilder.setLength(0);
        int ch = readChar(); // opening '
        lexemeBuilder.append((char)ch);
        commit();
        int content = readChar();
        if(content == -1 || content == '\n'){
            lastLexeme = lexemeBuilder.toString();
            lastToken = YYERROR;
            return YYERROR;
        }
        lexemeBuilder.append((char)content);
        commit();
        if(content == '\\'){
            int escaped = readChar();
            if(escaped == -1 || escaped == '\n'){
                lastLexeme = lexemeBuilder.toString();
                lastToken = YYERROR;
                return YYERROR;
            }
            lexemeBuilder.append((char)escaped);
            commit();
            int closing = readChar();
            if(closing != '\''){
                if(closing != -1){
                    unreadChar(closing);
                }
                lastLexeme = lexemeBuilder.toString();
                lastToken = YYERROR;
                return YYERROR;
            }
            lexemeBuilder.append((char)closing);
            commit();
        }else{
            int closing = readChar();
            if(closing != '\''){
                if(closing != -1){
                    unreadChar(closing);
                }
                lastLexeme = lexemeBuilder.toString();
                lastToken = YYERROR;
                return YYERROR;
            }
            lexemeBuilder.append((char)closing);
            commit();
        }
        lastLexeme = lexemeBuilder.toString();
        lastToken = TOKEN_CHAR_LITERAL;
        return TOKEN_CHAR_LITERAL;
    }

    private int scanPlus() throws IOException{
        lexemeBuilder.setLength(0);
        int ch = readChar();
        lexemeBuilder.append((char)ch);
        commit();
        int next = readChar();
        if(next == '+'){
            lexemeBuilder.append((char)next);
            commit();
            lastLexeme = lexemeBuilder.toString();
            lastToken = TOKEN_INCREMENT;
            return TOKEN_INCREMENT;
        }
        unreadChar(next);
        lastLexeme = lexemeBuilder.toString();
        lastToken = asciiToken(ch);
        return lastToken;
    }

    private int scanMinus() throws IOException{
        lexemeBuilder.setLength(0);
        int ch = readChar();
        lexemeBuilder.append((char)ch);
        commit();
        int next = readChar();
        if(next == '-'){
            lexemeBuilder.append((char)next);
            commit();
            lastLexeme = lexemeBuilder.toString();
            lastToken = TOKEN_DECREMENT;
            return TOKEN_DECREMENT;
        }
        if(next == '>'){
            lexemeBuilder.append((char)next);
            commit();
            lastLexeme = lexemeBuilder.toString();
            lastToken = TOKEN_ARROW;
            return TOKEN_ARROW;
        }
        unreadChar(next);
        lastLexeme = lexemeBuilder.toString();
        lastToken = asciiToken(ch);
        return lastToken;
    }

    private int scanAmpersand() throws IOException{
        lexemeBuilder.setLength(0);
        int ch = readChar();
        lexemeBuilder.append((char)ch);
        commit();
        int next = readChar();
        if(next == '&'){
            lexemeBuilder.append((char)next);
            commit();
            lastLexeme = lexemeBuilder.toString();
            lastToken = TOKEN_AND_AND;
            return TOKEN_AND_AND;
        }
        unreadChar(next);
        lastLexeme = lexemeBuilder.toString();
        lastToken = asciiToken(ch);
        return lastToken;
    }

    private int scanPipe() throws IOException{
        lexemeBuilder.setLength(0);
        int ch = readChar();
        lexemeBuilder.append((char)ch);
        commit();
        int next = readChar();
        if(next == '|'){
            lexemeBuilder.append((char)next);
            commit();
            lastLexeme = lexemeBuilder.toString();
            lastToken = TOKEN_OR_OR;
            return TOKEN_OR_OR;
        }
        unreadChar(next);
        lastLexeme = lexemeBuilder.toString();
        lastToken = asciiToken(ch);
        return lastToken;
    }

    private int scanEquals() throws IOException{
        lexemeBuilder.setLength(0);
        int ch = readChar();
        lexemeBuilder.append((char)ch);
        commit();
        int next = readChar();
        if(next == '='){
            lexemeBuilder.append((char)next);
            commit();
            lastLexeme = lexemeBuilder.toString();
            lastToken = TOKEN_EQUAL_EQUAL;
            return TOKEN_EQUAL_EQUAL;
        }
        unreadChar(next);
        lastLexeme = lexemeBuilder.toString();
        lastToken = asciiToken(ch);
        return lastToken;
    }

    private int scanExclamation() throws IOException{
        lexemeBuilder.setLength(0);
        int ch = readChar();
        lexemeBuilder.append((char)ch);
        commit();
        int next = readChar();
        if(next == '='){
            lexemeBuilder.append((char)next);
            commit();
            lastLexeme = lexemeBuilder.toString();
            lastToken = TOKEN_NOT_EQUAL;
            return TOKEN_NOT_EQUAL;
        }
        unreadChar(next);
        lastLexeme = lexemeBuilder.toString();
        lastToken = asciiToken(ch);
        return lastToken;
    }

    private int scanGreater() throws IOException{
        lexemeBuilder.setLength(0);
        int ch = readChar();
        lexemeBuilder.append((char)ch);
        commit();
        int next = readChar();
        if(next == '='){
            lexemeBuilder.append((char)next);
            commit();
            lastLexeme = lexemeBuilder.toString();
            lastToken = TOKEN_GREATER_EQUAL;
            return TOKEN_GREATER_EQUAL;
        }
        unreadChar(next);
        lastLexeme = lexemeBuilder.toString();
        lastToken = asciiToken(ch);
        return lastToken;
    }

    private int scanLess() throws IOException{
        lexemeBuilder.setLength(0);
        int ch = readChar();
        lexemeBuilder.append((char)ch);
        commit();
        int next = readChar();
        if(next == '='){
            lexemeBuilder.append((char)next);
            commit();
            lastLexeme = lexemeBuilder.toString();
            lastToken = TOKEN_LESS_EQUAL;
            return TOKEN_LESS_EQUAL;
        }
        unreadChar(next);
        lastLexeme = lexemeBuilder.toString();
        lastToken = asciiToken(ch);
        return lastToken;
    }

    private int scanSingleOrAsciiToken() throws IOException{
        lexemeBuilder.setLength(0);
        int ch = readChar();
        if(ch == -1){
            reachedEOF = true;
            lastLexeme = "";
            lastToken = YYEOF;
            return YYEOF;
        }
        lexemeBuilder.append((char)ch);
        commit();
        lastLexeme = lexemeBuilder.toString();
        lastToken = asciiToken(ch);
        return lastToken;
    }

    private void consumeLineComment() throws IOException{
        int ch;
        while((ch = readChar()) != -1){
            commit();
            if(ch == '\n'){
                break;
            }
        }
    }

    private boolean consumeBlockComment() throws IOException{
        int prev = 0;
        int ch;
        while((ch = readChar()) != -1){
            commit();
            if(prev == '*' && ch == '/'){
                return true;
            }
            prev = ch;
        }
        return false;
    }

    private int readChar() throws IOException{
        int ch = reader.read();
        history.push(new int[]{line,column});
        if(ch == '\n'){
            line++;
            column = 0;
        }else if(ch != -1){
            column++;
        }
        return ch;
    }

    private void unreadChar(int ch) throws IOException{
        if(history.isEmpty()){
            return;
        }
        int[] pos = history.pop();
        line = pos[0];
        column = pos[1];
        if(ch != -1){
            reader.unread(ch);
        }
    }

    private void commit(){
        if(!history.isEmpty()){
            history.pop();
        }
    }

    private int asciiToken(int ch){
        if(ch < 0){
            return YYERROR;
        }
        int token = asciiTokens[ch & 0xFF];
        if(token == SimbEspeciales.ERROR){
            return ch;
        }
        return token;
    }

    public int tokenForAscii(char c){
        return asciiTokens[c & 0xFF];
    }

    public int[] copiaTablaAscii(){
        int[] copia = new int[asciiTokens.length];
        System.arraycopy(asciiTokens, 0, copia, 0, asciiTokens.length);
        return copia;
    }

    public Map<String,Integer> palabrasReservadas(){
        return new HashMap<String,Integer>(reservedWords);
    }

}
