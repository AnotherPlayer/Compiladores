package analisisintactico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.*;

// IMPORTS DE BACKEND COMENTADOS
// import BackEnd.Gramatica;
// import BackEnd.LadoIzq;
// import BackEnd.LL1;
// import BackEnd.SimbolG;

public class PanelAnalisisLL1 extends JPanel {

	private File archivoAFDLexico;
	private JTextArea areaGramatica;
	private JTextField txtSigma;
	private JButton btnCrearTabla;
	private JButton btnSeleccionarAfdLexico;
	private JButton btnProbarLexico;
	private JButton btnAsignarTokens;
	private JButton btnAnalizarSintacticamente;
	private JTable tablaNoTerminal;
	private JTable tablaTerminalToken;
	private JTable tablaLL1;
	private JTable tablaLexemaToken;
	private JTable tablaPilaCadena;

	// VARIABLES DE BACKEND COMENTADAS
	// private Gramatica gramaticaActual;
	// private LL1.TablaLL1 tablaActual;

	public PanelAnalisisLL1() {
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
		gbc.weightx = 0.25;
		gbc.weighty = 0.30;
		gbc.gridheight = 1;
		panelPrincipal.add(panelGramatica, gbc);

		JPanel panelCentroSuperior = crearPanelCentroSuperior();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.40;
		gbc.weighty = 0.30;
		panelPrincipal.add(panelCentroSuperior, gbc);

		JPanel panelDerechaSuperior = crearPanelDerechaSuperior();
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0.35;
		gbc.weighty = 0.30;
		panelPrincipal.add(panelDerechaSuperior, gbc);

		JPanel panelTablaLL1 = crearPanelTablaLL1();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 0.65;
		gbc.weighty = 0.35;
		panelPrincipal.add(panelTablaLL1, gbc);

		JPanel panelLexico = crearPanelLexico();
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0.35;
		gbc.weighty = 0.35;
		panelPrincipal.add(panelLexico, gbc);

		JPanel panelPilaCadena = crearPanelPilaCadena();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		gbc.weightx = 1.0;
		gbc.weighty = 0.35;
		panelPrincipal.add(panelPilaCadena, gbc);

		this.add(panelPrincipal, BorderLayout.CENTER);

		btnCrearTabla.addActionListener(e -> btnCrearTablaActionPerformed(e));
		btnProbarLexico.addActionListener(e -> btnProbarLexicoActionPerformed(e));
		btnSeleccionarAfdLexico.addActionListener(e -> btnSeleccionarAfdLexicoActionPerformed(e));
		btnAsignarTokens.addActionListener(e -> btnAsignarTokensActionPerformed(e));
		btnAnalizarSintacticamente.addActionListener(e -> btnAnalizarSintacticamenteActionPerformed(e));
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

		JLabel lblTitulo = new JLabel("Análisis LL(1)", SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
		panel.add(lblTitulo, BorderLayout.NORTH);

		JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
		panelCentral.setBackground(new Color(230, 240, 250));

		JPanel panelBotonesSup = new JPanel(new GridLayout(1, 2, 5, 5));
		panelBotonesSup.setBackground(new Color(230, 240, 250));

		btnCrearTabla = new JButton("Crear Tabla");
		btnCrearTabla.setFont(new Font("Arial", Font.PLAIN, 11));
		panelBotonesSup.add(btnCrearTabla);

		btnAsignarTokens = new JButton("Asignar Tokens a terminales");
		btnAsignarTokens.setFont(new Font("Arial", Font.PLAIN, 10));
		panelBotonesSup.add(btnAsignarTokens);

		panelCentral.add(panelBotonesSup, BorderLayout.NORTH);

		JPanel panelListas = new JPanel(new GridLayout(1, 2, 5, 5));
		panelListas.setBackground(new Color(230, 240, 250));

		JPanel pNoTerminal = crearPanelTabla("NOTerminal");
		tablaNoTerminal = new JTable();
		tablaNoTerminal.setFont(new Font("Monospaced", Font.PLAIN, 11));
		tablaNoTerminal.setRowHeight(22);
		tablaNoTerminal.setGridColor(Color.LIGHT_GRAY);
		tablaNoTerminal.setShowGrid(true);
		tablaNoTerminal.getTableHeader().setReorderingAllowed(false);
		tablaNoTerminal.getTableHeader().setFont(new Font("Arial", Font.BOLD, 10));
		tablaNoTerminal.getTableHeader().setBackground(new Color(220, 220, 220));
		tablaNoTerminal.setModel(new DefaultTableModel(
			new Object[]{"NoTerminal"}, 0
		));
		JScrollPane scrollNT = new JScrollPane(tablaNoTerminal);
		scrollNT.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
		pNoTerminal.add(scrollNT, BorderLayout.CENTER);
		panelListas.add(pNoTerminal);

		JPanel pTerminalToken = crearPanelTabla("Terminal / Token");
		tablaTerminalToken = new JTable();
		tablaTerminalToken.setFont(new Font("Monospaced", Font.PLAIN, 11));
		tablaTerminalToken.setRowHeight(22);
		tablaTerminalToken.setGridColor(Color.LIGHT_GRAY);
		tablaTerminalToken.setShowGrid(true);
		tablaTerminalToken.getTableHeader().setReorderingAllowed(false);
		tablaTerminalToken.getTableHeader().setFont(new Font("Arial", Font.BOLD, 10));
		tablaTerminalToken.getTableHeader().setBackground(new Color(220, 220, 220));
		tablaTerminalToken.setModel(new DefaultTableModel(
			new Object[]{"Terminal", "Token"}, 0
		));
		JScrollPane scrollTT = new JScrollPane(tablaTerminalToken);
		scrollTT.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
		pTerminalToken.add(scrollTT, BorderLayout.CENTER);
		panelListas.add(pTerminalToken);

		panelCentral.add(panelListas, BorderLayout.CENTER);

		panel.add(panelCentral, BorderLayout.CENTER);

		return panel;
	}

	private JPanel crearPanelTabla(String titulo) {
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

		JPanel panelCentro = new JPanel(new BorderLayout(5, 5));
		panelCentro.setBackground(new Color(230, 240, 250));

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

		panelCentro.add(panelSigma, BorderLayout.CENTER);

		btnAnalizarSintacticamente = new JButton("Analizar Sintácticamente Sigma");
		btnAnalizarSintacticamente.setFont(new Font("Arial", Font.PLAIN, 10));
		JPanel panelBotonAbajo = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelBotonAbajo.setBackground(new Color(230, 240, 250));
		panelBotonAbajo.add(btnAnalizarSintacticamente);
		panelCentro.add(panelBotonAbajo, BorderLayout.SOUTH);

		panel.add(panelCentro, BorderLayout.CENTER);

		return panel;
	}

	private JPanel crearPanelTablaLL1() {
		JPanel panel = new JPanel(new BorderLayout(3, 3));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
			BorderFactory.createEmptyBorder(5, 5, 5, 5)
		));

