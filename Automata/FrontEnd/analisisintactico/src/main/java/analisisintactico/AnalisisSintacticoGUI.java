package analisisintactico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AnalisisSintacticoGUI extends JFrame {
    
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
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Análisis Sintáctico");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        menuBar = new JMenuBar();
        
        menuAFNs = new JMenu("AFN's");
        
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
        
        menuItemBasicoAFN.addActionListener(e -> abrirDialogAFN("Básico"));
        menuItemUnirAFN.addActionListener(e -> abrirDialogAFN("Unir"));
        menuItemConcatenarAFN.addActionListener(e -> abrirDialogAFN("Concatenar"));
        menuItemCerraduraPositivaAFN.addActionListener(e -> abrirDialogAFN("Cerradura +"));
        menuItemCerraduraKleeneAFN.addActionListener(e -> abrirDialogAFN("Cerradura *"));
        menuItemOpcionalAFN.addActionListener(e -> abrirDialogAFN("Opcional"));
        menuItemERtoAFN.addActionListener(e -> abrirDialogERtoAFN());
        menuItemUnionAnalizadorLexico.addActionListener(e -> abrirDialogAFN("Unión para Analizador Léxico"));
        menuItemAFNtoAFD.addActionListener(e -> abrirDialogAFN("Convertir AFN a AFD"));
        menuItemAnalizarCadena.addActionListener(e -> abrirDialogAFN("Analizar una cadena"));
        menuItemProbarAnalizadorLexico.addActionListener(e -> abrirDialogAFN("Probar analizador Léxico"));
        
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
        menuAFNs.addSeparator();
        menuAFNs.add(menuItemAnalizarCadena);
        menuAFNs.add(menuItemProbarAnalizadorLexico);
        
        menuAnalisisSintactico = new JMenu("Análisis Sintáctico");
        
        menuDescensoRecursivo = new JMenu("Descenso Recursivo");
        menuItemCalculadora = new JMenuItem("Calculadora");
        menuItemCalculadora.addActionListener(e -> abrirDialogCalculadora());
        menuDescensoRecursivo.add(menuItemCalculadora);
        
        menuDescRecGramdeGram = new JMenu("Descenso Rec Gramática de Gramáticas");
        menuItemAbrirGramatica = new JMenuItem("Abrir Gramática");
        menuItemAbrirGramatica.addActionListener(e -> abrirDialogGramaticaGramaticas());
        menuDescRecGramdeGram.add(menuItemAbrirGramatica);
        
        menuAnalisisLL1 = new JMenu("Análisis LL(1)");
        menuItemAbrirLL1 = new JMenuItem("Abrir LL(1)");
        menuItemAbrirLL1.addActionListener(e -> abrirDialogAnalisisSintactico("Análisis LL(1)"));
        menuAnalisisLL1.add(menuItemAbrirLL1);
        
        menuAnalisisSLR = new JMenu("Análisis SLR");
        menuItemAbrirSLR = new JMenuItem("Abrir SLR");
        menuItemAbrirSLR.addActionListener(e -> abrirDialogAnalisisSintactico("Análisis SLR"));
        menuAnalisisSLR.add(menuItemAbrirSLR);
        
        menuAnalisisLRCanonico = new JMenu("Análisis LR Canónico");
        menuItemAbrirLRCanonico = new JMenuItem("Abrir LR Canónico");
        menuItemAbrirLRCanonico.addActionListener(e -> abrirDialogAnalisisSintactico("Análisis LR Canónico"));
        menuAnalisisLRCanonico.add(menuItemAbrirLRCanonico);
        
        menuMatrices = new JMenu("Ejemplo matrices");
        menuItemAbrirMatrices = new JMenuItem("Abrir Matrices");
        menuItemAbrirMatrices.addActionListener(e -> abrirDialogAnalisisSintactico("Ejemplo Matrices"));
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
        
        panelContenidoPrincipal = new JPanel(new BorderLayout());
        panelContenidoPrincipal.setBackground(Color.WHITE);
        
        JLabel lblBienvenida = new JLabel("Bienvenido al Análisis Sintáctico", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 24));
        lblBienvenida.setForeground(new Color(60, 60, 60));
        panelContenidoPrincipal.add(lblBienvenida, BorderLayout.CENTER);
        
        add(panelContenidoPrincipal);
    }
    
    private void abrirDialogAFN(String funcionalidad) {
        JDialog dialog = new JDialog(this, "AFN: " + funcionalidad, true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void abrirDialogAnalisisSintactico(String funcionalidad) {
        JDialog dialog = new JDialog(this, "Análisis Sintáctico: " + funcionalidad, true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void abrirDialogCalculadora() {
        JDialog dialog = new JDialog(this, "Analizador Léxico / Calculadora", true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        
        PanelCalculadora panelCalc = new PanelCalculadora();
        
        dialog.add(panelCalc);
        dialog.setVisible(true);
    }
    
    private void abrirDialogERtoAFN() {
        JDialog dialog = new JDialog(this, "ER -> AFN", true);
        dialog.setSize(650, 400);
        dialog.setLocationRelativeTo(this);
        
        PanelERtoAFN panelER = new PanelERtoAFN();
        
        dialog.add(panelER);
        dialog.setVisible(true);
    }
    
    private void abrirDialogGramaticaGramaticas() {
        JDialog dialog = new JDialog(this, "Gramática de Gramáticas", true);
        dialog.setSize(900, 650);
        dialog.setLocationRelativeTo(this);
        
        PanelGramaticaGramaticas panelGram = new PanelGramaticaGramaticas();
        
        dialog.add(panelGram);
        dialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            AnalisisSintacticoGUI frame = new AnalisisSintacticoGUI();
            frame.setVisible(true);
        });
    }
}