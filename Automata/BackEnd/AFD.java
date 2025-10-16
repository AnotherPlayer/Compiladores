package BackEnd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class AFD {

    private EdoAFD[] EdosAFD;
	private Alfabeto alfabeto;
	private int NumEdos;
	
	public AFD(){
		NumEdos = 0;
		alfabeto = new Alfabeto();
	}
	
	public AFD( int n ){
		EdosAFD = new EdoAFD[n];
		NumEdos = n;
		alfabeto = new Alfabeto();
		for(int i=0;i<n;i++){
			EdosAFD[i] = new EdoAFD();
			EdosAFD[i].Id = i;
		}
	}

	public AFD( int n,Alfabeto alf ){
		this(n);
		alfabeto.clear();
		if(alf != null){
			alfabeto.union(alf);
		}
	}
	
	public EdoAFD[] getEstados(){
		return EdosAFD;
	}

	public EdoAFD getEstado(int index){
		if(index < 0 || index >= NumEdos){
			throw new IllegalArgumentException("Índice de estado fuera de rango");
		}
		return EdosAFD[index];
	}

	public Alfabeto getAlfabeto(){
		return alfabeto;
	}

	public List<Character> getSimbolos(){
		return alfabeto.asList();
	}

	public int getNumEdos(){
		return NumEdos;
	}

	public void setEstado(int index,EdoAFD estado){
		if(index < 0 || index >= NumEdos){
			throw new IllegalArgumentException("Índice de estado fuera de rango");
		}
		EdosAFD[index] = estado;
	}

	public boolean SaveAFD( String nameFile ){
		if(nameFile == null || nameFile.isEmpty()){
			return false;
		}

		try (BufferedWriter writer = Files.newBufferedWriter(Path.of(nameFile))) {
			writer.write(Integer.toString(NumEdos));
			writer.newLine();
			for(int i=0;i<NumEdos;i++){
				EdoAFD edo = EdosAFD[i];
				if(edo == null){
					edo = new EdoAFD();
					edo.Id = i;
				}
				writer.write(Integer.toString(edo.Id));
				writer.write(" ");
				writer.write(Integer.toString(edo.token));
				writer.newLine();
				for(int j=0;j<edo.TransAFD.length;j++){
					writer.write(Integer.toString(edo.TransAFD[j]));
					if(j < edo.TransAFD.length - 1){
						writer.write(" ");
					}
				}
				writer.newLine();
			}
			writer.write(Integer.toString(alfabeto.size()));
			writer.newLine();
			for(char c : alfabeto.asList()){
				writer.write(Integer.toString((int)c));
				writer.newLine();
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean LoadAFD( String nameFile ){
		if(nameFile == null || nameFile.isEmpty()){
			return false;
		}

		try (BufferedReader reader = Files.newBufferedReader(Path.of(nameFile))) {
			String line = reader.readLine();
			if(line == null){
				return false;
			}
			NumEdos = Integer.parseInt(line.trim());
			EdosAFD = new EdoAFD[NumEdos];
			for(int i=0;i<NumEdos;i++){
				line = reader.readLine();
				if(line == null){
					return false;
				}
				String[] parts = line.trim().split("\\s+");
				if(parts.length < 2){
					return false;
				}
				EdoAFD edo = new EdoAFD();
				edo.Id = Integer.parseInt(parts[0]);
				edo.token = Integer.parseInt(parts[1]);
				line = reader.readLine();
				if(line == null){
					return false;
				}
				String[] transParts = line.trim().split("\\s+");
				for(int j=0;j<edo.TransAFD.length && j<transParts.length;j++){
					edo.TransAFD[j] = Integer.parseInt(transParts[j]);
				}
				EdosAFD[i] = edo;
			}
			alfabeto.clear();
			line = reader.readLine();
			if(line == null){
				return false;
			}
			int alfSize = Integer.parseInt(line.trim());
			for(int i=0;i<alfSize;i++){
				line = reader.readLine();
				if(line == null){
					return false;
				}
				int codePoint = Integer.parseInt(line.trim());
				alfabeto.add((char)codePoint);
			}
			return true;
		} catch (IOException | NumberFormatException e) {
			return false;
		}
	}

	public TransitionTable buildTransitionTable(){
		List<Character> simbolos = getSimbolos();
		Collections.sort(simbolos);
		List<ColumnDefinition> columnas = construirColumnas(simbolos);

		List<String> headers = new ArrayList<String>();
		headers.add("Estado");
		for(ColumnDefinition columna : columnas){
			headers.add(columna.label);
		}
		headers.add("Token");
		headers.add("Acepta");

		List<String[]> filas = new ArrayList<String[]>();
		for(int i=0;i<NumEdos;i++){
			EdoAFD edo = EdosAFD[i];
			if(edo == null){
				edo = new EdoAFD();
				edo.Id = i;
				EdosAFD[i] = edo;
			}
			String[] fila = new String[headers.size()];
			fila[0] = Integer.toString(i);
			for(int j=0;j<columnas.size();j++){
				fila[1 + j] = valorTransicion(edo, columnas.get(j));
			}
			fila[headers.size()-2] = edo.token >= 0 ? Integer.toString(edo.token) : "";
			fila[headers.size()-1] = edo.esAceptacion ? "Sí" : "No";
			filas.add(fila);
		}
		return new TransitionTable(headers, filas);
	}

	public boolean exportTransitionTable(Path path){
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			TransitionTable tabla = buildTransitionTable();
			writer.write(String.join("\t", tabla.headers));
			writer.newLine();
			for(String[] fila : tabla.rows){
				writer.write(String.join("\t", fila));
				writer.newLine();
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static class TransitionTable{
		public final List<String> headers;
		public final List<String[]> rows;

		public TransitionTable(List<String> headers, List<String[]> rows){
			this.headers = headers;
			this.rows = rows;
		}
	}

	private List<ColumnDefinition> construirColumnas(List<Character> simbolos){
		ColumnDefinition digitos = new ColumnDefinition("D");
		ColumnDefinition letras = new ColumnDefinition("L");
		ColumnDefinition espacios = new ColumnDefinition("S");
		java.util.LinkedHashMap<Character, ColumnDefinition> otros = new java.util.LinkedHashMap<Character, ColumnDefinition>();

		for(char c : simbolos){
			if(Character.isDigit(c)){
				digitos.add(c);
			}else if(Character.isLetter(c)){
				letras.add(c);
			}else if(Character.isWhitespace(c)){
				espacios.add(c);
			}else{
				ColumnDefinition col = otros.get(c);
				if(col == null){
					col = new ColumnDefinition(formatearSimbolo(c));
					otros.put(c, col);
				}
				col.add(c);
			}
		}

		List<ColumnDefinition> resultado = new ArrayList<ColumnDefinition>();
		if(!digitos.isEmpty()){
			resultado.add(digitos);
		}
		if(!letras.isEmpty()){
			resultado.add(letras);
		}
		if(!espacios.isEmpty()){
			resultado.add(espacios);
		}
		resultado.addAll(otros.values());
		return resultado;
	}

	private String valorTransicion(EdoAFD edo, ColumnDefinition columna){
		if(columna.simbolos.isEmpty()){
			return "-";
		}
		ArrayList<String> valores = new ArrayList<String>();
		for(char c : columna.simbolos){
			int destino = edo.TransAFD[c & 0xFF];
			String valor = destino >= 0 ? Integer.toString(destino) : "-";
			if(!valores.contains(valor)){
				valores.add(valor);
			}
		}
		if(valores.isEmpty()){
			return "-";
		}
		if(valores.size() == 1){
			return valores.get(0);
		}
		return String.join("/", valores);
	}

	private String formatearSimbolo(char c){
		if(c == SimbEspeciales.EPSILON){
			return "ε";
		}
		if(c == SimbEspeciales.FIN){
			return "FIN";
		}
		if(Character.isWhitespace(c)){
			return "WS(" + Integer.toString((int)c) + ")";
		}
		if(Character.isISOControl(c)){
			return "ASCII " + Integer.toString((int)c);
		}
		return Character.toString(c);
	}

	private static class ColumnDefinition{
		final String label;
		final ArrayList<Character> simbolos;

		ColumnDefinition(String label){
			this.label = label;
			this.simbolos = new ArrayList<Character>();
		}

		void add(char c){
			simbolos.add(c);
		}

		boolean isEmpty(){
			return simbolos.isEmpty();
		}
	}

}
