import java.util.ArrayList;

// Clase ElemSj según pseudocódigo líneas 427-441
public class ElemSj {

    int Id;
    ArrayList<Estado> S;

    ElemSj(){
        Id = -1;
        S = new ArrayList<Estado>();
        S.clear();
    }

    // Constructor adicional para facilitar el uso
    ElemSj(int id){
        Id = id;
        S = new ArrayList<Estado>();
        S.clear();
    }
}