package analisisintactico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;

public class PanelCerraduraOpcional extends JPanel {
    private static final Color COLOR_AZUL_ACCENT = new Color(100, 149, 237);
    private static final Color COLOR_TEXTO_OSCURO = new Color(25, 50, 95);
    
    private final List<String> availableAutomataNames;

    public PanelCerraduraOpcional(List<String> currentAfnNames) {
        this.availableAutomataNames = (currentAfnNames != null) ? currentAfnNames : new ArrayList<>();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        setBackground(Color.WHITE);

        add(tituloPanel("Operación: Cerradura Opcional (?)"));
        add(Box.createVerticalStrut(20));

        add(crearEtiqueta("Seleccione el autómata:"));
        JComboBox<String> combo = comboAut();
        add(combo);
        add(Box.createVerticalStrut(10));

        add(crearEtiqueta("Nombre para el AFN resultante:"));
            JTextField txtNombreNuevo = campoTexto();
            add(txtNombreNuevo);
            add(Box.createVerticalStrut(10));
        
        add(crearEtiqueta("Token Léxico Resultante (Opcional):"));
        JTextField txtToken = campoTexto();
        add(txtToken);
        add(Box.createVerticalStrut(20));

        JButton btnAplicar = botonAzul("Aplicar Cerradura Opcional");
        btnAplicar.addActionListener((ActionEvent e) -> {
            String auto = (String) combo.getSelectedItem();
            String tokenStr = txtToken.getText().trim();
            String nombreUsuario = txtNombreNuevo.getText().trim();
            
            if (auto == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un autómata válido.", "Error", JOptionPane.ERROR_MESSAGE);
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
                availableAutomataNames.remove(auto); 
                availableAutomataNames.add(nombreUsuario);
            
            JOptionPane.showMessageDialog(this, 
                "Cerradura Opcional aplicada. AFN creado: '" + nombreUsuario + "'." + tokenMsg,
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            SwingUtilities.getWindowAncestor(this).dispose(); 
        });
        add(btnAplicar);
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