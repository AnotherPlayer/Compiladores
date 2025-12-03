package BackEnd;

//1° parcial

// Clase Transicion según pseudocódigo líneas 373-4002
public class Transicion {

    public char SimboloInf;  // Simbolo_inferior en pseudocódigo
    public char SimboloSup;  // Simbolo_superior en pseudocódigo
    public Estado EdoDestino;

    // Alias usados por la GUI (campos en minúsculas)
    public char simboloInf;
    public char simboloSup;

    Transicion(){
        EdoDestino = null;
    }

    Transicion(char c, Estado e){
        SimboloInf = SimboloSup = c;
        simboloInf = simboloSup = c;
        EdoDestino = e;
    }

    Transicion(char c_inf, char c_sup, Estado e){
        SimboloInf = c_inf;
        SimboloSup = c_sup;
        simboloInf = c_inf;
        simboloSup = c_sup;
        EdoDestino = e;
    }

    // Método clear no está en el pseudocódigo pero lo dejamos por si se necesita
    int clear(){
        if(this.EdoDestino != null){
            this.EdoDestino = null;
            this.SimboloInf = this.SimboloSup = '\0';
            this.simboloInf = this.simboloSup = '\0';
            return 1; //Se realizó la limpieza
        }
        return 0;
    }

    
}
