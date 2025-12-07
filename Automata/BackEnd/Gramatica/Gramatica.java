package BackEnd;

//2° parcial
//
// Implementación alineada al pseudocódigo de los apuntes:
//  - Lectura de gramática (gramática de gramáticas simplificada)
//  - Cálculo de First y Follow
//  - Soporte para construcción de tabla LL(1) (ver clase LL1)

import java.util.*;

public class Gramatica {

    public static final String EPS = "ε";
    public static final String FIN_CADENA = "$";

    public ArrayList<LadoIzq> Reglas;      // P
    public ArrayList<SimbolG> Vn;          // No terminales
    public ArrayList<SimbolG> Vt;          // Terminales
    public SimbolG SimbIni;                // Símbolo inicial

    // Mapas de First y Follow precalculados
    private Map<String, Set<String>> firstMemo;
    private Map<String, Set<String>> followMemo;

    public Gramatica() {
        this.Reglas = new ArrayList<>();
        this.Vn = new ArrayList<>();
        this.Vt = new ArrayList<>();
        this.SimbIni = null;
        this.firstMemo = new HashMap<>();
        this.followMemo = new HashMap<>();
    }

    /**
     * Parser por descenso recursivo de la “gramática de gramáticas” de los apuntes.
     * Formato esperado (caso general):
     *   G  -> Regla (';' Regla)*
     *   Regla -> LadoIzq '->' LadosDerecho
     *   LadosDerecho -> Sec ( '|' Sec )*
     *   Sec -> (SIMBOLO)*
     *
     * Donde:
     *   - SIMBOLO es cualquier lexema sin espacios ni separadores (;,|,->)
     *   - ε se representa como "ε" o "epsilon" en un Sec vacío o explícito.
     *
     * Se crea una producción por cada alternativa en LadosDerecho.
     */
    public static Gramatica parseDescRec(String texto) {
        GrammarLexer lx = new GrammarLexer(texto);
        Gramatica g = new Gramatica();

        // G -> Regla (';' Regla)*
        do {
            LadoIzq regla = parseRegla(lx, g);
            g.Reglas.add(regla);
        } while (lx.accept(TokenType.SEMICOLON));

        if (lx.current.type != TokenType.EOF) {
            throw new IllegalArgumentException("Entrada no consumida en gramática: " + lx.current.lexema);
        }

        if (!g.Vn.isEmpty()) {
            g.SimbIni = g.Vn.get(0);
        }
        return g;
    }

    // Regla -> LadoIzq '->' LadosDerecho
    private static LadoIzq parseRegla(GrammarLexer lx, Gramatica g) {
        if (lx.current.type != TokenType.SYMBOL) {
            throw new IllegalArgumentException("Se esperaba un símbolo para el lado izquierdo y se encontró: " + lx.current.lexema);
        }
        SimbolG izq = g.obtenerONuevoNoTerminal(lx.current.lexema);
        lx.next(); // consumir lado izquierdo

        if (!lx.accept(TokenType.ARROW)) {
            throw new IllegalArgumentException("Se esperaba '->' después de " + izq.NombSimb);
        }

        // LadosDerecho -> Sec ('|' Sec)*
        ArrayList<ArrayList<SimbolG>> alternativas = new ArrayList<>();
        alternativas.add(parseSec(lx, g));
        while (lx.accept(TokenType.OR)) {
            alternativas.add(parseSec(lx, g));
        }

        // Crear una producción por alternativa
        // (en nuestro modelo, cada LadoIzq contiene una sola alternativa)
        if (alternativas.size() == 1) {
            LadoIzq prod = new LadoIzq(izq);
            prod.LadoDerecho.addAll(alternativas.get(0));
            return prod;
        } else {
            // Para múltiples alternativas agregamos la primera aquí y el resto como nuevas reglas
            // aprovechando que parseDescRec las agregará en el mismo orden.
            LadoIzq prod = new LadoIzq(izq);
            prod.LadoDerecho.addAll(alternativas.get(0));
            for (int i = 1; i < alternativas.size(); i++) {
                LadoIzq alt = new LadoIzq(izq);
                alt.LadoDerecho.addAll(alternativas.get(i));
                g.Reglas.add(alt);
            }
            return prod;
        }
    }

