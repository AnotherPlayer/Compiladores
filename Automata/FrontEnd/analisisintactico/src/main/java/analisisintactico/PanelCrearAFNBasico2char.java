package analisisintactico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;

public class PanelCrearAFNBasico2char extends JPanel { 

    private static final Color COLOR_AZUL_ACCENT = new Color(100, 149, 237);
    private static final Color COLOR_TEXTO_OSCURO = new Color(25, 50, 95);
    @SuppressWarnings("unused")
    private final List<String> currentAfnNames;

    public PanelCrearAFNBasico2char(List<String> currentAfnNames) {
        this.currentAfnNames = (currentAfnNames != null) ? currentAfnNames : new ArrayList<>();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        setBackground(Color.WHITE);

        add(tituloPanel("Crear AFN (Rango de Caracteres)"));
        add(Box.createVerticalStrut(20));

        add(crearEtiqueta("Nombre del autómata:"));
        JTextField txtNombre = campoTexto(280);
        add(txtNombre);
        add(Box.createVerticalStrut(10));

        add(crearEtiqueta("Definición del Rango [Inicio - Fin]:"));
        
        JPanel panelRango = new JPanel();
        panelRango.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelRango.setBackground(Color.WHITE);
        panelRango.setAlignmentX(CENTER_ALIGNMENT);
        
        JTextField txtInicioRango = campoTexto(60);
        txtInicioRango.setHorizontalAlignment(JTextField.CENTER);
        
        JTextField txtFinRango = campoTexto(60);
        txtFinRango.setHorizontalAlignment(JTextField.CENTER);
        
        panelRango.add(new JLabel("["));
        panelRango.add(txtInicioRango);
        panelRango.add(new JLabel(" - "));
        panelRango.add(txtFinRango);
        panelRango.add(new JLabel("]"));
        
        add(panelRango);
        add(Box.createVerticalStrut(10));
        
        add(crearEtiqueta("Token Léxico (Opcional):"));
        JTextField txtToken = campoTexto(280);
        add(txtToken);
        add(Box.createVerticalStrut(20));

        JButton btnCrear = botonAzul("Crear AFN de Rango");
        btnCrear.addActionListener((ActionEvent e) -> {
            String nombre = txtNombre.getText().trim();
            String inicioStr = txtInicioRango.getText().trim();
            String finStr = txtFinRango.getText().trim();
            String tokenStr = txtToken.getText().trim();
            
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un nombre para el autómata.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (inicioStr.length() != 1 || finStr.length() != 1) {
                JOptionPane.showMessageDialog(this, "El Inicio y el Fin del rango deben ser un solo carácter.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            char inicio = inicioStr.charAt(0);
            char fin = finStr.charAt(0);

            if (inicio > fin) {
                JOptionPane.showMessageDialog(this, "El carácter de inicio debe ser menor o igual al carácter final.", "Error de Lógica", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (currentAfnNames.contains(nombre)) {
                JOptionPane.showMessageDialog(this, "Ya existe un autómata con el nombre '" + nombre + "'.", "Error", JOptionPane.ERROR_MESSAGE);
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
            
            String rangoFinal = inicioStr + "-" + finStr;
            currentAfnNames.add(nombre);
            JOptionPane.showMessageDialog(this, 
                "AFN '" + nombre + "' para el rango [" + rangoFinal + "] creado y registrado." + tokenMsg, 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            SwingUtilities.getWindowAncestor(this).dispose(); 
        });
        
        add(btnCrear);
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

    private JTextField campoTexto(int width) {
        JTextField txt = new JTextField(15);
        txt.setMaximumSize(new Dimension(width, 35));
        txt.setPreferredSize(new Dimension(width, 35));
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