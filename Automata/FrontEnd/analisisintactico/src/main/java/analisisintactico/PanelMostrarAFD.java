package analisisintactico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;

import BackEnd.ConexionBaF;

public class PanelMostrarAFD extends JPanel {
    private static final Color COLOR_AZUL_ACCENT = new Color(100, 149, 237);
    private static final Color COLOR_TEXTO_OSCURO = new Color(25, 50, 95);
    
    private final List<String> availableAutomataNames;

    public PanelMostrarAFD(List<String> currentAfdNames) {
        this.availableAutomataNames = (currentAfdNames != null) ? currentAfdNames : new ArrayList<>();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        setBackground(Color.WHITE);

        add(tituloPanel("Mostrar AFD"));
        add(Box.createVerticalStrut(20));

        if (availableAutomataNames.isEmpty()) {
            add(crearEtiqueta("No hay AFD disponibles para mostrar."));
        } else {
            add(crearEtiqueta("Seleccione el AFD a mostrar:"));
            JComboBox<String> combo = comboAut();
            add(combo);
            add(Box.createVerticalStrut(20));

            JButton btnMostrar = botonAzul("Mostrar Detalles/Grafo");
            btnMostrar.addActionListener((ActionEvent e) -> {
                String auto = (String) combo.getSelectedItem();
                
                ConexionBaF ctrl = AnalisisSintacticoGUI.getControlador();
                BackEnd.AFD afd = ctrl.obtenerAFD(auto);
                if(afd == null){
                    JOptionPane.showMessageDialog(this, "No se encontró AFD " + auto, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                BackEnd.AFD.TransitionTable tabla = afd.buildTransitionTable();
                String[] cols = tabla.headers.toArray(new String[0]);
                String[][] data = tabla.rows.toArray(new String[0][]);
                JTable t = new JTable(data, cols);
                t.setEnabled(false);
                JScrollPane sp = new JScrollPane(t);
                sp.setPreferredSize(new Dimension(640, 360));
                JOptionPane.showMessageDialog(this, sp, "AFD: " + auto, JOptionPane.INFORMATION_MESSAGE);
            });
            add(btnMostrar);
        }
    }

    // --- Helpers UI ---
    private JLabel tituloPanel(String titulo) {
        JLabel l = new JLabel(titulo);
        l.setFont(new Font("Segoe UI", Font.BOLD, 28)); 
        l.setForeground(COLOR_TEXTO_OSCURO);
        l.setAlignmentX(CENTER_ALIGNMENT);
        return l;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        l.setForeground(COLOR_TEXTO_OSCURO);
        l.setAlignmentX(CENTER_ALIGNMENT);
        return l;
    }

    private JComboBox<String> comboAut() {
        JComboBox<String> combo = new JComboBox<>(availableAutomataNames.toArray(new String[0]));
        combo.setMaximumSize(new Dimension(280, 35));
        combo.setPreferredSize(new Dimension(280, 35));
        combo.setAlignmentX(CENTER_ALIGNMENT);
        return combo;
    }
    
    private JButton botonAzul(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(COLOR_AZUL_ACCENT);
        b.setForeground(Color.WHITE);
        b.setAlignmentX(CENTER_ALIGNMENT);
        return b;
    }
}
