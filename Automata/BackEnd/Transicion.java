// Clase Transicion según pseudocódigo líneas 373-400
public class Transicion {

    char SimboloInf;  // Simbolo_inferior en pseudocódigo
    char SimboloSup;  // Simbolo_superior en pseudocódigo
    Estado EdoDestino;

    Transicion(){
        EdoDestino = null;
    }

    Transicion(char c, Estado e){
        SimboloInf = SimboloSup = c;
        EdoDestino = e;
    }

    Transicion(char c_inf, char c_sup, Estado e){
        SimboloInf = c_inf;
        SimboloSup = c_sup;
        EdoDestino = e;
    }

    // Método clear no está en el pseudocódigo pero lo dejamos por si se necesita
    int clear(){
        if(this.EdoDestino != null){
            this.EdoDestino = null;
            this.SimboloInf = this.SimboloSup = '\0';
            return 1; //Se realizó la limpieza
        }
        return 0;
    }

    
}
