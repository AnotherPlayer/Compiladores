package BackEnd;

//2° parcial

import java.util.Stack;

// Clase analizadorLexico con método yylex según pseudocódigo líneas 586-650
public class analizadorLexico {

    private AFD Automata;
    private String CadenaSigma;
    private int IndiceCaracterActual;
    private int Token;
    private String yytext;
    private int IniLexema;
    private int FinLexema;
    private Stack<EstadoAnalisis> Pila;

    // Clase auxiliar para guardar el estado del análisis
    class EstadoAnalisis {
        int indice;
        int token;
        String texto;
        int inicio;
        int fin;
        //Templates para la facilitacion de codigo
        EstadoAnalisis(int idx, int tk, String txt, int ini, int f){
            indice = idx;
            token = tk;
            texto = txt;
            inicio = ini;
            fin = f;
        }
    }

    analizadorLexico(){
        Automata = null;
        CadenaSigma = "";
        IndiceCaracterActual = 0;
        Token = -1;
        yytext = "";
        IniLexema = 0;
        FinLexema = 0;
        Pila = new Stack<EstadoAnalisis>();
    }

    analizadorLexico(AFD afd){
        Automata = afd;
        CadenaSigma = "";
        IndiceCaracterActual = 0;
        Token = -1;
        yytext = "";
        IniLexema = 0;
        FinLexema = 0;
        Pila = new Stack<EstadoAnalisis>();
    }

    // Establecer la cadena a analizar
    public void SetSigma(String sigma){
        CadenaSigma = sigma;
        IndiceCaracterActual = 0;
        Token = -1;
        yytext = "";
        IniLexema = 0;
        FinLexema = 0;
        Pila.clear();
    }

    // Método yylex según pseudocódigo líneas 587-650
    public int yylex(){

        // Caso base
        if(IndiceCaracterActual >= CadenaSigma.length()){
            Token = -1;
            yytext = "";
            return -1;
        }

        // Inicializar el análisis
        int est_actual = Automata.EdoInicial;
        int i = IndiceCaracterActual;

        int longitud_valida = 0;
        int token_valido = -1;
        @SuppressWarnings("unused")
        int ultimo_estado_valido = -1;

        // Procesar caracteres mientras sea posible
        while(i < CadenaSigma.length()){
            char c = CadenaSigma.charAt(i);
            int idx = (int)c;

            // Verificar si el estado actual es válido
            if(est_actual < 0 || est_actual >= Automata.NumEdos){
                break;
            }

            EdoAFD edo_actual = Automata.EdosAFD[est_actual];

            // Verificar la transición
            if(idx >= 0 && idx < 257){
                int destino = edo_actual.transAFD[idx];
                if(destino == -1){
                    break;  // No hay transición válida
                }
                est_actual = destino;
            }
            
            else {
                break;  // Carácter fuera de rango
            }

            // Verificar si es estado de aceptación
            boolean esAceptacion = false;

            for(Integer edoAcept : Automata.EdosAceptacion){
                if(edoAcept == est_actual){
                    esAceptacion = true;
                    break;
                }
            }

            if(esAceptacion){

                EdoAFD edoDestino = Automata.EdosAFD[est_actual];
                int token_valido_actual = edoDestino.Token;

                if(token_valido_actual != -1){
                    longitud_valida = i - IndiceCaracterActual + 1;
                    token_valido = token_valido_actual;
                    ultimo_estado_valido = est_actual;
                }
            }

            i++;
        }

        // Si encontramos un lexema válido
        if(longitud_valida > 0){
            // Guardar el estado actual en la pila para posible retroceso
            Pila.push(new EstadoAnalisis(
                IndiceCaracterActual,
                Token,
                yytext,
                IniLexema,
                FinLexema
            ));

            // Actualizar los valores del lexema encontrado
            yytext = CadenaSigma.substring(IndiceCaracterActual, IndiceCaracterActual + longitud_valida);
            IniLexema = IndiceCaracterActual;
            FinLexema = IndiceCaracterActual + longitud_valida - 1;
            IndiceCaracterActual += longitud_valida;
            Token = token_valido;

            return token_valido;
        }
        
        else {
            // Si no se encontró lexema válido, avanzar uno y devolver error
            if(IndiceCaracterActual < CadenaSigma.length()){
                yytext = String.valueOf(CadenaSigma.charAt(IndiceCaracterActual));
                System.out.println("Error Léxico: carácter inválido '" + yytext + "' en posición " + IndiceCaracterActual);
                IniLexema = IndiceCaracterActual;
                FinLexema = IndiceCaracterActual;
                IndiceCaracterActual++;
                Token = -1; 
            }

            return -1;  // Error
            
        }
    }

