public class SimbolG {
    
    String NombSimb; //V_T y V_N
	int token;
	boolean esTerminal;
    
    SimbolG( String NombSimb, int token ){

        this.NombSimb = NombSimb;
        this.token = token;
        this.esTerminal = false;

    }

    SimbolG( String NombSimb, int token, boolean esTerminal ){

        this.NombSimb = NombSimb;
        this.token = token;
        this.esTerminal = esTerminal;

    }

}