    // Sec -> (SIMBOLO)*  (vacío = epsilon)
    private static ArrayList<SimbolG> parseSec(GrammarLexer lx, Gramatica g) {
        ArrayList<SimbolG> sec = new ArrayList<>();

        // Sec vacío -> epsilon
        if (lx.current.type != TokenType.SYMBOL) {
            sec.add(g.obtenerONuevoTerminal(EPS, -1));
            return sec;
        }

        // Consumir símbolos hasta que llegue un separador
        while (lx.current.type == TokenType.SYMBOL) {
            String lex = lx.current.lexema;
            lx.next();

            if (lex.equals(EPS) || lex.equalsIgnoreCase("epsilon")) {
                sec.add(g.obtenerONuevoTerminal(EPS, -1));
                continue;
            }

            // Si ya existe como no terminal, se usa ese; en otro caso, es terminal nuevo
            SimbolG nt = g.buscaSimboloNoTerminal(lex);
            if (nt != null) {
                sec.add(nt);
            } else {
                sec.add(g.obtenerONuevoTerminal(lex, -1));
            }
        }

        // Secuencia vacía significa epsilon
        if (sec.isEmpty()) {
            sec.add(g.obtenerONuevoTerminal(EPS, -1));
        }
        return sec;
    }

    /**
     * Parser sencillo de gramática a partir de texto (una regla por línea, formato A->α|β).
     * Este parser se apega a la “gramática de gramáticas” descrita en los apuntes:
     * G -> Reglas ; Regla -> LadoIzq FLECHA LadosDerecho ; LadosDerecho -> SecSimbolos (OR SecSimbolos)*
     */
    public static Gramatica desdeTexto(String texto) {
        Gramatica g = new Gramatica();
        String[] lineas = texto.split("\\r?\\n");
        for (String l : lineas) {
            l = l.trim();
            if (l.isEmpty()) continue;
            String[] partes = l.split("->");
            if (partes.length != 2) continue; // línea mal formada, se ignora
            String lhs = partes[0].trim();
            String rhs = partes[1].trim();

            SimbolG izq = g.obtenerONuevoNoTerminal(lhs);
            String[] alts = rhs.split("\\|");
            for (String alt : alts) {
                String[] tokens = alt.trim().split("\\s+");
                ArrayList<SimbolG> ladoDer = new ArrayList<>();
                for (String tok : tokens) {
                    if (tok.isEmpty()) continue;
                    if (tok.equals(EPS)) {
                        ladoDer.add(g.obtenerONuevoTerminal(EPS, -1)); // ε se trata como terminal especial
                    } else if (Character.isUpperCase(tok.charAt(0))) { // convención: mayúscula = no terminal
                        ladoDer.add(g.obtenerONuevoNoTerminal(tok));
                    } else { // minúscula o símbolo = terminal
                        ladoDer.add(g.obtenerONuevoTerminal(tok, -1));
                    }
                }
                LadoIzq regla = new LadoIzq(izq);
                for (SimbolG s : ladoDer) {
                    regla.AgregarSimbolo(s);
                }
                g.Reglas.add(regla);
            }
        }
        if (!g.Vn.isEmpty()) {
            g.SimbIni = g.Vn.get(0);
        }
        return g;
    }

    private SimbolG obtenerONuevoNoTerminal(String nombre) {
        for (SimbolG s : Vn) {
            if (s.NombSimb.equals(nombre)) return s;
        }
        SimbolG nuevo = new SimbolG(nombre, -1, false);
        Vn.add(nuevo);
        return nuevo;
    }

    private SimbolG buscaSimboloNoTerminal(String nombre) {
        for (SimbolG s : Vn) {
            if (s.NombSimb.equals(nombre)) return s;
        }
        return null;
    }

    private SimbolG obtenerONuevoTerminal(String nombre, int token) {
        for (SimbolG s : Vt) {
            if (s.NombSimb.equals(nombre)) return s;
        }
        SimbolG nuevo = new SimbolG(nombre, token, true);
        Vt.add(nuevo);
        return nuevo;
    }

    /**
     * FIRST(A) para no terminal A, según pseudocódigo de los apuntes.
     */
    public Set<String> first(String nombre) {
        if (firstMemo.containsKey(nombre)) return firstMemo.get(nombre);
        Set<String> R = new LinkedHashSet<>();
        SimbolG simbolo = buscaSimbolo(nombre);
        if (simbolo == null) return R;
        // Si es terminal, FIRST = { a }
        if (simbolo.esTerminal) {
            R.add(simbolo.NombSimb);
            firstMemo.put(nombre, R);
            return R;
        }
        // Para cada regla A -> α
        for (LadoIzq r : Reglas) {
            if (!r.SimbIzq.NombSimb.equals(nombre)) continue;
            if (r.LadoDerecho.isEmpty()) continue;
            boolean todosEpsilon = true;
            for (SimbolG s : r.LadoDerecho) {
                Set<String> firstS = first(s.NombSimb);
                R.addAll(sinEpsilon(firstS));
                if (!firstS.contains(EPS)) {
                    todosEpsilon = false;
                    break;
                }
            }
            if (todosEpsilon) R.add(EPS);
        }
        firstMemo.put(nombre, R);
        return R;
    }

