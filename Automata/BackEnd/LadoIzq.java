import java.util.ArrayList;

public class LadoIzq {

    SimbolG SimbIzq;
    ArrayList<SimbolG> LadoDerecho;

    LadoIzq( SimbolG SimIzq ){

        this.SimbIzq = SimIzq;
        LadoDerecho = new ArrayList<SimbolG>();
        LadoDerecho.clear();

    }

    void AgregarSimbolo( SimbolG Simbolo ){

        LadoDerecho.add( Simbolo );

    }
    
}
