package analisisintactico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;

public class PanelMostrarAFN extends JPanel {
    private static final Color COLOR_AZUL_ACCENT = new Color(100, 149, 237);
    private static final Color COLOR_TEXTO_OSCURO = new Color(25, 50, 95);
    
    private final List<String> availableAutomataNames;

    public PanelMostrarAFN(List<String> currentAfnNames) {
        this.availableAutomataNames = (currentAfnNames != null) ? currentAfnNames : new ArrayList<>();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        setBackground(Color.WHITE);

        add(tituloPanel("Mostrar AFN"));
        add(Box.createVerticalStrut(20));

        if (availableAutomataNames.isEmpty()) {
            add(crearEtiqueta("No hay AFN disponibles para mostrar."));
        } else {
            add(crearEtiqueta("Seleccione el AFN a mostrar:"));
            JComboBox<String> combo = comboAut();
            add(combo);
            add(Box.createVerticalStrut(20));

            JButton btnMostrar = botonAzul("Mostrar Detalles/Grafo");
            btnMostrar.addActionListener((ActionEvent e) -> {
                String auto = (String) combo.getSelectedItem();
                
                JOptionPane.showMessageDialog(this, 
                    "Mostrando detalles y grafo del AFN: '" + auto + "'.\n" +
                    "(Integración de showAutomataDetails/mostrarGrafoAFN pendiente)", 
                    "Visualización", JOptionPane.INFORMATION_MESSAGE);
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
