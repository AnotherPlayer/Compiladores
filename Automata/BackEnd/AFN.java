import java.util.HashSet;
import java.util.Stack;

public class AFN {

    HashSet<Estado> Estados;
    Estado EdoInicial;
    HashSet<char> alfabeto;
    HashSet<Estado> EdosAcept;
    
    AFN(){

        Estados = new HashSet<Estado>();
        Estados.clear();

        EdoInicial = null;

        alfabeto = new HashSet<char>();
        alfabeto.clear();

        EdosAcept = new HashSet<Estado>();
        EdosAcept.clear();
        

    }

    //Automáta básico
    AFN doBasic( char c ){

        Estado e1,e2;
        e1 = new Estado();
        e2 = new Estado();

        this.Estados.add(e1);
        this.Estados.add(e2);
        
        this.EdoInicial = e1;

        e1.transiciones.add( new Transicion(c,e2) );
        e2.EdoAcept = true;

        this.alfabeto.add(c);
        
        return this;

    }

    AFN doBasic( char c1,char c2 ){

        Estado e1,e2;
        e1 = new Estado();
        e2 = new Estado();

        this.Estados.add(e1);
        this.Estados.add(e2);
        
        this.EdoInicial = e1;

        e1.transiciones.add( new Transicion(c1,c2,e2) );
        e2.EdoAcept = true;

        this.EdosAcept.add(e2);

        for( int i=c1 ; i<=c2 ; i++ )
            this.alfabeto.add( i );
        
        return this;

    }

    //Operación unión
    AFN union( AFN F2 ){

        Estado e1,e2;
        e1 = new Estado();
        e2 = new Estado();
        
        e1.transiciones.add( new Transicion( simbEspeciales.EPSILON,this.EdoInicial ) );
        e2.transiciones.add( new Transicion( simbEspeciales.EPSILON,F2.EdoInicial ) );

        for( Estado e : this.EdosAcept ){
            e.transiciones.add( new Transicion( simbEspeciales.EPSILON,e2 ) );
            e.EdoAcept = false;
        }

        e2.EdoAcept = true;

        this.EdoInicial = e1;
        this.Estados.union( F2.Estados );

        this.Estados.add(e1);
        this.Estados.add(e2);

        this.EdosAcept.clear();
        this.EdosAcept.add(e2);

        this.alfabeto.union( F2.alfabeto );
        
        return this;

    }

    //Operación concatenación
    AFN join( AFN F2 ){

        for ( Estado e : this.EdosAcept )
            for( Transicion t : F2.EdoInicial.transiciones )
                e.EdoAcept = false;
        

        

        return this;

    }

    //Cerradura positiva (+)
    AFN cerrPos(){

        return this;

    }

    //Cerradura de Kleene (*)
    AFN cerrKleene(){

        return this;

    }

    //Operación opcional (?)
    AFN opcional(){

        return this;

    }

    //Cerradura de épsilon
    HashSet<Estado> CerraduraEpsilon( Estado e ){

        HashSet<Estado> C = new HashSet<Estado>();
        Stack<Estado> P = new Stack<Estado>();
        Estado e2;

        C.clear();
        P.clear();
        P.push(e);

        while( !P.empty() ){

            e2 = P.pop();

            if( !C.contains(e2) ){

                C.add(e2);

                for( Transicion t : e2.transiciones )
                    if( t.simboloInf == simbEspeciales.EPSILON && t.simboloSup == simbEspeciales.EPSILON )
                        P.push( t.EdoDestino );

            }

        }

        return C;

    }

    HashSet<Estado> CerraduraEpsilon( HashSet<Estado> E ){

        HashSet<Estado> R = new HashSet<Estado>();

        R.clear();

        for ( Estado e : E )
            //R.union(  CerraduraEpsilon( e ) );
            R = new HashSet<Estado>( R ); //Hacer la unión

        return R;

    }

    //Mover
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

    //IrA
    HashSet<Estado> IrA( Estado e,char c ){

        return CerraduraEpsilon( Mover( e,c ) );

    }

    HashSet<Estado> IrA( HashSet<Estado> E,char c ){

        return CerraduraEpsilon( Mover( E,c ) );

    }

    public HashSet<Estado> getEstados() {
        return Estados;
    }

    public void setEstados(HashSet<Estado> estados) {
        Estados = estados;
    }

    public Estado getEdoInicial() {
        return EdoInicial;
    }

    public void setEdoInicial(Estado edoInicial) {
        EdoInicial = edoInicial;
    }

    public HashSet<Estado> getEdosAcept() {
        return EdosAcept;
    }

    public void setEdosAcept(HashSet<Estado> edosAcept) {
        EdosAcept = edosAcept;
    }

}
