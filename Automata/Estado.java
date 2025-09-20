import java.util.HashSet;

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
    
    HashSet<Estado> CerraduraEpsilon( HashSet<Estado> E ){

        HashSet<Estado> R = new HashSet<Estado>();
        R.clear();

        for ( Estado e : E )
            for( Transicion t : e.transiciones )
                if( t.simboloInf == '\0' && t.simboloSup == '\0' )
                    R.add( t.EdoDestino );

        return R;

    }

    HashSet<Estado> Mover( Estado e,char c ){

        HashSet<Estado> R = new HashSet<Estado>();
        R.clear();

            for( Transicion t : e.transiciones )
                if( t.simboloInf <= c && t.simboloSup >= c )
                    R.add( t.EdoDestino );

        return R;

    }
    
    HashSet<Estado> Mover( HashSet<Estado> E,char c ){

        HashSet<Estado> R = new HashSet<Estado>();
        R.clear();

        for ( Estado e : E )
            for( Transicion t : e.transiciones )
                if( t.simboloInf <= c && t.simboloSup >= c )
                    R.add( t.EdoDestino );

        return R;

    }
    
    HashSet<Estado> IrA( Estado e,char c ){

        return CerraduraEpsilon( Mover( e,c ) );

    }

    HashSet<Estado> IrA( HashSet<Estado> E,char c ){

        return CerraduraEpsilon( Mover( E,c ) );

    }

    Estado tieneTransition( char c ){

        Estado R = new Estado();
        R.clear();

        return R;
        
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