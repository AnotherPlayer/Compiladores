package BackEnd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class AFN {

    private final ArrayList<Estado> Estados;
    private Estado EdoInicial;
    private final Alfabeto alfabeto;
    private final ArrayList<Estado> EdosAcept;
    
    public AFN(){

        Estados = new ArrayList<Estado>();
        EdoInicial = null;

        alfabeto = new Alfabeto();

        EdosAcept = new ArrayList<Estado>();

    }

    //Automáta básico
    public AFN doBasic( char c ){

        Estados.clear();
        EdosAcept.clear();
        alfabeto.clear();

        Estado e1 = new Estado();
        Estado e2 = new Estado();

        Estados.add(e1);
        Estados.add(e2);
        
        EdoInicial = e1;

        e1.addTransicion( new Transicion(c,e2) );
        e2.EdoAcept = true;

        EdosAcept.add(e2);
        alfabeto.add(c);
        
        return this;

    }

    public AFN doBasic( char c1,char c2 ){

        Estados.clear();
        EdosAcept.clear();
        alfabeto.clear();

        Estado e1 = new Estado();
        Estado e2 = new Estado();

        Estados.add(e1);
        Estados.add(e2);
        
        EdoInicial = e1;

        e1.addTransicion( new Transicion(c1,c2,e2) );
        e2.EdoAcept = true;

        EdosAcept.add(e2);

        for( int i=c1 ; i<=c2 ; i++ )
            alfabeto.add((char)i);
        
        return this;

    }

    public static AFN fromString(String input){
        if(input == null || input.isEmpty()){
            throw new IllegalArgumentException("La sentencia para el AFN no puede estar vacía.");
        }
        AFN resultado = new AFN();
        resultado.doBasic(input.charAt(0));
        for(int i=1;i<input.length();i++){
            AFN siguiente = new AFN();
            siguiente.doBasic(input.charAt(i));
            resultado.join(siguiente);
        }
        return resultado;
    }

    public AFN copia(){
        AFN copia = new AFN();
        Map<Estado, Estado> equivalencias = new HashMap<Estado, Estado>();

        // Crear nuevos estados y mapear
        for(Estado original : this.Estados){
            Estado replicado = new Estado();
            replicado.EdoAcept = original.EdoAcept;
            replicado.token = original.token;
            equivalencias.put(original, replicado);
            copia.Estados.add(replicado);
        }

        if(this.EdoInicial != null){
            copia.EdoInicial = equivalencias.get(this.EdoInicial);
        }

        copia.EdosAcept.clear();
        for(Estado acept : this.EdosAcept){
            Estado replicado = equivalencias.get(acept);
            if(replicado != null){
                replicado.EdoAcept = true;
                copia.EdosAcept.add(replicado);
            }
        }

        for(Estado original : this.Estados){
            Estado replicado = equivalencias.get(original);
            for(Transicion t : original.transiciones){
                Estado destinoReplicado = equivalencias.get(t.EdoDestino);
                if(destinoReplicado != null){
                    replicado.addTransicion(new Transicion(t.simboloInf, t.simboloSup, destinoReplicado));
                }
            }
        }

        copia.alfabeto.union(this.alfabeto);

        return copia;
    }

    public static AFN unir(AFN f1, AFN f2){
        if(f1 == null || f2 == null){
            throw new IllegalArgumentException("Los autómatas para la unión no pueden ser nulos.");
        }
        return f1.copia().union(f2.copia());
    }

    public static AFN concatenar(AFN f1, AFN f2){
        if(f1 == null || f2 == null){
            throw new IllegalArgumentException("Los autómatas para la concatenación no pueden ser nulos.");
        }
        return f1.copia().join(f2.copia());
    }

    public static AFN cerraduraPositiva(AFN f1){
        if(f1 == null){
            throw new IllegalArgumentException("El autómata para cerradura positiva no puede ser nulo.");
        }
        return f1.copia().cerrPos();
    }

    public static AFN cerraduraKleene(AFN f1){
        if(f1 == null){
            throw new IllegalArgumentException("El autómata para cerradura de Kleene no puede ser nulo.");
        }
        return f1.copia().cerrKleene();
    }

    public static AFN cerraduraOpcional(AFN f1){
        if(f1 == null){
            throw new IllegalArgumentException("El autómata para cerradura opcional no puede ser nulo.");
        }
        return f1.copia().opcional();
    }

    //Operación unión
    public AFN union( AFN F2 ){

        Estado nuevoInicio = new Estado();
        Estado nuevoFin = new Estado();
        
        nuevoInicio.addTransicion( new Transicion( SimbEspeciales.EPSILON,this.EdoInicial ) );
        nuevoInicio.addTransicion( new Transicion( SimbEspeciales.EPSILON,F2.EdoInicial ) );

        for( Estado e : this.EdosAcept ){
            e.addTransicion( new Transicion( SimbEspeciales.EPSILON,nuevoFin ) );
            e.EdoAcept = false;
        }
        for( Estado e : F2.EdosAcept ){
            e.addTransicion( new Transicion( SimbEspeciales.EPSILON,nuevoFin ) );
            e.EdoAcept = false;
        }

        nuevoFin.EdoAcept = true;

        this.EdoInicial = nuevoInicio;
        addUniqueStateList(this.Estados, F2.Estados);
        addUniqueState(this.Estados, nuevoInicio);
        addUniqueState(this.Estados, nuevoFin);

        this.EdosAcept.clear();
        this.EdosAcept.add(nuevoFin);

        this.alfabeto.union( F2.alfabeto );
        
        return this;

    }

    //Operación concatenación
    public AFN join( AFN F2 ){

        for ( Estado e : this.EdosAcept ){
            e.addTransicion( new Transicion( SimbEspeciales.EPSILON,F2.EdoInicial ) );
            e.EdoAcept = false;
        }

        addUniqueStateList(this.Estados, F2.Estados);
        this.EdosAcept.clear();
        addUniqueStateList(this.EdosAcept, F2.EdosAcept);
        this.alfabeto.union( F2.alfabeto );

        return this;

    }

    //Cerradura positiva (+)
    public AFN cerrPos(){

        Estado nuevoInicio = new Estado();
        Estado nuevoFin = new Estado();

        nuevoInicio.addTransicion( new Transicion( SimbEspeciales.EPSILON,this.EdoInicial ) );
        for( Estado e : this.EdosAcept ){
            
            e.addTransicion( new Transicion( SimbEspeciales.EPSILON,this.EdoInicial ) );
            e.addTransicion( new Transicion( SimbEspeciales.EPSILON,nuevoFin ) );
            e.EdoAcept = false;

        }

        nuevoFin.EdoAcept = true;
        
        this.EdoInicial = nuevoInicio;
        this.EdosAcept.clear();
        this.EdosAcept.add(nuevoFin);
        addUniqueState(this.Estados, nuevoInicio);
        addUniqueState(this.Estados, nuevoFin);

        return this;

    }

    //Cerradura de Kleene (*)
    public AFN cerrKleene(){

        Estado nuevoInicio = new Estado();
        Estado nuevoFin = new Estado();

        nuevoInicio.addTransicion( new Transicion( SimbEspeciales.EPSILON,this.EdoInicial ) );
        nuevoInicio.addTransicion( new Transicion( SimbEspeciales.EPSILON,nuevoFin ) );

        for( Estado e : this.EdosAcept ){
            
            e.addTransicion( new Transicion( SimbEspeciales.EPSILON,this.EdoInicial ) );
            e.addTransicion( new Transicion( SimbEspeciales.EPSILON,nuevoFin ) );
            e.EdoAcept = false;

        }

        nuevoFin.EdoAcept = true;
        
        this.EdoInicial = nuevoInicio;
        this.EdosAcept.clear();
        this.EdosAcept.add(nuevoFin);
        addUniqueState(this.Estados, nuevoInicio);
        addUniqueState(this.Estados, nuevoFin);

        return this;

    }

    //Operación opcional (?)
    public AFN opcional(){

        Estado nuevoInicio = new Estado();
        Estado nuevoFin = new Estado();

        nuevoInicio.addTransicion( new Transicion( SimbEspeciales.EPSILON,this.EdoInicial ) );
        nuevoInicio.addTransicion( new Transicion( SimbEspeciales.EPSILON,nuevoFin ) );

        for( Estado e : this.EdosAcept ){
            
            e.addTransicion( new Transicion( SimbEspeciales.EPSILON,nuevoFin ) );
            e.EdoAcept = false;

        }

        nuevoFin.EdoAcept = true;
        
        this.EdoInicial = nuevoInicio;
        this.EdosAcept.clear();
        this.EdosAcept.add(nuevoFin);
        addUniqueState(this.Estados, nuevoInicio);
        addUniqueState(this.Estados, nuevoFin);

        return this;

    }

    //Cerradura de épsilon
    public ArrayList<Estado> CerraduraEpsilon( Estado e ){

        ArrayList<Estado> C = new ArrayList<Estado>();  //Conjunto resultado
        Stack<Estado> P = new Stack<Estado>();          //Pila

        P.push(e);

        while( !P.empty() ){

            Estado actual = P.pop();

            if( !C.contains(actual) ){

                C.add(actual);

                for( Transicion t : actual.transiciones )
                    if( t.simboloInf == SimbEspeciales.EPSILON && t.simboloSup == SimbEspeciales.EPSILON )
                        P.push( t.EdoDestino );

            }
        }

        return C;

    }

    public ArrayList<Estado> CerraduraEpsilon( ArrayList<Estado> E ){

        ArrayList<Estado> R = new ArrayList<Estado>();

        for ( Estado e : E )
            addUniqueStateList(R, CerraduraEpsilon( e ));

        return R;

    }

    //Mover
    public ArrayList<Estado> Mover( Estado e,char c ){

        ArrayList<Estado> R = new ArrayList<Estado>();

        for( Transicion t : e.transiciones )
            if( t.simboloInf <= c && t.simboloSup >= c )
                addUniqueState(R, t.EdoDestino);

        return R;

    }
    
    public ArrayList<Estado> Mover( ArrayList<Estado> E,char c ){

        ArrayList<Estado> R = new ArrayList<Estado>();

        for ( Estado e : E )
            for( Transicion t : e.transiciones )
                if( t.simboloInf <= c && t.simboloSup >= c )
                    addUniqueState(R, t.EdoDestino);

        return R;

    }

    //IrA
    public ArrayList<Estado> IrA( Estado e,char c ){

        return CerraduraEpsilon( Mover( e,c ) );

    }

    public ArrayList<Estado> IrA( ArrayList<Estado> E,char c ){

        return CerraduraEpsilon( Mover( E,c ) );

    }

    public ArrayList<Estado> getEstados() {
        return Estados;
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
        EdosAcept.clear();
        addUniqueStateList(EdosAcept, edosAcept);
    }

    public Alfabeto getAlfabeto() {
        return alfabeto;
    }

    public String resumen(){
        StringBuilder sb = new StringBuilder();
        sb.append("Total de estados: ").append(Estados.size()).append('\n');
        sb.append("Estado inicial: ").append(EdoInicial != null ? EdoInicial.IdEdo : "Ninguno").append('\n');
        sb.append("Estados de aceptación: ");
        if(EdosAcept.isEmpty()){
            sb.append("Ninguno");
        }else{
            for(int i=0;i<EdosAcept.size();i++){
                sb.append(EdosAcept.get(i).IdEdo);
                if(i < EdosAcept.size() -1){
                    sb.append(", ");
                }
            }
        }
        sb.append("\nTransiciones:\n");
        for(Estado estado : Estados){
            if(estado.transiciones.isEmpty()){
                sb.append("  ").append(estado.IdEdo).append(" -- sin transiciones\n");
                continue;
            }
            for(Transicion t : estado.transiciones){
                String etiquetaInf = simboloATexto(t.simboloInf);
                String etiquetaSup = simboloATexto(t.simboloSup);
                String etiqueta = t.simboloInf == t.simboloSup ? etiquetaInf : etiquetaInf + "-" + etiquetaSup;
                sb.append("  ").append(estado.IdEdo)
                  .append(" -- [").append(etiqueta).append("] --> ")
                  .append(t.EdoDestino != null ? t.EdoDestino.IdEdo : "null")
                  .append('\n');
            }
        }
        return sb.toString();
    }

    private void addUniqueState(ArrayList<Estado> lista, Estado estado){
        if(estado != null && !lista.contains(estado)){
            lista.add(estado);
        }
    }

    private void addUniqueStateList(ArrayList<Estado> destino, ArrayList<Estado> origen){
        for(Estado estado : origen){
            addUniqueState(destino, estado);
        }
    }

    private String simboloATexto(char simbolo){
        if(simbolo == SimbEspeciales.EPSILON){
            return "ε";
        }
        if(Character.isISOControl(simbolo)){
            return "(" + (int)simbolo + ")";
        }
        return String.valueOf(simbolo);
    }

}
