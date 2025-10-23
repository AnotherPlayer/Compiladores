import java.util.ArrayList;

public class AFD {

    EdoAFD[] EdosAFD;
	ArrayList<Character> alfabeto;
	int NumEdos;
	
	AFD(){
		NumEdos = 0;
		alfabeto = new ArrayList<Character>();
	}
	
	AFD( int n ){
		EdosAFD = new EdoAFD[n];
		NumEdos = n;
		alfabeto = new ArrayList<Character>();
	}

	AFD( int n,ArrayList<Character> alf ){
		EdosAFD = new EdoAFD[n];
		NumEdos = n;
		alfabeto = new ArrayList<Character>();
		alfabeto.clear();
		alfabeto.union(alf);
	}
	
	boolean SaveAFD( String nameFile ){
		

        return true;

	}

	boolean LoadAFD( String nameFile ){
		//Vas Memo, te toca el código
		
		return true;
		
	}

}
