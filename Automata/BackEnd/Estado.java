package BackEnd;
import java.util.ArrayList;

public class Estado {

    int IdEdo;
    boolean EdoAcept;
    int token;

    ArrayList<Transicion> transiciones;
    static int NumEstados = 0;

    public Estado(){

        IdEdo = NumEstados++;
        EdoAcept = false;
        token = -1;
        transiciones = new ArrayList<Transicion>();

    }

    public Estado tieneTransition( char c ){

        for(Transicion t : transiciones){
            if(t.simboloInf <= c && c <= t.simboloSup){
                return t.EdoDestino;
            }
        }

        return null;
        
    }
    
    public void clear(){
        
        IdEdo = -1;
        EdoAcept = false;
        token = -1;
        transiciones.clear();

    }

    public void addTransicion(Transicion transicion){
        if(transicion != null){
            transiciones.add(transicion);
        }
    }

    public void add( Estado e ){

        if(e == null){
            return;
        }

        for(Transicion t : e.transiciones){
            transiciones.add(t);
        }
        this.EdoAcept = this.EdoAcept || e.EdoAcept;

        if(this.token < 0){
            this.token = e.token;
        }

    }
    
}
