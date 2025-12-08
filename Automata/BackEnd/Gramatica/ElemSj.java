package BackEnd;

//2° parcial

import java.util.ArrayList;

public class ElemSj {

    int Id;
    ArrayList<Estado> S;

    ElemSj(){
        Id = -1;
        S = new ArrayList<Estado>();
        S.clear();
    }

    ElemSj(int id){
        Id = id;
        S = new ArrayList<Estado>();
        S.clear();
    }
}
