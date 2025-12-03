package com.automata.frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Arrays;

import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import BackEnd.AFN;
import BackEnd.AFD;
import BackEnd.Estado;
import BackEnd.Transicion;
import BackEnd.EdoAFD;
import BackEnd.SimbEspeciales;
import BackEnd.AFD;
import BackEnd.RegexAFNBuilder;

public class AutomataMenuGUI extends JFrame {

    // Paleta de colores homogénea en tonalidades azules
    private static final Color COLOR_AZUL_PRINCIPAL = new Color(30, 60, 114);
    private static final Color COLOR_AZUL_SECUNDARIO = new Color(45, 85, 135);
    private static final Color COLOR_AZUL_MEDIO = new Color(65, 105, 155);
    private static final Color COLOR_AZUL_CLARO = new Color(85, 125, 175);
    private static final Color COLOR_AZUL_SUAVE = new Color(225, 235, 245);
    private static final Color COLOR_AZUL_ACCENT = new Color(100, 149, 237);
    private static final Color COLOR_FONDO_PRINCIPAL = new Color(240, 245, 250);
    private static final Color COLOR_TEXTO_CLARO = new Color(245, 248, 252);
    private static final Color COLOR_TEXTO_OSCURO = new Color(25, 50, 95);

    private JMenuBar menuBar;
    private JPanel mainPanel;
    private JPanel panelLateral;
    private JPanel panelCentralContenido;
    private JPanel tarjetaBienvenida;

    private JButton btnToggleSidebar;
    private boolean sidebarVisible = true;

    // Lista para almacenar los nombres de los autómatas creados
    private final List<String> createdAutomataNames = new ArrayList<>();
    private final Map<String, AFN> afnMap = new HashMap<>();
    private final List<String> createdAfdNames = new ArrayList<>();
    private final Map<String, AFD> afdMap = new HashMap<>();
    private final Map<Character, Integer> tokenPorSimbolo = new HashMap<>();
    private final RegexAFNBuilder regexBuilder = new RegexAFNBuilder();
    private int nextTokenValue = 40;

    // JComboBoxes para las operaciones
    private JComboBox<String> comboUnion1;
    private JComboBox<String> comboUnion2;
    private JComboBox<String> comboConcatenacion1;
    private JComboBox<String> comboConcatenacion2;
    private JComboBox<String> comboCerraduraPositiva;
    private JComboBox<String> comboCerraduraKleene;
    private JComboBox<String> comboCerraduraOpcional;
    private JComboBox<String> comboMostrarAFN;
    private JComboBox<String> comboMostrarAFD;


    public AutomataMenuGUI() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Analizador Léxico - Taller de Autómatas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        createMenuBar();

        mainPanel = new RoundedPanel(15);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(COLOR_FONDO_PRINCIPAL);

        createSidePanel();
        createCentralPanel();

        mainPanel.add(panelLateral, BorderLayout.WEST);
        mainPanel.add(panelCentralContenido, BorderLayout.CENTER);

