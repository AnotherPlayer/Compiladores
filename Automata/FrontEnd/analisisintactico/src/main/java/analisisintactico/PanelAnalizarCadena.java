package analisisintactico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;

public class PanelAnalizarCadena extends JPanel { 

    private static final Color COLOR_AZUL_ACCENT = new Color(100, 149, 237);
    private static final Color COLOR_TEXTO_OSCURO = new Color(25, 50, 95);
    private final List<String> availableAfdNames;

    public PanelAnalizarCadena(List<String> currentAfdNames) {
        this.availableAfdNames = (currentAfdNames != null) ? currentAfdNames : new ArrayList<>();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        setBackground(Color.WHITE);

        add(tituloPanel("Analizar Cadena con AFD"));
        add(Box.createVerticalStrut(20));

        if (availableAfdNames.isEmpty()) {
            add(crearEtiqueta("No hay AFD disponibles para analizar cadenas."));
        } else {
            add(crearEtiqueta("Seleccione el AFD:"));
            JComboBox<String> comboAFD = comboAut(availableAfdNames);
            add(comboAFD);
            add(Box.createVerticalStrut(10));

            add(crearEtiqueta("Ingrese la cadena a analizar:"));
            JTextField txtCadena = campoTexto();
            add(txtCadena);
            add(Box.createVerticalStrut(20));

            JButton btnAnalizar = botonAzul("Analizar Cadena");
            btnAnalizar.addActionListener((ActionEvent e) -> {
                String afdSeleccionado = (String) comboAFD.getSelectedItem();
                String cadena = txtCadena.getText();
                
                if (cadena.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "La cadena no puede estar vacía.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Simulación de la lógica de análisis
                // Aquí iría la llamada a AFD.analizar(cadena).
                /*boolean esAceptada = Math.random() > 0.5; // Simulación: 50% de probabilidad
                
                if (esAceptada) {
                    JOptionPane.showMessageDialog(this, 
                        "La cadena '" + cadena + "' fue **ACEPTADA** por el AFD: '" + afdSeleccionado + "'.", 
                        "Resultado: Aceptada", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "La cadena '" + cadena + "' fue **RECHAZADA** por el AFD: '" + afdSeleccionado + "'.", 
                        "Resultado: Rechazada", JOptionPane.ERROR_MESSAGE);
                }*/
            });
            add(btnAnalizar);
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

    private JComboBox<String> comboAut(List<String> names) {
        JComboBox<String> combo = new JComboBox<>(names.toArray(new String[0]));
        combo.setMaximumSize(new Dimension(280, 35));
        combo.setPreferredSize(new Dimension(280, 35));
        combo.setAlignmentX(CENTER_ALIGNMENT);
        return combo;
    }
    
    private JTextField campoTexto() {
        JTextField txt = new JTextField(15);
        txt.setMaximumSize(new Dimension(280, 35));
        txt.setPreferredSize(new Dimension(280, 35));
        txt.setAlignmentX(CENTER_ALIGNMENT);
        return txt;
    }
    
    private JButton botonAzul(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(COLOR_AZUL_ACCENT);
        b.setForeground(Color.WHITE);
        b.setAlignmentX(CENTER_ALIGNMENT);
        return b;
    }
}