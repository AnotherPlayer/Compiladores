//2° parcial

import java.util.ArrayList;
import java.util.List;

public class Gramatica {

    int numReglas;
	LadoIzq[] Reglas;
    ArrayList<SimbolG> Vn;
    ArrayList<SimbolG> Vt;
	SimbolG SimbIni;

    Gramatica(){

        numReglas++;
        Reglas = new LadoIzq[100];
        Vn = new ArrayList<SimbolG>();
        Vn.clear();
        Vt = new ArrayList<SimbolG>();
        Vt.clear();
        SimbIni = null;

    }
    //Devulve una sublista entre los indices a y b
    ArrayList<SimbolG> subLista( ArrayList<SimbolG> l,int a, int b){

        ArrayList<SimbolG> X = new ArrayList<SimbolG>();
        List<SimbolG> sub = l.subList(a, b);

        for( SimbolG s : sub )
            X.add(s);

        return X;

    }

    //Operación first
    ArrayList<SimbolG> First( ArrayList<SimbolG> l ) {

        ArrayList<SimbolG> R = new ArrayList<SimbolG>();
        ArrayList<SimbolG> aux = new ArrayList<SimbolG>();
        R.clear();
        aux.clear();
        
        //Criterio para detener recursión
        if ( l.get(0).esTerminal ) {
            
            R.add(l.get(0));
            return R;

        }
        
        for (int i = 0; i < numReglas; i++) {
            if (Reglas[i].SimbIzq.NombSimb == l.get(0).NombSimb)
                R.addAll(First(Reglas[i].LadoDerecho));
        }
            
        if (R.contains(simbEspeciales.EPSILON)) {
            if (l.size() == 1)
                return R;
        
        aux = subLista(l, 1, l.size() - 1);
        R.addAll( First(aux) );

        }
        
        

        return R;    
    }

    ArrayList<SimbolG> Follow( SimbolG s ){

	    ArrayList<SimbolG> R = new ArrayList<SimbolG>();
	    ArrayList<SimbolG> aux = new ArrayList<SimbolG>();
	    int j;
	
	    R.clear();
	    //aux.clear();//??
	
	    if( s.esTerminal )
		    return R;
	
	    //Buscar "s" en los lados derechos
	    for( int i=0 ; i<numReglas ; i++ ){
            
		    j = Reglas[i].LadoDerecho.indexOf(s);
            
		    if( j == -1 )
		    	continue;
            
		    if( j == (Reglas[i].LadoDerecho.size()-1) ){
		    	if( s == Reglas[i].SimbIzq )
		    		continue;
            
		    	R.addAll( Follow( Reglas[i].SimbIzq ) ); //??
		    	continue;
            
		    }
        
		    aux.clear();		
		    aux = First( subLista( Reglas[i].LadoDerecho, j, Reglas[i].LadoDerecho.size()-j ) );

            if( aux.contains(simbEspeciales.EPSILON) ){

                aux.remove(simbEspeciales.EPSILON);//aux - {epsilon}
                R.addAll(aux);
                R.addAll( Follow( Reglas[i].SimbIzq ) );
                continue;

            }

            R.addAll(aux);
	
	    }

        return R;

    }
    
}