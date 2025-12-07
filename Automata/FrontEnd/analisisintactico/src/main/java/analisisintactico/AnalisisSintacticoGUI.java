package analisisintactico;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class AnalisisSintacticoGUI extends JFrame {

    private final List<String> createdAfnNames = new ArrayList<>(); 
    private final List<String> createdAfdNames = new ArrayList<>(); 

    private JMenuBar menuBar;
    private JPanel panelContenidoPrincipal;

    private JMenu menuAFNs;
    private JMenu menuAnalizadorLexico;
    private JMenu menuAnalisisSintactico;

    // Menú AFNs
    private JMenuItem menuItemBasicoAFN;
    private JMenuItem menuItemBasicoAFN2Char;
    private JMenuItem menuItemUnirAFN;
    private JMenuItem menuItemConcatenarAFN;
    private JMenuItem menuItemCerraduraPositivaAFN;
    private JMenuItem menuItemCerraduraKleeneAFN;
    private JMenuItem menuItemOpcionalAFN;
    private JMenuItem menuItemERtoAFN;
    
    // Menú Analizador Léxico
    private JMenuItem menuItemMostrarAFN;
    private JMenuItem menuItemMostrarAFD;
    private JMenuItem menuItemAFNtoAFD;
    private JMenuItem menuItemAnalizarCadena;

    // Menú Análisis Sintáctico
    private JMenuItem menuItemCalculadora;
    private JMenuItem menuItemAbrirGramatica;
    private JMenuItem menuItemAbrirLL1;
    private JMenuItem menuItemAbrirSLR;

    public AnalisisSintacticoGUI() {
        aplicarEstilosGlobales();
        initComponents();
    }

    /** --------------------------
     * ESTILOS GLOBALES AZUL FUERTE + BLANCO
     * -------------------------- */
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
     * INICIALIZACIÓN
     * -------------------------- */
    private void initComponents() {

        setTitle("Análisis Sintáctico");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color azulFuerte = new Color(13, 71, 161);
        Color blanco = Color.WHITE;

        menuBar = new JMenuBar();
        menuBar.setBackground(azulFuerte);

        // =======================================================
        // MENÚ DE AFN's
        // =======================================================
        menuAFNs = new JMenu("AFN's");
        menuAFNs.setForeground(blanco);

        menuItemBasicoAFN = new JMenuItem("Basico (Cadena)");
        menuItemBasicoAFN.addActionListener(e -> abrirDialogBasico1char());

        menuItemBasicoAFN2Char = new JMenuItem("Basico (Rango)");
        menuItemBasicoAFN2Char.addActionListener(e -> abrirDialogBasico2char()); 

        menuItemUnirAFN = new JMenuItem("Unir");
        menuItemUnirAFN.addActionListener(e -> abrirDialogUnir());

        menuItemConcatenarAFN = new JMenuItem("Concatenar");        
        menuItemConcatenarAFN.addActionListener(e -> abrirDialogConcatenar());

        menuItemCerraduraPositivaAFN = new JMenuItem("Cerradura Positiva (+)");
        menuItemCerraduraPositivaAFN.addActionListener(e -> abrirDialogCerraduraPositiva());

        menuItemCerraduraKleeneAFN = new JMenuItem("Cerradura Kleene (*)");
        menuItemCerraduraKleeneAFN.addActionListener(e -> abrirDialogCerraduraKleene());

        menuItemOpcionalAFN = new JMenuItem("Cerradura Opcional (?)");
        menuItemOpcionalAFN.addActionListener(e -> abrirDialogCerraduraOpcional());

        menuItemERtoAFN = new JMenuItem("ER -> AFN");
        menuItemERtoAFN.addActionListener(e -> abrirDialogERtoAFN());

        menuAFNs.add(menuItemBasicoAFN);
        menuAFNs.add(menuItemBasicoAFN2Char);
        menuAFNs.addSeparator();
        menuAFNs.add(menuItemUnirAFN);
        menuAFNs.add(menuItemConcatenarAFN);
        menuAFNs.add(menuItemCerraduraPositivaAFN);
        menuAFNs.add(menuItemCerraduraKleeneAFN);
        menuAFNs.add(menuItemOpcionalAFN);
        menuAFNs.addSeparator();
        menuAFNs.add(menuItemERtoAFN);


        // =======================================================
        // MENÚ DE ANALIZADOR LÉXICO 
        // =======================================================
        menuAnalizadorLexico = new JMenu("Analizador Léxico");
        menuAnalizadorLexico.setForeground(blanco);

        menuItemMostrarAFN = new JMenuItem("Mostrar AFN");
        menuItemMostrarAFN.addActionListener(e -> abrirDialogMostrarAFN());

        menuItemMostrarAFD = new JMenuItem("Mostrar AFD");
        menuItemMostrarAFD.addActionListener(e -> abrirDialogMostrarAFD());

        menuItemAFNtoAFD = new JMenuItem("Convertir AFN a AFD");        
        menuItemAFNtoAFD.addActionListener(e -> abrirDialogAFNtoAFD());

        menuItemAnalizarCadena = new JMenuItem("Analizar Cadena");
        menuItemAnalizarCadena.addActionListener(e -> abrirDialogAnalizarCadena());
        
        menuAnalizadorLexico.add(menuItemMostrarAFN);
        menuAnalizadorLexico.add(menuItemMostrarAFD);
        menuAnalizadorLexico.addSeparator();
        menuAnalizadorLexico.add(menuItemAFNtoAFD);
        menuAnalizadorLexico.addSeparator();
        menuAnalizadorLexico.add(menuItemAnalizarCadena);


        // =======================================================
        // MENÚ DE ANÁLISIS SINTÁCTICO
        // =======================================================
        menuAnalisisSintactico = new JMenu("Análisis Sintáctico");
        menuAnalisisSintactico.setForeground(blanco);

        menuItemCalculadora = new JMenuItem("Calculadora");
        menuItemCalculadora.addActionListener(e -> abrirDialogCalculadora());

        menuItemAbrirGramatica = new JMenuItem("Gramática de Gramáticas");
        menuItemAbrirGramatica.addActionListener(e -> abrirFrameGramaticaGramaticas());

        menuItemAbrirLL1 = new JMenuItem("Análisis LL(1)");
        menuItemAbrirLL1.addActionListener(e -> abrirDialogAnalisisLL1());

        menuItemAbrirSLR = new JMenuItem("Análisis SLR");
        menuItemAbrirSLR.addActionListener(e -> abrirDialogAnalisisLR0());

        menuAnalisisSintactico.add(menuItemCalculadora);
        menuAnalisisSintactico.add(menuItemAbrirGramatica);
        menuAnalisisSintactico.add(menuItemAbrirLL1);
        menuAnalisisSintactico.add(menuItemAbrirSLR);

        menuBar.add(menuAFNs);
        menuBar.add(menuAnalizadorLexico);
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
     * MÉTODOS DE APERTURA DE DIÁLOGOS
     * -------------------------- */

    private void abrirDialogBasico1char() {
        JDialog dialog = new JDialog(this, "Basico (Cadena)", true);
        dialog.setSize(400, 350);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(this);

        PanelCrearAFNBasico panel = new PanelCrearAFNBasico(createdAfnNames); 
        estilizarPanelDialog(panel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void abrirDialogBasico2char() {
        JDialog dialog = new JDialog(this, "Basico (Rango)", true);
        dialog.setSize(600, 400);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(this);

        PanelCrearAFNBasico2char panel = new PanelCrearAFNBasico2char(createdAfnNames); 
        estilizarPanelDialog(panel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void abrirDialogUnir() {
        JDialog dialog = new JDialog(this, "Unir", true);
        dialog.setSize(500, 450);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(this);

        PanelUnionAFN panel = new PanelUnionAFN(createdAfnNames);
        estilizarPanelDialog(panel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void abrirDialogConcatenar() {
        JDialog dialog = new JDialog(this, "Concatenacion", true);
        dialog.setSize(600, 450);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(this);

        PanelConcatenacionAFN panel = new PanelConcatenacionAFN(createdAfnNames); 
        estilizarPanelDialog(panel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void abrirDialogCerraduraPositiva() {
        JDialog dialog = new JDialog(this, "Cerradura Positiva ", true);
        dialog.setSize(600, 450);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(this);

        PanelCerraduraPositiva panel = new PanelCerraduraPositiva(createdAfnNames);
        estilizarPanelDialog(panel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void abrirDialogCerraduraKleene() {
        JDialog dialog = new JDialog(this, "Cerradura Kleene ", true);
        dialog.setSize(600, 450);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(this);

        PanelCerraduraKleene panel = new PanelCerraduraKleene(createdAfnNames);
        estilizarPanelDialog(panel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void abrirDialogCerraduraOpcional() {
        JDialog dialog = new JDialog(this, "Cerradura Opcional ", true);
        dialog.setSize(600, 450);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(this);

        PanelCerraduraOpcional panel = new PanelCerraduraOpcional(createdAfnNames);
        estilizarPanelDialog(panel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void abrirDialogERtoAFN() {
        JDialog dialog = new JDialog(this, "ER → AFN", true);
        dialog.setSize(600, 350);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(this);

        PanelERtoAFN panel = new PanelERtoAFN(); 
        estilizarPanelDialog(panel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // ----- MENU ANALIZADOR LEXICO  ------
    private void abrirDialogMostrarAFN() {
        JDialog dialog = new JDialog(this, "Mostrar AFN", true);
        dialog.setSize(600, 500);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(this);

        PanelMostrarAFN panel = new PanelMostrarAFN(createdAfnNames);
        estilizarPanelDialog(panel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void abrirDialogMostrarAFD() {
        JDialog dialog = new JDialog(this, "Mostrar AFD", true);
        dialog.setSize(600, 500);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(this);

        PanelMostrarAFD panel = new PanelMostrarAFD(createdAfdNames);
        estilizarPanelDialog(panel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void abrirDialogAFNtoAFD() {
        JDialog dialog = new JDialog(this, "AFN -> AFD", true);
        dialog.setSize(600, 500);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(this);

        PanelAFNtoAFD panel = new PanelAFNtoAFD(createdAfnNames, createdAfdNames);
        estilizarPanelDialog(panel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void abrirDialogAnalizarCadena() {
        JDialog dialog = new JDialog(this, "Analizar una cadena", true);
        dialog.setSize(600, 500);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLocationRelativeTo(this);

        PanelAnalizarCadena panel = new PanelAnalizarCadena(createdAfdNames);
        estilizarPanelDialog(panel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // ----- MENU ANALISIS SINTACTICO  ------
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

    private void abrirFrameGramaticaGramaticas() {

        JFrame frame = new JFrame("Gramática de Gramáticas");

        frame.setSize(1100, 700);
        frame.setLocationRelativeTo(this);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setResizable(true); 

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

    /** --------------------------
     * Helpers UI
     * -------------------------- */

    private void estilizarPanelDialog(JPanel p) {
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
    
    private void mensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    // ===================================
    // MÉTODO PARA REGISTRAR Y MANTENER LISTAS
    // ===================================
    public void registerAutomata(String name, boolean isAFD) {
        if (name == null || name.trim().isEmpty()) return;
        
        if (isAFD) {
            if (!createdAfdNames.contains(name)) createdAfdNames.add(name);
        } else {
            if (!createdAfnNames.contains(name)) createdAfnNames.add(name);
        }
        
        Collections.sort(createdAfnNames);
        Collections.sort(createdAfdNames);
        
    }


    /** --------------------------
     * MAIN
     * -------------------------- */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AnalisisSintacticoGUI().setVisible(true));
    }
}