package analisisintactico;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelERtoAFN extends JPanel {

	private JTextField txtExpresionRegular;
	private JButton btnCrearAFN;

	public PanelERtoAFN() {
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		setLayout(new BorderLayout(10, 10));
		setBackground(new Color(240, 240, 240));
		setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

		JPanel panelPrincipal = new JPanel(new GridBagLayout());
		panelPrincipal.setBackground(new Color(240, 240, 240));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		int row = 0;

		// Título
		gbc.gridx = 0;
		gbc.gridy = row++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		JLabel lblTitulo = new JLabel("Expresión Regular a AFN");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
		lblTitulo.setForeground(new Color(40, 40, 40));
		panelPrincipal.add(lblTitulo, gbc);

		// Espacio
		gbc.gridy = row++;
		panelPrincipal.add(Box.createVerticalStrut(25), gbc);

		// Label Expresión Regular
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.weightx = 0.3;
		JLabel lblExpresionRegular = new JLabel("Expresión Regular:");
		lblExpresionRegular.setFont(new Font("Arial", Font.PLAIN, 15));
		panelPrincipal.add(lblExpresionRegular, gbc);

		// Campo de texto Expresión Regular
		gbc.gridx = 1;
		gbc.gridy = row++;
		gbc.weightx = 0.7;
		gbc.anchor = GridBagConstraints.WEST;
		txtExpresionRegular = new JTextField(25);
		txtExpresionRegular.setFont(new Font("Arial", Font.PLAIN, 14));
		txtExpresionRegular.setPreferredSize(new Dimension(400, 30));
		panelPrincipal.add(txtExpresionRegular, gbc);

		// Espacio
		gbc.gridx = 0;
		gbc.gridy = row++;
		gbc.gridwidth = 2;
		panelPrincipal.add(Box.createVerticalStrut(25), gbc);

		// Botón Crear AFN
		gbc.gridx = 0;
		gbc.gridy = row++;
		gbc.gridwidth = 2;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		btnCrearAFN = new JButton("Crear AFN");
		btnCrearAFN.setFont(new Font("Arial", Font.BOLD, 15));
		btnCrearAFN.setPreferredSize(new Dimension(200, 40));
		btnCrearAFN.setFocusPainted(false);
		btnCrearAFN.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnCrearAFN.addActionListener(e -> crearAFN());
		panelPrincipal.add(btnCrearAFN, gbc);

		add(panelPrincipal, BorderLayout.CENTER);
	}

	private void crearAFN() {
		String expresionRegular = txtExpresionRegular.getText().trim();

		if (expresionRegular.isEmpty()) {
			JOptionPane.showMessageDialog(this,
				"Por favor, ingrese una expresión regular",
				"Advertencia",
				JOptionPane.WARNING_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(this,
			"AFN creado exitosamente\nExpresión: " + expresionRegular,
			"Éxito",
			JOptionPane.INFORMATION_MESSAGE);
	}

	public JTextField getTxtExpresionRegular() { return txtExpresionRegular; }
	public JButton getBtnCrearAFN() { return btnCrearAFN; }
}