package analisisintactico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class PanelAnalisisLR0 extends JPanel {

    private File archivoAFDLexico;
    private JTextArea areaGramatica;
    private JTextField txtSigma;
    private JButton btnCrearTabla;
    private JButton btnSeleccionarAfdLexico;
    private JButton btnProbarLexico;
    private JButton btnAsignarTokens;
    private JList<String> listNoTerminal;
    private JList<String> listTerminal;
    private JList<String> listToken;
    private JTable tablaItems;
    private JTable tablaLR;
    private JTable tablaLexemaToken;
    private JTable tablaPilaCadenaAccion;

    public PanelAnalisisLR0() {
        inicializarComponentes();
        inicializarModelosTablas();
    }

    public void inicializarComponentes() {
        this.setLayout(new BorderLayout(5, 5));
        this.setBackground(new Color(230, 240, 250));

        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(new Color(230, 240, 250));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);

        JPanel panelGramatica = crearPanelGramatica();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.20;
        gbc.weighty = 0.25;
        gbc.gridheight = 1;
        panelPrincipal.add(panelGramatica, gbc);

        JPanel panelCentroSuperior = crearPanelCentroSuperior();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.45;
        gbc.weighty = 0.25;
        panelPrincipal.add(panelCentroSuperior, gbc);

        JPanel panelDerechaSuperior = crearPanelDerechaSuperior();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.35;
        gbc.weighty = 0.25;
        panelPrincipal.add(panelDerechaSuperior, gbc);

        JPanel panelItems = crearPanelItems();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.20;
        gbc.weighty = 0.40;
        panelPrincipal.add(panelItems, gbc);

        JPanel panelTablaLR = crearPanelTablaLR();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.45;
        gbc.weighty = 0.40;
        panelPrincipal.add(panelTablaLR, gbc);

        JPanel panelLexico = crearPanelLexico();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.35;
        gbc.weighty = 0.40;
        panelPrincipal.add(panelLexico, gbc);

        JPanel panelSimulacion = crearPanelSimulacion();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 0.35;
        panelPrincipal.add(panelSimulacion, gbc);

        this.add(panelPrincipal, BorderLayout.CENTER);

        btnCrearTabla.addActionListener(e -> btnCrearTablaActionPerformed(e));
        btnProbarLexico.addActionListener(e -> btnProbarLexicoActionPerformed(e));
        btnSeleccionarAfdLexico.addActionListener(e -> btnSeleccionarAfdLexicoActionPerformed(e));
        btnAsignarTokens.addActionListener(e -> btnAsignarTokensActionPerformed(e));
    }

    private JPanel crearPanelGramatica() {
        JPanel panel = new JPanel(new BorderLayout(3, 3));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel lblTitulo = new JLabel("Gramática:");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblTitulo, BorderLayout.NORTH);

        areaGramatica = new JTextArea();
        areaGramatica.setFont(new Font("Monospaced", Font.PLAIN, 11));
        areaGramatica.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        JScrollPane scroll = new JScrollPane(areaGramatica);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelCentroSuperior() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(230, 240, 250));

        JLabel lblTitulo = new JLabel("Análisis LR(0)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        panelCentral.setBackground(new Color(230, 240, 250));

        btnCrearTabla = new JButton("Crear Tabla");
        btnCrearTabla.setFont(new Font("Arial", Font.PLAIN, 11));
        btnCrearTabla.setPreferredSize(new Dimension(120, 30));
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(new Color(230, 240, 250));
        panelBoton.add(btnCrearTabla);
        panelCentral.add(panelBoton, BorderLayout.NORTH);

        JPanel panelListas = new JPanel(new GridLayout(1, 3, 5, 5));
        panelListas.setBackground(new Color(230, 240, 250));

        JPanel pNoTerminal = crearPanelLista("NoTerminal");
        listNoTerminal = new JList<>(new DefaultListModel<>());
        listNoTerminal.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollNT = new JScrollPane(listNoTerminal);
        scrollNT.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        pNoTerminal.add(scrollNT, BorderLayout.CENTER);
        panelListas.add(pNoTerminal);

        JPanel pTerminal = crearPanelLista("Terminal");
        listTerminal = new JList<>(new DefaultListModel<>());
        listTerminal.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollT = new JScrollPane(listTerminal);
        scrollT.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        pTerminal.add(scrollT, BorderLayout.CENTER);
        panelListas.add(pTerminal);

        JPanel pToken = crearPanelLista("Token");
        listToken = new JList<>(new DefaultListModel<>());
        listToken.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollTk = new JScrollPane(listToken);
        scrollTk.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        pToken.add(scrollTk, BorderLayout.CENTER);
        panelListas.add(pToken);

        panelCentral.add(panelListas, BorderLayout.CENTER);

        btnAsignarTokens = new JButton("Asignar Tokens a terminales");
        btnAsignarTokens.setFont(new Font("Arial", Font.PLAIN, 10));
        JPanel panelBotonAbajo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotonAbajo.setBackground(new Color(230, 240, 250));
        panelBotonAbajo.add(btnAsignarTokens);
        panelCentral.add(panelBotonAbajo, BorderLayout.SOUTH);

        panel.add(panelCentral, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelLista(String titulo) {
        JPanel panel = new JPanel(new BorderLayout(2, 2));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
            BorderFactory.createEmptyBorder(3, 3, 3, 3)
        ));

        JLabel lbl = new JLabel(titulo, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 10));
        panel.add(lbl, BorderLayout.NORTH);

        return panel;
    }

    private JPanel crearPanelDerechaSuperior() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(230, 240, 250));

        btnSeleccionarAfdLexico = new JButton("Seleccionar AFD léxico");
        btnSeleccionarAfdLexico.setFont(new Font("Arial", Font.PLAIN, 11));
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(new Color(230, 240, 250));
        panelBoton.add(btnSeleccionarAfdLexico);
        panel.add(panelBoton, BorderLayout.NORTH);

        JPanel panelSigma = new JPanel(new BorderLayout(3, 3));
        panelSigma.setBackground(Color.WHITE);
        panelSigma.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel lblSigma = new JLabel("Sigma");
        lblSigma.setFont(new Font("Arial", Font.BOLD, 12));
        panelSigma.add(lblSigma, BorderLayout.NORTH);

        txtSigma = new JTextField();
        txtSigma.setFont(new Font("Monospaced", Font.PLAIN, 11));
        txtSigma.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(3, 5, 3, 5)
        ));
        panelSigma.add(txtSigma, BorderLayout.CENTER);

        panel.add(panelSigma, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelItems() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));

        tablaItems = new JTable();
        tablaItems.setFont(new Font("Monospaced", Font.PLAIN, 10));
        tablaItems.setRowHeight(25);
        tablaItems.setGridColor(Color.BLACK);
        tablaItems.setShowGrid(true);
        tablaItems.setShowVerticalLines(true);
        tablaItems.setShowHorizontalLines(true);
        tablaItems.getTableHeader().setReorderingAllowed(false);
        tablaItems.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        tablaItems.getTableHeader().setBackground(new Color(220, 220, 220));
        tablaItems.setIntercellSpacing(new Dimension(1, 1));
        
        JScrollPane scroll = new JScrollPane(tablaItems);
        scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelTablaLR() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));

        tablaLR = new JTable();
        tablaLR.setFont(new Font("Monospaced", Font.PLAIN, 10));
        tablaLR.setRowHeight(25);
        tablaLR.setGridColor(Color.BLACK);
        tablaLR.setShowGrid(true);
        tablaLR.setShowVerticalLines(true);
        tablaLR.setShowHorizontalLines(true);
        tablaLR.getTableHeader().setReorderingAllowed(false);
        tablaLR.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        tablaLR.getTableHeader().setBackground(new Color(220, 220, 220));
        tablaLR.setIntercellSpacing(new Dimension(1, 1));
        
        JScrollPane scroll = new JScrollPane(tablaLR);
        scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelLexico() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(230, 240, 250));

        btnProbarLexico = new JButton("Probar Léxico");
        btnProbarLexico.setFont(new Font("Arial", Font.PLAIN, 11));
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(new Color(230, 240, 250));
        panelBoton.add(btnProbarLexico);
        panel.add(panelBoton, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));

        tablaLexemaToken = new JTable();
        tablaLexemaToken.setFont(new Font("Monospaced", Font.PLAIN, 10));
        tablaLexemaToken.setRowHeight(25);
        tablaLexemaToken.setGridColor(Color.BLACK);
        tablaLexemaToken.setShowGrid(true);
        tablaLexemaToken.setShowVerticalLines(true);
        tablaLexemaToken.setShowHorizontalLines(true);
        tablaLexemaToken.getTableHeader().setReorderingAllowed(false);
        tablaLexemaToken.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        tablaLexemaToken.getTableHeader().setBackground(new Color(220, 220, 220));
        tablaLexemaToken.setIntercellSpacing(new Dimension(1, 1));
        
        JScrollPane scroll = new JScrollPane(tablaLexemaToken);
        scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        panelTabla.add(scroll, BorderLayout.CENTER);

        panel.add(panelTabla, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelSimulacion() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));

        tablaPilaCadenaAccion = new JTable();
        tablaPilaCadenaAccion.setFont(new Font("Monospaced", Font.PLAIN, 10));
        tablaPilaCadenaAccion.setRowHeight(25);
        tablaPilaCadenaAccion.setGridColor(Color.BLACK);
        tablaPilaCadenaAccion.setShowGrid(true);
        tablaPilaCadenaAccion.setShowVerticalLines(true);
        tablaPilaCadenaAccion.setShowHorizontalLines(true);
        tablaPilaCadenaAccion.getTableHeader().setReorderingAllowed(false);
        tablaPilaCadenaAccion.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        tablaPilaCadenaAccion.getTableHeader().setBackground(new Color(220, 220, 220));
        tablaPilaCadenaAccion.setIntercellSpacing(new Dimension(1, 1));
        
        JScrollPane scroll = new JScrollPane(tablaPilaCadenaAccion);
        scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    public void inicializarModelosTablas() {
        tablaItems.setModel(new DefaultTableModel(
            new Object[]{"Ij", "I-A(Ij,Simb)", "Items"}, 0
        ));

        tablaLR.setModel(new DefaultTableModel(
            new Object[]{"Estado"}, 0
        ));

        tablaLexemaToken.setModel(new DefaultTableModel(
            new Object[]{"Lexema", "Token"}, 0
        ));

        tablaPilaCadenaAccion.setModel(new DefaultTableModel(
            new Object[]{"Pila", "Cadena", "Acción"}, 0
        ));
    }

    private void btnCrearTablaActionPerformed(ActionEvent evt) {
        String gramatica = areaGramatica.getText();
        JOptionPane.showMessageDialog(this, 
            "Creando tabla LR(0) para:\n" + gramatica,
            "Proceso LR(0)", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnProbarLexicoActionPerformed(ActionEvent evt) {
        String sigma = txtSigma.getText();
        JOptionPane.showMessageDialog(this,
            "Probando análisis léxico en: " + sigma,
            "Proceso Léxico",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnSeleccionarAfdLexicoActionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo AFD Léxico");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Archivos de texto (*.txt, *.afd)", "txt", "afd"));
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivoAFDLexico = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(this,
                "Archivo seleccionado:\n" + archivoAFDLexico.getAbsolutePath(),
                "AFD Léxico",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void btnAsignarTokensActionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(this,
            "Asignando tokens a terminales...",
            "Asignación de Tokens",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public JTextArea getAreaGramatica() { return areaGramatica; }
    public JTextField getTxtSigma() { return txtSigma; }
    public JList<String> getListNoTerminal() { return listNoTerminal; }
    public JList<String> getListTerminal() { return listTerminal; }
    public JList<String> getListToken() { return listToken; }
    public JTable getTablaItems() { return tablaItems; }
    public JTable getTablaLR() { return tablaLR; }
    public JTable getTablaLexemaToken() { return tablaLexemaToken; }
    public JTable getTablaPilaCadenaAccion() { return tablaPilaCadenaAccion; }
    public File getArchivoAFDLexico() { return archivoAFDLexico; }
}