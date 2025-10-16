package BackEnd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LexicalAnalyzer {

    private final List<Definition> definitions;
    private final Map<Integer, Definition> tokenMap;
    private final AFD afd;

    private LexicalAnalyzer(List<Definition> definitions, AFD afd, Map<Integer, Definition> tokenMap) {
        this.definitions = definitions;
        this.afd = afd;
        this.tokenMap = tokenMap;
    }

    public static LexicalAnalyzer build(List<Definition> definitions) {
        if (definitions == null || definitions.isEmpty()) {
            throw new IllegalArgumentException("Se requiere al menos una clase léxica.");
        }

        AFN combinado = null;
        for (Definition def : definitions) {
            AFN parcial = AFN.fromRegex(def.regex);
            parcial.asignarToken(def.token);
            if (combinado == null) {
                combinado = parcial;
            } else {
                combinado = AFN.unir(combinado, parcial);
            }
        }

        AFD afd = combinado.toAFD();
        Map<Integer, Definition> tokenMap = new HashMap<Integer, Definition>();
        for (Definition def : definitions) {
            tokenMap.put(def.token, def);
        }

        return new LexicalAnalyzer(new ArrayList<Definition>(definitions), afd, tokenMap);
    }

    public List<TokenResult> analyze(String input) throws LexicalException {
        List<TokenResult> resultados = new ArrayList<TokenResult>();
        if (input == null) {
            input = "";
        }
        int indice = 0;
        while (indice < input.length()) {
            ResultadoAvance avance = avanzar(input, indice);
            if (!avance.encontroToken) {
                char ch = input.charAt(indice);
                throw new LexicalException("Símbolo no reconocido en la posición " + indice + ": '" + mostrarCaracter(ch) + "'");
            }
            Definition def = tokenMap.get(avance.token);
            if (def == null) {
                throw new LexicalException("Token " + avance.token + " sin definición asociada.");
            }
            String lexema = input.substring(indice, avance.fin + 1);
            resultados.add(new TokenResult(def, lexema, avance.token, false));
            indice = avance.fin + 1;
        }
        resultados.add(new TokenResult(null, "", SimbEspeciales.FIN, true));
        return resultados;
    }

    private ResultadoAvance avanzar(String input, int inicio) {
        int estado = 0;
        int mejorFin = -1;
        int mejorToken = -1;

        for (int i = inicio; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c > 255) {
                estado = -1;
            } else {
                EdoAFD edo = afd.getEstado(estado);
                estado = edo.TransAFD[c & 0xFF];
            }

            if (estado < 0) {
                break;
            }

            EdoAFD edoActual = afd.getEstado(estado);
            if (edoActual.esAceptacion) {
                mejorFin = i;
                mejorToken = edoActual.token;
            }
        }

        if (mejorFin >= inicio) {
            return new ResultadoAvance(true, mejorFin, mejorToken);
        }
        return new ResultadoAvance(false, -1, -1);
    }

    private String mostrarCaracter(char c) {
        if (c == '\n') {
            return "\\n";
        }
        if (c == '\t') {
            return "\\t";
        }
        if (c == '\r') {
            return "\\r";
        }
        if (Character.isISOControl(c)) {
            return String.valueOf((int) c);
        }
        return Character.toString(c);
    }

    public AFD getAfd() {
        return afd;
    }

    public List<Definition> getDefinitions() {
        return Collections.unmodifiableList(definitions);
    }

    public static final class Definition {
        public final String name;
        public final String regex;
        public final int token;

        public Definition(String name, String regex, int token) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre de la clase léxica no puede estar vacío.");
            }
            if (regex == null || regex.trim().isEmpty()) {
                throw new IllegalArgumentException("La expresión regular no puede estar vacía.");
            }
            this.name = name.trim();
            this.regex = regex.trim();
            this.token = token;
        }
    }

    public static final class TokenResult {
        public final Definition definition;
        public final String lexeme;
        public final int token;
        public final boolean finDeCadena;

        TokenResult(Definition definition, String lexeme, int token, boolean finDeCadena) {
            this.definition = definition;
            this.lexeme = lexeme;
            this.token = token;
            this.finDeCadena = finDeCadena;
        }
    }

    private static final class ResultadoAvance {
        final boolean encontroToken;
        final int fin;
        final int token;

        ResultadoAvance(boolean encontroToken, int fin, int token) {
            this.encontroToken = encontroToken;
            this.fin = fin;
            this.token = token;
        }
    }

    public static final class LexicalException extends Exception {
        public LexicalException(String message) {
            super(message);
        }
    }
}
