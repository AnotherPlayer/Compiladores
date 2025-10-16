package BackEnd;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

final class RegexAFNBuilder {

    private RegexAFNBuilder() {
    }

    private static final char CONCAT = '.';
    private static final char UNION = '|';
    private static final char KLEENE = '*';
    private static final char PLUS = '+';
    private static final char OPTIONAL = '?';

    private enum TokenType { OPERAND, OPERATOR, LPAREN, RPAREN }

    private enum UnitType { EPSILON, CHAR_CLASS }

    private static final class Token {
        final TokenType type;
        final char operator;
        final RegexUnit unit;

        Token(TokenType type, char operator, RegexUnit unit) {
            this.type = type;
            this.operator = operator;
            this.unit = unit;
        }

        static Token operand(RegexUnit unit) {
            return new Token(TokenType.OPERAND, '\0', unit);
        }

        static Token operator(char op) {
            return new Token(TokenType.OPERATOR, op, null);
        }

        static Token lparen() {
            return new Token(TokenType.LPAREN, '(', null);
        }

        static Token rparen() {
            return new Token(TokenType.RPAREN, ')', null);
        }
    }

    private static final class RegexUnit {
        final UnitType type;
        final List<CharRange> ranges;

        RegexUnit(UnitType type, List<CharRange> ranges) {
            this.type = type;
            this.ranges = ranges;
        }
    }

    private static final class CharRange {
        final char start;
        final char end;

        CharRange(char start, char end) {
            this.start = start;
            this.end = end;
        }
    }

    public static AFN fromRegex(String regex) {
        if (regex == null || regex.isEmpty()) {
            throw new IllegalArgumentException("La expresión regular no puede estar vacía.");
        }
        List<Token> tokens = tokenize(regex);
        List<Object> postfix = toPostfix(tokens);
        return evaluate(postfix);
    }

    private static List<Token> tokenize(String regex) {
        List<Token> tokens = new ArrayList<Token>();
        boolean expectConcat = false;
        for (int i = 0; i < regex.length(); ) {
            char c = regex.charAt(i);

            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            if (c == '\\') {
                if (i + 1 >= regex.length()) {
                    throw new IllegalArgumentException("Secuencia de escape incompleta en la expresión regular.");
                }
                char escaped = translateEscape(regex.charAt(i + 1));
                RegexUnit unit = unitForLiteral(escaped);
                if (expectConcat) {
                    tokens.add(Token.operator(CONCAT));
                }
                tokens.add(Token.operand(unit));
                expectConcat = true;
                i += 2;
                continue;
            }

            if (c == '(') {
                if (expectConcat) {
                    tokens.add(Token.operator(CONCAT));
                }
                tokens.add(Token.lparen());
                expectConcat = false;
                i++;
                continue;
            }

            if (c == ')') {
                tokens.add(Token.rparen());
                expectConcat = true;
                i++;
                continue;
            }

            if (c == '[') {
                RegexUnit unit = parseCharClass(regex, i);
                if (expectConcat) {
                    tokens.add(Token.operator(CONCAT));
                }
                tokens.add(Token.operand(unit));
                expectConcat = true;
                i = skipCharClass(regex, i);
                continue;
            }

            if (c == UNION) {
                tokens.add(Token.operator(UNION));
                expectConcat = false;
                i++;
                continue;
            }

            if (c == KLEENE || c == PLUS || c == OPTIONAL) {
                if (!expectConcat) {
                    throw new IllegalArgumentException("El operador '" + c + "' no puede colocarse aquí.");
                }
                tokens.add(Token.operator(c));
                expectConcat = true;
                i++;
                continue;
            }

            if (c == '.') {
                RegexUnit unit = unitForDot();
                if (expectConcat) {
                    tokens.add(Token.operator(CONCAT));
                }
                tokens.add(Token.operand(unit));
                expectConcat = true;
                i++;
                continue;
            }

            if (c == 'ε' || c == 'Ɛ') {
                RegexUnit unit = new RegexUnit(UnitType.EPSILON, null);
                if (expectConcat) {
                    tokens.add(Token.operator(CONCAT));
                }
                tokens.add(Token.operand(unit));
                expectConcat = true;
                i++;
                continue;
            }

            RegexUnit unit = unitForLiteral(c);
            if (expectConcat) {
                tokens.add(Token.operator(CONCAT));
            }
            tokens.add(Token.operand(unit));
            expectConcat = true;
            i++;
        }
        return tokens;
    }

