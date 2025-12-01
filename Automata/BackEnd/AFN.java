//1° parcial

import java.util.ArrayList;
import java.util.Stack;

// Clase AFN según pseudocódigo líneas 39-226
public class AFN {

    private ArrayList<Estado> Estados;
    private Estado EdoInicial;
    private ArrayList<Character> alfabeto;  // Conjunto<char> en pseudocódigo
    private ArrayList<Estado> EdosAcept;

    AFN(){
        Estados = new ArrayList<Estado>();
        Estados.clear();

        EdoInicial = null;

        alfabeto = new ArrayList<Character>();
        alfabeto.clear();

        EdosAcept = new ArrayList<Estado>();
        EdosAcept.clear();
    }

    // AFN básico - líneas 60-77
    AFN doBasic(char c){
        Estado e1, e2;
        e1 = new Estado();
        e2 = new Estado();

        this.Estados.add(e1);
        this.Estados.add(e2);

        this.EdoInicial = e1;

        e1.Transiciones.add(new Transicion(c, e2));
        e2.EdoAcept = true;

        this.alfabeto.add(c);
        this.EdosAcept.add(e2);

        return this;
    }

    // AFN básico con rango - líneas 79-99
    AFN doBasic(char c1, char c2){
        Estado e1, e2;
        e1 = new Estado();
        e2 = new Estado();

        this.Estados.add(e1);
        this.Estados.add(e2);

        this.EdoInicial = e1;

        e1.Transiciones.add(new Transicion(c1, c2, e2));
        e2.EdoAcept = true;

        this.EdosAcept.add(e2);

        for(int i = c1; i <= c2; i++)
            this.alfabeto.add((char)i);

        return this;
    }

    // Unión entre "this" y F2 - líneas 101-128
    AFN AFN_union(AFN F2){
        Estado e1, e2;
        e1 = new Estado();
        e2 = new Estado();

        e1.Transiciones.add(new Transicion(simbEspeciales.EPSILON, this.EdoInicial));
        e1.Transiciones.add(new Transicion(simbEspeciales.EPSILON, F2.EdoInicial));

        for(Estado e : this.EdosAcept){
            e.Transiciones.add(new Transicion(simbEspeciales.EPSILON, e2));
            e.EdoAcept = false;
        }

        for(Estado e : F2.EdosAcept){
            e.Transiciones.add(new Transicion(simbEspeciales.EPSILON, e2));
            e.EdoAcept = false;
        }

        e2.EdoAcept = true;

        this.EdoInicial = e1;
        this.Estados.addAll(F2.Estados);

        this.Estados.add(e1);
        this.Estados.add(e2);

        this.EdosAcept.clear();
        this.EdosAcept.add(e2);

        // Union de alfabetos
        for(Character c : F2.alfabeto){
            if(!this.alfabeto.contains(c))
                this.alfabeto.add(c);
        }

        return this;
    }

    // Concatenación entre "this" y F2 - líneas 131-146
    AFN AFN_join(AFN F2){
        for(Estado e : this.EdosAcept){
            for(Transicion t : F2.EdoInicial.Transiciones){
                e.Transiciones.add(new Transicion(t.SimboloInf, t.SimboloSup, t.EdoDestino));
            }
            e.EdoAcept = false;
        }

        this.EdosAcept.clear();
        this.EdosAcept.addAll(F2.EdosAcept);

        // Union de alfabetos
        for(Character c : F2.alfabeto){
            if(!this.alfabeto.contains(c))
                this.alfabeto.add(c);
        }

        F2.Estados.remove(F2.EdoInicial);
        this.Estados.addAll(F2.Estados);

        return this;
    }

    // Cerradura positiva de "this" - líneas 149-171
    AFN AFN_cerrPos(){
        Estado e1, e2;
        e1 = new Estado();
        e2 = new Estado();

        for(Estado e : this.EdosAcept){
            e.Transiciones.add(new Transicion(simbEspeciales.EPSILON, this.EdoInicial));
            e.Transiciones.add(new Transicion(simbEspeciales.EPSILON, e2));
            e.EdoAcept = false;
        }

        e1.Transiciones.add(new Transicion(simbEspeciales.EPSILON, this.EdoInicial));

        this.EdoInicial = e1;
        this.EdosAcept.clear();
        this.EdosAcept.add(e2);
        this.Estados.add(e1);
        this.Estados.add(e2);

        e2.EdoAcept = true;

        return this;
    }

