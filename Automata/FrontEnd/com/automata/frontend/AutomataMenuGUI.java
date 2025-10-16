package com.automata.frontend;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import BackEnd.AFD;
import BackEnd.AFN;
import BackEnd.EdoAFD;
import BackEnd.Estado;
import BackEnd.SimbEspeciales;
import BackEnd.Transicion;
import BackEnd.LexicalAnalyzer;
import BackEnd.LexicalAnalyzer.Definition;
import BackEnd.LexicalAnalyzer.TokenResult;
import BackEnd.LexicalAnalyzer.LexicalException;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

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
    private LexicalAnalyzer lexicalAnalyzer;
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
        JMenuItem itemImportarLexico = new JMenuItem("Importar Clases Léxicas...");
        
        itemSalir.addActionListener(e -> System.exit(0)); 
        itemNuevo.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(this,
                    "Se eliminarán todos los AFN, AFD y clases léxicas cargadas.\n¿Deseas continuar?",
                    "Nuevo Proyecto",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (respuesta == JOptionPane.YES_OPTION) {
                clearWorkspace();
                JOptionPane.showMessageDialog(this, "Espacio de trabajo limpiado.", "Nuevo Proyecto", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        itemGuardar.addActionListener(e -> showSaveDialog()); // Al darle clic se abre un diálogo de guardar
        itemCargar.addActionListener(e -> showLoadDialog()); // Al darle clic se abre un diálogo de cargar
        itemImportarLexico.addActionListener(e -> importarClasesLexicas());
        
        menuArchivo.add(itemNuevo);
        menuArchivo.add(itemGuardar);
        menuArchivo.add(itemCargar);
        menuArchivo.add(itemImportarLexico);
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
        JMenuItem itemAnalizarCadena = new JMenuItem("Analizar cadena...");
        JMenuItem itemMinimizacion = new JMenuItem("Minimizar AFD");
        
        itemConvAFNtoAFD.addActionListener(e -> showConversionDialog()); // Al darle clic se abre el diálogo de conversión
        itemAnalizarCadena.addActionListener(e -> showLexicalAnalysisDialog());
        itemMinimizacion.addActionListener(e -> showMinimizationDialog()); // Al darle clic se abre el diálogo de minimización

        menuHerramientas.add(itemConvAFNtoAFD);
        menuHerramientas.add(itemAnalizarCadena);
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
        if (afdMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aún no existen AFD para guardar.\nConvierte un AFN primero.", "Sin AFD disponibles", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.add(new JLabel("Selecciona el AFD que deseas guardar:"));

        String[] nombres = createdAfdNames.toArray(new String[0]);
        JComboBox<String> combo = new JComboBox<>(nombres);
        combo.setMaximumSize(new Dimension(240, 28));
        panel.add(combo);

        int opcion = JOptionPane.showConfirmDialog(this, panel, "Guardar AFD", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }
        String seleccionado = (String) combo.getSelectedItem();
        if (seleccionado == null) {
            return;
        }
        AFD afd = afdMap.get(seleccionado);
        if (afd == null) {
            JOptionPane.showMessageDialog(this, "No se pudo recuperar el AFD seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar AFD");
        fileChooser.setSelectedFile(new File(seleccionado + ".afd"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File destino = fileChooser.getSelectedFile();
            if (destino.exists()) {
                int sobrescribir = JOptionPane.showConfirmDialog(this,
                        "El archivo ya existe. ¿Deseas sobrescribirlo?",
                        "Confirmar sobrescritura",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (sobrescribir != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            boolean ok = afd.SaveAFD(destino.getAbsolutePath());
            if (ok) {
                JOptionPane.showMessageDialog(this, "AFD guardado en:\n" + destino.getAbsolutePath(), "Guardado completado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar el AFD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showLoadDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Cargar AFD");
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File archivo = fileChooser.getSelectedFile();
        if (archivo == null || !archivo.exists()) {
            JOptionPane.showMessageDialog(this, "Selecciona un archivo válido.", "Archivo no encontrado", JOptionPane.ERROR_MESSAGE);
            return;
        }
        AFD cargado = new AFD();
        if (!cargado.LoadAFD(archivo.getAbsolutePath())) {
            JOptionPane.showMessageDialog(this, "El archivo no contiene un AFD válido.", "Carga fallida", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String nombreSugerido = archivo.getName();
        if (nombreSugerido.lastIndexOf('.') > 0) {
            nombreSugerido = nombreSugerido.substring(0, nombreSugerido.lastIndexOf('.'));
        }
        String nombre = solicitarNombreDisponible(nombreSugerido);
        if (nombre == null) {
            return;
        }
        registerAFD(nombre, cargado);
        JOptionPane.showMessageDialog(this, "AFD cargado como '" + nombre + "'.", "Carga completada", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearWorkspace() {
        afnMap.clear();
        afdMap.clear();
        createdAutomataNames.clear();
        createdAfdNames.clear();
        lexicalAnalyzer = null;
        updateAutomataLists();
        showWelcomePanel();
    }

    private void importarClasesLexicas() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Importar clases léxicas");
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File archivo = fileChooser.getSelectedFile();
        if (archivo == null || !archivo.exists()) {
            JOptionPane.showMessageDialog(this, "Selecciona un archivo válido.", "Archivo no encontrado", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            List<Definition> definiciones = parseLexicalDefinitions(archivo.toPath());
            lexicalAnalyzer = LexicalAnalyzer.build(definiciones);
            String nombreBase = archivo.getName();
            int punto = nombreBase.lastIndexOf('.');
            if (punto > 0) {
                nombreBase = nombreBase.substring(0, punto);
            }
            String nombreAFD = solicitarNombreDisponible(nombreBase + "_AFD");
            if (nombreAFD != null && !nombreAFD.isEmpty()) {
                registerAFD(nombreAFD, lexicalAnalyzer.getAfd());
            }
            JOptionPane.showMessageDialog(this,
                    "Se importaron " + definiciones.size() + " clases léxicas.\n" +
                    "Utiliza Herramientas → Analizar cadena para obtener la secuencia de tokens.",
                    "Importación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No fue posible importar las clases léxicas:\n" + ex.getMessage(),
                    "Error al importar",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showLexicalAnalysisDialog() {
        if (lexicalAnalyzer == null) {
            JOptionPane.showMessageDialog(this,
                    "Primero importa las clases léxicas (Archivo → Importar clases léxicas).",
                    "Analizador no disponible",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JTextArea areaEntrada = new JTextArea(5, 40);
        areaEntrada.setLineWrap(true);
        areaEntrada.setWrapStyleWord(true);
        JScrollPane scrollEntrada = new JScrollPane(areaEntrada);
        scrollEntrada.setBorder(BorderFactory.createTitledBorder("Cadena a analizar"));

        JTextArea areaSalida = new JTextArea(12, 40);
        areaSalida.setEditable(false);
        areaSalida.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollSalida = new JScrollPane(areaSalida);
        scrollSalida.setBorder(BorderFactory.createTitledBorder("Resultado"));

        JButton btnAnalizar = new RoundedButton("Analizar", 8);
        btnAnalizar.setBackground(COLOR_AZUL_ACCENT);
        btnAnalizar.setForeground(Color.WHITE);
        btnAnalizar.addActionListener(e -> {
            String texto = areaEntrada.getText();
            try {
                List<TokenResult> resultados = lexicalAnalyzer.analyze(texto);
                areaSalida.setText(formatearResultadoLexico(resultados));
                areaSalida.setCaretPosition(0);
            } catch (LexicalException ex) {
                areaSalida.setText("Error: " + ex.getMessage());
            }
        });

        JPanel panelCentro = new JPanel(new BorderLayout(15, 15));
        panelCentro.setOpaque(false);
        panelCentro.add(scrollEntrada, BorderLayout.NORTH);
        panelCentro.add(scrollSalida, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setOpaque(false);
        panelBotones.add(btnAnalizar);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contenedor.add(panelCentro, BorderLayout.CENTER);
        contenedor.add(panelBotones, BorderLayout.SOUTH);

        JDialog dialog = new JDialog(this, "Analizar cadena", true);
        dialog.getContentPane().add(contenedor);
        dialog.setSize(680, 520);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private List<Definition> parseLexicalDefinitions(Path path) throws IOException {
        List<Definition> definiciones = new ArrayList<Definition>();
        int linea = 0;
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String raw;
            while ((raw = reader.readLine()) != null) {
                linea++;
                String line = raw.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String nombre;
                String expresion;
                String tokenTexto;
                if (line.contains("->")) {
                    String[] partes = line.split("->", 3);
                    if (partes.length < 3) {
                        throw new IllegalArgumentException("Formato inválido en la línea " + linea + ": " + raw);
                    }
                    nombre = partes[0].trim();
                    expresion = partes[1].trim();
                    tokenTexto = partes[2].trim();
                } else {
                    String[] partes = line.split("\\s+", 3);
                    if (partes.length < 3) {
                        throw new IllegalArgumentException("Formato inválido en la línea " + linea + ": " + raw);
                    }
                    nombre = partes[0].trim();
                    expresion = partes[1].trim();
                    tokenTexto = partes[2].trim();
                }
                if (nombre.isEmpty() || expresion.isEmpty() || tokenTexto.isEmpty()) {
                    throw new IllegalArgumentException("Datos incompletos en la línea " + linea + ": " + raw);
                }
                int token;
                try {
                    token = Integer.parseInt(tokenTexto);
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Token inválido en la línea " + linea + ": " + tokenTexto);
                }
                definiciones.add(new Definition(nombre, expresion, token));
            }
        }
        if (definiciones.isEmpty()) {
            throw new IllegalArgumentException("El archivo no contiene clases léxicas.");
        }
        return definiciones;
    }

    private String formatearResultadoLexico(List<TokenResult> resultados) {
        StringBuilder secuencia = new StringBuilder("Secuencia de tokens: ");
        StringBuilder detalle = new StringBuilder();

        for (int i = 0; i < resultados.size(); i++) {
            TokenResult resultado = resultados.get(i);
            if (resultado.finDeCadena) {
                if (i > 0) {
                    secuencia.append(", ");
                }
                secuencia.append("FIN");
                detalle.append("Fin de cadena");
                break;
            } else {
                if (i > 0) {
                    secuencia.append(", ");
                }
                secuencia.append(resultado.token);

                String lexema = resultado.lexeme
                        .replace("\n", "\\n")
                        .replace("\t", "\\t")
                        .replace("\r", "\\r");
                detalle.append(String.format("%-12s -> %-20s (token %d)%n",
                        resultado.definition.name,
                        lexema,
                        resultado.token));
            }
        }

        return secuencia.toString() + "\n\n" + detalle.toString();
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
        panel.add(Box.createVerticalStrut(10));

        JLabel lblToken = new JLabel("Token para los estados de aceptación:");
        lblToken.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblToken.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblToken);

        JTextField txtToken = new JTextField(10);
        txtToken.setMaximumSize(new Dimension(200, 30));
        panel.add(txtToken);
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
            String tokenTexto = txtToken.getText().trim();
            if (tokenTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa el token que deseas asignar.", "Token requerido", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int tokenValor;
            try {
                tokenValor = Integer.parseInt(tokenTexto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El token debe ser un número entero.", "Token inválido", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                AFN nuevo = AFN.fromString(input);
                registerAFN(automataName, nuevo, tokenValor);
                showAutomataDetails("AFN creado", automataName, nuevo);
                txtNombre.setText("");
                txtInput.setText("");
                txtToken.setText("");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "No se pudo crear el AFN: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            CapturaToken captura = solicitarNombreYToken(auto1 + "_U_" + auto2, eliminar);
            if (captura != null) {
                try {
                    AFN resultado = AFN.unir(afn1, afn2);
                    removeAFNs(eliminar);
                    registerAFN(captura.nombre, resultado, Integer.valueOf(captura.token));
                    showAutomataDetails("Unión completada", captura.nombre, resultado);
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
            CapturaToken captura = solicitarNombreYToken(auto1 + "_C_" + auto2, eliminar);
            if (captura != null) {
                try {
                    AFN resultado = AFN.concatenar(afn1, afn2);
                    removeAFNs(eliminar);
                    registerAFN(captura.nombre, resultado, Integer.valueOf(captura.token));
                    showAutomataDetails("Concatenación completada", captura.nombre, resultado);
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
            CapturaToken captura = solicitarNombreYToken(auto + "_pos", eliminar);
            if (captura != null) {
                try {
                    AFN resultado = AFN.cerraduraPositiva(base);
                    removeAFNs(eliminar);
                    registerAFN(captura.nombre, resultado, Integer.valueOf(captura.token));
                    showAutomataDetails("Cerradura positiva", captura.nombre, resultado);
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
            CapturaToken captura = solicitarNombreYToken(auto + "_kleene", eliminar);
            if (captura != null) {
                try {
                    AFN resultado = AFN.cerraduraKleene(base);
                    removeAFNs(eliminar);
                    registerAFN(captura.nombre, resultado, Integer.valueOf(captura.token));
                    showAutomataDetails("Cerradura Kleene", captura.nombre, resultado);
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
            CapturaToken captura = solicitarNombreYToken(auto + "_opc", eliminar);
            if (captura != null) {
                try {
                    AFN resultado = AFN.cerraduraOpcional(base);
                    removeAFNs(eliminar);
                    registerAFN(captura.nombre, resultado, Integer.valueOf(captura.token));
                    showAutomataDetails("Cerradura opcional", captura.nombre, resultado);
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
        boolean hayAfdDisponibles = !createdAfdNames.isEmpty();

        JButton btnMostrar = new RoundedButton("Mostrar", 10);
        btnMostrar.setBackground(COLOR_AZUL_ACCENT);
        btnMostrar.setForeground(Color.WHITE);
        btnMostrar.setAlignmentX(CENTER_ALIGNMENT);
        btnMostrar.setEnabled(hayAfdDisponibles);
        comboMostrarAFD.setEnabled(hayAfdDisponibles);
        if(!hayAfdDisponibles){
            JLabel lblAviso = new JLabel("<html><center>No hay AFD registrados.<br>Utiliza Herramientas → Convertir AFN a AFD para generar uno.</center></html>");
            lblAviso.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            lblAviso.setForeground(COLOR_TEXTO_OSCURO);
            lblAviso.setAlignmentX(CENTER_ALIGNMENT);
            panel.add(lblAviso);
            panel.add(Box.createVerticalStrut(10));
        }
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

    private CapturaToken solicitarNombreYToken(String sugerencia, java.util.List<String> nombresReservados) {
        java.util.Set<String> reservados = new java.util.HashSet<String>();
        if (nombresReservados != null) {
            reservados.addAll(nombresReservados);
        }
        String propuesta = sugerencia != null ? sugerencia : "Automata" + (createdAutomataNames.size() + 1);
        JTextField txtNombre = new JTextField(propuesta, 18);
        JTextField txtToken = new JTextField(8);

        while (true) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(new JLabel("Nombre del nuevo autómata:"));
            panel.add(txtNombre);
            panel.add(Box.createVerticalStrut(8));
            panel.add(new JLabel("Token (entero):"));
            panel.add(txtToken);

            int opcion = JOptionPane.showConfirmDialog(this, panel, "Configurar nuevo autómata", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (opcion != JOptionPane.OK_OPTION) {
                return null;
            }

            String nombre = txtNombre.getText().trim();
            String tokenTexto = txtToken.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (!reservados.contains(nombre) && (afnMap.containsKey(nombre) || afdMap.containsKey(nombre))) {
                JOptionPane.showMessageDialog(this, "Ya existe un autómata con ese nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                txtNombre.setText(nombre + "_1");
                continue;
            }

            if (tokenTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa el token que deseas asignar.", "Token requerido", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            int token;
            try {
                token = Integer.parseInt(tokenTexto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El token debe ser un número entero.", "Token inválido", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            return new CapturaToken(nombre, token);
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
        tablaVisual.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaVisual.setFillsViewportHeight(true);
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

        JButton btnImagen = new RoundedButton("Guardar imagen", 8);
        btnImagen.setBackground(COLOR_AZUL_CLARO);
        btnImagen.setForeground(Color.WHITE);
        btnImagen.addActionListener(ev -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Exportar tabla como imagen");
            FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("Imagen PNG (*.png)", "png");
            FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("Imagen JPG (*.jpg, *.jpeg)", "jpg", "jpeg");
            chooser.addChoosableFileFilter(pngFilter);
            chooser.addChoosableFileFilter(jpgFilter);
            chooser.setFileFilter(pngFilter);
            chooser.setSelectedFile(new File(nombre + "_tabla.png"));
            int opcion = chooser.showSaveDialog(dialogo);
            if(opcion == JFileChooser.APPROVE_OPTION){
                File archivo = asegurarExtensionImagen(chooser.getSelectedFile(), chooser.getFileFilter());
                try {
                    exportarTablaComoImagen(tablaVisual, archivo);
                    JOptionPane.showMessageDialog(dialogo, "Imagen guardada en\n" + archivo.getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(dialogo, "No se pudo exportar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnCerrar = new RoundedButton("Cerrar", 8);
        btnCerrar.setBackground(COLOR_AZUL_PRINCIPAL);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.addActionListener(ev -> dialogo.dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnImagen);
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
        GraphVisualizationPanel panel = GraphVisualizationPanel.fromAFN(afn, this::etiquetaTransicion);
        mostrarGrafoEnDialogo("AFN: " + nombre, panel);
    }

    private void mostrarGrafoAFD(String nombre, AFD afd) {
        GraphVisualizationPanel panel = GraphVisualizationPanel.fromAFD(afd, this::etiquetaTransicion);
        mostrarGrafoEnDialogo("AFD: " + nombre, panel);
    }

    private void mostrarGrafoEnDialogo(String titulo, GraphVisualizationPanel panel) {
        JDialog dialogo = new JDialog(this, titulo, true);
        dialogo.getContentPane().setLayout(new BorderLayout());
        dialogo.getContentPane().add(new JScrollPane(panel), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JButton btnCerrar = new RoundedButton("Cerrar", 8);
        btnCerrar.setBackground(COLOR_AZUL_PRINCIPAL);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.addActionListener(ev -> dialogo.dispose());
        panelBotones.add(btnCerrar);
        dialogo.getContentPane().add(panelBotones, BorderLayout.SOUTH);

        dialogo.setSize(820, 620);
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

    private File asegurarExtensionImagen(File archivo, javax.swing.filechooser.FileFilter filtroSeleccionado) {
        if (archivo == null) {
            return null;
        }
        String extensionActual = obtenerExtensionArchivo(archivo);
        if (!extensionActual.isEmpty()) {
            return archivo;
        }
        String extensionPreferida = "png";
        if (filtroSeleccionado instanceof FileNameExtensionFilter) {
            String[] extensiones = ((FileNameExtensionFilter) filtroSeleccionado).getExtensions();
            if (extensiones != null && extensiones.length > 0) {
                extensionPreferida = extensiones[0];
            }
        }
        File parent = archivo.getAbsoluteFile().getParentFile();
        String nombre = archivo.getName();
        return new File(parent, nombre + "." + extensionPreferida);
    }

    private String obtenerExtensionArchivo(File archivo) {
        String nombre = archivo.getName();
        int idx = nombre.lastIndexOf('.');
        if (idx >= 0 && idx < nombre.length() - 1) {
            return nombre.substring(idx + 1).toLowerCase();
        }
        return "";
    }

    private String determinarFormatoImagen(File archivo) {
        String extension = obtenerExtensionArchivo(archivo);
        if (extension.equals("jpg") || extension.equals("jpeg")) {
            return "jpg";
        }
        return "png";
    }

    private void exportarTablaComoImagen(JTable tablaFuente, File destino) throws IOException {
        if (tablaFuente.getModel().getRowCount() == 0) {
            throw new IOException("La tabla no contiene datos.");
        }

        JTable tabla = new JTable(tablaFuente.getModel());
        tabla.setFont(tablaFuente.getFont());
        tabla.setRowHeight(tablaFuente.getRowHeight());
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.setBackground(Color.WHITE);
        tabla.setShowGrid(true);

        int columnas = tablaFuente.getColumnModel().getColumnCount();
        for (int i = 0; i < columnas; i++) {
            int ancho = tablaFuente.getColumnModel().getColumn(i).getWidth();
            if (ancho <= 0) {
                ancho = tablaFuente.getColumnModel().getColumn(i).getPreferredWidth();
            }
            tabla.getColumnModel().getColumn(i).setPreferredWidth(ancho);
        }
        tabla.doLayout();

        JTableHeader header = tabla.getTableHeader();
        header.setFont(tablaFuente.getTableHeader().getFont());
        header.setBackground(Color.WHITE);
        int width = Math.max(tabla.getPreferredSize().width, 600);
        int headerHeight = header.getPreferredSize().height;
        int height = tabla.getPreferredSize().height + headerHeight;

        BufferedImage imagen = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = imagen.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);

        header.setSize(width, headerHeight);
        header.paint(g2);
        g2.translate(0, headerHeight);
        tabla.setSize(width, tabla.getPreferredSize().height);
        tabla.paint(g2);
        g2.dispose();

        ImageIO.write(imagen, determinarFormatoImagen(destino), destino);
    }

    @FunctionalInterface
    private interface TransitionLabelProvider {
        String format(char simboloInferior, char simboloSuperior);
    }

    private static final class CapturaToken {
        final String nombre;
        final int token;

        CapturaToken(String nombre, int token) {
            this.nombre = nombre;
            this.token = token;
        }
    }

    private static final class GraphVisualizationPanel extends JPanel {
        private static final int NODE_RADIUS = 40;
        private static final Color EDGE_COLOR = new Color(62, 111, 181);
        private static final Color NODE_FILL = new Color(230, 240, 255);
        private static final Color NODE_FILL_INICIAL = new Color(255, 244, 204);
        private static final Color NODE_BORDER = new Color(30, 60, 114);

        private final List<NodeInfo> nodes;
        private final List<EdgeInfo> edges;

        private GraphVisualizationPanel(List<NodeInfo> nodes, List<EdgeInfo> edges) {
            this.nodes = nodes;
            this.edges = edges;
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(780, 560));
        }

        static GraphVisualizationPanel fromAFN(AFN afn, TransitionLabelProvider labelProvider) {
            List<NodeInfo> nodes = new ArrayList<>();
            Map<Estado, NodeInfo> nodeMap = new LinkedHashMap<>();
            Estado inicial = afn.getEdoInicial();
            for (Estado estado : afn.getEstados()) {
                StringBuilder etiqueta = new StringBuilder("q").append(estado.getId());
                if (estado.isAceptacion()) {
                    etiqueta.append("\nT=").append(estado.getToken() >= 0 ? estado.getToken() : "-");
                }
                NodeInfo info = new NodeInfo(etiqueta.toString(), estado.isAceptacion(), estado == inicial);
                nodes.add(info);
                nodeMap.put(estado, info);
            }
            List<EdgeInfo> edges = new ArrayList<>();
            for (Estado estado : afn.getEstados()) {
                for (Transicion transicion : estado.getTransiciones()) {
                    NodeInfo origen = nodeMap.get(estado);
                    NodeInfo destino = nodeMap.get(transicion.EdoDestino);
                    if (origen != null && destino != null) {
                        String etiqueta = labelProvider.format(transicion.simboloInf, transicion.simboloSup);
                        edges.add(new EdgeInfo(origen, destino, etiqueta));
                    }
                }
            }
            return new GraphVisualizationPanel(nodes, edges);
        }

        static GraphVisualizationPanel fromAFD(AFD afd, TransitionLabelProvider labelProvider) {
            List<NodeInfo> nodes = new ArrayList<>();
            Map<Integer, NodeInfo> nodeMap = new LinkedHashMap<>();

            for (int i = 0; i < afd.getNumEdos(); i++) {
                EdoAFD edo = afd.getEstado(i);
                StringBuilder etiqueta = new StringBuilder("S").append(i);
                if (edo.token >= 0) {
                    etiqueta.append("\nT=").append(edo.token);
                }
                NodeInfo info = new NodeInfo(etiqueta.toString(), edo.esAceptacion, i == 0);
                nodes.add(info);
                nodeMap.put(i, info);
            }

            List<EdgeInfo> edges = new ArrayList<>();
            List<Character> simbolos = afd.getSimbolos();
            for (int i = 0; i < afd.getNumEdos(); i++) {
                EdoAFD edo = afd.getEstado(i);
                Map<Integer, LinkedHashSet<String>> destinos = new LinkedHashMap<>();
                for (char simbolo : simbolos) {
                    int indiceDestino = edo.TransAFD[simbolo & 0xFF];
                    if (indiceDestino >= 0) {
                        destinos.computeIfAbsent(indiceDestino, k -> new LinkedHashSet<>())
                                .add(labelProvider.format(simbolo, simbolo));
                    }
                }
                NodeInfo origen = nodeMap.get(i);
                for (Map.Entry<Integer, LinkedHashSet<String>> entry : destinos.entrySet()) {
                    NodeInfo destino = nodeMap.get(entry.getKey());
                    if (origen != null && destino != null) {
                        String etiqueta = String.join("\n", entry.getValue());
                        edges.add(new EdgeInfo(origen, destino, etiqueta));
                    }
                }
            }

            return new GraphVisualizationPanel(nodes, edges);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (nodes.isEmpty()) {
                g2d.dispose();
                return;
            }

            positionNodes(getWidth(), getHeight());

            g2d.setStroke(new BasicStroke(2f));
            for (EdgeInfo edge : edges) {
                if (edge.origen == null || edge.destino == null) {
                    continue;
                }
                if (edge.origen == edge.destino) {
                    drawSelfLoop(g2d, edge);
                } else {
                    drawEdge(g2d, edge);
                }
            }

            for (NodeInfo node : nodes) {
                drawNode(g2d, node);
            }

            g2d.dispose();
        }

        private void positionNodes(int width, int height) {
            int n = nodes.size();
            double centerX = width / 2.0;
            double centerY = height / 2.0;
            if (n == 1) {
                NodeInfo único = nodes.get(0);
                único.x = centerX;
                único.y = centerY;
                return;
            }
            double radius = Math.min(width, height) / 2.4;
            if (radius < NODE_RADIUS + 40) {
                radius = NODE_RADIUS + 40;
            }
            double angle = -Math.PI / 2;
            double step = 2 * Math.PI / n;
            for (NodeInfo node : nodes) {
                node.x = centerX + radius * Math.cos(angle);
                node.y = centerY + radius * Math.sin(angle);
                angle += step;
            }
        }

        private void drawEdge(Graphics2D g2d, EdgeInfo edge) {
            Point2D start = adjustTowards(edge.origen.point(), edge.destino.point(), NODE_RADIUS);
            Point2D end = adjustTowards(edge.destino.point(), edge.origen.point(), NODE_RADIUS);

            g2d.setColor(EDGE_COLOR);
            g2d.draw(new Line2D.Double(start, end));
            drawArrowHead(g2d, start, end);
            drawEdgeLabel(g2d, edge.label, start, end);
        }

        private void drawSelfLoop(Graphics2D g2d, EdgeInfo edge) {
            NodeInfo node = edge.origen;
            double startX = node.x - NODE_RADIUS * 0.6;
            double startY = node.y - NODE_RADIUS;
            double endX = node.x + NODE_RADIUS * 0.6;
            double endY = startY;
            double controlX = node.x;
            double controlY = node.y - NODE_RADIUS - 60;

            g2d.setColor(EDGE_COLOR);
            QuadCurve2D.Double curve = new QuadCurve2D.Double(startX, startY, controlX, controlY, endX, endY);
            g2d.draw(curve);

            Point2D controlPoint = new Point2D.Double(controlX, controlY);
            Point2D endPoint = new Point2D.Double(endX, endY);
            drawArrowHead(g2d, controlPoint, endPoint);
            drawEdgeLabel(g2d, edge.label, controlPoint, endPoint);
        }

        private void drawEdgeLabel(Graphics2D g2d, String label, Point2D start, Point2D end) {
            if (label == null || label.isEmpty()) {
                return;
            }
            String[] lines = label.split("\\R");
            double midX = (start.getX() + end.getX()) / 2.0;
            double midY = (start.getY() + end.getY()) / 2.0;
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 12f));
            FontMetrics fm = g2d.getFontMetrics();
            g2d.setColor(new Color(30, 60, 114));
            double textHeight = lines.length * fm.getHeight();
            double currentY = midY - textHeight / 2.0 + fm.getAscent();
            for (String line : lines) {
                int textWidth = fm.stringWidth(line);
                g2d.drawString(line, (float) (midX - textWidth / 2.0), (float) currentY);
                currentY += fm.getHeight();
            }
        }

        private void drawNode(Graphics2D g2d, NodeInfo node) {
            double diameter = NODE_RADIUS * 2.0;
            double topLeftX = node.x - NODE_RADIUS;
            double topLeftY = node.y - NODE_RADIUS;

            g2d.setColor(node.initial ? NODE_FILL_INICIAL : NODE_FILL);
            g2d.fillOval((int) Math.round(topLeftX), (int) Math.round(topLeftY), (int) Math.round(diameter), (int) Math.round(diameter));

            g2d.setColor(NODE_BORDER);
            g2d.drawOval((int) Math.round(topLeftX), (int) Math.round(topLeftY), (int) Math.round(diameter), (int) Math.round(diameter));

            if (node.acceptance) {
                double innerPadding = 6;
                g2d.drawOval((int) Math.round(topLeftX + innerPadding), (int) Math.round(topLeftY + innerPadding),
                        (int) Math.round(diameter - innerPadding * 2), (int) Math.round(diameter - innerPadding * 2));
            }

            drawNodeLabel(g2d, node);
            if (node.initial) {
                drawInitialMarker(g2d, node);
            }
        }

        private void drawNodeLabel(Graphics2D g2d, NodeInfo node) {
            String[] lines = node.label.split("\\R");
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 12f));
            FontMetrics fm = g2d.getFontMetrics();
            g2d.setColor(NODE_BORDER);
            double totalHeight = lines.length * fm.getHeight();
            double currentY = node.y - totalHeight / 2.0 + fm.getAscent();
            for (String line : lines) {
                int textWidth = fm.stringWidth(line);
                g2d.drawString(line, (float) (node.x - textWidth / 2.0), (float) currentY);
                currentY += fm.getHeight();
            }
        }

        private void drawInitialMarker(Graphics2D g2d, NodeInfo node) {
            double padding = NODE_RADIUS + 25;
            Point2D end = new Point2D.Double(node.x - NODE_RADIUS, node.y);
            Point2D start = new Point2D.Double(end.getX() - padding, node.y);
            g2d.setColor(EDGE_COLOR);
            g2d.draw(new Line2D.Double(start, end));
            drawArrowHead(g2d, start, end);
        }

        private void drawArrowHead(Graphics2D g2d, Point2D start, Point2D end) {
            double angle = Math.atan2(end.getY() - start.getY(), end.getX() - start.getX());
            double arrowSize = 10;
            Path2D.Double path = new Path2D.Double();
            path.moveTo(end.getX(), end.getY());
            path.lineTo(end.getX() - arrowSize * Math.cos(angle - Math.PI / 6),
                    end.getY() - arrowSize * Math.sin(angle - Math.PI / 6));
            path.lineTo(end.getX() - arrowSize * Math.cos(angle + Math.PI / 6),
                    end.getY() - arrowSize * Math.sin(angle + Math.PI / 6));
            path.closePath();
            g2d.fill(path);
        }

        private Point2D adjustTowards(Point2D from, Point2D to, double distance) {
            double angle = Math.atan2(to.getY() - from.getY(), to.getX() - from.getX());
            return new Point2D.Double(
                    from.getX() + Math.cos(angle) * distance,
                    from.getY() + Math.sin(angle) * distance
            );
        }

        private static final class NodeInfo {
            final String label;
            final boolean acceptance;
            final boolean initial;
            double x;
            double y;

            NodeInfo(String label, boolean acceptance, boolean initial) {
                this.label = label;
                this.acceptance = acceptance;
                this.initial = initial;
            }

            Point2D point() {
                return new Point2D.Double(x, y);
            }
        }

        private static final class EdgeInfo {
            final NodeInfo origen;
            final NodeInfo destino;
            final String label;

            EdgeInfo(NodeInfo origen, NodeInfo destino, String label) {
                this.origen = origen;
                this.destino = destino;
                this.label = label;
            }
        }
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
