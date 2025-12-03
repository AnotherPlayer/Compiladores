package analisisintactico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AnalisisSintacticoGUI extends JFrame {

    private final String[] mockAutomataNames = {"AFN_ID", "AFN_NUM", "AFN_OP"};

    private JMenuBar menuBar;
    private JPanel panelContenidoPrincipal;

    private JMenu menuAFNs;
    private JMenu menuAnalisisSintactico;

    private JMenuItem menuItemBasicoAFN;
    private JMenuItem menuItemUnirAFN;
    private JMenuItem menuItemConcatenarAFN;
    private JMenuItem menuItemCerraduraPositivaAFN;
    private JMenuItem menuItemCerraduraKleeneAFN;
    private JMenuItem menuItemOpcionalAFN;
    private JMenuItem menuItemERtoAFN;
    private JMenuItem menuItemUnionAnalizadorLexico;
    private JMenuItem menuItemAFNtoAFD;
    private JMenuItem menuItemAnalizarCadena;
    private JMenuItem menuItemProbarAnalizadorLexico;

    private JMenu menuDescensoRecursivo;
    private JMenu menuDescRecGramdeGram;
    private JMenu menuAnalisisLL1;
    private JMenu menuAnalisisSLR;
    private JMenu menuAnalisisLRCanonico;
    private JMenu menuMatrices;

    private JMenuItem menuItemCalculadora;
    private JMenuItem menuItemAbrirGramatica;
    private JMenuItem menuItemAbrirLL1;
    private JMenuItem menuItemAbrirSLR;
    private JMenuItem menuItemAbrirLRCanonico;
    private JMenuItem menuItemAbrirMatrices;

    public AnalisisSintacticoGUI() {
        aplicarEstilosGlobales();
        initComponents();
    }

    /** --------------------------
     *  ESTILOS GLOBALES AZUL FUERTE + BLANCO
     *  -------------------------- */
    private void aplicarEstilosGlobales() {

        Font fuenteGeneral = new Font("Segoe UI", Font.PLAIN, 16);
        Font fuenteNegrita = new Font("Segoe UI", Font.BOLD, 22);

        UIManager.put("Label.font", fuenteGeneral);
        UIManager.put("Button.font", fuenteGeneral);
        UIManager.put("Menu.font", fuenteGeneral);
        UIManager.put("MenuItem.font", fuenteGeneral);
        UIManager.put("TextField.font", fuenteGeneral);
        UIManager.put("ComboBox.font", fuenteGeneral);

        Color azulFuerte = new Color(13, 71, 161);     // #0D47A1
        Color azulMedio = new Color(21, 101, 192);     // #1565C0
        Color blanco = Color.WHITE;
        Color textoOscuro = new Color(15, 15, 15);

        // GENERAL UI
        UIManager.put("Panel.background", azulFuerte);
        UIManager.put("Label.foreground", textoOscuro);
        UIManager.put("TextField.background", azulFuerte);
        UIManager.put("TextField.foreground", textoOscuro);
        UIManager.put("TextField.border", BorderFactory.createLineBorder(azulMedio, 1));

        // BOTONES
        UIManager.put("Button.background", azulFuerte);
        UIManager.put("Button.foreground", blanco);
        UIManager.put("Button.border", BorderFactory.createEmptyBorder(10, 18, 10, 18));
        UIManager.put("Button.focus", azulMedio);

        // MENÚ (barra azul fuerte)
        UIManager.put("MenuBar.background", azulFuerte);
        UIManager.put("MenuBar.foreground", blanco);
        UIManager.put("Menu.foreground", azulFuerte);
        UIManager.put("Menu.selectionBackground", azulMedio);
        UIManager.put("MenuItem.foreground", textoOscuro);
        UIManager.put("MenuItem.selectionBackground", azulMedio);
        UIManager.put("MenuItem.selectionForeground", blanco);

        // Bordes suaves
        UIManager.put("Button.arc", 12);
    }

    /** --------------------------
     *  INICIALIZACIÓN
     *  -------------------------- */
    private void initComponents() {

        setTitle("Análisis Sintáctico");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color azulFuerte = new Color(13, 71, 161);
        Color blanco = Color.WHITE;

        menuBar = new JMenuBar();
        menuBar.setBackground(azulFuerte);

        menuAFNs = new JMenu("AFN's");
        menuAFNs.setForeground(blanco);

        menuItemBasicoAFN = new JMenuItem("Básico");
        menuItemUnirAFN = new JMenuItem("Unir");
        menuItemConcatenarAFN = new JMenuItem("Concatenar");
        menuItemCerraduraPositivaAFN = new JMenuItem("Cerradura +");
        menuItemCerraduraKleeneAFN = new JMenuItem("Cerradura *");
        menuItemOpcionalAFN = new JMenuItem("Opcional");
        menuItemERtoAFN = new JMenuItem("ER->AFN");
        menuItemUnionAnalizadorLexico = new JMenuItem("Unión para Analizador Léxico");
        menuItemAFNtoAFD = new JMenuItem("Convertir AFN a AFD");
        menuItemAnalizarCadena = new JMenuItem("Analizar una cadena");
        menuItemProbarAnalizadorLexico = new JMenuItem("Probar analizador Léxico");

        // ACCIONES
        menuItemBasicoAFN.addActionListener(e -> showAutomataDialog("Crear AFN Básico", createAFNPanel("AFN Básico")));
        menuItemUnirAFN.addActionListener(e -> showAutomataDialog("Unión (A|B)", createUnionPanel()));
        menuItemConcatenarAFN.addActionListener(e -> showAutomataDialog("Concatenación", createConcatenacionPanel()));
        menuItemCerraduraPositivaAFN.addActionListener(e -> showAutomataDialog("Cerradura +", createCerraduraUnitariaPanel("Cerradura Positiva")));
        menuItemCerraduraKleeneAFN.addActionListener(e -> showAutomataDialog("Cerradura *", createCerraduraUnitariaPanel("Cerradura Kleene")));
        menuItemOpcionalAFN.addActionListener(e -> showAutomataDialog("Cerradura Opcional", createCerraduraUnitariaPanel("Cerradura Opcional")));
        menuItemERtoAFN.addActionListener(e -> abrirDialogERtoAFN());
        menuItemAFNtoAFD.addActionListener(e -> showAutomataDialog("Convertir AFN a AFD", createConversionToAFDPanel()));

        menuAFNs.add(menuItemBasicoAFN);
        menuAFNs.add(menuItemUnirAFN);
        menuAFNs.add(menuItemConcatenarAFN);
        menuAFNs.add(menuItemCerraduraPositivaAFN);
        menuAFNs.add(menuItemCerraduraKleeneAFN);
        menuAFNs.add(menuItemOpcionalAFN);
        menuAFNs.addSeparator();
        menuAFNs.add(menuItemERtoAFN);
        menuAFNs.add(menuItemUnionAnalizadorLexico);
        menuAFNs.add(menuItemAFNtoAFD);
        menuAFNs.add(menuItemAnalizarCadena);
        menuAFNs.add(menuItemProbarAnalizadorLexico);

        // MENÚS DE ANÁLISIS SINTÁCTICO
        menuAnalisisSintactico = new JMenu("Análisis Sintáctico");
        menuAnalisisSintactico.setForeground(blanco);

        menuDescensoRecursivo = new JMenu("Descenso Recursivo");
        menuItemCalculadora = new JMenuItem("Calculadora");
        menuItemCalculadora.addActionListener(e -> abrirDialogCalculadora());
        menuDescensoRecursivo.add(menuItemCalculadora);

        menuDescRecGramdeGram = new JMenu("Gramáticas");
        menuItemAbrirGramatica = new JMenuItem("Abrir Gramática");
        menuItemAbrirGramatica.addActionListener(e -> abrirFrameGramaticaGramaticas());
        menuDescRecGramdeGram.add(menuItemAbrirGramatica);

        menuAnalisisLL1 = new JMenu("Análisis LL(1)");
        menuItemAbrirLL1 = new JMenuItem("Abrir LL(1)");
        menuItemAbrirLL1.addActionListener(e -> abrirDialogAnalisisLL1());
        menuAnalisisLL1.add(menuItemAbrirLL1);

        menuAnalisisSLR = new JMenu("Análisis SLR");
        menuItemAbrirSLR = new JMenuItem("Abrir SLR");
        menuItemAbrirSLR.addActionListener(e -> abrirDialogAnalisisLR0());
        menuAnalisisSLR.add(menuItemAbrirSLR);

        menuAnalisisLRCanonico = new JMenu("Análisis LR(1)");
        menuItemAbrirLRCanonico = new JMenuItem("Abrir LR Canónico");
        menuItemAbrirLRCanonico.addActionListener(e -> abrirDialogAnalisisSintactico("LR(1)"));
        menuAnalisisLRCanonico.add(menuItemAbrirLRCanonico);

        menuMatrices = new JMenu("Matrices Demo");
        menuItemAbrirMatrices = new JMenuItem("Abrir Matrices");
        menuMatrices.add(menuItemAbrirMatrices);

        menuAnalisisSintactico.add(menuDescensoRecursivo);
        menuAnalisisSintactico.add(menuDescRecGramdeGram);
        menuAnalisisSintactico.add(menuAnalisisLL1);
        menuAnalisisSintactico.add(menuAnalisisSLR);
        menuAnalisisSintactico.add(menuAnalisisLRCanonico);
        menuAnalisisSintactico.add(menuMatrices);

        menuBar.add(menuAFNs);
        menuBar.add(menuAnalisisSintactico);
        setJMenuBar(menuBar);

        /** Panel Principal */
panelContenidoPrincipal = new JPanel(new GridBagLayout());
panelContenidoPrincipal.setBackground(new Color(119, 182, 230)); // Azul claro

GridBagConstraints gbc = new GridBagConstraints();
gbc.gridx = 0;
gbc.insets = new Insets(10, 10, 10, 10);

// Título
JLabel titulo = new JLabel("Bienvenido al Sistema de Análisis Sintáctico");
titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
titulo.setForeground(new Color(13, 71, 161));
panelContenidoPrincipal.add(titulo, gbc);

// Línea decorativa
gbc.gridy = 1;
JPanel linea = new JPanel();
linea.setPreferredSize(new Dimension(500, 3));
linea.setBackground(new Color(13, 71, 161));
panelContenidoPrincipal.add(linea, gbc);

// Subtítulo
gbc.gridy = 2;
JLabel subtitulo = new JLabel("Seleccione una opción del menú superior para comenzar");
subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
subtitulo.setForeground(Color.WHITE);
panelContenidoPrincipal.add(subtitulo, gbc);

add(panelContenidoPrincipal);

UIManager.put("Label.foreground", new Color(20,20,20)); // gris oscuro legible
UIManager.put("Panel.background", Color.WHITE);
UIManager.put("TextField.background", Color.WHITE);
UIManager.put("ComboBox.background", Color.WHITE);

    }

    /** --------------------------
     * PANELES GENERADORES (con fondo blanco)
     * -------------------------- */

    private JPanel createAFNPanel(String tipo) {
        JPanel panel = basePanel();

        JLabel lblTitulo = tituloPanel(tipo);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(20));

        panel.add(new JLabel("Nombre del autómata:"));
        JTextField txt = campoTexto();
        panel.add(txt);

        panel.add(new JLabel("Ingrese una sentencia:"));
        JTextField txt2 = campoTexto();
        panel.add(txt2);

        JButton btn = botonAzul("Crear");
        btn.addActionListener(e -> mensaje("Frontend pendiente del método: " + tipo));
        panel.add(btn);

        return panel;
    }

    private JPanel createUnionPanel() {
        JPanel panel = basePanel();

        JLabel lblTitulo = tituloPanel("Unión (A | B)");
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(20));

        panel.add(new JLabel("Autómata A:"));
        panel.add(comboAut());

        panel.add(new JLabel("Autómata B:"));
        panel.add(comboAut());

        JButton btn = botonAzul("Unir");
        btn.addActionListener(e -> mensaje("Unión pendiente."));
        panel.add(btn);

        return panel;
    }

    private JPanel createConcatenacionPanel() {
        JPanel panel = basePanel();

        panel.add(tituloPanel("Concatenación (A · B)"));
        panel.add(Box.createVerticalStrut(20));

        panel.add(new JLabel("Autómata A:"));
        panel.add(comboAut());

        panel.add(new JLabel("Autómata B:"));
        panel.add(comboAut());

        JButton btn = botonAzul("Concatenar");
        btn.addActionListener(e -> mensaje("Concatenación pendiente."));
        panel.add(btn);

        return panel;
    }

    private JPanel createCerraduraUnitariaPanel(String tipo) {
        JPanel panel = basePanel();

        panel.add(tituloPanel(tipo));
        panel.add(Box.createVerticalStrut(20));

        panel.add(new JLabel("Seleccione el autómata:"));
        panel.add(comboAut());

        JButton btn = botonAzul("Aplicar");
        btn.addActionListener(e -> mensaje("Cerradura pendiente."));
        panel.add(btn);

        return panel;
    }

    private JPanel createConversionToAFDPanel() {
        JPanel panel = basePanel();

        panel.add(tituloPanel("Convertir AFN → AFD"));
        panel.add(Box.createVerticalStrut(20));

        panel.add(new JLabel("Seleccione el AFN:"));
        panel.add(comboAut());

        panel.add(new JLabel("Nombre del AFD:"));
        panel.add(campoTexto());

        JButton btn = botonAzul("Convertir");
        panel.add(btn);

        return panel;
    }

    /** --------------------------
     * DIALOGOS ESTILIZADOS
     * -------------------------- */

    private void showAutomataDialog(String title, JPanel contentPanel) {
        JDialog dialog = new JDialog(this, title, true);

        dialog.getContentPane().setBackground(Color.WHITE);

        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));

        dialog.add(contentPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void abrirDialogCalculadora() {
        JDialog dialog = new JDialog(this, "Calculadora", true);
        dialog.setSize(450, 650);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(this);

        PanelCalculadora panel = new PanelCalculadora();
        estilizarPanelDialog(panel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void abrirDialogERtoAFN() {
        JDialog dialog = new JDialog(this, "ER → AFN", true);
        dialog.setSize(600, 500);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(this);

        PanelERtoAFN panel = new PanelERtoAFN();
        estilizarPanelDialog(panel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void abrirFrameGramaticaGramaticas() {

    JFrame frame = new JFrame("Gramática de Gramáticas");

    frame.setSize(1100, 700);
    frame.setLocationRelativeTo(this);

    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    frame.setResizable(true); // ← ACTIVAMOS MAXIMIZAR Y MINIMIZAR

    PanelGramaticaGramaticas panel = new PanelGramaticaGramaticas();
    estilizarPanelDialog(panel);

    frame.add(panel);

    frame.setVisible(true);
}



    private void abrirDialogAnalisisLR0() {
        JDialog dialog = new JDialog(this, "SLR", true);
        dialog.setSize(1200, 800);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(this);

        PanelAnalisisLR0 panel = new PanelAnalisisLR0();
        estilizarPanelDialog(panel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void abrirDialogAnalisisLL1() {
        JDialog dialog = new JDialog(this, "LL(1)", true);
        dialog.setSize(1000, 700);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(this);

        PanelAnalisisLL1 panel = new PanelAnalisisLL1();
        estilizarPanelDialog(panel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void abrirDialogAnalisisSintactico(String funcionalidad) {
        JDialog dialog = new JDialog(this, funcionalidad, true);
        dialog.setSize(600, 400);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /** --------------------------
     * Helpers UI
     * -------------------------- */

    private JPanel basePanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));
        return p;
    }

    private JLabel tituloPanel(String titulo) {
        JLabel l = new JLabel(titulo);
        l.setFont(new Font("Segoe UI", Font.BOLD, 22));
        l.setForeground(new Color(13, 71, 161));
        l.setAlignmentX(CENTER_ALIGNMENT);
        return l;
    }

    private JTextField campoTexto() {
        JTextField txt = new JTextField(15);
        txt.setMaximumSize(new Dimension(300, 30));
        return txt;
    }

    private JComboBox<String> comboAut() {
        JComboBox<String> combo = new JComboBox<>(mockAutomataNames);
        combo.setMaximumSize(new Dimension(300, 30));
        return combo;
    }

    private JButton botonAzul(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(new Color(13, 71, 161));
        b.setForeground(Color.WHITE);

        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setBackground(new Color(21, 101, 192));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                b.setBackground(new Color(13, 71, 161));
            }
        });

        b.setAlignmentX(CENTER_ALIGNMENT);
        return b;
    }

    private void mensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    private void estilizarPanelDialog(JPanel p) {
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    /** --------------------------
     * MAIN
     * -------------------------- */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AnalisisSintacticoGUI().setVisible(true));
    }
}
