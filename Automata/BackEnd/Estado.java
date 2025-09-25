import java.util.HashSet;
import java.util.Stack;

public class Estado {

    int IdEdo;
    boolean EdoAcept;
    int token;

    HashSet<Transicion> transiciones;
    static int NumEstados = 0;

    Estado(){

        IdEdo = NumEstados++;
        EdoAcept = false;
        token = -1;
        transiciones = new HashSet<Transicion>();
        transiciones.clear();

    }

    Estado tieneTransition( char c ){

        Estado R = new Estado();
        R.clear();

        return R;
        
    }

    void union( HashSet<Estado> Estados ){

        //????

    }
    
    void clear(){
        
        IdEdo = -1;
        EdoAcept = false;
        token = -1;
        transiciones.clear();

    }

    void add( Estado e ){



    }
    
}