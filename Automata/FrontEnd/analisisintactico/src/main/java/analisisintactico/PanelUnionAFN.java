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

public class PanelUnionAFN extends JPanel {
    private static final Color COLOR_AZUL_ACCENT = new Color(100, 149, 237);
    private static final Color COLOR_TEXTO_OSCURO = new Color(25, 50, 95);
    
    private final List<String> availableAutomataNames; 

    public PanelUnionAFN(List<String> currentAfnNames) {
        this.availableAutomataNames = (currentAfnNames != null) ? currentAfnNames : new ArrayList<>();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        setBackground(Color.WHITE);

        add(tituloPanel("Operación: Unión (A | B)"));
        add(Box.createVerticalStrut(20));

            add(crearEtiqueta("Seleccione el primer autómata (A):"));
            JComboBox<String> comboUnion1 = comboAut();
            add(comboUnion1);
            add(Box.createVerticalStrut(10));

            add(crearEtiqueta("Seleccione el segundo autómata (B):"));
            JComboBox<String> comboUnion2 = comboAut();
            add(comboUnion2);
            add(Box.createVerticalStrut(10));

            add(crearEtiqueta("Nombre para el AFN resultante:"));
            JTextField txtNombreNuevo = campoTexto();
            add(txtNombreNuevo);
            add(Box.createVerticalStrut(10));
            
            add(crearEtiqueta("Token Léxico Resultante (Opcional):"));
            JTextField txtToken = campoTexto();
            add(txtToken);
            add(Box.createVerticalStrut(20));

            JButton btnUnir = botonAzul("Realizar Unión");
            btnUnir.addActionListener((ActionEvent e) -> {
                String auto1 = (String) comboUnion1.getSelectedItem();
                String auto2 = (String) comboUnion2.getSelectedItem();
                String tokenStr = txtToken.getText().trim();
                String nombreUsuario = txtNombreNuevo.getText().trim();
                
                if (auto1 == null || auto2 == null) {
                    JOptionPane.showMessageDialog(this, "Debe seleccionar dos autómatas válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int token = -1;
                String tokenMsg = " (Sin asignar)";
                
                if (!tokenStr.isEmpty()) {
                    try {
                        token = Integer.parseInt(tokenStr);
                        if (token < 1) {
                            JOptionPane.showMessageDialog(this, "El Token debe ser un número entero positivo.", "Error de Token", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        tokenMsg = " (Token: " + token + ")";
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "El Token debe ser un número entero válido.", "Error de Token", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                ConexionBaF ctrl = AnalisisSintacticoGUI.getControlador();
                String res = ctrl.unirAFNs(auto1, auto2, nombreUsuario, token);
                if(res.startsWith("Error")){
                    JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
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

                JOptionPane.showMessageDialog(this, res + tokenMsg, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.getWindowAncestor(this).dispose(); 
            });
            add(btnUnir);
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