		JLabel lblTitulo = new JLabel("Tabla LL(1)");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(lblTitulo, BorderLayout.NORTH);

		tablaLL1 = new JTable();
		tablaLL1.setFont(new Font("Monospaced", Font.PLAIN, 10));
		tablaLL1.setRowHeight(25);
		tablaLL1.setGridColor(Color.BLACK);
		tablaLL1.setShowGrid(true);
		tablaLL1.setShowVerticalLines(true);
		tablaLL1.setShowHorizontalLines(true);
		tablaLL1.getTableHeader().setReorderingAllowed(false);
		tablaLL1.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
		tablaLL1.getTableHeader().setBackground(new Color(220, 220, 220));
		tablaLL1.setIntercellSpacing(new Dimension(1, 1));

		JScrollPane scroll = new JScrollPane(tablaLL1);
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

	private JPanel crearPanelPilaCadena() {
		JPanel panel = new JPanel(new BorderLayout(3, 3));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
			BorderFactory.createEmptyBorder(5, 5, 5, 5)
		));

		JLabel lblTitulo = new JLabel("Pila/Cadena");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(lblTitulo, BorderLayout.NORTH);

		tablaPilaCadena = new JTable();
		tablaPilaCadena.setFont(new Font("Monospaced", Font.PLAIN, 10));
		tablaPilaCadena.setRowHeight(25);
		tablaPilaCadena.setGridColor(Color.BLACK);
		tablaPilaCadena.setShowGrid(true);
		tablaPilaCadena.setShowVerticalLines(true);
		tablaPilaCadena.setShowHorizontalLines(true);
		tablaPilaCadena.getTableHeader().setReorderingAllowed(false);
		tablaPilaCadena.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
		tablaPilaCadena.getTableHeader().setBackground(new Color(220, 220, 220));
		tablaPilaCadena.setIntercellSpacing(new Dimension(1, 1));

		JScrollPane scroll = new JScrollPane(tablaPilaCadena);
		scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		panel.add(scroll, BorderLayout.CENTER);

		return panel;
	}

	public void inicializarModelosTablas() {
		tablaLL1.setModel(new DefaultTableModel(
			new Object[]{"No terminal"}, 0
		));

		tablaLexemaToken.setModel(new DefaultTableModel(
			new Object[]{"Lexema", "Token"}, 0
		));

		tablaPilaCadena.setModel(new DefaultTableModel(
			new Object[]{"Pila", "Cadena", "Acción"}, 0
		));
	}

	private void btnCrearTablaActionPerformed(ActionEvent evt) {
		// FUNCIONALIDAD BACKEND COMENTADA
		JOptionPane.showMessageDialog(this,
			"Funcionalidad temporalmente deshabilitada.\nNecesita conexión con BackEnd.",
			"En desarrollo",
			JOptionPane.INFORMATION_MESSAGE);
		
		/*
		String gramatica = areaGramatica.getText();
		try {
			gramaticaActual = Gramatica.desdeTexto(gramatica);
			LL1 constructor = new LL1();
			tablaActual = constructor.construir(gramaticaActual);
			llenarTablasVnVt();
			llenarTablaLL1();
			JOptionPane.showMessageDialog(this,
				"Tabla LL(1) generada usando el pseudocódigo de los apuntes.",
				"Proceso LL(1)",
				JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this,
				"Error al construir la tabla LL(1): " + ex.getMessage(),
				"Error", JOptionPane.ERROR_MESSAGE);
		}
		*/
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

	private void btnAnalizarSintacticamenteActionPerformed(ActionEvent e) {
		// FUNCIONALIDAD BACKEND COMENTADA
		JOptionPane.showMessageDialog(this,
			"Funcionalidad temporalmente deshabilitada.\nNecesita conexión con BackEnd.",
			"En desarrollo",
			JOptionPane.INFORMATION_MESSAGE);
		
		/*
		String sigma = txtSigma.getText();
		if (gramaticaActual == null || tablaActual == null) {
			JOptionPane.showMessageDialog(this,
				"Primero genera la tabla LL(1).",
				"Falta tabla", JOptionPane.WARNING_MESSAGE);
			return;
		}
		simularAnalisisLL1(sigma);
		*/
	}

	public JTextArea getAreaGramatica() { return areaGramatica; }
	public JTextField getTxtSigma() { return txtSigma; }
	public JTable getTablaNoTerminal() { return tablaNoTerminal; }
	public JTable getTablaTerminalToken() { return tablaTerminalToken; }
	public JTable getTablaLL1() { return tablaLL1; }
	public JTable getTablaLexemaToken() { return tablaLexemaToken; }
	public JTable getTablaPilaCadena() { return tablaPilaCadena; }
	public File getArchivoAFDLexico() { return archivoAFDLexico; }
	public JButton getBtnCrearTabla() { return btnCrearTabla; }
	public JButton getBtnAnalizarSintacticamente() { return btnAnalizarSintacticamente; }

	// === MÉTODOS DE BACKEND COMENTADOS ===
	
	/*
	private void llenarTablasVnVt() {
		DefaultTableModel ntModel = new DefaultTableModel(new Object[]{"NoTerminal"}, 0);
		for (SimbolG nt : gramaticaActual.Vn) ntModel.addRow(new Object[]{nt.NombSimb});
		tablaNoTerminal.setModel(ntModel);

		DefaultTableModel tModel = new DefaultTableModel(new Object[]{"Terminal", "Token"}, 0);
		for (SimbolG t : gramaticaActual.Vt) tModel.addRow(new Object[]{t.NombSimb, t.token});
		tablaTerminalToken.setModel(tModel);
	}

	private void llenarTablaLL1() {
		if (tablaActual == null) return;
		ArrayList<String> cols = new ArrayList<>(tablaActual.terminales);
		cols.add(0, "NT");
		DefaultTableModel model = new DefaultTableModel(cols.toArray(), 0);
		for (String nt : tablaActual.noTerminales) {
			Object[] fila = new Object[cols.size()];
			fila[0] = nt;
			for (int i = 1; i < cols.size(); i++) {
				String term = cols.get(i);
				Integer regla = tablaActual.tabla.getOrDefault(nt, Collections.emptyMap()).get(term);
				fila[i] = (regla == null) ? "" : regla;
			}
			model.addRow(fila);
		}
		tablaLL1.setModel(model);
	}

	private void simularAnalisisLL1(String cadena) {
		DefaultTableModel pilaCadena = new DefaultTableModel(new Object[]{"Pila", "Cadena", "Acción"}, 0);
		if (cadena == null) cadena = "";
		List<String> tokens = new ArrayList<>(Arrays.asList(cadena.trim().split("\\s+")));
		if (tokens.size() == 1 && tokens.get(0).isEmpty()) tokens.clear();
		tokens.add(Gramatica.FIN_CADENA);

		Deque<String> pila = new ArrayDeque<>();
		pila.push(Gramatica.FIN_CADENA);
		pila.push(gramaticaActual.SimbIni.NombSimb);

		int idx = 0;
		boolean aceptado = false;
		while (idx < tokens.size() && !pila.isEmpty()) {
			String a = tokens.get(idx);
			String X = pila.peek();
			if (X.equals(a)) {
				pilaCadena.addRow(new Object[]{pilaToString(pila), resto(tokens, idx), "match " + a});
				pila.pop();
				idx++;
			} else if (esTerminal(X)) {
				pilaCadena.addRow(new Object[]{pilaToString(pila), resto(tokens, idx), "error"});
				break;
			} else {
				Integer reglaIdx = tablaActual.tabla.getOrDefault(X, Collections.emptyMap()).get(a);
				if (reglaIdx == null) {
					pilaCadena.addRow(new Object[]{pilaToString(pila), resto(tokens, idx), "error"});
					break;
				}
				LadoIzq r = gramaticaActual.Reglas.get(reglaIdx);
				pila.pop();
				List<SimbolG> alpha = new ArrayList<>(r.LadoDerecho);
				Collections.reverse(alpha);
				for (SimbolG s : alpha) {
					if (!s.NombSimb.equals(Gramatica.EPS)) pila.push(s.NombSimb);
				}
				pilaCadena.addRow(new Object[]{pilaToString(pila), resto(tokens, idx),
					"usar r" + reglaIdx + ": " + r.SimbIzq.NombSimb + "->" + rhsToString(r)});
			}
			if (pila.size() == 1 && pila.peek().equals(Gramatica.FIN_CADENA) && a.equals(Gramatica.FIN_CADENA)) {
				aceptado = true;
				break;
			}
		}
		if (aceptado) {
			pilaCadena.addRow(new Object[]{pilaToString(pila), resto(tokens, idx), "aceptar"});
		}
		tablaPilaCadena.setModel(pilaCadena);
	}

	private boolean esTerminal(String simbolo) {
		for (SimbolG s : gramaticaActual.Vt) if (s.NombSimb.equals(simbolo)) return true;
		return simbolo.equals(Gramatica.FIN_CADENA);
	}

	private String pilaToString(Deque<String> pila) {
		return String.join(" ", pila);
	}

	private String resto(List<String> tokens, int idx) {
		StringBuilder sb = new StringBuilder();
		for (int i = idx; i < tokens.size(); i++) {
			sb.append(tokens.get(i)).append(" ");
		}
		return sb.toString().trim();
	}

	private String rhsToString(LadoIzq r) {
		StringBuilder sb = new StringBuilder();
		for (SimbolG s : r.LadoDerecho) sb.append(s.NombSimb).append(" ");
		return sb.toString().trim();
	}
	*/
}