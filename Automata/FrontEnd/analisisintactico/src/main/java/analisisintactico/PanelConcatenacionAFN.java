package analisisintactico;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import BackEnd.ConexionBaF;

public class PanelConcatenacionAFN extends JPanel {
    private static final Color COLOR_AZUL_ACCENT = new Color(100, 149, 237);
    private static final Color COLOR_TEXTO_OSCURO = new Color(25, 50, 95);
    
    private final List<String> availableAutomataNames;

    public PanelConcatenacionAFN(List<String> currentAfnNames) {
        this.availableAutomataNames = (currentAfnNames != null) ? currentAfnNames : new ArrayList<>();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        setBackground(Color.WHITE);

        add(tituloPanel("Operación: Concatenación (A • B)"));
        add(Box.createVerticalStrut(20));

        add(crearEtiqueta("Seleccione el primer autómata (A):"));
        JComboBox<String> comboConcatenacion1 = comboAut();
        add(comboConcatenacion1);
        add(Box.createVerticalStrut(10));

        add(crearEtiqueta("Seleccione el segundo autómata (B):"));
        JComboBox<String> comboConcatenacion2 = comboAut();
        add(comboConcatenacion2);
        add(Box.createVerticalStrut(10));

        add(crearEtiqueta("Nombre para el AFN resultante:"));
            JTextField txtNombreNuevo = campoTexto();
            add(txtNombreNuevo);
            add(Box.createVerticalStrut(10));
        
        add(crearEtiqueta("Token Léxico Resultante (Opcional):"));
        JTextField txtToken = campoTexto();
        add(txtToken);
        add(Box.createVerticalStrut(20));

        JButton btnConcatenar = botonAzul("Realizar Concatenación");
        btnConcatenar.addActionListener((ActionEvent e) -> {
            String auto1 = (String) comboConcatenacion1.getSelectedItem();
            String auto2 = (String) comboConcatenacion2.getSelectedItem();
            String tokenStr = txtToken.getText().trim();
            String nombreUsuario = txtNombreNuevo.getText().trim();
            
            if (auto1 == null || auto2 == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar dos autómatas válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int token = 0;
            if (!tokenStr.isEmpty()) {
                try {
                    token = Integer.parseInt(tokenStr);
                    if (token < 0) {
                        JOptionPane.showMessageDialog(this, "El Token debe ser un número entero no negativo.", "Error de Token", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "El Token debe ser un número entero válido.", "Error de Token", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            ConexionBaF ctrl = AnalisisSintacticoGUI.getControlador();
            String res = ctrl.concatenarAFNs(auto1, auto2, nombreUsuario);
            if(res.startsWith("Error")){
                JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Asignar token al resultado si se pidió
            if(token > 0){
                BackEnd.AFN afnRes = ctrl.obtenerAFN(nombreUsuario);
                if(afnRes != null){
                    afnRes.asignarToken(token);
                }
            }

            availableAutomataNames.remove(auto1); 
            availableAutomataNames.remove(auto2);
            if(!availableAutomataNames.contains(nombreUsuario)){
                availableAutomataNames.add(nombreUsuario);
            }
            AnalisisSintacticoGUI gui = (AnalisisSintacticoGUI) SwingUtilities.getWindowAncestor(this);
            if(gui != null){
                gui.registerAutomata(nombreUsuario, false);
            }

            JOptionPane.showMessageDialog(this, res, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.getWindowAncestor(this).dispose(); 
        });
        add(btnConcatenar);
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
