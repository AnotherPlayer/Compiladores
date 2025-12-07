package BackEnd;

import java.util.*;

/**
 * Construcción de autómata de ítems LR(0) y tabla acción/ir_a
 * siguiendo el pseudocódigo de los apuntes (Cerradura, Mover, IrA).
 */
public class LR0 {

    public static class Item {
        String lhs;
        List<String> rhs;
        int punto;
        Item(String lhs, List<String> rhs, int punto) {
            this.lhs = lhs; this.rhs = rhs; this.punto = punto;
        }
        Item avanzar() { return new Item(lhs, rhs, punto+1); }
        boolean completo() { return punto >= rhs.size(); }
        String simboloDespuesPunto() { return completo() ? null : rhs.get(punto); }
        @Override public boolean equals(Object o){
            if(!(o instanceof Item)) return false;
            Item i=(Item)o;
            return lhs.equals(i.lhs)&&rhs.equals(i.rhs)&&punto==i.punto;
        }
        @Override public int hashCode(){ return Objects.hash(lhs,rhs,punto);}
        @Override public String toString(){
            StringBuilder sb=new StringBuilder(lhs+" -> ");
            for(int i=0;i<=rhs.size();i++){
                if(i==punto) sb.append("• ");
                if(i<rhs.size()) sb.append(rhs.get(i)).append(" ");
            }
            if(punto==rhs.size()) sb.append("•");
            return sb.toString().trim();
        }
    }

    public static class EstadoItems {
        Set<Item> items = new LinkedHashSet<>();
    }

    public static class TablaLR0 {
        public Map<Integer, Map<String, String>> accion = new LinkedHashMap<>(); // terminal -> shift sX / rY / acc
        public Map<Integer, Map<String, Integer>> irA = new LinkedHashMap<>();   // no terminal -> estado
        public List<EstadoItems> estados = new ArrayList<>();
    }

    private Gramatica g;
    private List<LadoIzq> producciones; // incluye la augmentada S'->S

    public TablaLR0 construir(Gramatica gr) {
        this.g = gr;
        producciones = new ArrayList<>();
        // Gramática aumentada: S' -> S
        SimbolG s0 = gr.SimbIni;
        LadoIzq aug = new LadoIzq(new SimbolG(s0.NombSimb+"'", -1, false));
        aug.AgregarSimbolo(s0);
        producciones.add(aug);
        producciones.addAll(gr.Reglas);

        TablaLR0 tabla = new TablaLR0();
        EstadoItems I0 = cerradura(Collections.singleton(new Item(aug.SimbIzq.NombSimb, rhs(aug),0)));
        List<EstadoItems> C = new ArrayList<>();
        C.add(I0);
        tabla.estados.add(I0);

        Queue<Integer> cola = new ArrayDeque<>();
        cola.add(0);
        while(!cola.isEmpty()){
            int i = cola.poll();
            EstadoItems I = tabla.estados.get(i);
            Set<String> simbolos = simbolosDespuesPunto(I);
            for(String X : simbolos){
                EstadoItems gotoI = irA(I, X);
                int idxEstado = indexOf(tabla.estados, gotoI);
                if(idxEstado == -1){
                    tabla.estados.add(gotoI);
                    idxEstado = tabla.estados.size()-1;
                    cola.add(idxEstado);
                }
                if(esTerminal(X)){
                    accion(tabla, i).put(X, "s"+idxEstado);
                }else{
                    irA(tabla, i).put(X, idxEstado);
                }
            }
        }
        // Reducciones y aceptar
        for(int i=0;i<tabla.estados.size();i++){
            for(Item it : tabla.estados.get(i).items){
                if(it.completo()){
                    if(it.lhs.equals(aug.SimbIzq.NombSimb)) {
                        accion(tabla, i).put(Gramatica.FIN_CADENA, "acc");
                    } else {
                        int idxProd = indexProduccion(it.lhs, it.rhs);
                        for(SimbolG t : g.Vt){
                            accion(tabla, i).put(t.NombSimb, "r"+idxProd);
                        }
                        accion(tabla, i).put(Gramatica.FIN_CADENA, "r"+idxProd);
                    }
                }
            }
        }
        return tabla;
    }

    private EstadoItems cerradura(Set<Item> I){
        EstadoItems R = new EstadoItems();
        R.items.addAll(I);
        boolean cambio=true;
        while(cambio){
            cambio=false;
            List<Item> nuevos = new ArrayList<>();
            for(Item it : R.items){
                String B = it.simboloDespuesPunto();
                if(B!=null && esNoTerminal(B)){
                    for(LadoIzq p : producciones){
                        if(p.SimbIzq.NombSimb.equals(B)){
                            Item nuevo = new Item(p.SimbIzq.NombSimb, rhs(p),0);
                            if(!R.items.contains(nuevo) && !nuevos.contains(nuevo)){
                                nuevos.add(nuevo);
                                cambio=true;
                            }
                        }
                    }
                }
            }
            R.items.addAll(nuevos);
        }
        return R;
    }

    private EstadoItems irA(EstadoItems I, String X){
        Set<Item> mov = new LinkedHashSet<>();
        for(Item it : I.items){
            if(X.equals(it.simboloDespuesPunto())){
                mov.add(it.avanzar());
            }
        }
        return cerradura(mov);
    }

    private Set<String> simbolosDespuesPunto(EstadoItems I){
        Set<String> s = new LinkedHashSet<>();
        for(Item it : I.items){
            String x = it.simboloDespuesPunto();
            if(x!=null) s.add(x);
        }
        return s;
    }

    private boolean esTerminal(String s){
        for(SimbolG t:g.Vt) if(t.NombSimb.equals(s)) return true;
        return false;
    }
    private boolean esNoTerminal(String s){
        for(SimbolG t:g.Vn) if(t.NombSimb.equals(s)) return true;
        // también la augmentada
        for(LadoIzq p:producciones) if(p.SimbIzq.NombSimb.equals(s) && !esTerminal(s)) return true;
        return false;
    }

    private List<String> rhs(LadoIzq p){
        List<String> r = new ArrayList<>();
        for(SimbolG s: p.LadoDerecho) r.add(s.NombSimb);
        return r;
    }

    private int indexProduccion(String lhs, List<String> rhs){
        for(int i=0;i<producciones.size();i++){
            LadoIzq p = producciones.get(i);
            if(p.SimbIzq.NombSimb.equals(lhs) && rhs(p).equals(rhs)) return i;
        }
        return -1;
    }

    private List<String> rhs(LadoIzq p){
        List<String> r=new ArrayList<>();
        for(SimbolG s: p.LadoDerecho) r.add(s.NombSimb);
        return r;
    }

    private int indexOf(List<EstadoItems> estados, EstadoItems e){
        for(int i=0;i<estados.size();i++){
            if(estadosIguales(estados.get(i), e)) return i;
        }
        return -1;
    }

    private boolean estadosIguales(EstadoItems a, EstadoItems b){
        return a.items.equals(b.items);
    }

    private Map<String,String> accion(TablaLR0 t, int estado){
        return t.accion.computeIfAbsent(estado,k->new LinkedHashMap<>());
    }
    private Map<String,Integer> irA(TablaLR0 t, int estado){
        return t.irA.computeIfAbsent(estado,k->new LinkedHashMap<>());
    }
}
