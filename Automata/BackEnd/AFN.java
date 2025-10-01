package BackEnd;

import java.util.ArrayList;
import java.util.Stack;

public class AFN {

    private ArrayList<Estado> Estados;
    private Estado EdoInicial;
    private Alfabeto alfabeto;
    private ArrayList<Estado> EdosAcept;
    
    AFN(){

        Estados = new ArrayList<Estado>();
        Estados.clear();

        EdoInicial = null;

        alfabeto = new Alfabeto();
        alfabeto.clear();

        EdosAcept = new ArrayList<Estado>();
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
            this.alfabeto.add((char)i);
        
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
        this.Estados.addAll(F2.Estados);

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
        

        this.EdosAcept.clear();
        this.EdosAcept.addAll( F2.EdosAcept );
        this.alfabeto.union( F2.alfabeto );

        F2.Estados.remove( F2.alfabeto );
        //this.Estados.

        return this;

    }

    //Cerradura positiva (+)
    AFN cerrPos(){

        Estado e1,e2;
        e1 = new Estado();
        e2 = new Estado();

        for( Estado e : this.EdosAcept ){
            
            e.transiciones.add( new Transicion( simbEspeciales.EPSILON,this.EdoInicial ) );
            e.transiciones.add( new Transicion( simbEspeciales.EPSILON,e2 ) );
            e.EdoAcept = false;

        }

        e1.transiciones.add( new Transicion( simbEspeciales.EPSILON,this.EdoInicial ) );
        
        this.EdoInicial = e1;
        this.EdosAcept.clear();
        this.EdosAcept.add(e2);
        this.Estados.add(e1);
        this.Estados.add(e2);

        return this;

    }

    //Cerradura de Kleene (*)
    AFN cerrKleene(){

        Estado e1,e2;
        e1 = new Estado();
        e2 = new Estado();

        for( Estado e : this.EdosAcept ){
            
            e.transiciones.add( new Transicion( simbEspeciales.EPSILON,this.EdoInicial ) );
            e.transiciones.add( new Transicion( simbEspeciales.EPSILON,e2 ) );
            e.EdoAcept = false;

        }

        e1.transiciones.add( new Transicion( simbEspeciales.EPSILON,this.EdoInicial ) );
        
        this.EdoInicial = e1;
        this.EdosAcept.clear();
        this.EdosAcept.add(e2);
        this.Estados.add(e1);
        this.Estados.add(e2);

        e1.transiciones.add( new Transicion( simbEspeciales.EPSILON,e2 ) );

        return this;

    }

    //Operación opcional (?)
    AFN opcional(){

        Estado e1,e2;
        e1 = new Estado();
        e2 = new Estado();

        for( Estado e : this.EdosAcept ){
            
            e.transiciones.add( new Transicion( simbEspeciales.EPSILON,e2 ) );
            e.EdoAcept = false;

        }

        e1.transiciones.add( new Transicion( simbEspeciales.EPSILON,this.EdoInicial ) );
        
        this.EdoInicial = e1;
        this.EdosAcept.clear();
        this.EdosAcept.add(e2);
        this.Estados.add(e1);
        this.Estados.add(e2);

        e1.transiciones.add( new Transicion( simbEspeciales.EPSILON,e2 ) );

        return this;

    }

    //Cerradura de épsilon
    ArrayList<Estado> CerraduraEpsilon( Estado e ){

        ArrayList<Estado> C = new ArrayList<Estado>();  //Conjunto resultado
        Stack<Estado> P = new Stack<Estado>();          //Pila
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

    ArrayList<Estado> CerraduraEpsilon( ArrayList<Estado> E ){

        ArrayList<Estado> R = new ArrayList<Estado>();

        R.clear();

        for ( Estado e : E )
            //R.union(  CerraduraEpsilon( e ) );
            R = new ArrayList<Estado>( R ); //Hacer la unión

        return R;

    }

    //Mover
    ArrayList<Estado> Mover( Estado e,char c ){

        ArrayList<Estado> R = new ArrayList<Estado>();
        R.clear();

            for( Transicion t : e.transiciones )
                if( t.simboloInf <= c && t.simboloSup >= c )
                    R.add( t.EdoDestino );

        return R;

    }
    
    ArrayList<Estado> Mover( ArrayList<Estado> E,char c ){

        ArrayList<Estado> R = new ArrayList<Estado>();
        R.clear();

        for ( Estado e : E )
            for( Transicion t : e.transiciones )
                if( t.simboloInf <= c && t.simboloSup >= c )
                    R.add( t.EdoDestino );

        return R;

    }

    //IrA
    ArrayList<Estado> IrA( Estado e,char c ){

        return CerraduraEpsilon( Mover( e,c ) );

    }

    ArrayList<Estado> IrA( ArrayList<Estado> E,char c ){

        return CerraduraEpsilon( Mover( E,c ) );

    }

    public ArrayList<Estado> getEstados() {
        return Estados;
    }

    public void setEstados(ArrayList<Estado> estados) {
        Estados = estados;
    }

    public Estado getEdoInicial() {
        return EdoInicial;
    }

    public void setEdoInicial(Estado edoInicial) {
        EdoInicial = edoInicial;
    }

    public ArrayList<Estado> getEdosAcept() {
        return EdosAcept;
    }

    public void setEdosAcept(ArrayList<Estado> edosAcept) {
        EdosAcept = edosAcept;
    }

}
