public class Transicion {

    char simboloInf;
    char simboloSup;
    Estado EdoDestino;

    Transicion(){

        EdoDestino = null;

    }

    Transicion( char c,Estado e ){

        simboloSup = simboloInf = c;
        EdoDestino = e;

    }

    Transicion( char cInf,char cSup,Estado e ){

        simboloSup = cSup;
        simboloInf = cInf;
        EdoDestino = e;

    }

    int clear(){

        if( this.EdoDestino != null ){
            this.EdoDestino = null;
            this.simboloInf = this.simboloSup = '\0';
            return 1; //Se realizó la limpieza
        }

        return 0;

    }

    
}
