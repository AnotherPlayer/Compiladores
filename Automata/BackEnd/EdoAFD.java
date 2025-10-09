package BackEnd;

public class EdoAFD {

    int[] TransAFD;
	int Id;            //--> En caso de usar conjuntos
	int token;
	
	EdoAFD(){
		TransAFD = new int[257];
		Id = -1;
		for( int i=0 ; i<=256 ; i++ )
			TransAFD[i] = -1;
	}
    
}
