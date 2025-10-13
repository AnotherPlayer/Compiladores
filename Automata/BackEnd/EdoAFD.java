package BackEnd;

public class EdoAFD {

    public int[] TransAFD;
	public int Id;            //--> En caso de usar conjuntos
	public int token;
	
	public EdoAFD(){
		TransAFD = new int[257];
		Id = -1;
		token = -1;
		for( int i=0 ; i<=256 ; i++ )
			TransAFD[i] = -1;
	}
    
}
