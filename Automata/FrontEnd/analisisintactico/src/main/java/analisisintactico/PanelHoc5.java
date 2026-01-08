package analisisintactico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelHoc5 extends JPanel {

    private JTextArea areaCadena;
    private JTable tablaTokens;
    private JTextArea areaConsolaSintactica;
    private JTable tablaCodigo;
    private JTable tablaPila;
    private JTextArea areaResultados;
    private JButton btnAnalizarLexico;
    private JButton btnAnalizarSintactico;
    private JButton btnEjecutar;

    public PanelHoc5() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new GridBagLayout());
        setBackground(new Color(220, 225, 235));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        JPanel panelSuperior = new JPanel(new GridBagLayout());
        panelSuperior.setOpaque(false);
        GridBagConstraints gbcSup = new GridBagConstraints();
        gbcSup.insets = new Insets(2, 2, 2, 2);
        gbcSup.fill = GridBagConstraints.BOTH;

        areaCadena = new JTextArea(8, 20);
        areaCadena.setEditable(true);
        JScrollPane scrollCadena = new JScrollPane(areaCadena);
        scrollCadena.setBorder(BorderFactory.createTitledBorder("Cadena a Analizar Léxicamente"));
        gbcSup.gridx = 0; gbcSup.gridy = 1; gbcSup.weightx = 0.5; gbcSup.weighty = 1.0;
        panelSuperior.add(scrollCadena, gbcSup);

        btnAnalizarLexico = new JButton("Analizar Léxicamente");
        gbcSup.gridx = 1; gbcSup.gridy = 0; gbcSup.weightx = 0; gbcSup.weighty = 0;
        gbcSup.fill = GridBagConstraints.NONE;
        panelSuperior.add(btnAnalizarLexico, gbcSup);

        tablaTokens = new JTable(new DefaultTableModel(new Object[]{"Token", "Identificador", "Lexema"}, 0));
        tablaTokens.setEnabled(false);
        JScrollPane scrollTokens = new JScrollPane(tablaTokens);
        gbcSup.gridx = 1; gbcSup.gridy = 1; gbcSup.weightx = 0.5; gbcSup.weighty = 1.0;
        gbcSup.fill = GridBagConstraints.BOTH;
        panelSuperior.add(scrollTokens, gbcSup);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weighty = 0.3;
        add(panelSuperior, gbc);

        JPanel panelMedio = new JPanel(new GridBagLayout());
        panelMedio.setOpaque(false);
        GridBagConstraints gbcMid = new GridBagConstraints();
        gbcMid.insets = new Insets(2, 2, 2, 2);
        gbcMid.fill = GridBagConstraints.BOTH;

        btnAnalizarSintactico = new JButton("Analizar Sintácticamente");
        gbcMid.gridx = 0; gbcMid.gridy = 0; gbcMid.weightx = 0; gbcMid.weighty = 0;
        gbcMid.fill = GridBagConstraints.NONE;
        panelMedio.add(btnAnalizarSintactico, gbcMid);

        areaConsolaSintactica = new JTextArea(8, 15);
        areaConsolaSintactica.setEditable(false);
        JScrollPane scrollConsola = new JScrollPane(areaConsolaSintactica);
        gbcMid.gridx = 0; gbcMid.gridy = 1; gbcMid.weightx = 0.3; gbcMid.weighty = 1.0;
        gbcMid.fill = GridBagConstraints.BOTH;
        panelMedio.add(scrollConsola, gbcMid);

        btnEjecutar = new JButton("Ejecutar código");
        gbcMid.gridx = 1; gbcMid.gridy = 0; gbcMid.weightx = 0; gbcMid.weighty = 0;
        gbcMid.fill = GridBagConstraints.NONE;
        panelMedio.add(btnEjecutar, gbcMid);

        tablaCodigo = new JTable(new DefaultTableModel(new Object[]{"INST-SYMB-FUNC", "NAME", "VAL", "FUNCIÓN"}, 0));
        tablaCodigo.setEnabled(false);
        JScrollPane scrollCodigo = new JScrollPane(tablaCodigo);
        gbcMid.gridx = 1; gbcMid.gridy = 1; gbcMid.weightx = 0.4; gbcMid.weighty = 1.0;
        gbcMid.fill = GridBagConstraints.BOTH;
        panelMedio.add(scrollCodigo, gbcMid);

        tablaPila = new JTable(new DefaultTableModel(new Object[]{"Datum", "Valor", "Symbol", "Type Symbol", "Val Symbol"}, 0));
        tablaPila.setEnabled(false);
        JScrollPane scrollPila = new JScrollPane(tablaPila);
        scrollPila.setBorder(BorderFactory.createTitledBorder("PILA"));
        gbcMid.gridx = 2; gbcMid.gridy = 1; gbcMid.weightx = 0.3; gbcMid.weighty = 1.0;
        panelMedio.add(scrollPila, gbcMid);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weighty = 0.5;
        add(panelMedio, gbc);

        areaResultados = new JTextArea(4, 20);
        areaResultados.setEditable(false);
        JScrollPane scrollRes = new JScrollPane(areaResultados);
        scrollRes.setBorder(BorderFactory.createTitledBorder("RESULTADOS"));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0.3; gbc.weighty = 0.2;
        add(scrollRes, gbc);
    }
}