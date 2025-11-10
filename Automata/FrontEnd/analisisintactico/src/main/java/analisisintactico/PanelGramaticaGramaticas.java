package analisisintactico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PanelGramaticaGramaticas extends JPanel {
    
    private JTextField txtIdAFD;
    private JButton btnSeleccionarArchivo;
    private JTextField txtCadenaAnalizar;
    private JButton btnAnalizar;
    private JTable tablaSimboloTerminal;
    private JTextField txtListaSimbolos;
    private JButton btnReset;
    private JTable tablaFirst;
    private JTable tablaSimbolosNoTerminales;
    private JTextField txtSimboloNoTerminal;
    private JButton btnResetFollow;
    private JTable tablaFollow;
    
    public PanelGramaticaGramaticas() {
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
        gbc.fill = GridBagConstraints.BOTH;
        
        int row = 0;
        
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel lblTitulo = new JLabel("Analizador Léxico");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelPrincipal.add(lblTitulo, gbc);
        
        gbc.gridy = row++;
        panelPrincipal.add(Box.createVerticalStrut(10), gbc);
        
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblIdAFD = new JLabel("Id del AFD");
        lblIdAFD.setFont(new Font("Arial", Font.PLAIN, 12));
        panelPrincipal.add(lblIdAFD, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        txtIdAFD = new JTextField(25);
        txtIdAFD.setPreferredSize(new Dimension(250, 25));
        panelPrincipal.add(txtIdAFD, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        btnSeleccionarArchivo = new JButton("Seleccionar archivo del AFD");
        btnSeleccionarArchivo.setPreferredSize(new Dimension(200, 28));
        btnSeleccionarArchivo.setFocusPainted(false);
        btnSeleccionarArchivo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSeleccionarArchivo.addActionListener(e -> seleccionarArchivo());
        panelPrincipal.add(btnSeleccionarArchivo, gbc);
        
        gbc.gridy = row++;
        panelPrincipal.add(Box.createVerticalStrut(10), gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lblCadena = new JLabel("Cadena a analizar sintacticamente");
        lblCadena.setFont(new Font("Arial", Font.PLAIN, 12));
        panelPrincipal.add(lblCadena, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        txtCadenaAnalizar = new JTextField(30);
        txtCadenaAnalizar.setPreferredSize(new Dimension(400, 25));
        panelPrincipal.add(txtCadenaAnalizar, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        btnAnalizar = new JButton("Analizar");
        btnAnalizar.setPreferredSize(new Dimension(120, 28));
        btnAnalizar.setFocusPainted(false);
        btnAnalizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAnalizar.setFont(new Font("Arial", Font.BOLD, 12));
        btnAnalizar.addActionListener(e -> analizar());
        panelPrincipal.add(btnAnalizar, gbc);
        
        gbc.gridy = row++;
        panelPrincipal.add(Box.createVerticalStrut(10), gbc);
        
        JPanel panelTablas = new JPanel(new GridBagLayout());
        panelTablas.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbcTablas = new GridBagConstraints();
        gbcTablas.insets = new Insets(5, 5, 5, 5);
        gbcTablas.fill = GridBagConstraints.BOTH;
        
        gbcTablas.gridx = 0;
        gbcTablas.gridy = 0;
        gbcTablas.weightx = 0.50;
        gbcTablas.weighty = 1.0;
        JPanel panelTexto = crearPanelTexto();
        panelTablas.add(panelTexto, gbcTablas);
        
        gbcTablas.gridx = 1;
        gbcTablas.gridy = 0;
        gbcTablas.weightx = 0.65;
        JPanel panelDerecho = crearPanelDerecho();
        panelTablas.add(panelDerecho, gbcTablas);
        
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelPrincipal.add(panelTablas, gbc);
        
        add(panelPrincipal, BorderLayout.CENTER);
    }
    
    private void configurarEstiloTabla(JTable tabla) {
        // Configurar el renderizador para centrar el texto y mostrar bordes
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        // Aplicar el renderizador a todas las columnas
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Configurar la apariencia de la tabla
        tabla.setShowGrid(true);
        tabla.setGridColor(Color.BLACK);
        tabla.setIntercellSpacing(new Dimension(1, 1));
        tabla.setSelectionBackground(new Color(184, 207, 229));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setResizingAllowed(false);
        
        // Estilo del encabezado
        tabla.getTableHeader().setBackground(new Color(200, 200, 200));
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
    }
    
    private JPanel crearPanelTexto() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(240, 240, 240));
        
        JLabel lblTexto = new JLabel("<html>De doble click en el símbolo que desea<br>agregar a la cadena de la que se calculará el FIRST</html>");
        lblTexto.setFont(new Font("Arial", Font.BOLD, 11));
        
        String[] columnas = {"", "Simbolo", "Terminal / No terminal"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 12) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        tablaSimboloTerminal = new JTable(modeloTabla);
        tablaSimboloTerminal.setRowHeight(25);
        tablaSimboloTerminal.getColumnModel().getColumn(0).setPreferredWidth(20);
        tablaSimboloTerminal.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaSimboloTerminal.getColumnModel().getColumn(2).setPreferredWidth(180);
        
        configurarEstiloTabla(tablaSimboloTerminal);
        
        JScrollPane scrollTabla = new JScrollPane(tablaSimboloTerminal);
        scrollTabla.setPreferredSize(new Dimension(450, 350));
        
        panel.add(lblTexto, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblListaSimbolos = new JLabel("Lista símbolos para calcular el FIRST");
        lblListaSimbolos.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(lblListaSimbolos, gbc);
        
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        txtListaSimbolos = new JTextField(15);
        txtListaSimbolos.setPreferredSize(new Dimension(200, 25));
        panel.add(txtListaSimbolos, gbc);
        
        gbc.gridx = 1;
        btnReset = new JButton("Reset");
        btnReset.setPreferredSize(new Dimension(80, 25));
        btnReset.setFocusPainted(false);
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReset.addActionListener(e -> resetear());
        panel.add(btnReset, gbc);
        
        JPanel panelTresTables = new JPanel(new GridBagLayout());
        panelTresTables.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbcTres = new GridBagConstraints();
        gbcTres.insets = new Insets(5, 5, 5, 5);
        gbcTres.fill = GridBagConstraints.BOTH;
        gbcTres.weightx = 0.33;
        gbcTres.weighty = 1.0;
        
        gbcTres.gridx = 0;
        gbcTres.gridy = 0;
        JPanel panelFirst = crearPanelFirst();
        panelTresTables.add(panelFirst, gbcTres);
        
        gbcTres.gridx = 1;
        JPanel panelNoTerminales = crearPanelSimbolosNoTerminales();
        panelTresTables.add(panelNoTerminales, gbcTres);
        
        gbcTres.gridx = 2;
        JPanel panelFollow = crearPanelFollow();
        panelTresTables.add(panelFollow, gbcTres);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        panel.add(panelTresTables, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelFirst() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(240, 240, 240));
        
        JLabel lblFirst = new JLabel("First");
        lblFirst.setFont(new Font("Arial", Font.BOLD, 12));
        
        String[] columnas = {"", "Simbolo"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 8) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaFirst = new JTable(modeloTabla);
        tablaFirst.setRowHeight(22);
        tablaFirst.getColumnModel().getColumn(0).setPreferredWidth(20);
        tablaFirst.getColumnModel().getColumn(1).setPreferredWidth(80);
        
        configurarEstiloTabla(tablaFirst);
        
        JScrollPane scrollTabla = new JScrollPane(tablaFirst);
        scrollTabla.setPreferredSize(new Dimension(150, 200));
        scrollTabla.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        panel.add(lblFirst, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelSimbolosNoTerminales() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(240, 240, 240));
        
        JLabel lblNoTerminales = new JLabel("Símbolos no terminales");
        lblNoTerminales.setFont(new Font("Arial", Font.BOLD, 12));
        
        String[] columnas = {"", "Simbolo"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 8) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaSimbolosNoTerminales = new JTable(modeloTabla);
        tablaSimbolosNoTerminales.setRowHeight(22);
        tablaSimbolosNoTerminales.getColumnModel().getColumn(0).setPreferredWidth(20);
        tablaSimbolosNoTerminales.getColumnModel().getColumn(1).setPreferredWidth(80);
        
        configurarEstiloTabla(tablaSimbolosNoTerminales);
        
        JScrollPane scrollTabla = new JScrollPane(tablaSimbolosNoTerminales);
        scrollTabla.setPreferredSize(new Dimension(150, 200));
        scrollTabla.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        panel.add(lblNoTerminales, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelFollow() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(240, 240, 240));
        
        JPanel panelSuperior = new JPanel(new GridBagLayout());
        panelSuperior.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblSimboloNT = new JLabel("Simbolo no terminal");
        lblSimboloNT.setFont(new Font("Arial", Font.PLAIN, 10));
        panelSuperior.add(lblSimboloNT, gbc);
        
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        txtSimboloNoTerminal = new JTextField(10);
        txtSimboloNoTerminal.setPreferredSize(new Dimension(80, 22));
        panelSuperior.add(txtSimboloNoTerminal, gbc);
        
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel lblFollow = new JLabel("Follow");
        lblFollow.setFont(new Font("Arial", Font.BOLD, 12));
        panelSuperior.add(lblFollow, gbc);
        
        gbc.gridx = 1;
        btnResetFollow = new JButton("Reset");
        btnResetFollow.setPreferredSize(new Dimension(60, 22));
        btnResetFollow.setFocusPainted(false);
        btnResetFollow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnResetFollow.addActionListener(e -> resetearFollow());
        panelSuperior.add(btnResetFollow, gbc);
        
        String[] columnas = {"", "Simbolo"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 6) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaFollow = new JTable(modeloTabla);
        tablaFollow.setRowHeight(22);
        tablaFollow.getColumnModel().getColumn(0).setPreferredWidth(20);
        tablaFollow.getColumnModel().getColumn(1).setPreferredWidth(80);
        
        configurarEstiloTabla(tablaFollow);
        
        JScrollPane scrollTabla = new JScrollPane(tablaFollow);
        scrollTabla.setPreferredSize(new Dimension(150, 130));
        scrollTabla.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void seleccionarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo del AFD");
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
            txtIdAFD.setText(rutaArchivo);
        }
    }
    
    private void analizar() {
        String cadena = txtCadenaAnalizar.getText().trim();
        
        if (cadena.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, ingrese una cadena a analizar", 
                "Advertencia", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this, 
            "Analizando cadena: " + cadena, 
            "Análisis", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void resetear() {
        txtListaSimbolos.setText("");
        DefaultTableModel modeloFirst = (DefaultTableModel) tablaFirst.getModel();
        for (int i = 0; i < modeloFirst.getRowCount(); i++) {
            modeloFirst.setValueAt("", i, 0);
            modeloFirst.setValueAt("", i, 1);
        }
    }
    
    private void resetearFollow() {
        txtSimboloNoTerminal.setText("");
        DefaultTableModel modeloFollow = (DefaultTableModel) tablaFollow.getModel();
        for (int i = 0; i < modeloFollow.getRowCount(); i++) {
            modeloFollow.setValueAt("", i, 0);
            modeloFollow.setValueAt("", i, 1);
        }
    }
}