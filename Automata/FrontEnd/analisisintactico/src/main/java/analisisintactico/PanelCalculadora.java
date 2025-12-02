package analisisintactico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;

public class PanelCalculadora extends JPanel {

	private JTextField txtIdAFD;
	private JButton btnSeleccionarAFD;
	private JTextField txtExpresion;
	private JButton btnEvaluar;
	private JTextField txtResultado;
	private JTextField txtExpPrefija;

	private JPanel panelBotonesCalculadora;

	private static final Color DARK_BACKGROUND = new Color(48, 51, 56);
	private static final Color BUTTON_DARK = new Color(40, 43, 49);
	private static final Color BUTTON_FUNCTION = new Color(75, 80, 88);
	private static final Color BUTTON_EQUAL_OP = new Color(138, 180, 248);
	private static final Color TEXT_COLOR = Color.WHITE;
	private static final Color ACCENT_COLOR = new Color(255, 185, 0);

	private final String[] buttons = {
		"RAD", "DEG", "AC", "DEL", "%", "/",
		"SIN", "COS", "TAN", "LOG", "LN", "*",
		"ASIN", "ACOS", "ATAN", "EXP", "7", "8",
		"PI", "sqrt", "(", "4", "5", "6",
		"1", "2", "3", "-", ".", "9",
		")", "NUM", "0", "00", "=", "+"
	};

	public PanelCalculadora() {
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		setLayout(new BorderLayout(10, 10));
		setBackground(new Color(240, 240, 240));
		setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		JPanel panelPrincipal = createAnalysisPanel();
		add(panelPrincipal, BorderLayout.NORTH);

		panelBotonesCalculadora = createButtonPanel();
		add(panelBotonesCalculadora, BorderLayout.CENTER);
	}

