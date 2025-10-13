package BackEnd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

	public Alfabeto getAlfabeto(){
		return alfabeto;
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

}
