//2° parcial

import BackEnd.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Gramatica {

    public int numReglas;

    public ArrayList<LadoIzq> Reglas;      // P
    public ArrayList<SimbolG> Vn;          // No terminales
    public ArrayList<SimbolG> Vt;          // Terminales
    public SimbolG SimbIni;                // Símbolo inicial

    public Gramatica(){

        numReglas = 0;
        Reglas = new ArrayList<LadoIzq>();
        Vn = new ArrayList<SimbolG>();
        Vt = new ArrayList<SimbolG>();

    }

    public void moreReglas( LadoIzq Regla ){

        if( numReglas == 0 ){

            SimbIni = Regla.SimbIzq;

        }

        Reglas.add( Regla );
        numReglas++;


    }

    public boolean asignarToken( String nombreSimb, int nuevoToken ){

        // Identificar terminales y no terminales actuales
        obtenerVt( Reglas );
        obtenerVn( Reglas );

        // Verificar que el token no esté usado por otro símbolo distinto
        for( LadoIzq li : Reglas ){
            if( li.SimbIzq != null && li.SimbIzq.token == nuevoToken && !li.SimbIzq.NombSimb.equals( nombreSimb ) )
                return false;
            for( SimbolG s : li.LadoDerecho ){
                if( s.token == nuevoToken && !s.NombSimb.equals( nombreSimb ) )
                    return false;
            }
        }
        for( SimbolG s : Vn ){
            if( s.token == nuevoToken && !s.NombSimb.equals( nombreSimb ) )
                return false;
        }
        for( SimbolG s : Vt ){
            if( s.token == nuevoToken && !s.NombSimb.equals( nombreSimb ) )
                return false;
        }

        boolean actualizado = false;

        for( LadoIzq li : Reglas ){
            if( li.SimbIzq != null && li.SimbIzq.NombSimb.equals( nombreSimb ) ){
                li.SimbIzq.token = nuevoToken;
                actualizado = true;
            }
            for( SimbolG s : li.LadoDerecho ){
                if( s.NombSimb.equals( nombreSimb ) ){
                    s.token = nuevoToken;
                    actualizado = true;
                }
            }
        }

        for( SimbolG s : Vn ){
            if( s.NombSimb.equals( nombreSimb ) ){
                s.token = nuevoToken;
                actualizado = true;
            }
        }
        for( SimbolG s : Vt ){
            if( s.NombSimb.equals( nombreSimb ) ){
                s.token = nuevoToken;
                actualizado = true;
            }
        }

        return actualizado;
    }

    public void cargarGramatica(){

        // ====================== Primera sección ======================
        ArrayList<String> lineas = new ArrayList<String>();
        try{
            File archivo = new File("BackEnd/Prueba_LL1.txt");
            if( !archivo.exists() ){
                archivo = new File("PruebaLL1.txt");
            }
            BufferedReader br = new BufferedReader( new FileReader( archivo ) );
            String linea;
            while( (linea = br.readLine()) != null ){
                if( linea.trim().isEmpty() )
                    continue;
                lineas.add( linea.trim() );
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
            return;
        }

        // ====================== Segunda sección ======================
        int tokenActual = 10;
        for( String linea : lineas ){

            String[] partes = linea.split("->");
            if( partes.length < 2 )
                continue;

            String ladoIzqCadena = partes[0].trim();
            SimbolG simbIzq = new SimbolG( ladoIzqCadena, tokenActual, true );
            tokenActual += 10;
            LadoIzq reglaBase = new LadoIzq( simbIzq );

            String ladoDerCompleto = partes[1].trim();
            if( ladoDerCompleto.endsWith(";") )
                ladoDerCompleto = ladoDerCompleto.substring( 0, ladoDerCompleto.length() - 1 );

            String[] alternativas = ladoDerCompleto.split("\\|");
            for( int idxAlt = 0 ; idxAlt < alternativas.length ; idxAlt++ ){

                String alternativa = alternativas[idxAlt].trim();
                LadoIzq reglaActual = ( idxAlt == 0 ) ? reglaBase : new LadoIzq( simbIzq );

                if( alternativa.endsWith(";") )
                    alternativa = alternativa.substring( 0, alternativa.length() - 1 );

                if( !alternativa.isEmpty() ){
                    String[] simbolos = alternativa.split("\\s+");
                    for( String s : simbolos ){
                        if( s.trim().isEmpty() )
                            continue;
                        String limpio = s.trim();
                        if( limpio.endsWith(";") )
                            limpio = limpio.substring( 0, limpio.length() - 1 );

                        SimbolG simb = new SimbolG( limpio, tokenActual, false );
                        tokenActual += 10;
                        reglaActual.AgregarSimbolo( simb );
                    }
                }

                moreReglas( reglaActual );

            }

        }

        // ====================== Tercera sección ======================
        for( LadoIzq regla : Reglas ){
            for( SimbolG simbDer : regla.LadoDerecho ){
                for( LadoIzq reglaIzq : Reglas ){
                    if( simbDer.NombSimb.equals( reglaIzq.SimbIzq.NombSimb ) ){
                        simbDer.token = reglaIzq.SimbIzq.token;
                        simbDer.esTerminal = reglaIzq.SimbIzq.esTerminal;
                        break;
                    }
                }
            }
        }
    }

    public ArrayList<SimbolG> First( ArrayList<SimbolG> l ){
        
        ArrayList<SimbolG> R = new ArrayList<SimbolG>();
	    R.clear();
	
	    //Criterio para detener recursión
	    if( l.get(0).esTerminal ){
	
    		R.add( l.get(0) );
		    return R;
	
	    }
	
	    for( int i=0 ; i<numReglas ; i++ ){

		    if( Reglas.get(i).SimbIzq.NombSimb.equals( l.get(0).NombSimb ) )
			    R.addAll( First( Reglas.get(i).LadoDerecho ) );
			
	        if( R.contains(SimbEspeciales.EPSILON) ){
    		    if( l.size() == 1 )
			        return R;
		
                R.addAll( First( listToArray(l.subList( 1,l.size()-1 )) ) );
		
	        }

        }

        return R;

    }
    
    public ArrayList<SimbolG> Follow(SimbolG s){
        
        ArrayList<SimbolG> R = new ArrayList<SimbolG>();
        ArrayList<SimbolG> aux = new ArrayList<SimbolG>();
        int j;

        R.clear();

        if ( s.esTerminal )
            return R;

        for(int i = 0; i < numReglas; i++){

            j = Reglas.get(i).LadoDerecho.indexOf(s);
            
            if (j == -1)
                continue;

            if( j == Reglas.get(i).LadoDerecho.size() - 1 ){
                if( s.equals(Reglas.get(i).SimbIzq) )
                    continue;

                R.addAll( Follow( Reglas.get(i).SimbIzq ) );
                continue;

            }

            aux.clear();
            //Crear función nueva para sublista
            aux = listToArray(Reglas.get(i).LadoDerecho.subList( j, Reglas.get(i).LadoDerecho.size() - j ));
            aux = First( aux );
            
            if( aux.get(i).NombSimb.equals( SimbEspeciales.EPSILON ) ){

                aux.remove(SimbEspeciales.EPSILON);
                R.addAll( aux );
                R.addAll( Follow( Reglas.get(i).SimbIzq ) );
                continue;

            }

            R.addAll(aux);
            
        }

        return R;

    }

    public ArrayList<SimbolG> listToArray( List<SimbolG> l ){
        
        ArrayList<SimbolG> R = new ArrayList<SimbolG>();

        for( SimbolG s : l )
            R.add(s);
        
        return R;

    }
    
    public void obtenerVt( ArrayList<LadoIzq> Reglas ){

        for( LadoIzq li : Reglas ){
            for( SimbolG s : li.LadoDerecho ){

                if( s.esTerminal && !Vt.contains(s) )
                    Vt.add(s);

            }

        }

    }

    public void obtenerVn( ArrayList<LadoIzq> Reglas ){

        for( LadoIzq li : Reglas ){
            for( SimbolG s : li.LadoDerecho ){

                if( !s.esTerminal && !Vn.contains(s) )
                    Vn.add(s);

            }

        }

    }
    
    

}