        add(mainPanel);
    }
    
    // Método auxiliar que ayuda a crear los JMenu con el estilo uniforme
    private JMenu createBaseMenu(String name, String iconName) {
        JMenu menu = new JMenu(name);
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        menu.setForeground(COLOR_TEXTO_CLARO);

        ImageIcon icon = loadIcon(iconName);
        if (icon != null) {
            menu.setIcon(icon);
        }
        return menu;
    }

    private void createMenuBar() {
        menuBar = new JMenuBar() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        menuBar.setBackground(COLOR_AZUL_PRINCIPAL);
        menuBar.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        btnToggleSidebar = new RoundedButton(loadIcon("hamburger.png") != null ? "" : "☰", 8);
        if (loadIcon("hamburger.png") != null) {
            btnToggleSidebar.setIcon(loadIcon("hamburger.png"));
        } else {
            btnToggleSidebar.setText("☰");
        }
        btnToggleSidebar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnToggleSidebar.setForeground(COLOR_TEXTO_CLARO);
        btnToggleSidebar.setBackground(COLOR_AZUL_PRINCIPAL);
        btnToggleSidebar.setPreferredSize(new Dimension(40, 30));
        btnToggleSidebar.addActionListener(e -> toggleSidebar());

        btnToggleSidebar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnToggleSidebar.setBackground(COLOR_AZUL_CLARO);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnToggleSidebar.setBackground(COLOR_AZUL_PRINCIPAL);
            }
        });

        menuBar.add(btnToggleSidebar);
        menuBar.add(Box.createHorizontalStrut(15));

        // =======================================================
        // MENÚ ARCHIVO
        // =======================================================
        JMenu menuArchivo = createBaseMenu("Archivo", "archivo.png");
        JMenuItem itemNuevo = new JMenuItem("Nuevo Proyecto");
        JMenuItem itemGuardar = new JMenuItem("Guardar Autómata");
        JMenuItem itemCargar = new JMenuItem("Cargar Autómata");
        JMenuItem itemSalir = new JMenuItem("Salir");
        
        itemSalir.addActionListener(e -> System.exit(0)); 
        itemNuevo.addActionListener(e -> JOptionPane.showMessageDialog(this, "Se limpiará el espacio de trabajo para un Nuevo Proyecto.", "Nuevo Proyecto", JOptionPane.INFORMATION_MESSAGE));
        itemGuardar.addActionListener(e -> showSaveDialog()); // Al darle clic se abre un diálogo de guardar
        itemCargar.addActionListener(e -> showLoadDialog()); // Al darle clic se abre un diálogo de cargar
        
        menuArchivo.add(itemNuevo);
        menuArchivo.add(itemGuardar);
        menuArchivo.add(itemCargar);
        menuArchivo.addSeparator(); 
        menuArchivo.add(itemSalir);
        menuBar.add(menuArchivo);

        // =======================================================
        // MENÚ INICIO (Al darle clic regresa al menú principal)
        // =======================================================
        JMenu menuInicio = createBaseMenu("Inicio", "inicio.png");
        JMenuItem itemMenuPrincipal = new JMenuItem("Ir a Menú Principal");
        
        itemMenuPrincipal.addActionListener(e -> showWelcomePanel()); 
        
        menuInicio.add(itemMenuPrincipal);
        menuBar.add(menuInicio);

        // =======================================================
        // MENÚ HERRAMIENTAS
        // =======================================================
        JMenu menuHerramientas = createBaseMenu("Herramientas", "herramientas.png");
        JMenuItem itemConvAFNtoAFD = new JMenuItem("Convertir AFN a AFD");
        JMenuItem itemMinimizacion = new JMenuItem("Minimizar AFD");
        JMenuItem itemRegexToAFD = new JMenuItem("AFD desde ER (archivo)");
        
        itemConvAFNtoAFD.addActionListener(e -> showConversionDialog()); // Al darle clic se abre el diálogo de conversión
        itemMinimizacion.addActionListener(e -> showMinimizationDialog()); // Al darle clic se abre el diálogo de minimización
        itemRegexToAFD.addActionListener(e -> construirAFDDesdeArchivoER());

        menuHerramientas.add(itemConvAFNtoAFD);
        menuHerramientas.add(itemMinimizacion);
        menuHerramientas.add(itemRegexToAFD);
        menuBar.add(menuHerramientas);

        // =======================================================
        // MENÚ AYUDA
        // =======================================================
        JMenu menuAyuda = createBaseMenu("Ayuda", "ayuda.png");
        JMenuItem itemAcercaDe = new JMenuItem("Acerca de...");
        JMenuItem itemManual = new JMenuItem("Manual de Usuario");
        
        itemAcercaDe.addActionListener(e -> showAboutDialog()); // Al darle clic se abre el diálogo "Acerca de"
        itemManual.addActionListener(e -> showManualDialog()); // Al darle clic se abre el diálogo del manual
        
        menuAyuda.add(itemAcercaDe);
        menuAyuda.add(itemManual);
        menuBar.add(menuAyuda);
        
        setJMenuBar(menuBar);
    }
    
    // =================================================================================
    // MÉTODOS DE IMPLEMENTACIÓN DE DIÁLOGOS DE MENÚ SUPERIOR 
    // =================================================================================

    // Muestra un JFileChooser para simular la acción de Guardar
    private void showSaveDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Autómata");
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (comboMostrarAFD == null || comboMostrarAFD.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "No hay AFD seleccionados para guardar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String seleccionado = (String) comboMostrarAFD.getSelectedItem();
            AFD afd = afdMap.get(seleccionado);
            if (afd == null) {
                JOptionPane.showMessageDialog(this, "No se pudo recuperar el AFD seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean ok = afd.SaveAFD(fileToSave.getAbsolutePath());
            if (ok) {
                JOptionPane.showMessageDialog(this, "AFD guardado exitosamente en:\n" + fileToSave.getAbsolutePath(), "Guardado Completo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar el AFD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Construye un AFD léxico desde un archivo de ER (formato: token|expresion_regular)
    private void construirAFDDesdeArchivoER() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecciona archivo de ER (token|ER)");
        int res = chooser.showOpenDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File archivo = chooser.getSelectedFile();
        try {
            java.util.List<String> lineas = java.nio.file.Files.readAllLines(archivo.toPath());
            AFN unido = null;
            for (String l : lineas) {
                if (l == null) continue;
                String linea = l.trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;
                String[] partes = linea.split("\\|", 2);
                if (partes.length != 2) continue;
                int token = Integer.parseInt(partes[0].trim());
                String er = partes[1].trim();
                AFN afn = regexBuilder.construir(er, token);
                if (unido == null) {
                    unido = afn;
                } else {
                    unido.AFN_union(afn);
                }
            }
            if (unido == null) {
                JOptionPane.showMessageDialog(this, "No se construyó ningún AFN. Revisa el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AFD afd = unido.toAFD();
            String base = archivo.getName();
            if (base.contains(".")) {
                base = base.substring(0, base.lastIndexOf('.'));
            }
            String nombreAFD = base + "_AFD";
            int suf = 1;
            while (afdMap.containsKey(nombreAFD)) {
                nombreAFD = base + "_AFD_" + (suf++);
            }
            registerAFD(nombreAFD, afd);
            JOptionPane.showMessageDialog(this, "AFD construido y registrado como '" + nombreAFD + "'.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            showAfdDetails("AFD generado", nombreAFD, afd);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al construir AFD: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Muestra un JFileChooser para simular la acción de Cargar
    private void showLoadDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Cargar Autómata");
        
        int userSelection = fileChooser.showOpenDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            try {
                AFD afd = new AFD();
                boolean ok = afd.LoadAFD(fileToLoad.getAbsolutePath());
                if (ok) {
                    String nombreAFD = fileToLoad.getName();
                    registerAFD(nombreAFD, afd);
                    JOptionPane.showMessageDialog(this, "Autómata cargado desde:\n" + fileToLoad.getAbsolutePath(), "Carga Completa", JOptionPane.INFORMATION_MESSAGE);
                    showAfdDetails("AFD cargado", nombreAFD, afd);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo cargar el AFD.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar AFD: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Muestra el Diálogo de Conversión (Herramientas)
    private void showConversionDialog() {
        if(createdAutomataNames.isEmpty()){
            JOptionPane.showMessageDialog(this, "No hay AFN disponibles. Crea uno primero.", "Conversión AFN → AFD", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(COLOR_AZUL_SUAVE);

        JLabel lblTitulo = new JLabel("Selecciona el AFN a convertir:");
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(COLOR_TEXTO_OSCURO);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(12));

        JComboBox<String> comboAFN = new JComboBox<>(createdAutomataNames.toArray(new String[0]));
        comboAFN.setMaximumSize(new Dimension(240, 30));
        panel.add(comboAFN);
        panel.add(Box.createVerticalStrut(16));

        JLabel lblNombre = new JLabel("Nombre para el nuevo AFD (opcional):");
        lblNombre.setAlignmentX(CENTER_ALIGNMENT);
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNombre.setForeground(COLOR_TEXTO_OSCURO);
        panel.add(lblNombre);

        JTextField txtNombre = new JTextField();
        txtNombre.setMaximumSize(new Dimension(240, 30));
        panel.add(txtNombre);
        panel.add(Box.createVerticalStrut(18));

        JButton btnConvertir = new RoundedButton("Convertir", 10);
        btnConvertir.setBackground(COLOR_AZUL_ACCENT);
        btnConvertir.setForeground(Color.WHITE);
        btnConvertir.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(btnConvertir);

        JDialog dialog = new JDialog(this, "Herramientas: Convertir AFN a AFD", true);
        dialog.getContentPane().add(panel);
        dialog.setSize(420, 260);
        dialog.setLocationRelativeTo(this);

        btnConvertir.addActionListener(ev -> {
            String seleccionado = (String) comboAFN.getSelectedItem();
            if(seleccionado == null){
                JOptionPane.showMessageDialog(dialog, "Selecciona un AFN válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String nombreAFD = txtNombre.getText().trim();
            if(nombreAFD.isEmpty()){
                nombreAFD = seleccionado + "_AFD";
            }
            if(afdMap.containsKey(nombreAFD)){
                JOptionPane.showMessageDialog(dialog, "Ya existe un AFD con el nombre '" + nombreAFD + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try{
                AFN base = afnMap.get(seleccionado);
                if(base == null){
                    JOptionPane.showMessageDialog(dialog, "No fue posible recuperar el AFN seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                AFD afd = base.copia().toAFD();
                registerAFD(nombreAFD, afd);
                dialog.dispose();
                showAfdDetails("AFD generado", nombreAFD, afd);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(dialog, "Ocurrió un error durante la conversión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }
    
    // Método auxiliar para crear una tarjeta simple con un título (para vistas pendientes)
private JPanel createDefaultCard(String title) {
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
    panel.setBackground(COLOR_AZUL_SUAVE);
    
    JLabel lblTitle = new JLabel("IMPLEMENTACIÓN PENDIENTE: " + title);
    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
    lblTitle.setForeground(COLOR_TEXTO_OSCURO);
    
    panel.add(lblTitle);
    return panel;
}
    
    // Muestra el Diálogo de Minimización (Herramientas)
    private void showMinimizationDialog() {
        JPanel content = createDefaultCard("Funcionalidad: Minimización de AFD");
        JLabel hint = new JLabel("Aquí se implementaría la selección de un AFD existente para minimizarlo.");
        content.add(hint);
        
        JDialog dialog = new JDialog(this, "Herramientas: Minimizar AFD", true);
        dialog.getContentPane().add(content);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    // Muestra el Diálogo "Acerca de"
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this, 
            "Analizador Léxico y Taller de Autómatas\n\n" +
            "Versión: 1.0\n" +
            "Objetivo: Creación, Operación y Visualización de AFN/AFD.\n" +
            "Desarrollado por: Equipo 9\n" +
            "Fernández Anguiano Guillermo Jesus\n"+
            "Gonzalez Tetuan Hector David\n"+
            "Ramirez Piña Cinthya Lucila\n",
            "Acerca de esta Aplicación", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Muestra el Diálogo del Manual de Usuario
    private void showManualDialog() {
         JOptionPane.showMessageDialog(this, 
            "MANUAL DE USUARIO\n\n" +
            "1. Crear: Selecciona una operación del panel lateral.\n" +
            "2. Operar: Los resultados de las operaciones (Unión, Kleene) crean nuevos autómatas.\n" +
            "3. Mostrar: Usa 'Mostrar AFN/AFD' para ver la representación gráfica.\n" +
            "4. Guardar: Usa 'Archivo > Guardar Autómata' para persistir tu trabajo.", 
            "Ayuda: Manual de Usuario", 
            JOptionPane.QUESTION_MESSAGE);
    }
    
    // =================================================================================
    // ESTRUCTURA PRINCIPAL DE LA INTERFAZ 
    // =================================================================================


    private void createSidePanel() {
        panelLateral = new RoundedPanel(15);
        panelLateral.setBackground(COLOR_AZUL_SECUNDARIO);
        panelLateral.setPreferredSize(new Dimension(280, getHeight()));
        panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
        panelLateral.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));

        JLabel lblCrearAutomata = createSidebarHeader("1. Crear Autómata");
        panelLateral.add(lblCrearAutomata);
        panelLateral.add(Box.createVerticalStrut(15));

        panelLateral.add(createSidebarButton(" AFN Básico", "CrearAFN1", COLOR_AZUL_ACCENT, "afn_basico.png"));
        panelLateral.add(Box.createVerticalStrut(6));
        panelLateral.add(createSidebarButton(" AFD Básico", "CrearAFD1", COLOR_AZUL_ACCENT, "afd_basico.png"));
        panelLateral.add(Box.createVerticalStrut(6));
        panelLateral.add(createSidebarButton(" Unión", "Union", COLOR_AZUL_MEDIO, "union.png"));
        panelLateral.add(Box.createVerticalStrut(6));
        panelLateral.add(createSidebarButton(" Concatenación", "Concatenacion", COLOR_AZUL_CLARO, "concatenacion.png"));
        panelLateral.add(Box.createVerticalStrut(6));
        panelLateral.add(createSidebarButton(" Cerradura Positiva", "CerraduraPositiva", COLOR_AZUL_ACCENT, "cerradura_positiva.png"));
        panelLateral.add(Box.createVerticalStrut(6));
        panelLateral.add(createSidebarButton(" Cerradura Kleene", "CerraduraKleene", COLOR_AZUL_MEDIO, "cerradura_kleene.png"));
        panelLateral.add(Box.createVerticalStrut(6));
        panelLateral.add(createSidebarButton(" Cerradura Opcional", "CerraduraOpcional", COLOR_AZUL_CLARO, "cerradura_opcional.png"));
        panelLateral.add(Box.createVerticalStrut(25));

        JLabel lblMostrarAutomata = createSidebarHeader("2. Mostrar Autómata");
        panelLateral.add(lblMostrarAutomata);
        panelLateral.add(Box.createVerticalStrut(15));

        panelLateral.add(createSidebarButton(" Mostrar AFN", "MostrarAFN", COLOR_AZUL_ACCENT, "mostrar_afn.png"));
        panelLateral.add(Box.createVerticalStrut(6));
        panelLateral.add(createSidebarButton(" Mostrar AFD", "MostrarAFD", COLOR_AZUL_MEDIO, "mostrar_afd.png"));
    }

    private void createCentralPanel() {
        panelCentralContenido = new RoundedPanel(15);
        panelCentralContenido.setLayout(new BorderLayout()); 
        panelCentralContenido.setBackground(COLOR_AZUL_SUAVE);

        createWelcomePanel();

        panelCentralContenido.add(tarjetaBienvenida, BorderLayout.CENTER);
        mainPanel.add(panelCentralContenido, BorderLayout.CENTER);
    }

    private void createWelcomePanel() {
        tarjetaBienvenida = new RoundedPanel(15);
        tarjetaBienvenida.setLayout(new BoxLayout(tarjetaBienvenida, BoxLayout.Y_AXIS));
        tarjetaBienvenida.setBackground(COLOR_AZUL_SUAVE);
        tarjetaBienvenida.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel lblTitulo = new JLabel("¡Bienvenido al Analizador Léxico");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblTitulo.setForeground(COLOR_AZUL_PRINCIPAL);
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        tarjetaBienvenida.add(lblTitulo);
        tarjetaBienvenida.add(Box.createVerticalStrut(30));

        AutomataGraphPanel graphPanel = new AutomataGraphPanel();
        graphPanel.setAlignmentX(CENTER_ALIGNMENT);
        graphPanel.setMaximumSize(new Dimension(400, 300));
        graphPanel.setPreferredSize(new Dimension(400, 300));
        tarjetaBienvenida.add(graphPanel);
        tarjetaBienvenida.add(Box.createVerticalStrut(30));

        JLabel lblMensaje = new JLabel("Utiliza el menú superior o el panel lateral para seleccionar una opción.");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblMensaje.setForeground(COLOR_AZUL_MEDIO);
        lblMensaje.setAlignmentX(CENTER_ALIGNMENT);
        tarjetaBienvenida.add(lblMensaje);

        tarjetaBienvenida.add(Box.createVerticalGlue());
    }

    // Método que implementa la lógica de 'Inicio' para regresar a la vista principal
    private void showWelcomePanel() {
        panelCentralContenido.removeAll(); // Quita el panel que esté actualmente
        panelCentralContenido.add(tarjetaBienvenida, BorderLayout.CENTER); // Agrega la tarjeta de bienvenida
        panelCentralContenido.revalidate(); // Le dice al contenedor que re-dibuje
        panelCentralContenido.repaint(); // Fuerza el re-dibujo
    }

    private JLabel createSidebarHeader(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(COLOR_AZUL_SUAVE);
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    private JButton createSidebarButton(String text, String cardName, Color accentColor, String iconName) {
        RoundedButton button = new RoundedButton(text, 10);
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setPreferredSize(new Dimension(240, 40));
        button.setBackground(COLOR_AZUL_PRINCIPAL);
        button.setForeground(COLOR_TEXTO_CLARO);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        ImageIcon icon = loadIcon(iconName);
        if (icon != null) {
            button.setIcon(icon);
        }

        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, accentColor),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(COLOR_AZUL_CLARO);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(COLOR_AZUL_PRINCIPAL);
            }
        });

        button.addActionListener(e -> {
            if (cardName != null && !cardName.isEmpty()) {
                JPanel dialogContent = getDialogContent(cardName);
                if (dialogContent != null) {
                    showAutomataDialog(text, dialogContent); // Al darle clic se abre un diálogo
                }
            }
        });

        return button;
    }

    private JPanel getDialogContent(String cardName) {
        switch (cardName) {
            case "CrearAFN1":
                return createAFNPanel();
            case "CrearAFD1":
                return createAFDPanel();
            case "Union":
                return createUnionPanel();
            case "Concatenacion":
                return createConcatenacionPanel();
            case "CerraduraPositiva":
                return createCerraduraPositivaPanel();
            case "CerraduraKleene":
                return createCerraduraKleenePanel();
            case "CerraduraOpcional":
                return createCerraduraOpcionalPanel();
            case "MostrarAFN":
                return createMostrarAFNPanel();
            case "MostrarAFD":
                return createMostrarAFDPanel();
            default:
                JPanel defaultPanel = new RoundedPanel(15);
                defaultPanel.setBackground(COLOR_AZUL_SUAVE);
                JLabel defaultLabel = new JLabel("Contenido de " + cardName);
                defaultLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
                defaultLabel.setForeground(COLOR_TEXTO_OSCURO);
                defaultPanel.add(defaultLabel);
                return defaultPanel;
        }
    }

    // Método para crear el panel de creación de AFN
    private JPanel createAFNPanel() {
        JPanel panel = new RoundedPanel(15);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setBackground(COLOR_AZUL_SUAVE);

        JLabel lblTitulo = new JLabel("Crear AFN Básico");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(20));

        JLabel lblNombre = new JLabel("Nombre del autómata:");
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblNombre.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblNombre);

        JTextField txtNombre = new JTextField(10);
        txtNombre.setMaximumSize(new Dimension(200, 30));
        panel.add(txtNombre);
        panel.add(Box.createVerticalStrut(10));

        JLabel lblInput = new JLabel("Ingrese una sentencia (ej. 'ab'):");
        lblInput.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblInput.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblInput);

        JTextField txtInput = new JTextField(10);
        txtInput.setMaximumSize(new Dimension(200, 30));
        panel.add(txtInput);
        panel.add(Box.createVerticalStrut(20));

        JButton btnCrear = new RoundedButton("Crear AFN", 10);
        btnCrear.setBackground(COLOR_AZUL_ACCENT);
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setAlignmentX(CENTER_ALIGNMENT);
        btnCrear.addActionListener(e -> {
            String automataName = txtNombre.getText().trim();
            String input = txtInput.getText().trim();

            if (automataName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un nombre para el autómata.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (afnMap.containsKey(automataName)) {
                JOptionPane.showMessageDialog(this, "Ya existe un autómata con el nombre '" + automataName + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese una sentencia.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                AFN nuevo = AFN.fromString(input);
                int token = calcularTokenParaLexema(input);
                registerAFN(automataName, nuevo, token);
                showAutomataDetails("AFN creado", automataName, nuevo);
                txtNombre.setText("");
                txtInput.setText("");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "No se pudo crear el AFN: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(btnCrear);

        return panel;
    }


    // Método para crear el panel de creación de AFD
    private JPanel createAFDPanel() {
        JPanel panel = new RoundedPanel(15);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setBackground(COLOR_AZUL_SUAVE);

        JLabel lblTitulo = new JLabel("Crear AFD Básico");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(20));

        JLabel lblInput = new JLabel("Ingrese dos caracteres (ej. 'ab'):");
        lblInput.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblInput.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblInput);

        JTextField txtInput = new JTextField(10);
        txtInput.setMaximumSize(new Dimension(200, 30));
        panel.add(txtInput);
        panel.add(Box.createVerticalStrut(20));

        JButton btnCrear = new RoundedButton("Crear AFD", 10);
        btnCrear.setBackground(COLOR_AZUL_ACCENT);
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setAlignmentX(CENTER_ALIGNMENT);
        btnCrear.addActionListener(e -> {
            String input = txtInput.getText().trim();
            if (input.length() == 2) {
                // Lógica de creación del AFD
                JOptionPane.showMessageDialog(this, "AFD creado para los caracteres: '" + input + "'", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese exactamente dos caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(btnCrear);

        return panel;
    }


    // Método para crear el panel de Unión
    private JPanel createUnionPanel() {
        JPanel panel = new RoundedPanel(15);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setBackground(COLOR_AZUL_SUAVE);

        JLabel lblTitulo = new JLabel("Operación: Unión de Autómatas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(20));

        JLabel lblAutomata1 = new JLabel("Seleccione el primer autómata:");
        lblAutomata1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblAutomata1.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblAutomata1);

        comboUnion1 = new JComboBox<>(createdAutomataNames.toArray(new String[0]));
        comboUnion1.setMaximumSize(new Dimension(200, 30));
        panel.add(comboUnion1);
        panel.add(Box.createVerticalStrut(10));

        JLabel lblAutomata2 = new JLabel("Seleccione el segundo autómata:");
        lblAutomata2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblAutomata2.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblAutomata2);

        comboUnion2 = new JComboBox<>(createdAutomataNames.toArray(new String[0]));
        comboUnion2.setMaximumSize(new Dimension(200, 30));
        panel.add(comboUnion2);
        panel.add(Box.createVerticalStrut(20));

        JButton btnUnir = new RoundedButton("Realizar Unión", 10);
        btnUnir.setBackground(COLOR_AZUL_ACCENT);
        btnUnir.setForeground(Color.WHITE);
        btnUnir.setAlignmentX(CENTER_ALIGNMENT);
        btnUnir.addActionListener(e -> {
            String auto1 = (String) comboUnion1.getSelectedItem();
            String auto2 = (String) comboUnion2.getSelectedItem();
            if (auto1 == null || auto2 == null) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione dos autómatas válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AFN afn1 = afnMap.get(auto1);
            AFN afn2 = afnMap.get(auto2);
            if (afn1 == null || afn2 == null) {
                JOptionPane.showMessageDialog(this, "No se pudieron recuperar los autómatas seleccionados.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            java.util.List<String> eliminar = Arrays.asList(auto1, auto2);
            String sugerencia = auto1 + "_U_" + auto2;
            String nuevoNombre = solicitarNombreDisponible(sugerencia, eliminar);
            if (nuevoNombre != null) {
                try {
                    AFN resultado = AFN.unir(afn1, afn2);
                    removeAFNs(eliminar);
                    registerAFN(nuevoNombre, resultado, null);
                    showAutomataDetails("Unión completada", nuevoNombre, resultado);
                } catch (Exception exUnion) {
                    JOptionPane.showMessageDialog(this, "No se pudo realizar la unión: " + exUnion.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(btnUnir);

        return panel;
    }


    // Método para crear el panel de Concatenación
    private JPanel createConcatenacionPanel() {
        JPanel panel = new RoundedPanel(15);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setBackground(COLOR_AZUL_SUAVE);

        JLabel lblTitulo = new JLabel("Operación: Concatenación de Autómatas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(20));

        JLabel lblAutomata1 = new JLabel("Seleccione el primer autómata:");
        lblAutomata1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblAutomata1.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblAutomata1);

        comboConcatenacion1 = new JComboBox<>(createdAutomataNames.toArray(new String[0]));
        comboConcatenacion1.setMaximumSize(new Dimension(200, 30));
        panel.add(comboConcatenacion1);
        panel.add(Box.createVerticalStrut(10));

        JLabel lblAutomata2 = new JLabel("Seleccione el segundo autómata:");
        lblAutomata2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblAutomata2.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblAutomata2);

        comboConcatenacion2 = new JComboBox<>(createdAutomataNames.toArray(new String[0]));
        comboConcatenacion2.setMaximumSize(new Dimension(200, 30));
        panel.add(comboConcatenacion2);
        panel.add(Box.createVerticalStrut(20));

        JButton btnConcatenar = new RoundedButton("Realizar Concatenación", 10);
        btnConcatenar.setBackground(COLOR_AZUL_ACCENT);
        btnConcatenar.setForeground(Color.WHITE);
        btnConcatenar.setAlignmentX(CENTER_ALIGNMENT);
        btnConcatenar.addActionListener(e -> {
            String auto1 = (String) comboConcatenacion1.getSelectedItem();
            String auto2 = (String) comboConcatenacion2.getSelectedItem();
            if (auto1 == null || auto2 == null) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione dos autómatas válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AFN afn1 = afnMap.get(auto1);
            AFN afn2 = afnMap.get(auto2);
            if (afn1 == null || afn2 == null) {
                JOptionPane.showMessageDialog(this, "No se pudieron recuperar los autómatas seleccionados.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            java.util.List<String> eliminar = Arrays.asList(auto1, auto2);
            String sugerencia = auto1 + "_C_" + auto2;
            String nuevoNombre = solicitarNombreDisponible(sugerencia, eliminar);
            if (nuevoNombre != null) {
                try {
                    AFN resultado = AFN.concatenar(afn1, afn2);
                    removeAFNs(eliminar);
                    registerAFN(nuevoNombre, resultado, null);
                    showAutomataDetails("Concatenación completada", nuevoNombre, resultado);
                } catch (Exception exJoin) {
                    JOptionPane.showMessageDialog(this, "No se pudo realizar la concatenación: " + exJoin.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(btnConcatenar);

        return panel;
    }


    // Método para crear el panel de Cerradura Positiva
    private JPanel createCerraduraPositivaPanel() {
        JPanel panel = new RoundedPanel(15);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setBackground(COLOR_AZUL_SUAVE);

        JLabel lblTitulo = new JLabel("Cerradura Positiva");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(20));

        JLabel lblAutomata = new JLabel("Seleccione el autómata:");
        lblAutomata.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblAutomata.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblAutomata);

        comboCerraduraPositiva = new JComboBox<>(createdAutomataNames.toArray(new String[0]));
        comboCerraduraPositiva.setMaximumSize(new Dimension(200, 30));
        panel.add(comboCerraduraPositiva);
        panel.add(Box.createVerticalStrut(20));

        JButton btnCerradura = new RoundedButton("Aplicar Cerradura Positiva", 10);
        btnCerradura.setBackground(COLOR_AZUL_ACCENT);
        btnCerradura.setForeground(Color.WHITE);
        btnCerradura.setAlignmentX(CENTER_ALIGNMENT);
        btnCerradura.addActionListener(e -> {
            String auto = (String) comboCerraduraPositiva.getSelectedItem();
            if (auto == null) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un autómata válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AFN base = afnMap.get(auto);
            if (base == null) {
                JOptionPane.showMessageDialog(this, "No se pudo recuperar el autómata '" + auto + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            java.util.List<String> eliminar = java.util.Collections.singletonList(auto);
            String nuevoNombre = solicitarNombreDisponible(auto + "_pos", eliminar);
            if (nuevoNombre != null) {
                try {
                    AFN resultado = AFN.cerraduraPositiva(base);
                    removeAFNs(eliminar);
                    registerAFN(nuevoNombre, resultado, null);
                    showAutomataDetails("Cerradura positiva", nuevoNombre, resultado);
                } catch (Exception exCPos) {
                    JOptionPane.showMessageDialog(this, "No se pudo aplicar la cerradura positiva: " + exCPos.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(btnCerradura);

        return panel;
    }


    // Método para crear el panel de Cerradura Kleene
    private JPanel createCerraduraKleenePanel() {
        JPanel panel = new RoundedPanel(15);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setBackground(COLOR_AZUL_SUAVE);

        JLabel lblTitulo = new JLabel("Cerradura Kleene");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(20));

        JLabel lblAutomata = new JLabel("Seleccione el autómata:");
        lblAutomata.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblAutomata.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblAutomata);

        comboCerraduraKleene = new JComboBox<>(createdAutomataNames.toArray(new String[0]));
        comboCerraduraKleene.setMaximumSize(new Dimension(200, 30));
        panel.add(comboCerraduraKleene);
        panel.add(Box.createVerticalStrut(20));

        JButton btnCerradura = new RoundedButton("Aplicar Cerradura Kleene", 10);
        btnCerradura.setBackground(COLOR_AZUL_ACCENT);
        btnCerradura.setForeground(Color.WHITE);
        btnCerradura.setAlignmentX(CENTER_ALIGNMENT);
        btnCerradura.addActionListener(e -> {
            String auto = (String) comboCerraduraKleene.getSelectedItem();
            if (auto == null) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un autómata válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AFN base = afnMap.get(auto);
            if (base == null) {
                JOptionPane.showMessageDialog(this, "No se pudo recuperar el autómata '" + auto + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            java.util.List<String> eliminar = java.util.Collections.singletonList(auto);
            String nuevoNombre = solicitarNombreDisponible(auto + "_kleene", eliminar);
            if (nuevoNombre != null) {
                try {
                    AFN resultado = AFN.cerraduraKleene(base);
                    removeAFNs(eliminar);
                    registerAFN(nuevoNombre, resultado, null);
                    showAutomataDetails("Cerradura Kleene", nuevoNombre, resultado);
                } catch (Exception exCK) {
                    JOptionPane.showMessageDialog(this, "No se pudo aplicar la cerradura Kleene: " + exCK.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(btnCerradura);

        return panel;
    }


    // Método para crear el panel de Cerradura Opcional
    private JPanel createCerraduraOpcionalPanel() {
        JPanel panel = new RoundedPanel(15);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setBackground(COLOR_AZUL_SUAVE);

        JLabel lblTitulo = new JLabel("Cerradura Opcional");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(20));

        JLabel lblAutomata = new JLabel("Seleccione el autómata:");
        lblAutomata.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblAutomata.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblAutomata);

        comboCerraduraOpcional = new JComboBox<>(createdAutomataNames.toArray(new String[0]));
        comboCerraduraOpcional.setMaximumSize(new Dimension(200, 30));
        panel.add(comboCerraduraOpcional);
        panel.add(Box.createVerticalStrut(20));

        JButton btnCerradura = new RoundedButton("Aplicar Cerradura Opcional", 10);
        btnCerradura.setBackground(COLOR_AZUL_ACCENT);
        btnCerradura.setForeground(Color.WHITE);
        btnCerradura.setAlignmentX(CENTER_ALIGNMENT);
        btnCerradura.addActionListener(e -> {
            String auto = (String) comboCerraduraOpcional.getSelectedItem();
            if (auto == null) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un autómata válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AFN base = afnMap.get(auto);
            if (base == null) {
                JOptionPane.showMessageDialog(this, "No se pudo recuperar el autómata '" + auto + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            java.util.List<String> eliminar = java.util.Collections.singletonList(auto);
            String nuevoNombre = solicitarNombreDisponible(auto + "_opc", eliminar);
            if (nuevoNombre != null) {
                try {
                    AFN resultado = AFN.cerraduraOpcional(base);
                    removeAFNs(eliminar);
                    registerAFN(nuevoNombre, resultado, null);
                    showAutomataDetails("Cerradura opcional", nuevoNombre, resultado);
                } catch (Exception exOpc) {
                    JOptionPane.showMessageDialog(this, "No se pudo aplicar la cerradura opcional: " + exOpc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(btnCerradura);

        return panel;
    }

    private JPanel createMostrarAFNPanel() {
        JPanel panel = new RoundedPanel(15);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setBackground(COLOR_AZUL_SUAVE);

        JLabel lblTitulo = new JLabel("Mostrar AFN");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(20));

        JLabel lblAutomata = new JLabel("Seleccione el AFN a mostrar:");
        lblAutomata.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblAutomata.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblAutomata);

        comboMostrarAFN = new JComboBox<>(createdAutomataNames.toArray(new String[0]));
        comboMostrarAFN.setMaximumSize(new Dimension(200, 30));
        panel.add(comboMostrarAFN);
        panel.add(Box.createVerticalStrut(20));

        JButton btnMostrar = new RoundedButton("Mostrar", 10);
        btnMostrar.setBackground(COLOR_AZUL_ACCENT);
        btnMostrar.setForeground(Color.WHITE);
        btnMostrar.setAlignmentX(CENTER_ALIGNMENT);
        btnMostrar.addActionListener(e -> {
            String auto = (String) comboMostrarAFN.getSelectedItem();
            if (auto == null) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un autómata válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AFN seleccionado = afnMap.get(auto);
            if (seleccionado == null) {
                JOptionPane.showMessageDialog(this, "No se pudo recuperar el autómata  + auto + .", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            showAutomataDetails("Detalle de AFN", auto, seleccionado);
        });
        panel.add(btnMostrar);

        return panel;
    }

    private JPanel createMostrarAFDPanel() {
        JPanel panel = new RoundedPanel(15);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setBackground(COLOR_AZUL_SUAVE);

        JLabel lblTitulo = new JLabel("Mostrar AFD");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(20));

        JLabel lblAutomata = new JLabel("Seleccione el AFD a mostrar:");
        lblAutomata.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblAutomata.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblAutomata);

        comboMostrarAFD = new JComboBox<>(createdAfdNames.toArray(new String[0]));
        comboMostrarAFD.setMaximumSize(new Dimension(200, 30));
        panel.add(comboMostrarAFD);
        panel.add(Box.createVerticalStrut(20));

        JButton btnMostrar = new RoundedButton("Mostrar", 10);
        btnMostrar.setBackground(COLOR_AZUL_ACCENT);
        btnMostrar.setForeground(Color.WHITE);
        btnMostrar.setAlignmentX(CENTER_ALIGNMENT);
        btnMostrar.addActionListener(e -> {
            String auto = (String) comboMostrarAFD.getSelectedItem();
            if (auto == null) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un AFD válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AFD afd = afdMap.get(auto);
            if (afd == null) {
                JOptionPane.showMessageDialog(this, "No se pudo recuperar el AFD '" + auto + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            showAfdDetails("Detalle de AFD", auto, afd);
        });
        panel.add(btnMostrar);

        return panel;
    }

    private void updateAutomataLists() {
        Collections.sort(createdAutomataNames);
        String[] automataArray = createdAutomataNames.toArray(new String[0]);
        if (comboUnion1 != null) {
            comboUnion1.setModel(new javax.swing.DefaultComboBoxModel<>(automataArray));
        }
        if (comboUnion2 != null) {
            comboUnion2.setModel(new javax.swing.DefaultComboBoxModel<>(automataArray));
        }
        if (comboConcatenacion1 != null) {
            comboConcatenacion1.setModel(new javax.swing.DefaultComboBoxModel<>(automataArray));
        }
        if (comboConcatenacion2 != null) {
            comboConcatenacion2.setModel(new javax.swing.DefaultComboBoxModel<>(automataArray));
        }
        if (comboCerraduraPositiva != null) {
            comboCerraduraPositiva.setModel(new javax.swing.DefaultComboBoxModel<>(automataArray));
        }
        if (comboCerraduraKleene != null) {
            comboCerraduraKleene.setModel(new javax.swing.DefaultComboBoxModel<>(automataArray));
        }
        if (comboCerraduraOpcional != null) {
            comboCerraduraOpcional.setModel(new javax.swing.DefaultComboBoxModel<>(automataArray));
        }
        if (comboMostrarAFN != null) {
            comboMostrarAFN.setModel(new javax.swing.DefaultComboBoxModel<>(automataArray));
        }
        if (comboMostrarAFD != null) {
            Collections.sort(createdAfdNames);
            String[] afdArray = createdAfdNames.toArray(new String[0]);
            comboMostrarAFD.setModel(new javax.swing.DefaultComboBoxModel<>(afdArray));
        }
    }

    private int calcularTokenParaLexema(String lexema){
        if(lexema == null || lexema.isEmpty()){
            return siguienteTokenSecuencial();
        }
        boolean soloDigitos = true;
        boolean soloLetras = true;
        boolean soloBlancos = true;
        for(char c : lexema.toCharArray()){
            if(!Character.isDigit(c)){
                soloDigitos = false;
            }
            if(!Character.isLetter(c)){
                soloLetras = false;
            }
            if(!Character.isWhitespace(c)){
                soloBlancos = false;
            }
        }
        if(soloDigitos){
            return 10;
        }
        if(soloLetras){
            return 20;
        }
        if(soloBlancos){
            return 30;
        }
        if(lexema.length() == 1){
            return tokenParaSimbolo(lexema.charAt(0));
        }
        return siguienteTokenSecuencial();
    }

    private int tokenParaSimbolo(char c){
        return tokenPorSimbolo.computeIfAbsent(c, key -> siguienteTokenSecuencial());
    }

    private int siguienteTokenSecuencial(){
        return nextTokenValue++;
    }

    private void registerAFN(String name, AFN automaton, Integer tokenOverride) {
        if (name == null || name.isEmpty() || automaton == null) {
            return;
        }
        if(tokenOverride != null){
            automaton.asignarToken(tokenOverride.intValue());
        }
        afnMap.put(name, automaton);
        if (!createdAutomataNames.contains(name)) {
            createdAutomataNames.add(name);
        }
        updateAutomataLists();
    }

    private void registerAFD(String name, AFD afd){
        if(name == null || name.isEmpty() || afd == null){
            return;
        }
        afdMap.put(name, afd);
        if(!createdAfdNames.contains(name)){
            createdAfdNames.add(name);
        }
        updateAutomataLists();
    }

    private void removeAFNs(java.util.List<String> nombres){
        boolean cambio = false;
        for(String nombre : nombres){
            if(nombre == null){
                continue;
            }
            AFN eliminado = afnMap.remove(nombre);
            if(eliminado != null){
                createdAutomataNames.remove(nombre);
                cambio = true;
            }
        }
        if(cambio){
            updateAutomataLists();
        }
    }

    private String solicitarNombreDisponible(String sugerencia) {
        return solicitarNombreDisponible(sugerencia, java.util.Collections.emptyList());
    }

    private String solicitarNombreDisponible(String sugerencia, java.util.List<String> nombresReservados) {
        java.util.Set<String> reservados = new java.util.HashSet<>(nombresReservados);
        String propuesta = sugerencia != null ? sugerencia : "Automata" + (createdAutomataNames.size() + 1);
        while (true) {
            String nombre = JOptionPane.showInputDialog(this, "Nombre para el nuevo autómata:", propuesta);
            if (nombre == null) {
                return null;
            }
            nombre = nombre.trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (!reservados.contains(nombre) && (afnMap.containsKey(nombre) || afdMap.containsKey(nombre))) {
                JOptionPane.showMessageDialog(this, "Ya existe un autómata con ese nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                propuesta = nombre + "_1";
                continue;
            }
            return nombre;
        }
    }

    private void showAfdDetails(String titulo, String nombre, AFD afd) {
        if (afd == null) {
            JOptionPane.showMessageDialog(this, "No hay información disponible para ese AFD.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        AFD.TransitionTable tabla = afd.buildTransitionTable();
        Object[][] datos = new Object[tabla.rows.size()][tabla.headers.size()];
        for (int i = 0; i < tabla.rows.size(); i++) {
            String[] fila = tabla.rows.get(i);
            for (int j = 0; j < fila.length; j++) {
                datos[i][j] = fila[j];
            }
        }
        DefaultTableModel modelo = new DefaultTableModel(datos, tabla.headers.toArray(new String[0]));
        JTable tablaVisual = new JTable(modelo);
        tablaVisual.setEnabled(false);
        tablaVisual.setRowHeight(24);
        JScrollPane scroll = new JScrollPane(tablaVisual);
        scroll.setPreferredSize(new Dimension(620, 360));
        JDialog dialogo = new JDialog(this, titulo + ": " + nombre, true);
        dialogo.getContentPane().setLayout(new BorderLayout());
        dialogo.getContentPane().add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnGuardar = new RoundedButton("Guardar tabla", 8);
        btnGuardar.setBackground(COLOR_AZUL_ACCENT);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(ev -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Guardar tabla AFD");
            chooser.setSelectedFile(new File(nombre + "_tabla.txt"));
            int opcion = chooser.showSaveDialog(dialogo);
            if(opcion == JFileChooser.APPROVE_OPTION){
                File archivo = chooser.getSelectedFile();
                try {
                    if(afd.exportTransitionTable(archivo.toPath())){
                        JOptionPane.showMessageDialog(dialogo, "Tabla guardada en\n" + archivo.getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(dialogo, "No se pudo guardar la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialogo, "Error al guardar la tabla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnGrafo = new RoundedButton("Ver grafo", 8);
        btnGrafo.setBackground(COLOR_AZUL_ACCENT);
        btnGrafo.setForeground(Color.WHITE);
        btnGrafo.addActionListener(ev -> mostrarGrafoAFD(nombre, afd));

        JButton btnCerrar = new RoundedButton("Cerrar", 8);
        btnCerrar.setBackground(COLOR_AZUL_PRINCIPAL);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.addActionListener(ev -> dialogo.dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnGrafo);
        panelBotones.add(btnCerrar);
        dialogo.getContentPane().add(panelBotones, BorderLayout.SOUTH);

        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    private void showAutomataDetails(String titulo, String nombre, AFN automata) {
        if (automata == null) {
            JOptionPane.showMessageDialog(this, "No hay información disponible para ese autómata.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JTextArea area = new JTextArea(automata.resumen());
        area.setEditable(false);
        area.setLineWrap(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        area.setCaretPosition(0);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(540, 360));

        JDialog dialogo = new JDialog(this, titulo + ": " + nombre, true);
        dialogo.getContentPane().setLayout(new BorderLayout());
        dialogo.getContentPane().add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnGrafo = new RoundedButton("Ver grafo", 8);
        btnGrafo.setBackground(COLOR_AZUL_ACCENT);
        btnGrafo.setForeground(Color.WHITE);
        btnGrafo.addActionListener(ev -> mostrarGrafoAFN(nombre, automata));

        JButton btnCerrar = new RoundedButton("Cerrar", 8);
        btnCerrar.setBackground(COLOR_AZUL_PRINCIPAL);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.addActionListener(ev -> dialogo.dispose());

        panelBotones.add(btnGrafo);
        panelBotones.add(btnCerrar);
        dialogo.getContentPane().add(panelBotones, BorderLayout.SOUTH);

        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    private void showAutomataDialog(String title, JPanel contentPanel) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.getContentPane().add(contentPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void mostrarGrafoAFN(String nombre, AFN afn) {
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        java.util.Map<Estado, Object> vertices = new HashMap<>();
        graph.getModel().beginUpdate();
        try {
            for (Estado estado : afn.getEstados()) {
                String etiqueta = "q" + estado.getId();
                if (estado.isAceptacion()) {
                    etiqueta += "\nT=" + (estado.getToken() >= 0 ? estado.getToken() : "-");
                }
                String style = "shape=ellipse;perimeter=ellipsePerimeter;fillColor=#E6F0FF;strokeColor=#3E6FB5;strokeWidth=2;fontColor=#1E335F;";
                if (estado.isAceptacion()) {
                    style += "double=1;";
                }
                if (afn.getEdoInicial() == estado) {
                    style += "fillColor=#FFF4CC;strokeColor=#F5A201;";
                }
                Object v = graph.insertVertex(parent, null, etiqueta, 0, 0, 90, 50, style);
                vertices.put(estado, v);
            }
            for (Estado estado : afn.getEstados()) {
                for (Transicion t : estado.getTransiciones()) {
                    Object origen = vertices.get(estado);
                    Object destino = vertices.get(t.EdoDestino);
                    String etiqueta = etiquetaTransicion(t.simboloInf, t.simboloSup);
                    graph.insertEdge(parent, null, etiqueta, origen, destino);
                }
            }
        } finally {
            graph.getModel().endUpdate();
        }
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setOrientation(mxHierarchicalLayout.ORIENTATION_WEST);
        layout.execute(parent);
        mostrarGrafoEnDialogo("AFN: " + nombre, graph);
    }

    private void mostrarGrafoAFD(String nombre, AFD afd) {
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        java.util.Map<Integer, Object> vertices = new HashMap<>();
        graph.getModel().beginUpdate();
        try {
            for (int i = 0; i < afd.getNumEdos(); i++) {
                EdoAFD edo = afd.getEstado(i);
                String etiqueta = "S" + i;
                if (edo.token >= 0) {
                    etiqueta += "\nT=" + edo.token;
                }
                String style = "shape=ellipse;perimeter=ellipsePerimeter;fillColor=#E6F0FF;strokeColor=#1E3C72;strokeWidth=2;fontColor=#1E335F;";
                if (edo.esAceptacion) {
                    style += "double=1;";
                }
                Object v = graph.insertVertex(parent, null, etiqueta, 0, 0, 90, 50, style);
                vertices.put(i, v);
            }
            java.util.List<Character> simbolos = afd.getSimbolos();
            for (int i = 0; i < afd.getNumEdos(); i++) {
                EdoAFD edo = afd.getEstado(i);
                java.util.Map<Integer, java.util.LinkedHashSet<String>> destinos = new java.util.LinkedHashMap<>();
                for (char c : simbolos) {
                    int destino = edo.TransAFD[c & 0xFF];
                    if (destino >= 0) {
                        destinos.computeIfAbsent(destino, k -> new java.util.LinkedHashSet<>()).add(etiquetaTransicion(c, c));
                    }
                }
                Object origen = vertices.get(i);
                for (java.util.Map.Entry<Integer, java.util.LinkedHashSet<String>> entry : destinos.entrySet()) {
                    Object destino = vertices.get(entry.getKey());
                    String etiqueta = String.join("/", entry.getValue());
                    graph.insertEdge(parent, null, etiqueta, origen, destino);
                }
            }
        } finally {
            graph.getModel().endUpdate();
        }
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setOrientation(mxHierarchicalLayout.ORIENTATION_WEST);
        layout.execute(parent);
        mostrarGrafoEnDialogo("AFD: " + nombre, graph);
    }

    private void mostrarGrafoEnDialogo(String titulo, mxGraph graph) {
        mxGraphComponent component = new mxGraphComponent(graph);
        component.setConnectable(false);
        component.getGraphControl().setBackground(Color.WHITE);
        JDialog dialogo = new JDialog(this, titulo, true);
        dialogo.getContentPane().setLayout(new BorderLayout());
        dialogo.getContentPane().add(component, BorderLayout.CENTER);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JButton btnCerrar = new RoundedButton("Cerrar", 8);
        btnCerrar.setBackground(COLOR_AZUL_PRINCIPAL);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.addActionListener(ev -> dialogo.dispose());
        panel.add(btnCerrar);
        dialogo.getContentPane().add(panel, BorderLayout.SOUTH);

        dialogo.setSize(800, 600);
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    private String etiquetaTransicion(char simboloInf, char simboloSup) {
        String etiquetaInf = simboloATexto(simboloInf);
        String etiquetaSup = simboloATexto(simboloSup);
        if (simboloInf == simboloSup) {
            return etiquetaInf;
        }
        return etiquetaInf + "-" + etiquetaSup;
    }

    private String simboloATexto(char c) {
        if (c == SimbEspeciales.EPSILON) {
            return "ε";
        }
        if (Character.isWhitespace(c)) {
            return "S";
        }
        if (Character.isDigit(c)) {
            return "D";
        }
        if (Character.isLetter(c)) {
            return "L";
        }
        return Character.toString(c);
    }

    private ImageIcon loadIcon(String iconName) {
        try {
            String iconPath = "/icons/" + iconName;
            java.net.URL imageURL = getClass().getResource(iconPath);

            if (imageURL != null) {
                ImageIcon originalIcon = new ImageIcon(imageURL);
                int size = iconName.contains("crear_") || iconName.contains("cargar_") || iconName.contains("configuracion") ? 20 : 16;
                java.awt.Image scaledImage = originalIcon.getImage().getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            } else {
                String projectPath = System.getProperty("user.dir") + File.separator + "icons" + File.separator + iconName;
                File iconFile = new File(projectPath);
                if (iconFile.exists()) {
                    ImageIcon originalIcon = new ImageIcon(projectPath);
                    int size = iconName.contains("crear_") || iconName.contains("cargar_") || iconName.contains("configuracion") ? 20 : 16;
                    java.awt.Image scaledImage = originalIcon.getImage().getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);
                    return new ImageIcon(scaledImage);
                }
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono: " + iconName + " - " + e.getMessage());
        }
        return null;
    }

    private void toggleSidebar() {
        sidebarVisible = !sidebarVisible;
        panelLateral.setVisible(sidebarVisible); // Al darle clic, muestra u oculta el panel lateral
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private class RoundedPanel extends JPanel {
        private int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
    }

    private class RoundedButton extends JButton {
        private int radius;

        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
                g2d.setColor(getBackground().darker());
            } else {
                g2d.setColor(getBackground());
            }

            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
    }

    private class AutomataGraphPanel extends RoundedPanel {
        public AutomataGraphPanel() {
            super(15);
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(COLOR_AZUL_ACCENT);
            g2d.setStroke(new java.awt.BasicStroke(2f));

            int[][] nodePositions = {
                {60, 100}, {140, 60}, {220, 100}, {300, 60}, {380, 100},
                {140, 140}, {220, 180}, {300, 140}, {60, 180}, {380, 180}
            };

            int[][] connections = {
                {0, 1}, {1, 2}, {2, 3}, {3, 4}, {0, 5}, {5, 6}, {6, 7}, {7, 4},
                {0, 8}, {8, 6}, {1, 5}, {2, 7}, {3, 7}, {6, 9}, {9, 4}
            };

            for (int[] connection : connections) {
                int x1 = nodePositions[connection[0]][0];
                int y1 = nodePositions[connection[0]][1];
                int x2 = nodePositions[connection[1]][0];
                int y2 = nodePositions[connection[1]][1];
                g2d.drawLine(x1, y1, x2, y2);
            }

            g2d.setColor(COLOR_AZUL_PRINCIPAL);
            for (int[] pos : nodePositions) {
                g2d.fillOval(pos[0] - 8, pos[1] - 8, 16, 16);
            }

            g2d.setColor(COLOR_AZUL_SUAVE);
            for (int[] pos : nodePositions) {
                g2d.drawOval(pos[0] - 8, pos[1] - 8, 16, 16);
            }
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new AutomataMenuGUI().setVisible(true);
        });
    }
}