    // Método para retroceder al estado anterior
    public boolean UndoToken(){
        if(!Pila.isEmpty()){
            EstadoAnalisis estado = Pila.pop();
            IndiceCaracterActual = estado.indice;
            Token = estado.token;
            yytext = estado.texto;
            IniLexema = estado.inicio;
            FinLexema = estado.fin;
            return true;
        }
        return false;
    }

    // Método para obtener todos los tokens de la cadena
    public void AnalizarCadena(String cadena){

        SetSigma(cadena);

        System.out.println("\n=== Análisis Léxico de: \"" + cadena + "\" ===\n");

        int token;
        while((token = yylex()) != -1){
            if(token > 0){
                System.out.println("Token: " + token + " | Lexema: \"" + yytext + "\" | Posición: [" + IniLexema + "," + FinLexema + "]");
            }
        }

        if(IndiceCaracterActual < CadenaSigma.length()){
            System.out.println("\nCadena no completamente analizada.");
            System.out.println("Resto: \"" + CadenaSigma.substring(IndiceCaracterActual) + "\"");
        }
        
        else {
            System.out.println("\nAnálisis completo.");
        }
    }

    // Método para mostrar la tabla de transiciones del AFD
    public void MostrarTablaAFD(){
        if(Automata == null){
            System.out.println("No hay autómata cargado.");
            return;
        }

        System.out.println("\n=== TABLA DE TRANSICIONES DEL AFD ===");
        System.out.println("Número de estados: " + Automata.NumEdos);
        System.out.println("Estado inicial: " + Automata.EdoInicial);

        // Mostrar alfabeto
        System.out.print("Alfabeto: { ");
        for(Character c : Automata.alfabeto){
            if(c == SimbEspeciales.EPSILON){
                System.out.print("ε ");
            } else if(c == ' '){
                System.out.print("' ' ");
            } else {
                System.out.print(c + " ");
            }
        }
        System.out.println("}");

        // Mostrar estados de aceptación con sus tokens
        System.out.println("\nEstados de aceptación:");
        for(Integer edo : Automata.EdosAceptacion){
            System.out.println("  Estado " + edo + " -> Token: " + Automata.EdosAFD[edo].Token);
        }

        // Mostrar tabla de transiciones (solo para caracteres del alfabeto)
        System.out.println("\nTabla de transiciones:");
        System.out.print("Estado\t");

        // Encabezados
        for(Character c : Automata.alfabeto){
            if(c != SimbEspeciales.EPSILON){
                if(c == ' '){
                    System.out.print("' '\t");
                } else {
                    System.out.print(c + "\t");
                }
            }
        }
        System.out.println("Token");

        // Filas de la tabla
        for(int i = 0; i < Automata.NumEdos; i++){
            System.out.print(i + "\t");

            for(Character c : Automata.alfabeto){
                if(c != SimbEspeciales.EPSILON){
                    int ascii = (int)c;
                    int destino = Automata.EdosAFD[i].transAFD[ascii];
                    if(destino == -1){
                        System.out.print("-\t");
                    } else {
                        System.out.print(destino + "\t");
                    }
                }
            }

            // Mostrar token si es estado de aceptación
            boolean esAceptacion = false;
            for(Integer edoAcept : Automata.EdosAceptacion){
                if(edoAcept == i){
                    esAceptacion = true;
                    break;
                }
            }

            if(esAceptacion){
                System.out.println(Automata.EdosAFD[i].Token);
            } else {
                System.out.println("-1");
            }
        }
    }

    // Getters y Setters
    public AFD getAutomata() {
        return Automata;
    }

    public void setAutomata(AFD automata) {
        Automata = automata;
    }

    public String getYytext() {
        return yytext;
    }

    public int getToken() {
        return Token;
    }

    public int getIniLexema() {
        return IniLexema;
    }

    public int getFinLexema() {
        return FinLexema;
    }
}
