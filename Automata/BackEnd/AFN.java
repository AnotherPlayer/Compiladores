package BackEnd;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Collections;
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

    public AFN doEpsilon(){

        Estados.clear();
        EdosAcept.clear();
        alfabeto.clear();

        Estado e1 = new Estado();
        Estado e2 = new Estado();

        Estados.add(e1);
        Estados.add(e2);

        EdoInicial = e1;

        e1.addTransicion( new Transicion(SimbEspeciales.EPSILON,e2) );
        e2.EdoAcept = true;

        EdosAcept.add(e2);

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
    
    public static AFN fromRegex(String regex){
        return RegexAFNBuilder.fromRegex(regex);
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

    public void asignarToken(int token){
        for(Estado e : this.EdosAcept){
            e.setToken(token);
        }
    }

    public AFD toAFD(){
        if(this.EdoInicial == null){
            throw new IllegalStateException("El AFN no tiene estado inicial definido.");
        }

        ArrayList<Character> simbolos = this.alfabeto.asList();

        Map<String,Integer> subconjuntoIndice = new HashMap<String,Integer>();
        ArrayList<ArrayList<Estado>> subconjuntos = new ArrayList<ArrayList<Estado>>();
        ArrayDeque<ArrayList<Estado>> cola = new ArrayDeque<ArrayList<Estado>>();
        ArrayList<EdoAFD> estadosAFD = new ArrayList<EdoAFD>();

        ArrayList<Estado> inicial = CerraduraEpsilon(this.EdoInicial);
        ordenarEstados(inicial);
        String claveInicial = claveSubconjunto(inicial);
        subconjuntoIndice.put(claveInicial, 0);
        subconjuntos.add(inicial);
        cola.add(inicial);

        while(!cola.isEmpty()){
            ArrayList<Estado> actual = cola.poll();
            String claveActual = claveSubconjunto(actual);
            int indiceActual = subconjuntoIndice.get(claveActual);

            while(estadosAFD.size() <= indiceActual){
                estadosAFD.add(null);
            }
            EdoAFD edoActual = estadosAFD.get(indiceActual);
            if(edoActual == null){
                edoActual = new EdoAFD();
                edoActual.Id = indiceActual;
                asignarAceptacion(edoActual, actual);
                estadosAFD.set(indiceActual, edoActual);
            }

            for(char simbolo : simbolos){
                ArrayList<Estado> destino = IrA(actual, simbolo);
                ordenarEstados(destino);
                if(destino.isEmpty()){
                    edoActual.TransAFD[simbolo & 0xFF] = -1;
                    continue;
                }

                String claveDestino = claveSubconjunto(destino);
                Integer indiceDestino = subconjuntoIndice.get(claveDestino);
                if(indiceDestino == null){
                    indiceDestino = subconjuntoIndice.size();
                    subconjuntoIndice.put(claveDestino, indiceDestino);
                    subconjuntos.add(destino);
                    cola.add(destino);

                    while(estadosAFD.size() <= indiceDestino){
                        estadosAFD.add(null);
                    }
                    EdoAFD nuevoEdo = new EdoAFD();
                    nuevoEdo.Id = indiceDestino;
                    asignarAceptacion(nuevoEdo, destino);
                    estadosAFD.set(indiceDestino, nuevoEdo);
                }
                edoActual.TransAFD[simbolo & 0xFF] = indiceDestino;
            }
        }

        AFD resultado = new AFD(estadosAFD.size(), this.alfabeto);
        for(int i=0;i<estadosAFD.size();i++){
            EdoAFD edo = estadosAFD.get(i);
            if(edo == null){
                edo = new EdoAFD();
                edo.Id = i;
                estadosAFD.set(i, edo);
            }
            resultado.setEstado(i, edo);
        }
        return resultado;
    }

    //Operación unión
    public AFN union( AFN F2 ){

        if(F2 == null){
            return this;
        }

        Estado nuevoInicio = new Estado();
        
        nuevoInicio.addTransicion( new Transicion( SimbEspeciales.EPSILON,this.EdoInicial ) );
        nuevoInicio.addTransicion( new Transicion( SimbEspeciales.EPSILON,F2.EdoInicial ) );

        this.EdoInicial = nuevoInicio;

        addUniqueState(this.Estados, nuevoInicio);
        addUniqueStateList(this.Estados, F2.Estados);

        addUniqueStateList(this.EdosAcept, F2.EdosAcept);

        this.alfabeto.union( F2.alfabeto );
        
        return this;

    }

    //Operación concatenación
    public AFN join( AFN F2 ){

        for ( Estado e : this.EdosAcept ){
            e.addTransicion( new Transicion( SimbEspeciales.EPSILON,F2.EdoInicial ) );
            e.EdoAcept = false;
            e.setToken(-1);
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
        int token = minimoTokenAceptacion();

        nuevoInicio.addTransicion( new Transicion( SimbEspeciales.EPSILON,this.EdoInicial ) );
        for( Estado e : this.EdosAcept ){
            
            e.addTransicion( new Transicion( SimbEspeciales.EPSILON,this.EdoInicial ) );
            e.addTransicion( new Transicion( SimbEspeciales.EPSILON,nuevoFin ) );
            e.EdoAcept = false;
            e.setToken(-1);

        }

        nuevoFin.EdoAcept = true;
        nuevoFin.setToken(token);
        
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
        int token = minimoTokenAceptacion();

        nuevoInicio.addTransicion( new Transicion( SimbEspeciales.EPSILON,this.EdoInicial ) );
        nuevoInicio.addTransicion( new Transicion( SimbEspeciales.EPSILON,nuevoFin ) );

        for( Estado e : this.EdosAcept ){
            
            e.addTransicion( new Transicion( SimbEspeciales.EPSILON,this.EdoInicial ) );
            e.addTransicion( new Transicion( SimbEspeciales.EPSILON,nuevoFin ) );
            e.EdoAcept = false;
            e.setToken(-1);

        }

        nuevoFin.EdoAcept = true;
        nuevoFin.setToken(token);
        
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
        int token = minimoTokenAceptacion();

        nuevoInicio.addTransicion( new Transicion( SimbEspeciales.EPSILON,this.EdoInicial ) );
        nuevoInicio.addTransicion( new Transicion( SimbEspeciales.EPSILON,nuevoFin ) );

        for( Estado e : this.EdosAcept ){
            
            e.addTransicion( new Transicion( SimbEspeciales.EPSILON,nuevoFin ) );
            e.EdoAcept = false;
            e.setToken(-1);

        }

        nuevoFin.EdoAcept = true;
        nuevoFin.setToken(token);
        
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
            sb.append(" (Tokens: ");
            for(int i=0;i<EdosAcept.size();i++){
                int token = EdosAcept.get(i).getToken();
                sb.append(token >= 0 ? Integer.toString(token) : "-");
                if(i < EdosAcept.size() -1){
                    sb.append(", ");
                }
            }
            sb.append(")");
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

    private void ordenarEstados(ArrayList<Estado> estados){
        Collections.sort(estados, (a, b) -> Integer.compare(a.getId(), b.getId()));
    }

    private String claveSubconjunto(ArrayList<Estado> estados){
        if(estados.isEmpty()){
            return "vacio";
        }
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<estados.size();i++){
            if(i>0){
                sb.append(',');
            }
            sb.append(estados.get(i).getId());
        }
        return sb.toString();
    }

    private void asignarAceptacion(EdoAFD estadoDet, ArrayList<Estado> subconjunto){
        int token = Integer.MAX_VALUE;
        boolean esAceptacion = false;
        for(Estado e : subconjunto){
            if(e.isAceptacion()){
                esAceptacion = true;
                if(e.getToken() >= 0 && e.getToken() < token){
                    token = e.getToken();
                }
            }
        }
        estadoDet.esAceptacion = esAceptacion;
        estadoDet.token = esAceptacion ? (token == Integer.MAX_VALUE ? -1 : token) : -1;
    }

    private int minimoTokenAceptacion(){
        int token = Integer.MAX_VALUE;
        for(Estado e : this.EdosAcept){
            if(e.getToken() >= 0 && e.getToken() < token){
                token = e.getToken();
            }
        }
        return token == Integer.MAX_VALUE ? -1 : token;
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
