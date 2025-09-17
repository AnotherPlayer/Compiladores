public class Estado {

    int IdEdo;
    boolean EdoAcept;
    int token;

    Transicion transiciones;    //??
    static int NumEstados = 0;

    Estado(){

        IdEdo = NumEstados++;
        EdoAcept = false;
        token = -1;
        transiciones = new Transicion();    //??
        transiciones.clear();

    }

    void clear(){

        
        IdEdo = -1;
        EdoAcept = false;
        token = -1;
        transiciones.clear();

    }

    void add( Estado e ){



    }

    Estado tieneTransition( char c ){

        Estado R = new Estado();
        R.clear();


        return R;
        
    }
    
}