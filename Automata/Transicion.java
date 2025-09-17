public class Transicion {

    char simboloInf;
    char simboloSup;
    Estado EdoDestino;

    Transicion(){

        EdoDestino = null;

    }

    Transicion( char c,Estado e ){

        simboloInf = simboloSup = c;
        EdoDestino = e;

    }

    Transicion( char cInf,char cSup,Estado e ){

        simboloInf = cInf;
        simboloSup = cSup;
        EdoDestino = e;

    }

    void clear(){

        

    }

    
}
