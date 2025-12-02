package analisisintactico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

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

		menuItemBasicoAFN.addActionListener(e -> showAutomataDialog("Crear AFN Básico", createAFNPanel("AFN Básico")));
		menuItemUnirAFN.addActionListener(e -> showAutomataDialog("Operación: Unión de Autómatas", createUnionPanel()));
		menuItemConcatenarAFN.addActionListener(e -> showAutomataDialog("Operación: Concatenación de Autómatas", createConcatenacionPanel()));
		menuItemCerraduraPositivaAFN.addActionListener(e -> showAutomataDialog("Cerradura Positiva (+)", createCerraduraUnitariaPanel("Cerradura Positiva")));
		menuItemCerraduraKleeneAFN.addActionListener(e -> showAutomataDialog("Cerradura Kleene (*)", createCerraduraUnitariaPanel("Cerradura Kleene")));
		menuItemOpcionalAFN.addActionListener(e -> showAutomataDialog("Cerradura Opcional (?)", createCerraduraUnitariaPanel("Cerradura Opcional")));

		menuItemERtoAFN.addActionListener(e -> abrirDialogERtoAFN());

		menuItemUnionAnalizadorLexico.addActionListener(e -> showAutomataDialog("Unión para Analizador Léxico", createAFNPanel("Unión Léxica")));
		menuItemAFNtoAFD.addActionListener(e -> showAutomataDialog("Convertir AFN a AFD", createConversionToAFDPanel()));
		menuItemAnalizarCadena.addActionListener(e -> showAutomataDialog("Analizar Cadena", createAFNPanel("Analizar Cadena")));
		menuItemProbarAnalizadorLexico.addActionListener(e -> showAutomataDialog("Probar Analizador Léxico", createAFNPanel("Probar Léxico")));

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
		menuItemAbrirLL1.addActionListener(e -> abrirDialogAnalisisLL1());
		menuAnalisisLL1.add(menuItemAbrirLL1);

		menuAnalisisSLR = new JMenu("Análisis SLR");
		menuItemAbrirSLR = new JMenuItem("Abrir SLR");
		menuItemAbrirSLR.addActionListener(e -> abrirDialogAnalisisLR0());
		menuAnalisisSLR.add(menuItemAbrirSLR);

		menuAnalisisLRCanonico = new JMenu("Análisis LR Canónico o LR(1)");
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

	private JPanel createAFNPanel(String tipo) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

		JLabel lblTitulo = new JLabel(tipo);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(lblTitulo);
		panel.add(Box.createVerticalStrut(20));

		JLabel lblNombre = new JLabel("Nombre del autómata:");
		lblNombre.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(lblNombre);

		JTextField txtNombre = new JTextField(15);
		txtNombre.setMaximumSize(new Dimension(200, 30));
		panel.add(txtNombre);
		panel.add(Box.createVerticalStrut(10));

		JLabel lblInput = new JLabel("Ingrese una sentencia:");
		lblInput.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(lblInput);

		JTextField txtInput = new JTextField(15);
		txtInput.setMaximumSize(new Dimension(200, 30));
		panel.add(txtInput);
		panel.add(Box.createVerticalStrut(20));

		JButton btnAccion = new JButton("Crear");
		btnAccion.setAlignmentX(CENTER_ALIGNMENT);
		btnAccion.addActionListener(e -> {
			JOptionPane.showMessageDialog(panel, "Frontend: La lógica de '" + tipo + "' está pendiente.", "Frontend Listo", JOptionPane.INFORMATION_MESSAGE);
		});
		panel.add(btnAccion);

		return panel;
	}

	private JPanel createUnionPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		JLabel lblTitulo = new JLabel("Operación: Unión (A | B)");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(lblTitulo);
		panel.add(Box.createVerticalStrut(20));

		panel.add(new JLabel("Seleccione el primer autómata (A):"));
		JComboBox<String> comboUnion1 = new JComboBox<>(mockAutomataNames);
		comboUnion1.setMaximumSize(new Dimension(200, 30));
		panel.add(comboUnion1);
		panel.add(Box.createVerticalStrut(10));

		panel.add(new JLabel("Seleccione el segundo autómata (B):"));
		JComboBox<String> comboUnion2 = new JComboBox<>(mockAutomataNames);
		comboUnion2.setMaximumSize(new Dimension(200, 30));
		panel.add(comboUnion2);
		panel.add(Box.createVerticalStrut(20));

		JButton btnUnir = new JButton("Realizar Unión");
		btnUnir.setAlignmentX(CENTER_ALIGNMENT);
		btnUnir.addActionListener(e -> {
			JOptionPane.showMessageDialog(panel, "Frontend: La lógica de Unión (A|B) está pendiente.", "Frontend Listo", JOptionPane.INFORMATION_MESSAGE);
		});
		panel.add(btnUnir);

		return panel;
	}

	private JPanel createConcatenacionPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		JLabel lblTitulo = new JLabel("Operación: Concatenación (A · B)");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(lblTitulo);
		panel.add(Box.createVerticalStrut(20));

		panel.add(new JLabel("Seleccione el primer autómata (A):"));
		JComboBox<String> comboConcatenacion1 = new JComboBox<>(mockAutomataNames);
		comboConcatenacion1.setMaximumSize(new Dimension(200, 30));
		panel.add(comboConcatenacion1);
		panel.add(Box.createVerticalStrut(10));

		panel.add(new JLabel("Seleccione el segundo autómata (B):"));
		JComboBox<String> comboConcatenacion2 = new JComboBox<>(mockAutomataNames);
		comboConcatenacion2.setMaximumSize(new Dimension(200, 30));
		panel.add(comboConcatenacion2);
		panel.add(Box.createVerticalStrut(20));

		JButton btnConcatenar = new JButton("Realizar Concatenación");
		btnConcatenar.setAlignmentX(CENTER_ALIGNMENT);
		btnConcatenar.addActionListener(e -> {
			JOptionPane.showMessageDialog(panel, "Frontend: La lógica de Concatenación (A·B) está pendiente.", "Frontend Listo", JOptionPane.INFORMATION_MESSAGE);
		});
		panel.add(btnConcatenar);

		return panel;
	}

	private JPanel createCerraduraUnitariaPanel(String tipo) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

		JLabel lblTitulo = new JLabel(tipo);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(lblTitulo);
		panel.add(Box.createVerticalStrut(20));

		JLabel lblAutomata = new JLabel("Seleccione el autómata:");
		lblAutomata.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(lblAutomata);

		JComboBox<String> comboCerraduraUnitaria = new JComboBox<>(mockAutomataNames);
		comboCerraduraUnitaria.setMaximumSize(new Dimension(200, 30));
		panel.add(comboCerraduraUnitaria);
		panel.add(Box.createVerticalStrut(20));

		JButton btnCerradura = new JButton("Aplicar " + tipo);
		btnCerradura.setAlignmentX(CENTER_ALIGNMENT);
		btnCerradura.addActionListener(e -> {
			JOptionPane.showMessageDialog(panel, "Frontend: La lógica de '" + tipo + "' está pendiente.", "Frontend Listo", JOptionPane.INFORMATION_MESSAGE);
		});
		panel.add(btnCerradura);

		return panel;
	}

	private JPanel createConversionToAFDPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

		JLabel lblTitulo = new JLabel("Convertir AFN a AFD");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(lblTitulo);
		panel.add(Box.createVerticalStrut(20));

		JLabel lblAFN = new JLabel("Seleccione el AFN a convertir:");
		lblAFN.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(lblAFN);

		JComboBox<String> comboAFN = new JComboBox<>(mockAutomataNames);
		comboAFN.setMaximumSize(new Dimension(200, 30));
		panel.add(comboAFN);
		panel.add(Box.createVerticalStrut(20));

		JLabel lblNombre = new JLabel("Nombre para el nuevo AFD (opcional):");
		lblNombre.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(lblNombre);

		JTextField txtNombre = new JTextField(15);
		txtNombre.setMaximumSize(new Dimension(200, 30));
		panel.add(txtNombre);
		panel.add(Box.createVerticalStrut(20));

		JButton btnConvertir = new JButton("Convertir a AFD");
		btnConvertir.setAlignmentX(CENTER_ALIGNMENT);
		btnConvertir.addActionListener(e -> {
			JOptionPane.showMessageDialog(panel, "Frontend: La lógica de Conversión AFN -> AFD está pendiente.", "Frontend Listo", JOptionPane.INFORMATION_MESSAGE);
		});
		panel.add(btnConvertir);

		return panel;
	}

	private void showAutomataDialog(String title, JPanel contentPanel) {
		JDialog dialog = new JDialog(this, title, true);
		dialog.getContentPane().add(contentPanel);
		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
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
		dialog.setSize(450, 650);
		dialog.setLocationRelativeTo(this);

		PanelCalculadora panelCalc = new PanelCalculadora();

		dialog.add(panelCalc);
		dialog.setVisible(true);
	}

	private void abrirDialogERtoAFN() {
		JDialog dialog = new JDialog(this, "ER -> AFN", true);
		dialog.setSize(600, 500);
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

	private void abrirDialogAnalisisLR0() {
		JDialog dialog = new JDialog(this, "Análisis SLR", true);
		dialog.setSize(1200, 800);
		dialog.setLocationRelativeTo(this);

		PanelAnalisisLR0 panelAnLR0 = new PanelAnalisisLR0();

		dialog.add(panelAnLR0);
		dialog.setVisible(true);
	}

	private void abrirDialogAnalisisLL1() {
		JDialog dialog = new JDialog(this, "Análisis LL(1)", true);
		dialog.setSize(1000, 700);
		dialog.setLocationRelativeTo(this);

		PanelAnalisisLL1 panelAnLL1 = new PanelAnalisisLL1();

		dialog.add(panelAnLL1);
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