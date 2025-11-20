package analisisintactico;

import javax.swing.*;
import java.awt.*;

public class PanelCalculadora extends JPanel {
    
    private JTextField txtIdAFD;
    private JButton btnSeleccionarAFD;
    private JTextField txtExpresion;
    private JButton btnEvaluar;
    private JTextField txtResultado;
    private JTextField txtExpPrefija;
    
    public PanelCalculadora() {
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel lblTitulo = new JLabel("Analizador Léxico / Calculadora");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(40, 40, 40));
        panelPrincipal.add(lblTitulo, gbc);
        
        gbc.gridy = row++;
        panelPrincipal.add(Box.createVerticalStrut(10), gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lblIdAFD = new JLabel("Id del AFD");
        lblIdAFD.setFont(new Font("Arial", Font.PLAIN, 12));
        panelPrincipal.add(lblIdAFD, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = row++;
        txtIdAFD = new JTextField(20);
        txtIdAFD.setFont(new Font("Arial", Font.PLAIN, 12));
        txtIdAFD.setPreferredSize(new Dimension(250, 25));
        panelPrincipal.add(txtIdAFD, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnSeleccionarAFD = new JButton("Seleccionar archivo del AFD");
        btnSeleccionarAFD.setFont(new Font("Arial", Font.PLAIN, 11));
        btnSeleccionarAFD.setPreferredSize(new Dimension(200, 28));
        btnSeleccionarAFD.setFocusPainted(false);
        btnSeleccionarAFD.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSeleccionarAFD.addActionListener(e -> seleccionarArchivoAFD());
        panelPrincipal.add(btnSeleccionarAFD, gbc);
        
        gbc.gridy = row++;
        panelPrincipal.add(Box.createVerticalStrut(10), gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lblExpresion = new JLabel("Expresión a evaluar:");
        lblExpresion.setFont(new Font("Arial", Font.PLAIN, 12));
        panelPrincipal.add(lblExpresion, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = row++;
        txtExpresion = new JTextField(20);
        txtExpresion.setFont(new Font("Arial", Font.PLAIN, 12));
        txtExpresion.setPreferredSize(new Dimension(250, 25));
        panelPrincipal.add(txtExpresion, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnEvaluar = new JButton("Evaluar");
        btnEvaluar.setFont(new Font("Arial", Font.BOLD, 11));
        btnEvaluar.setPreferredSize(new Dimension(100, 28));
        btnEvaluar.setFocusPainted(false);
        btnEvaluar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEvaluar.addActionListener(e -> evaluarExpresion());
        panelPrincipal.add(btnEvaluar, gbc);
        
        gbc.gridy = row++;
        panelPrincipal.add(Box.createVerticalStrut(5), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel lblResultado = new JLabel("Resultado");
        lblResultado.setFont(new Font("Arial", Font.PLAIN, 12));
        panelPrincipal.add(lblResultado, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        panelPrincipal.add(new JLabel(), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = row++;
        txtResultado = new JTextField(20);
        txtResultado.setFont(new Font("Arial", Font.PLAIN, 12));
        txtResultado.setPreferredSize(new Dimension(250, 25));
        txtResultado.setEditable(false);
        txtResultado.setBackground(Color.WHITE);
        txtResultado.setHorizontalAlignment(JTextField.CENTER);
        panelPrincipal.add(txtResultado, gbc);
        
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panelPrincipal.add(Box.createVerticalStrut(5), gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lblExpPrefija = new JLabel("Expresión prefija:");
        lblExpPrefija.setFont(new Font("Arial", Font.PLAIN, 12));
        panelPrincipal.add(lblExpPrefija, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = row;
        txtExpPrefija = new JTextField(20);
        txtExpPrefija.setFont(new Font("Arial", Font.PLAIN, 12));
        txtExpPrefija.setPreferredSize(new Dimension(250, 25));
        txtExpPrefija.setEditable(false);
        txtExpPrefija.setBackground(Color.WHITE);
        panelPrincipal.add(txtExpPrefija, gbc);
        
        add(panelPrincipal, BorderLayout.CENTER);
    }
    
    private void seleccionarArchivoAFD() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo del AFD");
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
            txtIdAFD.setText(rutaArchivo);
        }
    }
    
    private void evaluarExpresion() {
        String expresion = txtExpresion.getText().trim();
        
        if (expresion.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, ingrese una expresión a evaluar", 
                "Advertencia", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
}