package analisisintactico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import java.io.File;

public class PanelERtoAFN extends JPanel {

	private JTextField txtIdAFD;
	private JButton btnSeleccionarArchivo;
	private JTextField txtExpresionRegular;
	private JTextField txtIdParaAFN;
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

		gbc.gridx = 0;
		gbc.gridy = row++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		JLabel lblTitulo = new JLabel("Analizador Léxico");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
		lblTitulo.setForeground(new Color(40, 40, 40));
		panelPrincipal.add(lblTitulo, gbc);

		gbc.gridy = row++;
		panelPrincipal.add(Box.createVerticalStrut(25), gbc);

		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.weightx = 0.3;
		JLabel lblIdAFD = new JLabel("Id del AFD:");
		lblIdAFD.setFont(new Font("Arial", Font.PLAIN, 15));
		panelPrincipal.add(lblIdAFD, gbc);

		gbc.gridx = 1;
		gbc.gridy = row++;
		gbc.weightx = 0.7;
		gbc.anchor = GridBagConstraints.WEST;
		txtIdAFD = new JTextField(25);
		txtIdAFD.setFont(new Font("Arial", Font.PLAIN, 14));
		txtIdAFD.setPreferredSize(new Dimension(400, 30));
		panelPrincipal.add(txtIdAFD, gbc);

		gbc.gridx = 0;
		gbc.gridy = row++;
		gbc.gridwidth = 2;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		btnSeleccionarArchivo = new JButton("Seleccionar archivo del AFD");
		btnSeleccionarArchivo.setFont(new Font("Arial", Font.PLAIN, 13));
		btnSeleccionarArchivo.setPreferredSize(new Dimension(280, 35));
		btnSeleccionarArchivo.setFocusPainted(false);
		btnSeleccionarArchivo.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnSeleccionarArchivo.addActionListener(e -> seleccionarArchivoAFD());
		panelPrincipal.add(btnSeleccionarArchivo, gbc);

		gbc.gridy = row++;
		panelPrincipal.add(Box.createVerticalStrut(25), gbc);

		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.weightx = 0.3;
		JLabel lblExpresionRegular = new JLabel("Expresión Regular:");
		lblExpresionRegular.setFont(new Font("Arial", Font.PLAIN, 15));
		panelPrincipal.add(lblExpresionRegular, gbc);

		gbc.gridx = 1;
		gbc.gridy = row++;
		gbc.weightx = 0.7;
		gbc.anchor = GridBagConstraints.WEST;
		txtExpresionRegular = new JTextField(25);
		txtExpresionRegular.setFont(new Font("Arial", Font.PLAIN, 14));
		txtExpresionRegular.setPreferredSize(new Dimension(400, 30));
		panelPrincipal.add(txtExpresionRegular, gbc);

		gbc.gridy = row++;
		panelPrincipal.add(Box.createVerticalStrut(25), gbc);

		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.weightx = 0.3;
		JLabel lblIdParaAFN = new JLabel("Id. Para el AFN:");
		lblIdParaAFN.setFont(new Font("Arial", Font.PLAIN, 15));
		panelPrincipal.add(lblIdParaAFN, gbc);

		gbc.gridx = 1;
		gbc.gridy = row++;
		gbc.weightx = 0.7;
		gbc.anchor = GridBagConstraints.WEST;
		txtIdParaAFN = new JTextField(25);
		txtIdParaAFN.setFont(new Font("Arial", Font.PLAIN, 14));
		txtIdParaAFN.setPreferredSize(new Dimension(400, 30));
		panelPrincipal.add(txtIdParaAFN, gbc);

		gbc.gridx = 0;
		gbc.gridy = row++;
		gbc.gridwidth = 2;
		panelPrincipal.add(Box.createVerticalStrut(15), gbc);

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

	private void seleccionarArchivoAFD() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Seleccionar archivo del AFD");

		int resultado = fileChooser.showOpenDialog(this);

		if (resultado == JFileChooser.APPROVE_OPTION) {
			String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
			txtIdAFD.setText(rutaArchivo);
		}
	}

	private void crearAFN() {
		String expresionRegular = txtExpresionRegular.getText().trim();
		String idAFN = txtIdParaAFN.getText().trim();

		if (expresionRegular.isEmpty()) {
			JOptionPane.showMessageDialog(this,
				"Por favor, ingrese una expresión regular",
				"Advertencia",
				JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (idAFN.isEmpty()) {
			JOptionPane.showMessageDialog(this,
				"Por favor, ingrese un ID para el AFN",
				"Advertencia",
				JOptionPane.WARNING_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(this,
			"AFN creado exitosamente con ID: " + idAFN + "\nExpresión: " + expresionRegular,
			"Éxito",
			JOptionPane.INFORMATION_MESSAGE);
	}

	public JTextField getTxtIdAFD() { return txtIdAFD; }
	public JTextField getTxtExpresionRegular() { return txtExpresionRegular; }
	public JTextField getTxtIdParaAFN() { return txtIdParaAFN; }
	public JButton getBtnSeleccionarArchivo() { return btnSeleccionarArchivo; }
	public JButton getBtnCrearAFN() { return btnCrearAFN; }
}