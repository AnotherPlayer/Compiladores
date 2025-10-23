import java.util.ArrayList;

public class Estado {

    int IdEdo;
    boolean EdoAcept;
    int Token;  
    ArrayList<Transicion> Transiciones; 
    static int NumEstados = 0;

    Estado(){
        IdEdo = NumEstados++;
        EdoAcept = false;
        Token = -1;
        Transiciones = new ArrayList<Transicion>();
        Transiciones.clear();
    }

    // Método TieneTransicion del pseudocódigo (línea 403-414)
    ArrayList<Estado> TieneTransicion(char c){
        ArrayList<Estado> R = new ArrayList<Estado>();
        R.clear();

        for(Transicion t : this.Transiciones){
            if(t.SimboloInf <= c && c <= t.SimboloSup)
                R.add(t.EdoDestino);
        }

        return R;
    }
    
}