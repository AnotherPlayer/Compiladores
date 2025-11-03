import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.io.*;

// Clase AFD según pseudocódigo líneas 471-512
public class AFD {

    EdoAFD[] EdosAFD;
    ArrayList<Character> alfabeto;
    int NumEdos;

    // ATRIBUTOS QUE FALTABAN (necesarios para el funcionamiento)
    ArrayList<Integer> EdosAceptacion;  // Estados de aceptación
    int EdoInicial;                     // Estado inicial del AFD

    AFD(){
        NumEdos = 0;
        alfabeto = new ArrayList<Character>();
        EdosAceptacion = new ArrayList<Integer>();
        EdoInicial = 0;
    }

    AFD( int n ){
        EdosAFD = new EdoAFD[n];
        NumEdos = n;
        alfabeto = new ArrayList<Character>();
        EdosAceptacion = new ArrayList<Integer>();
        EdoInicial = 0;

        // Inicializar todos los EdoAFD
        for(int i = 0; i < n; i++) {
            EdosAFD[i] = new EdoAFD(i);
        }
    }

    AFD( int n, ArrayList<Character> alf ){
        EdosAFD = new EdoAFD[n];
        NumEdos = n;
        alfabeto = new ArrayList<Character>();
        alfabeto.clear();
        // Corregir: ArrayList no tiene método union, usar addAll
        alfabeto.addAll(alf);
        EdosAceptacion = new ArrayList<Integer>();
        EdoInicial = 0;

        // Inicializar todos los EdoAFD
        for(int i = 0; i < n; i++) {
            EdosAFD[i] = new EdoAFD(i);
        }
    }

    /**
     * Método AFNtoAFD - Conversión de AFN a AFD
     * Pseudocódigo líneas 516-555
     * Implementa el algoritmo de construcción de subconjuntos
     */
    public static AFD AFNtoAFD(AFN afn) {

        ArrayList<ElemSj> C = new ArrayList<ElemSj>();  // Conjunto de conjuntos de estados
        Queue<ElemSj> Q = new LinkedList<ElemSj>();     // Cola para procesar
        int NumElemSj = 0;                              // Contador de elementos Sj

        C.clear();
        Q.clear();

        // Crear S0 con la cerradura epsilon del estado inicial
        ElemSj S0 = new ElemSj();
        ArrayList<Estado> estadoInicial = new ArrayList<Estado>();
        estadoInicial.add(afn.getEdoInicial());
        S0.S = afn.CerraduraEpsilon(estadoInicial);
        S0.Id = NumElemSj++;

        C.add(S0);
        Q.add(S0);

        // Procesar la cola
        while(!Q.isEmpty()) {
            ElemSj SjAct = Q.poll();

            // Para cada símbolo del alfabeto
            for(Character simbolo : afn.getAlfabeto()) {
                if(simbolo == simbEspeciales.EPSILON) continue;

                // Calcular IrA(SjAct, simbolo)
                ArrayList<Estado> conjunto = afn.IrA(SjAct.S, simbolo);

                if(conjunto.isEmpty()) continue;

                // Buscar si ya existe este conjunto
                int indice = buscarConjunto(C, conjunto);

                if(indice == -1) {
                    // Nuevo conjunto, agregarlo
                    ElemSj SjNuevo = new ElemSj();
                    SjNuevo.S = conjunto;
                    SjNuevo.Id = NumElemSj++;

                    C.add(SjNuevo);
                    Q.add(SjNuevo);
                    indice = SjNuevo.Id;
                }

                // Aquí se agregaría la transición al AFD
                // (se hace después cuando construimos el AFD)
            }
        }

        // Construir el AFD a partir de los conjuntos
        AFD afd = new AFD(C.size());
        afd.alfabeto.addAll(afn.getAlfabeto());
        afd.EdoInicial = 0;  // S0 siempre es el estado inicial

        // Construir las transiciones
        for(ElemSj sj : C) {
            for(Character simbolo : afn.getAlfabeto()) {
                if(simbolo == simbEspeciales.EPSILON) continue;

                ArrayList<Estado> destino = afn.IrA(sj.S, simbolo);
                if(!destino.isEmpty()) {
                    int indiceDestino = buscarConjunto(C, destino);
                    if(indiceDestino != -1) {
                        int ascii = (int)simbolo;
                        afd.EdosAFD[sj.Id].transAFD[ascii] = indiceDestino;
                    }
                }
            }

            // Verificar si es estado de aceptación
            for(Estado e : sj.S) {
                if(e.EdoAcept) {
                    afd.EdosAceptacion.add(sj.Id);
                    afd.EdosAFD[sj.Id].Token = e.Token;
                    break;
                }
            }
        }

        return afd;
    }

