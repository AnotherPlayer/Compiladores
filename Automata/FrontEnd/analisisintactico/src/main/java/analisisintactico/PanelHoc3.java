package analisisintactico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelHoc3 extends JPanel {

    private JTextArea areaExpresion;
    private JTable tablaTokens;
    private JTextArea areaConsolaSintactica;
    private JButton btnAnalizar;

    public PanelHoc3() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new GridBagLayout());
        setBackground(new Color(230, 235, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4; gbc.weighty = 0;
        add(new JLabel("Expresión a analizar:", SwingConstants.LEFT), gbc);

        btnAnalizar = new JButton("Analizar Léxicamente");
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.6; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST;
        add(btnAnalizar, gbc);

        areaExpresion = new JTextArea(10, 20);
        areaExpresion.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaExpresion.setEditable(true);
        JScrollPane scrollExp = new JScrollPane(areaExpresion);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.4; gbc.weighty = 0.4; gbc.fill = GridBagConstraints.BOTH;
        add(scrollExp, gbc);

        String[] columnas = {"Token", "Identificador", "Lexema"};
        DefaultTableModel modeloTokens = new DefaultTableModel(columnas, 0);
        tablaTokens = new JTable(modeloTokens);
        tablaTokens.setEnabled(false);
        JScrollPane scrollTabla = new JScrollPane(tablaTokens);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.6;
        add(scrollTabla, gbc);

        JButton btnSintactico = new JButton("Sintáctico");
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.weighty = 0; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        add(btnSintactico, gbc);

        areaConsolaSintactica = new JTextArea(8, 20);
        areaConsolaSintactica.setEditable(false);
        areaConsolaSintactica.setBackground(Color.WHITE);
        areaConsolaSintactica.setFont(new Font("Consolas", Font.PLAIN, 12));
        JScrollPane scrollConsola = new JScrollPane(areaConsolaSintactica);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.weighty = 0.4; gbc.fill = GridBagConstraints.BOTH;
        add(scrollConsola, gbc);
    }
}
