//2° parcial

import java.util.ArrayList;
import java.util.Stack;

public class LL1 {

    private Gramatica gramatica;
    private analizadorLexico lexico;
    private int[][] tabla;
    private int numVn;
    private int numVt;
    private ArrayList<String> nombresVn;
    private ArrayList<Integer> tokensVt;
    private ArrayList<SimbolG> vtPorCol;
    private SimbolG finCadena;

    LL1(Gramatica g, analizadorLexico lx){
        gramatica = g;
        lexico = lx;
        nombresVn = new ArrayList<String>();
        tokensVt = new ArrayList<Integer>();
        vtPorCol = new ArrayList<SimbolG>();
        finCadena = new SimbolG("$", simbEspeciales.FIN, true);
        construirTabla();
    }

    // Construye la tabla LL(1) M[A,a]
    private void construirTabla(){

        numVn = gramatica.Vn.size();
        numVt = gramatica.Vt.size() + 1; // +1 por fin de cadena

        tabla = new int[numVn][numVt];

        nombresVn.clear();
        tokensVt.clear();
        vtPorCol.clear();

        // Inicializar tabla en -1 (sin producción)
        for(int i = 0; i < numVn; i++){
            for(int j = 0; j < numVt; j++){
                tabla[i][j] = -1;
            }
        }

        // Mapear Vn
        for(int i = 0; i < gramatica.Vn.size(); i++){
            nombresVn.add(gramatica.Vn.get(i).NombSimb);
        }

        // Mapear Vt
        for(int j = 0; j < gramatica.Vt.size(); j++){
            SimbolG vt = gramatica.Vt.get(j);
            tokensVt.add(vt.token);
            vtPorCol.add(vt);
        }

        tokensVt.add((int)simbEspeciales.FIN);
        vtPorCol.add(finCadena);

        // Llenar la tabla
        for(int r = 0; r < gramatica.numReglas; r++){
            LadoIzq regla = gramatica.Reglas[r];
            int fila = indiceVn(regla.SimbIzq.NombSimb);
            ArrayList<SimbolG> firstAlpha = gramatica.First(regla.LadoDerecho);

            boolean contieneEpsilon = contieneEpsilon(firstAlpha);

            for(SimbolG t : firstAlpha){
                if(esEpsilon(t)){
                    continue;
                }

                int col = indiceVt(t.token);

                if(col >= 0){
                    tabla[fila][col] = r;
                }
            }

            if(contieneEpsilon){
                ArrayList<SimbolG> followA = gramatica.Follow(regla.SimbIzq);
                for(SimbolG b : followA){
                    int col = indiceVt(b.token);
                    if(col >= 0){
                        tabla[fila][col] = r;
                    }
                }

                // También para fin de cadena
                tabla[fila][numVt - 1] = r;

            }
        }
    }

    private boolean contieneEpsilon(ArrayList<SimbolG> lista){
        
        for(SimbolG s : lista){
            if(esEpsilon(s)){
                return true;
            }
        }
        return false;
    }

    private boolean esEpsilon(SimbolG s){
        
        if(s == null){
            return false;
        }
        if(s.NombSimb != null && s.NombSimb.equals("ε")){
            return true;
        }
        return s.token == simbEspeciales.EPSILON;
    }

    private int indiceVn(String nombre){
        return nombresVn.indexOf(nombre);
    }

    private int indiceVt(int token){
        return tokensVt.indexOf(token);
    }

    private SimbolG simboloVtPorToken(int token){
        
        int col = tokensVt.indexOf(token);
        if(col >= 0 && col < vtPorCol.size()){
            return vtPorCol.get(col);
        }
        return null;
    }

    // Analiza una cadena de entrada (ya tokenizada por el analizador léxico)
    boolean analizar(String cadena){
        Stack<SimbolG> pila = new Stack<SimbolG>();
        int tokenActual;
        SimbolG simboloEntrada;
        SimbolG cima;
        int fila;
        int col;
        int prodIdx;

        lexico.SetSigma(cadena);
        pila.push(finCadena);
        pila.push(gramatica.SimbIni);
        tokenActual = lexico.yylex();
        simboloEntrada = simboloVtPorToken(tokenActual);

        while(!pila.isEmpty()){
            cima = pila.pop();

            if(cima.esTerminal){
                if(simboloEntrada != null && cima.token == simboloEntrada.token){
                    tokenActual = lexico.yylex();
                    simboloEntrada = simboloVtPorToken(tokenActual);
                }
                
                else {
                    return false;
                }
            }
            
            else {

                fila = indiceVn(cima.NombSimb);
                col = -1;

                if(simboloEntrada != null){
                    col = indiceVt(simboloEntrada.token);
                }
                
                else if(tokenActual == -1){
                    col = numVt - 1; // fin de cadena
                }

                if(col < 0){
                    return false;
                }

                prodIdx = tabla[fila][col];
                
                if(prodIdx < 0){
                    return false;
                }

                // Desapilar A y apilar el lado derecho en orden inverso (ignorando epsilon)
                ArrayList<SimbolG> rhs = gramatica.Reglas[prodIdx].LadoDerecho;

                for(int i = rhs.size() - 1; i >= 0; i--){
                    SimbolG s = rhs.get(i);
                    if(esEpsilon(s)){
                        continue;
                    }
                    pila.push(s);
                }
            }
        }

        return tokenActual == -1 || tokenActual == simbEspeciales.FIN;
        
    }

}
