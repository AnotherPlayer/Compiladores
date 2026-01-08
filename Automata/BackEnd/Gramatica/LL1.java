package BackEnd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

public class LL1 {

    public Gramatica gramatica;

    public HashMap<String, HashMap<String, Integer>> TablaLL1(Gramatica g){

        gramatica = g;
        g.obtenerVt( g.Reglas );
        g.obtenerVn( g.Reglas );

        HashMap<String, HashMap<String, Integer>> tabla = new HashMap<String, HashMap<String, Integer>>();

        for( SimbolG nt : g.Vn )
            tabla.put( nt.NombSimb, new HashMap<String, Integer>() );

        SimbolG finCadena = new SimbolG( "$", SimbEspeciales.FIN, true );

        for( int i=0 ; i<g.Reglas.size() ; i++ ){

            LadoIzq prod = g.Reglas.get(i);
            ArrayList<SimbolG> alpha = prod.LadoDerecho;

            ArrayList<SimbolG> firstAlpha = new ArrayList<SimbolG>();
            boolean generaEpsilon = false;

            int pos = 0;
            while( pos < alpha.size() ){

                SimbolG s = alpha.get(pos);
                boolean esEpsilonLiteral = s.NombSimb.equals("EPSILON") || s.NombSimb.equals("EPS") || s.token == SimbEspeciales.EPSILON;
                if( esEpsilonLiteral ){
                    generaEpsilon = true;
                    break;
                }

                if( s.esTerminal ){
                    boolean yaEsta = false;
                    for( SimbolG f : firstAlpha ){
                        if( f.NombSimb.equals( s.NombSimb ) ){
                            yaEsta = true;
                            break;
                        }
                    }
                    if( !yaEsta )
                        firstAlpha.add( s );
                    break;
                }

                ArrayList<SimbolG> temp = new ArrayList<SimbolG>();
                temp.add( s );
                ArrayList<SimbolG> firstS = g.First( temp );

                boolean tieneEpsilon = false;
                for( SimbolG f : firstS ){
                    boolean fEsEpsilon = f.NombSimb.equals("EPSILON") || f.NombSimb.equals("EPS") || f.token == SimbEspeciales.EPSILON;
                    if( fEsEpsilon ){
                        tieneEpsilon = true;
                        continue;
                    }
                    boolean yaEsta = false;
                    for( SimbolG acc : firstAlpha ){
                        if( acc.NombSimb.equals( f.NombSimb ) ){
                            yaEsta = true;
                            break;
                        }
                    }
                    if( !yaEsta )
                        firstAlpha.add( f );
                }

                if( tieneEpsilon ){
                    pos++;
                    if( pos >= alpha.size() )
                        generaEpsilon = true;
                } else {
                    break;
                }

            }

            for( SimbolG t : firstAlpha ){
                HashMap<String, Integer> fila = tabla.get( prod.SimbIzq.NombSimb );
                if( fila != null )
                    fila.put( t.NombSimb, i );
            }

            if( generaEpsilon ){
                ArrayList<SimbolG> follow = g.Follow( prod.SimbIzq );
                for( SimbolG b : follow ){
                    HashMap<String, Integer> fila = tabla.get( prod.SimbIzq.NombSimb );
                    if( fila != null )
                        fila.put( b.NombSimb, i );
                }
                HashMap<String, Integer> fila = tabla.get( prod.SimbIzq.NombSimb );
                if( fila != null )
                    fila.put( finCadena.NombSimb, i );
            }

        }

        return tabla;
    }

    public boolean algoritmoLL1(String cadena, HashMap<String, HashMap<String, Integer>> tabla){

        if( gramatica == null || tabla == null )
            return false;

        Stack<SimbolG> pila = new Stack<SimbolG>();

        ArrayList<String> tokens = new ArrayList<String>();
        if( cadena != null && !cadena.trim().isEmpty() )
            tokens.addAll( Arrays.asList( cadena.trim().split("\\s+") ) );
        tokens.add( "$" );

        SimbolG finCadena = new SimbolG( "$", SimbEspeciales.FIN, true );

        // Caso inicial: push de fin de cadena y símbolo inicial
        pila.push( finCadena );
        pila.push( gramatica.SimbIni );

        int idx = 0;
        while( !pila.isEmpty() && idx < tokens.size() ){

            SimbolG X = pila.peek();
            String a = tokens.get(idx);

            if( X.esTerminal ){
                // Caso 1: X es terminal y coincide con la entrada
                if( X.NombSimb.equals( a ) || ( X.token == SimbEspeciales.FIN && a.equals( "$" ) ) ){
                    pila.pop();
                    idx++;
                } else {
                    // Caso de error: terminal distinto
                    return false;
                }
            }
            else{
                // Caso 2: X es no terminal, consultar tabla M[X,a]
                HashMap<String, Integer> fila = tabla.get( X.NombSimb );
                if( fila == null )
                    return false;

                Integer prodIdx = fila.get( a );
                if( prodIdx == null )
                    return false; // Celda vacía => error

                LadoIzq prod = gramatica.Reglas.get( prodIdx );

                // Reemplazo en la pila: desapilar X y apilar α en orden inverso (omitiendo ε)
                pila.pop();
                for( int i = prod.LadoDerecho.size() - 1 ; i >= 0 ; i-- ){
                    SimbolG s = prod.LadoDerecho.get(i);
                    boolean esEpsilon = s.NombSimb.equals("EPS") || s.NombSimb.equals("EPSILON") || s.token == SimbEspeciales.EPSILON;
                    if( esEpsilon )
                        continue;
                    pila.push( s );
                }
            }

        }

        // Caso de aceptación: pila vacía y entrada consumida
        return pila.isEmpty() && idx == tokens.size();
    }
    
}
