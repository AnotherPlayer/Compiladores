//2° parcial

package BackEnd;

import java.util.ArrayList;

public class LadoIzq {

    public SimbolG SimbIzq;
    public ArrayList<SimbolG> LadoDerecho;

    public LadoIzq( SimbolG SimIzq ){

        this.SimbIzq = SimIzq;
        LadoDerecho = new ArrayList<SimbolG>();
        LadoDerecho.clear();

    }

    public void AgregarSimbolo( SimbolG Simbolo ){

        LadoDerecho.add( Simbolo );

    }

    public void AgregarSimboloArray( ArrayList<SimbolG> Simbolos ){
        
        LadoDerecho.addAll( Simbolos );

    }
    
}
