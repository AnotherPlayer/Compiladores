package analisisintactico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;

public class PanelCrearAFNBasico extends JPanel { 

    @SuppressWarnings("unused")
    private final List<String> currentAfnNames; 
    
    private static final Color COLOR_AZUL_ACCENT = new Color(100, 149, 237);
    private static final Color COLOR_TEXTO_OSCURO = new Color(25, 50, 95);

    public PanelCrearAFNBasico(List<String> currentAfnNames) {
        this.currentAfnNames = (currentAfnNames != null) ? currentAfnNames : new ArrayList<>();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        setBackground(Color.WHITE);

        add(tituloPanel("Crear AFN Básico (Cadena Literal)"));
        add(Box.createVerticalStrut(20));

        add(crearEtiqueta("Nombre del autómata:"));
        JTextField txtNombre = campoTexto();
        add(txtNombre);
        add(Box.createVerticalStrut(10));

        add(crearEtiqueta("Ingrese una sentencia (ej. 'if' o '+'):"));
        JTextField txtInput = campoTexto();
        add(txtInput);
        add(Box.createVerticalStrut(10));
        
        add(crearEtiqueta("Token Léxico (Opcional):"));
        JTextField txtToken = campoTexto();
        add(txtToken);
        add(Box.createVerticalStrut(20));

        JButton btnCrear = botonAzul("Crear AFN");
        btnCrear.addActionListener((ActionEvent e) -> {
            String nombre = txtNombre.getText().trim();
            String input = txtInput.getText().trim();
            String tokenStr = txtToken.getText().trim(); 
            
            if (nombre.isEmpty() || input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar el Nombre y la Sentencia.", "Advertencia", JOptionPane.WARNING_MESSAGE);
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
            
            if (currentAfnNames.contains(nombre)) {
                JOptionPane.showMessageDialog(this, "Ya existe un autómata con el nombre '" + nombre + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            currentAfnNames.add(nombre);
            JOptionPane.showMessageDialog(this, 
                "AFN '" + nombre + "' creado y registrado." + tokenMsg, 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            SwingUtilities.getWindowAncestor(this).dispose(); 
        });
        
        add(btnCrear);
    }
    
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