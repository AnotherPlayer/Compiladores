package BackEnd;

//2° parcial
import java.util.ArrayList;
import java.util.Stack;
import BackEnd.*;

import BackEnd.Gramatica;
import BackEnd.SimbEspeciales;
import BackEnd.SimbolG;
import BackEnd.analizadorLexico;

public class LL0 {

    private Gramatica gramatica;
    private analizadorLexico lexico;
    private int[] reglaPorVn;
    private boolean gramaticaValida;
    private SimbolG finCadena;

    LL0(Gramatica g, analizadorLexico lx){
        gramatica = g;
        lexico = lx;
        reglaPorVn = new int[gramatica.Vn.size()];
        gramaticaValida = true;
        finCadena = new SimbolG("$", SimbEspeciales.FIN, true);
        inicializarReglasUnicas();
    }

    private void inicializarReglasUnicas(){
        int i;
        int idx;

        for(i = 0; i < reglaPorVn.length; i++){
            reglaPorVn[i] = -1;
        }

        for(i = 0; i < gramatica.numReglas; i++){
            if(gramatica.Reglas.get(i) == null || gramatica.Reglas.get(i).SimbIzq == null){
                continue;
            }
            idx = indiceVn(gramatica.Reglas.get(i).SimbIzq.NombSimb);
            if(idx < 0){
                gramaticaValida = false;
                continue;
            }
            if(reglaPorVn[idx] == -1){
                reglaPorVn[idx] = i;
            } else {
                // Más de una producción para el mismo no terminal: no es LL(0)
                gramaticaValida = false;
            }
        }
    }

    private int indiceVn(String nombre){
        int i;

        for(i = 0; i < gramatica.Vn.size(); i++){
            if(gramatica.Vn.get(i).NombSimb.equals(nombre)){
                return i;
            }
        }
        return -1;
    }

    private boolean esEpsilon(SimbolG s){
        if(s == null){
            return false;
        }
        if(s.NombSimb != null && s.NombSimb.equals("ε")){
            return true;
        }
        return s.token == SimbEspeciales.EPSILON;
    }

    private SimbolG simboloVtPorToken(int token){
        int i;

        for(i = 0; i < gramatica.Vt.size(); i++){
            if(gramatica.Vt.get(i).token == token){
                return gramatica.Vt.get(i);
            }
        }
        if(token == SimbEspeciales.FIN){
            return finCadena;
        }
        return null;
    }

    boolean analizar(String cadena){
        Stack<SimbolG> pila = new Stack<SimbolG>();
        int tokenActual;
        SimbolG simboloEntrada;
        SimbolG cima;
        int fila;
        int prodIdx;
        ArrayList<SimbolG> rhs;
        int i;

        if(!gramaticaValida){
            return false;
        }

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
                } else {
                    return false;
                }
            } else {
                fila = indiceVn(cima.NombSimb);
                if(fila < 0){
                    return false;
                }
                prodIdx = reglaPorVn[fila];
                if(prodIdx < 0){
                    return false;
                }

                rhs = gramatica.Reglas.get(prodIdx).LadoDerecho;
                for(i = rhs.size() - 1; i >= 0; i--){
                    if(esEpsilon(rhs.get(i))){
                        continue;
                    }
                    pila.push(rhs.get(i));
                }
            }
        }

        return tokenActual == -1 || tokenActual == SimbEspeciales.FIN;
    }
}
