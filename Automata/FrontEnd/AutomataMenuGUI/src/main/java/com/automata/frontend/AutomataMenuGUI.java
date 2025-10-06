package com.automata.frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
import javax.swing.JFileChooser; // Nuevo: Para simular selección de archivos

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
    private List<String> createdAutomataNames = new ArrayList<>();

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
        
        itemConvAFNtoAFD.addActionListener(e -> showConversionDialog()); // Al darle clic se abre el diálogo de conversión
        itemMinimizacion.addActionListener(e -> showMinimizationDialog()); // Al darle clic se abre el diálogo de minimización

        menuHerramientas.add(itemConvAFNtoAFD);
        menuHerramientas.add(itemMinimizacion);
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
            // Lógica pendiente: llamar a AFD.SaveAFD(fileToSave.getAbsolutePath())
            JOptionPane.showMessageDialog(this, "Autómata guardado exitosamente en:\n" + fileToSave.getAbsolutePath(), "Guardado Completo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Muestra un JFileChooser para simular la acción de Cargar
    private void showLoadDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Cargar Autómata");
        
        int userSelection = fileChooser.showOpenDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            // Lógica pendiente: llamar a AFD.LoadAFD(fileToLoad.getAbsolutePath())
            JOptionPane.showMessageDialog(this, "Autómata cargado desde:\n" + fileToLoad.getAbsolutePath(), "Carga Completa", JOptionPane.INFORMATION_MESSAGE);
            // Si la carga es exitosa, se actualizaría la interfaz con los datos del autómata.
        }
    }
    
    // Muestra el Diálogo de Conversión (Herramientas)
    private void showConversionDialog() {
        // Este diálogo se construiría similar a los paneles del menú lateral
        JPanel content = createDefaultCard("Funcionalidad: Conversión AFN a AFD");
        JLabel hint = new JLabel("Aquí se implementaría la selección de un AFN existente para iniciar la conversión.");
        content.add(hint);
        
        JDialog dialog = new JDialog(this, "Herramientas: Convertir AFN a AFD", true);
        dialog.getContentPane().add(content);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
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
            } else if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese una sentencia.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                createdAutomataNames.add(automataName);
                updateAutomataLists(); // Al darle clic se actualizan los JComboBox
                JOptionPane.showMessageDialog(this, "AFN '" + automataName + "' creado para la sentencia: '" + input + "'", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                txtNombre.setText("");
                txtInput.setText("");
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
            if (auto1 != null && auto2 != null) {
                // Lógica de unión
                JOptionPane.showMessageDialog(this, "Unión de " + auto1 + " y " + auto2 + " realizada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione dos autómatas válidos.", "Error", JOptionPane.ERROR_MESSAGE);
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
            if (auto1 != null && auto2 != null) {
                // Lógica de concatenación
                JOptionPane.showMessageDialog(this, "Concatenación de " + auto1 + " y " + auto2 + " realizada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione dos autómatas válidos.", "Error", JOptionPane.ERROR_MESSAGE);
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
            if (auto != null) {
                // Lógica de cerradura
                JOptionPane.showMessageDialog(this, "Cerradura Positiva aplicada a: " + auto, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un autómata válido.", "Error", JOptionPane.ERROR_MESSAGE);
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
            if (auto != null) {
                // Lógica de cerradura
                JOptionPane.showMessageDialog(this, "Cerradura Kleene aplicada a: " + auto, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un autómata válido.", "Error", JOptionPane.ERROR_MESSAGE);
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
            if (auto != null) {
                // Lógica de cerradura
                JOptionPane.showMessageDialog(this, "Cerradura Opcional aplicada a: " + auto, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un autómata válido.", "Error", JOptionPane.ERROR_MESSAGE);
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
            if (auto != null) {
                JOptionPane.showMessageDialog(this, "Se mostrará el gráfico de: " + auto, "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un autómata válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
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

        comboMostrarAFD = new JComboBox<>(createdAutomataNames.toArray(new String[0]));
        comboMostrarAFD.setMaximumSize(new Dimension(200, 30));
        panel.add(comboMostrarAFD);
        panel.add(Box.createVerticalStrut(20));

        JButton btnMostrar = new RoundedButton("Mostrar", 10);
        btnMostrar.setBackground(COLOR_AZUL_ACCENT);
        btnMostrar.setForeground(Color.WHITE);
        btnMostrar.setAlignmentX(CENTER_ALIGNMENT);
        btnMostrar.addActionListener(e -> {
            String auto = (String) comboMostrarAFD.getSelectedItem();
            if (auto != null) {
                JOptionPane.showMessageDialog(this, "Se mostrará el gráfico de: " + auto, "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un autómata válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(btnMostrar);

        return panel;
    }

    private void updateAutomataLists() {
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
            comboMostrarAFD.setModel(new javax.swing.DefaultComboBoxModel<>(automataArray));
        }
    }

    private void showAutomataDialog(String title, JPanel contentPanel) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.getContentPane().add(contentPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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