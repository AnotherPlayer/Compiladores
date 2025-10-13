package BackEnd;

public class Transicion {

    public char simboloInf;
    public char simboloSup;
    public Estado EdoDestino;

    public Transicion(){

        EdoDestino = null;

    }

    public Transicion( char c,Estado e ){

        simboloSup = simboloInf = c;
        EdoDestino = e;

    }

    public Transicion( char cInf,char cSup,Estado e ){

        simboloSup = cSup;
        simboloInf = cInf;
        EdoDestino = e;

    }

    public int clear(){

        if( this.EdoDestino != null ){
            this.EdoDestino = null;
            this.simboloInf = this.simboloSup = '\0';
            return 1; //Se realizó la limpieza
        }

        return 0;

    }

    
}