	private JPanel createAnalysisPanel() {
		JPanel panelPrincipal = new JPanel(new GridBagLayout());
		panelPrincipal.setBackground(new Color(240, 240, 240));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		int row = 0;

		gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
		JLabel lblTitulo = new JLabel("Analizador Léxico / Calculadora");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16)); lblTitulo.setForeground(new Color(40, 40, 40));
		panelPrincipal.add(lblTitulo, gbc);

		gbc.gridy = row++; panelPrincipal.add(Box.createVerticalStrut(10), gbc);

		gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;

		gbc.gridx = 0; gbc.gridy = row;
		JLabel lblIdAFD = new JLabel("Id del AFD"); lblIdAFD.setFont(new Font("Arial", Font.PLAIN, 12));
		panelPrincipal.add(lblIdAFD, gbc);

		gbc.gridx = 1; gbc.gridy = row++;
		txtIdAFD = new JTextField(20); txtIdAFD.setFont(new Font("Arial", Font.PLAIN, 12));
		txtIdAFD.setPreferredSize(new Dimension(250, 25));
		panelPrincipal.add(txtIdAFD, gbc);

		gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
		btnSeleccionarAFD = new JButton("Seleccionar archivo del AFD");
		btnSeleccionarAFD.setFont(new Font("Arial", Font.PLAIN, 11)); btnSeleccionarAFD.setPreferredSize(new Dimension(200, 28));
		btnSeleccionarAFD.setFocusPainted(false); btnSeleccionarAFD.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnSeleccionarAFD.addActionListener(e -> seleccionarArchivoAFD());
		panelPrincipal.add(btnSeleccionarAFD, gbc);

		gbc.gridy = row++; panelPrincipal.add(Box.createVerticalStrut(10), gbc);

		gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;

		gbc.gridx = 0; gbc.gridy = row;
		JLabel lblExpresion = new JLabel("Expresión a evaluar:"); lblExpresion.setFont(new Font("Arial", Font.PLAIN, 12));
		panelPrincipal.add(lblExpresion, gbc);

		gbc.gridx = 1; gbc.gridy = row++;
		txtExpresion = new JTextField(20); txtExpresion.setFont(new Font("Arial", Font.PLAIN, 12));
		txtExpresion.setPreferredSize(new Dimension(250, 25));
		panelPrincipal.add(txtExpresion, gbc);

		gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
		btnEvaluar = new JButton("Evaluar");
		btnEvaluar.setFont(new Font("Arial", Font.BOLD, 11)); btnEvaluar.setPreferredSize(new Dimension(100, 28));
		btnEvaluar.setFocusPainted(false); btnEvaluar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnEvaluar.addActionListener(e -> evaluarExpresion());
		panelPrincipal.add(btnEvaluar, gbc);

		gbc.gridy = row++; panelPrincipal.add(Box.createVerticalStrut(5), gbc);

		gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
		JLabel lblResultado = new JLabel("Resultado"); lblResultado.setFont(new Font("Arial", Font.PLAIN, 12));
		panelPrincipal.add(lblResultado, gbc);

		gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;

		gbc.gridx = 1; gbc.gridy = row++;
		txtResultado = new JTextField(20); txtResultado.setFont(new Font("Arial", Font.PLAIN, 12));
		txtResultado.setPreferredSize(new Dimension(250, 25));
		txtResultado.setEditable(false); txtResultado.setBackground(Color.WHITE);
		txtResultado.setHorizontalAlignment(JTextField.CENTER);
		panelPrincipal.add(txtResultado, gbc);

		gbc.gridy = row++; gbc.gridwidth = 2; panelPrincipal.add(Box.createVerticalStrut(5), gbc);

		gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;

		gbc.gridx = 0; gbc.gridy = row;
		JLabel lblExpPrefija = new JLabel("Expresión prefija:"); lblExpPrefija.setFont(new Font("Arial", Font.PLAIN, 12));
		panelPrincipal.add(lblExpPrefija, gbc);

		gbc.gridx = 1; gbc.gridy = row++;
		txtExpPrefija = new JTextField(20); txtExpPrefija.setFont(new Font("Arial", Font.PLAIN, 12));
		txtExpPrefija.setPreferredSize(new Dimension(250, 25));
		txtExpPrefija.setEditable(false); txtExpPrefija.setBackground(Color.WHITE);
		panelPrincipal.add(txtExpPrefija, gbc);

		return panelPrincipal;
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new GridLayout(6, 6, 4, 4));
		panel.setBackground(new Color(20, 20, 20));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		for (String text : buttons) {
			JButton button = new JButton(text);
			button.setFont(new Font("Segoe UI", Font.BOLD, 14));

			button.setForeground(Color.BLACK);

			button.setFocusPainted(false);
			button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			if (isScientificFunction(text) || text.length() > 1) {
				button.setBackground(BUTTON_FUNCTION);
			} else if (isOperator(text) || text.equals("DEL") || text.equals("AC")) {
				button.setBackground(BUTTON_FUNCTION.darker());
			} else if (text.equals("=") || text.equals("+")) {
				button.setBackground(BUTTON_EQUAL_OP);
			} else {
				button.setBackground(BUTTON_DARK);
			}

			button.addActionListener(this::handleButtonClick);

			panel.add(button);
		}

		return panel;
	}

	private boolean isOperator(String s) {
		return s.equals("/") || s.equals("*") || s.equals("-") || s.equals("^") || s.equals("%");
	}

	private boolean isScientificFunction(String s) {
		return s.contains("SIN") || s.contains("COS") || s.contains("TAN") || s.contains("LOG") ||
				s.contains("LN") || s.equals("EXP") || s.equals("PI") || s.equals("sqrt") || s.contains("ACOS") || s.contains("ATAN") || s.equals("RAD") || s.equals("DEG");
	}

	private void handleButtonClick(ActionEvent e) {
		String command = ((JButton) e.getSource()).getText();
		String currentText = txtExpresion.getText().trim();

		if (command.equals("AC")) {
			txtExpresion.setText("");
		} else if (command.equals("DEL")) {
			if (!currentText.isEmpty()) {
				txtExpresion.setText(currentText.substring(0, currentText.length() - 1));
			}
		} else if (command.equals("=")) {
			evaluarExpresion();
		} else if (isScientificFunction(command) && !command.equals("PI")) {
			txtExpresion.setText(currentText + command + "(");
		} else {
			txtExpresion.setText(currentText + command);
		}
	}

	private void seleccionarArchivoAFD() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Seleccionar archivo del AFD");

		int resultado = fileChooser.showOpenDialog(this);

		if (resultado == JFileChooser.APPROVE_OPTION) {
			String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
			txtIdAFD.setText(rutaArchivo);
		}
	}

	private void evaluarExpresion() {
		String expresion = txtExpresion.getText().trim();

		if (expresion.isEmpty()) {
			JOptionPane.showMessageDialog(this,
				"Por favor, ingrese una expresión a evaluar",
				"Advertencia",
				JOptionPane.WARNING_MESSAGE);
		} else {
			txtResultado.setText("Evaluando: " + expresion);
			txtExpPrefija.setText("Generando prefija...");
		}
	}
}