    private static RegexUnit parseCharClass(String regex, int startIndex) {
        List<CharRange> ranges = new ArrayList<CharRange>();
        boolean first = true;
        char previousChar = 0;
        boolean hasPendingChar = false;

        int i = startIndex + 1;
        if (i >= regex.length()) {
            throw new IllegalArgumentException("Clase de caracteres sin cerrar en la expresión regular.");
        }

        while (i < regex.length()) {
            char c = regex.charAt(i);
            if (c == '\\') {
                if (i + 1 >= regex.length()) {
                    throw new IllegalArgumentException("Secuencia de escape incompleta en clase de caracteres.");
                }
                c = translateEscape(regex.charAt(i + 1));
                ranges.add(new CharRange(c, c));
                previousChar = c;
                hasPendingChar = true;
                first = false;
                i += 2;
                continue;
            }

            if (c == ']' && !first) {
                break;
            }

            if (c == '-' && hasPendingChar) {
                if (i + 1 >= regex.length()) {
                    throw new IllegalArgumentException("Rango incompleto en clase de caracteres.");
                }
                char endChar = regex.charAt(i + 1);
                if (endChar == '\\') {
                    if (i + 2 >= regex.length()) {
                        throw new IllegalArgumentException("Secuencia de escape incompleta en clase de caracteres.");
                    }
                    endChar = translateEscape(regex.charAt(i + 2));
                    i += 2;
                } else {
                    i += 1;
                }
                if (endChar < previousChar) {
                    throw new IllegalArgumentException("Rango inválido en clase de caracteres: " + previousChar + "-" + endChar);
                }
                if (!ranges.isEmpty()) {
                    CharRange last = ranges.get(ranges.size() - 1);
                    if (last.start == previousChar && last.end == previousChar) {
                        ranges.remove(ranges.size() - 1);
                    }
                }
                ranges.add(new CharRange(previousChar, endChar));
                hasPendingChar = false;
                i++;
                first = false;
                continue;
            }

            ranges.add(new CharRange(c, c));
            previousChar = c;
            hasPendingChar = true;
            first = false;
            i++;
        }

        if (i >= regex.length() || regex.charAt(i) != ']') {
            throw new IllegalArgumentException("Clase de caracteres sin cerrar en la expresión regular.");
        }

        if (ranges.isEmpty()) {
            throw new IllegalArgumentException("La clase de caracteres no puede estar vacía.");
        }

        return new RegexUnit(UnitType.CHAR_CLASS, ranges);
    }

    private static int skipCharClass(String regex, int startIndex) {
        int i = startIndex + 1;
        boolean first = true;
        while (i < regex.length()) {
            char c = regex.charAt(i);
            if (c == '\\') {
                i += 2;
                continue;
            }
            if (c == ']' && !first) {
                return i + 1;
            }
            first = false;
            i++;
        }
        throw new IllegalArgumentException("Clase de caracteres sin cerrar en la expresión regular.");
    }

    private static char translateEscape(char c) {
        switch (c) {
            case 'n': return '\n';
            case 't': return '\t';
            case 'r': return '\r';
            case '\\': return '\\';
            case '[': return '[';
            case ']': return ']';
            case '(': return '(';
            case ')': return ')';
            case '{': return '{';
            case '}': return '}';
            case '.': return '.';
            case '|': return '|';
            case '*': return '*';
            case '+': return '+';
            case '?': return '?';
            case '-': return '-';
            default:
                return c;
        }
    }

    private static RegexUnit unitForLiteral(char c) {
        List<CharRange> ranges = new ArrayList<CharRange>();
        ranges.add(new CharRange(c, c));
        return new RegexUnit(UnitType.CHAR_CLASS, ranges);
    }