    /**
     * FIRST(α) donde α es una secuencia de símbolos (lado derecho).
     */
    public Set<String> firstSecuencia(List<SimbolG> sec) {
        Set<String> R = new LinkedHashSet<>();
        if (sec.isEmpty()) {
            R.add(EPS);
            return R;
        }
        boolean todosEpsilon = true;
        for (SimbolG s : sec) {
            Set<String> fs = first(s.NombSimb);
            R.addAll(sinEpsilon(fs));
            if (!fs.contains(EPS)) {
                todosEpsilon = false;
                break;
            }
        }
        if (todosEpsilon) R.add(EPS);
        return R;
    }

    /**
     * FOLLOW(A) para no terminal A, según reglas de los apuntes.
     */
    public Set<String> follow(String nombre) {
        if (followMemo.containsKey(nombre)) return followMemo.get(nombre);
        Set<String> R = new LinkedHashSet<>();
        SimbolG A = buscaSimbolo(nombre);
        if (A == null || A.esTerminal) return R;
        if (SimbIni != null && SimbIni.NombSimb.equals(nombre)) {
            R.add(FIN_CADENA);
        }
        // Recorremos todas las reglas
        for (LadoIzq r : Reglas) {
            List<SimbolG> beta = r.LadoDerecho;
            for (int i = 0; i < beta.size(); i++) {
                SimbolG s = beta.get(i);
                if (!s.NombSimb.equals(nombre)) continue;
                // Caso A -> α B β
                List<SimbolG> betaRest = beta.subList(i + 1, beta.size());
                Set<String> firstBeta = firstSecuencia(betaRest);
                R.addAll(sinEpsilon(firstBeta));
                if (firstBeta.contains(EPS) || betaRest.isEmpty()) {
                    // Agregar FOLLOW(A) del lado izquierdo
                    if (!r.SimbIzq.NombSimb.equals(nombre)) {
                        R.addAll(follow(r.SimbIzq.NombSimb));
                    }
                }
            }
        }
        followMemo.put(nombre, R);
        return R;
    }

    private SimbolG buscaSimbolo(String nombre) {
        for (SimbolG s : Vn) if (s.NombSimb.equals(nombre)) return s;
        for (SimbolG s : Vt) if (s.NombSimb.equals(nombre)) return s;
        return null;
    }

    private Set<String> sinEpsilon(Set<String> conjunto) {
        Set<String> r = new LinkedHashSet<>(conjunto);
        r.remove(EPS);
        return r;
    }

    public void limpiarMemorias() {
        firstMemo.clear();
        followMemo.clear();
    }

    // ==== LÉXICO PARA GRAMÁTICA DE GRAMÁTICAS ====
    private enum TokenType { SYMBOL, ARROW, OR, SEMICOLON, EOF }

    private static class GrammarLexer {
        private final String src;
        private int idx;
        Token current;

        GrammarLexer(String src) {
            this.src = src;
            this.idx = 0;
            next();
        }

        void next() {
            saltarEspacios();
            if (idx >= src.length()) {
                current = new Token(TokenType.EOF, "");
                return;
            }

            // Flecha ->
            if (src.startsWith("->", idx)) {
                idx += 2;
                current = new Token(TokenType.ARROW, "->");
                return;
            }

            char c = src.charAt(idx);
            switch (c) {
                case '|':
                    idx++;
                    current = new Token(TokenType.OR, "|");
                    return;
                case ';':
                    idx++;
                    current = new Token(TokenType.SEMICOLON, ";");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    while (idx < src.length()) {
                        char ch = src.charAt(idx);
                        if (Character.isWhitespace(ch) || ch == '|' || ch == ';') break;
                        // Flecha detectada
                        if (ch == '-' && (idx + 1 < src.length()) && src.charAt(idx + 1) == '>') break;
                        sb.append(ch);
                        idx++;
                    }
                    String lex = sb.toString();
                    if (lex.isEmpty()) {
                        throw new IllegalArgumentException("Símbolo inesperado en posición " + idx);
                    }
                    current = new Token(TokenType.SYMBOL, lex);
            }
        }

        boolean accept(TokenType t) {
            if (current.type == t) {
                next();
                return true;
            }
            return false;
        }

        private void saltarEspacios() {
            while (idx < src.length() && Character.isWhitespace(src.charAt(idx))) idx++;
        }
    }

    private static class Token {
        TokenType type;
        String lexema;
        Token(TokenType t, String l) { type = t; lexema = l; }
    }
}
