package analisisintactico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import javax.swing.JFileChooser;

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
        setLayout(new BorderLayout());
        setBackground(new Color(235, 235, 235));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelContenedor = new JPanel(new BorderLayout(12, 12));
        panelContenedor.setBackground(Color.WHITE);
        panelContenedor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));

        // ------------------------------------------------------------------
        // PANEL SUPERIOR
        // ------------------------------------------------------------------
        JPanel panelSuperior = new JPanel(new GridBagLayout());
        panelSuperior.setBackground(Color.WHITE);
        GridBagConstraints gbcSup = new GridBagConstraints();
        gbcSup.insets = new Insets(6, 6, 6, 6);
        gbcSup.fill = GridBagConstraints.HORIZONTAL;

        gbcSup.gridx = 0;
        gbcSup.gridy = 0;
        gbcSup.gridwidth = 2;
        gbcSup.anchor = GridBagConstraints.CENTER;
        JLabel lblTitulo = new JLabel("Analizador Léxico");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panelSuperior.add(lblTitulo, gbcSup);

        gbcSup.gridy = 1;
        panelSuperior.add(Box.createVerticalStrut(6), gbcSup);

        gbcSup.gridy = 2;
        gbcSup.gridwidth = 1;
        gbcSup.anchor = GridBagConstraints.WEST;
        JLabel lblIdAFD = new JLabel("Id del AFD");
        lblIdAFD.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelSuperior.add(lblIdAFD, gbcSup);

        gbcSup.gridx = 1;
        txtIdAFD = new JTextField();
        txtIdAFD.setPreferredSize(new Dimension(420, 26));
        panelSuperior.add(txtIdAFD, gbcSup);

        gbcSup.gridx = 0;
        gbcSup.gridy = 3;
        gbcSup.gridwidth = 2;
        gbcSup.anchor = GridBagConstraints.CENTER;
        btnSeleccionarArchivo = new JButton("Seleccionar archivo del AFD");
        btnSeleccionarArchivo.setPreferredSize(new Dimension(320, 30));
        btnSeleccionarArchivo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSeleccionarArchivo.addActionListener(e -> seleccionarArchivo());
        panelSuperior.add(btnSeleccionarArchivo, gbcSup);

        gbcSup.gridy = 4;
        panelSuperior.add(Box.createVerticalStrut(6), gbcSup);

        gbcSup.gridy = 5;
        gbcSup.gridwidth = 1;
        gbcSup.gridx = 0;
        gbcSup.anchor = GridBagConstraints.WEST;
        JLabel lblCadena = new JLabel("Cadena a analizar sintácticamente");
        lblCadena.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelSuperior.add(lblCadena, gbcSup);

        gbcSup.gridx = 1;
        txtCadenaAnalizar = new JTextField();
        txtCadenaAnalizar.setPreferredSize(new Dimension(420, 26));
        panelSuperior.add(txtCadenaAnalizar, gbcSup);

        gbcSup.gridx = 0;
        gbcSup.gridy = 6;
        gbcSup.gridwidth = 2;
        gbcSup.anchor = GridBagConstraints.CENTER;
        btnAnalizar = new JButton("Analizar");
        btnAnalizar.setPreferredSize(new Dimension(140, 30));
        btnAnalizar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAnalizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAnalizar.addActionListener(e -> analizar());
        panelSuperior.add(btnAnalizar, gbcSup);

        panelContenedor.add(panelSuperior, BorderLayout.NORTH);

        // ------------------------------------------------------------------
        // PANEL INFERIOR
        // ------------------------------------------------------------------
        JPanel panelInferior = new JPanel(new GridBagLayout());
        panelInferior.setBackground(Color.WHITE);
        GridBagConstraints gbcInf = new GridBagConstraints();
        gbcInf.insets = new Insets(8, 8, 8, 8);
        gbcInf.fill = GridBagConstraints.BOTH;

        // Tabla izquierda ----------------------------------------------------------------------

        gbcInf.gridx = 0;
        gbcInf.gridy = 0;
        gbcInf.weightx = 1.5;
        gbcInf.weighty = 1.0;

        JPanel panelTablaGrande = new JPanel(new BorderLayout(6, 6));
        panelTablaGrande.setBackground(Color.WHITE);

        String[] columnas = {"Simbolo", "Terminal / No terminal"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 12) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaSimboloTerminal = new JTable(modeloTabla);
        tablaSimboloTerminal.setRowHeight(26);

        // FIX → columnas correctas
        tablaSimboloTerminal.getColumnModel().getColumn(0).setPreferredWidth(120);
        tablaSimboloTerminal.getColumnModel().getColumn(1).setPreferredWidth(220);

        configurarEstiloTabla(tablaSimboloTerminal);

        JScrollPane scrollTabla = new JScrollPane(tablaSimboloTerminal);
        scrollTabla.setPreferredSize(new Dimension(560, 360));
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        panelTablaGrande.add(scrollTabla, BorderLayout.CENTER);
        panelInferior.add(panelTablaGrande, gbcInf);

        // Derecha ----------------------------------------------------------------------

        gbcInf.gridx = 1;
        gbcInf.gridy = 0;
        gbcInf.weightx = 0.70;

        JPanel panelDerecha = new JPanel(new GridBagLayout());
        panelDerecha.setBackground(Color.WHITE);
        GridBagConstraints gbcD = new GridBagConstraints();
        gbcD.insets = new Insets(6, 6, 6, 6);
        gbcD.fill = GridBagConstraints.HORIZONTAL;

        gbcD.gridx = 0;
        gbcD.gridy = 0;
        gbcD.gridwidth = 2;
        JLabel lblListaSimbolos = new JLabel("Lista símbolos para calcular el FIRST");
        lblListaSimbolos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelDerecha.add(lblListaSimbolos, gbcD);

        gbcD.gridy = 1;
        gbcD.gridwidth = 1;
        gbcD.gridx = 1;
        txtListaSimbolos = new JTextField();
        txtListaSimbolos.setPreferredSize(new Dimension(180, 24));
        panelDerecha.add(txtListaSimbolos, gbcD);

        gbcD.gridx = 2;
        btnReset = new JButton("Reset");
        btnReset.setPreferredSize(new Dimension(30, 24));
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReset.addActionListener(e -> resetear());
        panelDerecha.add(btnReset, gbcD);

        gbcD.gridy = 3;
        gbcD.gridx = 0;
        gbcD.gridwidth = 2;
        gbcD.fill = GridBagConstraints.BOTH;
        gbcD.weightx = 1.0;
        gbcD.weighty = 1.0;

        JPanel panelTres = new JPanel(new GridLayout(1, 3, 8, 8));
        panelTres.setBackground(Color.WHITE);

        panelTres.add(crearPanelFirst());
        panelTres.add(crearPanelSimbolosNoTerminales());
        panelTres.add(crearPanelFollow());

        panelDerecha.add(panelTres, gbcD);

        panelInferior.add(panelDerecha, gbcInf);

        panelContenedor.add(panelInferior, BorderLayout.CENTER);
        add(panelContenedor, BorderLayout.CENTER);
    }

    // ----------------------------------------------------------------------
    // ESTILO TABLAS
    // ----------------------------------------------------------------------
    private void configurarEstiloTabla(JTable tabla) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tabla.setShowGrid(true);
        tabla.setGridColor(new Color(200, 200, 200));
        tabla.setIntercellSpacing(new Dimension(1, 1));
        tabla.setSelectionBackground(new Color(184, 207, 229));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setResizingAllowed(false);

        tabla.getTableHeader().setBackground(new Color(225, 225, 225));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    // ----------------------------------------------------------------------
    // PANEL FIRST
    // ----------------------------------------------------------------------
    private JPanel crearPanelFirst() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBackground(Color.WHITE);

        JButton btnFirst = new JButton("FIRST");
        btnFirst.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFirst.setFont(new Font("Arial", Font.BOLD, 12));

        panel.add(btnFirst, BorderLayout.NORTH);

        String[] columnas = {"Simbolo"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 8) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaFirst = new JTable(modeloTabla);
        tablaFirst.setRowHeight(22);

        tablaFirst.getColumnModel().getColumn(0).setPreferredWidth(80);

        configurarEstiloTabla(tablaFirst);

        JScrollPane scrollTabla = new JScrollPane(tablaFirst);
        scrollTabla.setPreferredSize(new Dimension(150, 200));
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        panel.add(scrollTabla, BorderLayout.CENTER);

        return panel;
    }

    // ----------------------------------------------------------------------
    // PANEL NO TERMINALES
    // ----------------------------------------------------------------------
    private JPanel crearPanelSimbolosNoTerminales() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBackground(Color.WHITE);

        JLabel lblNoTerminales = new JLabel("Símbolos no terminales", SwingConstants.CENTER);
        lblNoTerminales.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblNoTerminales, BorderLayout.NORTH);

        String[] columnas = {"Simbolo"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 8) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaSimbolosNoTerminales = new JTable(modeloTabla);
        tablaSimbolosNoTerminales.setRowHeight(22);

        tablaSimbolosNoTerminales.getColumnModel()
            .getColumn(0).setPreferredWidth(80);

        configurarEstiloTabla(tablaSimbolosNoTerminales);

        JScrollPane scrollTabla = new JScrollPane(tablaSimbolosNoTerminales);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        panel.add(scrollTabla, BorderLayout.CENTER);

        return panel;
    }

    // ----------------------------------------------------------------------
    // PANEL FOLLOW
    // ----------------------------------------------------------------------
    private JPanel crearPanelFollow() {

        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBackground(Color.WHITE);

        JPanel panelSuperior = new JPanel(new GridBagLayout());
        panelSuperior.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblSimboloNT = new JLabel("Símbolo no terminal");
        lblSimboloNT.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panelSuperior.add(lblSimboloNT, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 2;
        txtSimboloNoTerminal = new JTextField();
        txtSimboloNoTerminal.setPreferredSize(new Dimension(100, 24));
        panelSuperior.add(txtSimboloNoTerminal, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JButton btnFollowTitle = new JButton("FOLLOW");
        btnFollowTitle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFollowTitle.setFont(new Font("Arial", Font.BOLD, 12));
        panelSuperior.add(btnFollowTitle, gbc);

        gbc.gridx = 1;
        btnResetFollow = new JButton("Reset");
        btnResetFollow.setPreferredSize(new Dimension(100, 24));
        btnResetFollow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnResetFollow.addActionListener(e -> resetearFollow());
        panelSuperior.add(btnResetFollow, gbc);

        panel.add(panelSuperior, BorderLayout.NORTH);

        String[] columnas = {"Simbolo"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 6) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaFollow = new JTable(modeloTabla);
        tablaFollow.setRowHeight(22);
        tablaFollow.getColumnModel().getColumn(0).setPreferredWidth(80);
        configurarEstiloTabla(tablaFollow);

        JScrollPane scrollTabla = new JScrollPane(tablaFollow);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        panel.add(scrollTabla, BorderLayout.CENTER);

        return panel;
    }

    // ----------------------------------------------------------------------
    // FUNCIONES
    // ----------------------------------------------------------------------
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
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, ingrese una cadena a analizar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Analizando cadena: " + cadena,
                "Análisis",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void resetear() {
        txtListaSimbolos.setText("");

        DefaultTableModel modelo = (DefaultTableModel) tablaFirst.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.setValueAt("", i, 0);
        }
    }

    private void resetearFollow() {
        txtSimboloNoTerminal.setText("");

        DefaultTableModel modelo = (DefaultTableModel) tablaFollow.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.setValueAt("", i, 0);
        }
    }
}