    private static RegexUnit unitForDot() {
        List<CharRange> ranges = new ArrayList<CharRange>();
        ranges.add(new CharRange((char)0, (char)255));
        return new RegexUnit(UnitType.CHAR_CLASS, ranges);
    }

    private static List<Object> toPostfix(List<Token> tokens) {
        List<Object> output = new ArrayList<Object>();
        Deque<Character> operators = new ArrayDeque<Character>();

        for (Token token : tokens) {
            switch (token.type) {
                case OPERAND:
                    output.add(token.unit);
                    break;
                case OPERATOR:
                    char op = token.operator;
                    while (!operators.isEmpty()) {
                        char top = operators.peek();
                        if (top == '(') {
                            break;
                        }
                        if (hasHigherPrecedence(top, op)) {
                            output.add(operators.pop());
                            continue;
                        }
                        if (hasEqualPrecedence(top, op) && !isRightAssociative(op)) {
                            output.add(operators.pop());
                            continue;
                        }
                        break;
                    }
                    operators.push(op);
                    break;
                case LPAREN:
                    operators.push('(');
                    break;
                case RPAREN:
                    boolean matched = false;
                    while (!operators.isEmpty()) {
                        char top = operators.pop();
                        if (top == '(') {
                            matched = true;
                            break;
                        }
                        output.add(top);
                    }
                    if (!matched) {
                        throw new IllegalArgumentException("Paréntesis desbalanceados en la expresión regular.");
                    }
                    break;
            }
        }

        while (!operators.isEmpty()) {
            char top = operators.pop();
            if (top == '(' || top == ')') {
                throw new IllegalArgumentException("Paréntesis desbalanceados en la expresión regular.");
            }
            output.add(top);
        }

        return output;
    }

    private static boolean hasHigherPrecedence(char op1, char op2) {
        return precedence(op1) > precedence(op2);
    }

    private static boolean hasEqualPrecedence(char op1, char op2) {
        return precedence(op1) == precedence(op2);
    }

    private static int precedence(char op) {
        switch (op) {
            case UNION:
                return 1;
            case CONCAT:
                return 2;
            case KLEENE:
            case PLUS:
            case OPTIONAL:
                return 3;
            default:
                return 0;
        }
    }

    private static boolean isRightAssociative(char op) {
        return op == KLEENE || op == PLUS || op == OPTIONAL;
    }

    private static AFN evaluate(List<Object> postfix) {
        Deque<AFN> stack = new ArrayDeque<AFN>();
        for (Object element : postfix) {
            if (element instanceof RegexUnit) {
                stack.push(buildUnit((RegexUnit) element));
            } else if (element instanceof Character) {
                char op = (Character) element;
                switch (op) {
                    case UNION: {
                        AFN right = stack.pop();
                        AFN left = stack.pop();
                        stack.push(AFN.unir(left, right));
                        break;
                    }
                    case CONCAT: {
                        AFN right = stack.pop();
                        AFN left = stack.pop();
                        stack.push(AFN.concatenar(left, right));
                        break;
                    }
                    case KLEENE: {
                        AFN value = stack.pop();
                        stack.push(AFN.cerraduraKleene(value));
                        break;
                    }
                    case PLUS: {
                        AFN value = stack.pop();
                        stack.push(AFN.cerraduraPositiva(value));
                        break;
                    }
                    case OPTIONAL: {
                        AFN value = stack.pop();
                        stack.push(AFN.cerraduraOpcional(value));
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("Operador no soportado: " + op);
                }
            }
        }
        if (stack.size() != 1) {
            throw new IllegalStateException("Error al evaluar la expresión regular.");
        }
        return stack.pop();
    }

    private static AFN buildUnit(RegexUnit unit) {
        if (unit.type == UnitType.EPSILON) {
            return new AFN().doEpsilon();
        }
        AFN result = null;
        for (CharRange range : unit.ranges) {
            AFN fragment = new AFN();
            if (range.start == range.end) {
                fragment.doBasic(range.start);
            } else {
                fragment.doBasic(range.start, range.end);
            }
            if (result == null) {
                result = fragment;
            } else {
                result = AFN.unir(result, fragment);
            }
        }
        return result;
    }
}
