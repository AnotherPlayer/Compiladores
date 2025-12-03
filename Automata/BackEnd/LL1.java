//2° parcial
// Construcción de tabla LL(1) siguiendo el pseudocódigo de los apuntes.

package BackEnd;

import java.util.*;

public class LL1 {

    /** Representa una tabla LL(1) como mapa [NoTerminal][Terminal] = índice de regla. */
    public static class TablaLL1 {
        public Map<String, Map<String, Integer>> tabla = new LinkedHashMap<>();
        public Set<String> terminales = new LinkedHashSet<>();
        public Set<String> noTerminales = new LinkedHashSet<>();
    }

    /**
     * Construye la tabla LL(1) para una gramática ya parseada.
     * Regla i = gramatica.Reglas.get(i)
     */
    public TablaLL1 construir(Gramatica g) {
        g.limpiarMemorias();
        TablaLL1 t = new TablaLL1();
        for (SimbolG nt : g.Vn) {
            t.noTerminales.add(nt.NombSimb);
            t.tabla.put(nt.NombSimb, new LinkedHashMap<>());
        }
        for (SimbolG term : g.Vt) {
            t.terminales.add(term.NombSimb);
        }
        t.terminales.add(Gramatica.FIN_CADENA);

        for (int i = 0; i < g.Reglas.size(); i++) {
            LadoIzq regla = g.Reglas.get(i);
            Set<String> firstAlpha = g.firstSecuencia(regla.LadoDerecho);
            for (String a : firstAlpha) {
                if (a.equals(Gramatica.EPS)) continue;
                asignar(t, regla.SimbIzq.NombSimb, a, i);
            }
            if (firstAlpha.contains(Gramatica.EPS)) {
                Set<String> followA = g.follow(regla.SimbIzq.NombSimb);
                for (String b : followA) {
                    asignar(t, regla.SimbIzq.NombSimb, b, i);
                }
            }
        }
        return t;
    }

    private void asignar(TablaLL1 t, String A, String terminal, int regla) {
        Map<String, Integer> fila = t.tabla.get(A);
        if (fila == null) {
            fila = new LinkedHashMap<>();
            t.tabla.put(A, fila);
        }
        fila.put(terminal, regla);
    }
}
