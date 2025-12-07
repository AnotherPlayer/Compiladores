//1° parcial

package BackEnd;

// Clase EdoAFD según pseudocódigo líneas 445-469
public class EdoAFD {

    int[] transAFD;  // TransAFD en pseudocódigo - tabla de 257 posiciones (0-256 para ASCII extendido)
    public int[] TransAFD; // Alias para el front
    int ID;          // ID en pseudocódigo - En caso de usar conjuntos
    int Token;
    public int token;       // Alias para el front
    public boolean esAceptacion;
    
    EdoAFD(){
        transAFD = new int[257];
        TransAFD = transAFD;
        ID = -1;
        Token = -1;
        token = -1;
        esAceptacion = false;
        for(int i = 0; i <= 256; i++)
            transAFD[i] = -1;
    }

    EdoAFD(int idEdo){
        transAFD = new int[257];
        TransAFD = transAFD;
        ID = idEdo;
        Token = -1;
        token = -1;
        esAceptacion = false;
        for(int i = 0; i <= 256; i++)
            transAFD[i] = -1;
    }
    
}
