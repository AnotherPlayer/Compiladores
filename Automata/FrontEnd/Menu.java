import com.automata.frontend.AutomataMenuGUI;

//---------Equipo 9---------
//Presentación del proyecto --> Jueves 16 de Octubre (4° Equipo)
/*
 * Interfaz para el Menu de automaatas.
 * Columnas:
 * 1.- Crear Automata
 *  a. AFN basico ( 1 char )
 *  b. AFD basico ( 2 char )
 *  c. Union
 *  d. Concatenacion
 *  e. Cerradura positiva
 *  f. Cerradura Kleene
 *  g. Cerradura opcional
 * 2.- Mostrar Automata
 *  a. Mostrar AFN
 *  b. Mostrar AFD ¿Realizar aqui la conversión?
 * 
*/

public class Menu{
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new AutomataMenuGUI().setVisible(true);
        });
    }

}