    // Cerradura de Kleene de "this" - líneas 174-198
    AFN AFN_cerrKleene(){
        Estado e1, e2;
        e1 = new Estado();
        e2 = new Estado();

        for(Estado e : this.EdosAcept){
            e.Transiciones.add(new Transicion(simbEspeciales.EPSILON, this.EdoInicial));
            e.Transiciones.add(new Transicion(simbEspeciales.EPSILON, e2));
            e.EdoAcept = false;
        }

        e1.Transiciones.add(new Transicion(simbEspeciales.EPSILON, this.EdoInicial));

        this.EdoInicial = e1;
        this.EdosAcept.clear();
        this.EdosAcept.add(e2);
        this.Estados.add(e1);
        this.Estados.add(e2);

        e1.Transiciones.add(new Transicion(simbEspeciales.EPSILON, e2));

        e2.EdoAcept = true;

        return this;
    }

    // Operación opcional de "this" - líneas 201-224
    AFN AFN_opcional(){
        Estado e1, e2;
        e1 = new Estado();
        e2 = new Estado();

        for(Estado e : this.EdosAcept){
            e.Transiciones.add(new Transicion(simbEspeciales.EPSILON, e2));
            e.EdoAcept = false;
        }

        e1.Transiciones.add(new Transicion(simbEspeciales.EPSILON, this.EdoInicial));

        this.EdoInicial = e1;
        this.EdosAcept.clear();
        this.EdosAcept.add(e2);
        this.Estados.add(e1);
        this.Estados.add(e2);

        e1.Transiciones.add(new Transicion(simbEspeciales.EPSILON, e2));

        e2.EdoAcept = true;

        return this;
    }

    // Cerradura épsilon - líneas 230-257
    ArrayList<Estado> CerraduraEpsilon(Estado e){
        ArrayList<Estado> C = new ArrayList<Estado>();  // Conjunto resultado
        Stack<Estado> P = new Stack<Estado>();          // Pila
        Estado e2;

        C.clear();  // C --> Conteo de épsilon
        P.clear();  // P --> Pila
        P.push(e);

        while(!P.empty()){
            e2 = P.pop();

            if(!C.contains(e2)){
                C.add(e2);

                for(Transicion t : e2.Transiciones){
                    if(t.SimboloInf == simbEspeciales.EPSILON && t.SimboloSup == simbEspeciales.EPSILON)
                        P.push(t.EdoDestino);
                }
            }
        }

        return C;
    }

    // Cerradura épsilon para conjunto - líneas 259-270
    ArrayList<Estado> CerraduraEpsilon(ArrayList<Estado> E){
        ArrayList<Estado> R = new ArrayList<Estado>();

        R.clear();

        for(Estado e : E){
            ArrayList<Estado> temp = CerraduraEpsilon(e);
            for(Estado est : temp){
                if(!R.contains(est))
                    R.add(est);
            }
        }

        return R;
    }

    // Mover - líneas 275-287
    ArrayList<Estado> Mover(Estado e, char c){
        ArrayList<Estado> R = new ArrayList<Estado>();
        R.clear();

        for(Transicion t : e.Transiciones){
            if(t.SimboloInf <= c && c <= t.SimboloSup)
                R.add(t.EdoDestino);
        }

        return R;
    }

    // Mover para conjunto - líneas 291-304
    ArrayList<Estado> Mover(ArrayList<Estado> E, char c){
        ArrayList<Estado> R = new ArrayList<Estado>();
        R.clear();

        for(Estado e : E){
            for(Transicion t : e.Transiciones){
                if(t.SimboloInf <= c && c <= t.SimboloSup){
                    if(!R.contains(t.EdoDestino))
                        R.add(t.EdoDestino);
                }
            }
        }

        return R;
    }

    // IrA - líneas 309-313
    ArrayList<Estado> IrA(Estado e, char c){
        return CerraduraEpsilon(Mover(e, c));
    }

    // IrA para conjunto - líneas 315-319
    ArrayList<Estado> IrA(ArrayList<Estado> E, char c){
        return CerraduraEpsilon(Mover(E, c));
    }

    // Getters y setters
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

    public ArrayList<Character> getAlfabeto() {
        return alfabeto;
    }

    public void setAlfabeto(ArrayList<Character> alfabeto) {
        this.alfabeto = alfabeto;
    }
}