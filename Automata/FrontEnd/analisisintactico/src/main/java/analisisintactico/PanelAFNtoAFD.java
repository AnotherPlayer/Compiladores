package analisisintactico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;

import BackEnd.ConexionBaF;

public class PanelAFNtoAFD extends JPanel {
    private static final Color COLOR_AZUL_ACCENT = new Color(100, 149, 237);
    private static final Color COLOR_TEXTO_OSCURO = new Color(25, 50, 95);
    
    private final List<String> availableAfnNames;
    @SuppressWarnings("unused")
    private final List<String> currentAfdNames;

    public PanelAFNtoAFD(List<String> currentAfnNames, List<String> currentAfdNames) {
        this.availableAfnNames = (currentAfnNames != null) ? currentAfnNames : new ArrayList<>();
        this.currentAfdNames = (currentAfdNames != null) ? currentAfdNames : new ArrayList<>();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        setBackground(Color.WHITE);

        add(tituloPanel("Convertir AFN → AFD"));
        add(Box.createVerticalStrut(20));

        if (availableAfnNames.isEmpty()) {
            add(crearEtiqueta("No hay AFN disponibles para convertir."));
        } else {
            add(crearEtiqueta("Seleccione el AFN a convertir:"));
            JComboBox<String> comboAFN = comboAut(availableAfnNames);
            add(comboAFN);
            add(Box.createVerticalStrut(10));

            add(crearEtiqueta("Nombre para el nuevo AFD:"));
            JTextField txtNombreAFD = campoTexto();
            add(txtNombreAFD);
            add(Box.createVerticalStrut(20));

            JButton btnConvertir = botonAzul("Convertir a AFD");
            btnConvertir.addActionListener((ActionEvent e) -> {
                String afnSeleccionado = (String) comboAFN.getSelectedItem();
                String nombreAFD = txtNombreAFD.getText().trim();

                if (nombreAFD.isEmpty()) {
                    nombreAFD = afnSeleccionado + "_AFD";
                }
                if (currentAfdNames.contains(nombreAFD)) {
                    JOptionPane.showMessageDialog(this, "Ya existe un AFD con el nombre '" + nombreAFD + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                ConexionBaF ctrl = AnalisisSintacticoGUI.getControlador();
                String res = ctrl.convertirAFNaAFD(afnSeleccionado, nombreAFD);
                if(res.startsWith("Error")){
                    JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(!currentAfdNames.contains(nombreAFD)){
                    currentAfdNames.add(nombreAFD);
                }
                AnalisisSintacticoGUI gui = (AnalisisSintacticoGUI) SwingUtilities.getWindowAncestor(this);
                if(gui != null){
                    gui.registerAutomata(nombreAFD, true);
                }

                JOptionPane.showMessageDialog(this, res, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.getWindowAncestor(this).dispose(); 
            });
            add(btnConvertir);
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