    /**
     * Método auxiliar para buscar un conjunto de estados en la lista C
     */
    private static int buscarConjunto(ArrayList<ElemSj> C, ArrayList<Estado> conjunto) {
        for(ElemSj elem : C) {
            if(sonIguales(elem.S, conjunto)) {
                return elem.Id;
            }
        }
        return -1;
    }

    /**
     * Método auxiliar para comparar dos conjuntos de estados
     */
    private static boolean sonIguales(ArrayList<Estado> c1, ArrayList<Estado> c2) {
        if(c1.size() != c2.size()) return false;

        for(Estado e : c1) {
            boolean encontrado = false;
            for(Estado e2 : c2) {
                if(e.IdEdo == e2.IdEdo) {
                    encontrado = true;
                    break;
                }
            }
            if(!encontrado) return false;
        }
        return true;
    }

    /**
     * SaveAFD - Guardar AFD en archivo
     * Pseudocódigo líneas 571-583
     */
    boolean SaveAFD( String nameFile ){
        try {
            FileWriter writer = new FileWriter(nameFile);

            // Guardar número de estados
            writer.write(this.NumEdos + "\n");

            // Guardar estado inicial
            writer.write(this.EdoInicial + "\n");

            // Guardar alfabeto
            writer.write(this.alfabeto.size() + "\n");
            for(Character c : this.alfabeto) {
                writer.write((int)c + " ");
            }
            writer.write("\n");

            // Guardar estados de aceptación
            writer.write(this.EdosAceptacion.size() + "\n");
            for(Integer edo : this.EdosAceptacion) {
                writer.write(edo + " ");
            }
            writer.write("\n");

            // Guardar tabla de transiciones
            for(int i = 0; i < NumEdos; i++) {
                // Guardar token del estado
                writer.write(EdosAFD[i].Token + "\n");

                // Guardar transiciones (solo las del alfabeto para ahorrar espacio)
                for(Character c : alfabeto) {
                    if(c != simbEspeciales.EPSILON) {
                        int ascii = (int)c;
                        writer.write(EdosAFD[i].transAFD[ascii] + " ");
                    }
                }
                writer.write("\n");
            }

            writer.close();
            return true;

        } catch(IOException e) {
            System.err.println("Error al guardar AFD: " + e.getMessage());
            return false;
        }
    }

    /**
     * LoadAFD - Cargar AFD desde archivo
     * Pseudocódigo líneas 505-510
     */
    boolean LoadAFD( String nameFile ){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(nameFile));

            // Leer número de estados
            this.NumEdos = Integer.parseInt(reader.readLine());
            this.EdosAFD = new EdoAFD[NumEdos];
            for(int i = 0; i < NumEdos; i++) {
                EdosAFD[i] = new EdoAFD(i);
            }

            // Leer estado inicial
            this.EdoInicial = Integer.parseInt(reader.readLine());

            // Leer alfabeto
            int tamAlfabeto = Integer.parseInt(reader.readLine());
            this.alfabeto = new ArrayList<Character>();
            String[] simbolos = reader.readLine().split(" ");
            for(int i = 0; i < tamAlfabeto; i++) {
                this.alfabeto.add((char)Integer.parseInt(simbolos[i]));
            }

            // Leer estados de aceptación
            int numAceptacion = Integer.parseInt(reader.readLine());
            this.EdosAceptacion = new ArrayList<Integer>();
            if(numAceptacion > 0) {
                String[] estados = reader.readLine().split(" ");
                for(int i = 0; i < numAceptacion; i++) {
                    this.EdosAceptacion.add(Integer.parseInt(estados[i]));
                }
            }

            // Leer tabla de transiciones
            for(int i = 0; i < NumEdos; i++) {
                // Leer token del estado
                EdosAFD[i].Token = Integer.parseInt(reader.readLine());

                // Leer transiciones
                String[] transiciones = reader.readLine().split(" ");
                int j = 0;
                for(Character c : alfabeto) {
                    if(c != simbEspeciales.EPSILON) {
                        int ascii = (int)c;
                        EdosAFD[i].transAFD[ascii] = Integer.parseInt(transiciones[j++]);
                    }
                }
            }

            reader.close();
            return true;

        } catch(Exception e) {
            System.err.println("Error al cargar AFD: " + e.getMessage());
            return false;
        }
    }

}
