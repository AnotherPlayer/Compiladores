// Clase EdoAFD según pseudocódigo líneas 445-469
public class EdoAFD {

    int[] transAFD;  // TransAFD en pseudocódigo - tabla de 257 posiciones (0-256 para ASCII extendido)
    int ID;          // ID en pseudocódigo - En caso de usar conjuntos
    int Token;
    
    EdoAFD(){
        transAFD = new int[257];
        ID = -1;
        Token = -1;
        for(int i = 0; i <= 256; i++)
            transAFD[i] = -1;
    }

    EdoAFD(int idEdo){
        transAFD = new int[257];
        ID = idEdo;
        Token = -1;
        for(int i = 0; i <= 256; i++)
            transAFD[i] = -1;
    }
    
}
