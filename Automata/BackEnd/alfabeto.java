package BackEnd;

import java.util.ArrayList;

public class Alfabeto {

    private final ArrayList<Character> simbolos;

    public Alfabeto(){

        simbolos = new ArrayList<Character>();
        simbolos.clear();

    }
    
    public void union(Alfabeto a){

        for (Character c : a.simbolos)
            if (!this.simbolos.contains(c))
            this.simbolos.add(c);

    }
    
    public void clear(){

        simbolos.clear();

    }
    
    public boolean add(char c){

        if( simbolos.add(c) )
            return true;

        return false;

    }

    public boolean contains(char c){
        return simbolos.contains(c);
    }

    public int size(){
        return simbolos.size();
    }

    public ArrayList<Character> asList(){
        return new ArrayList<Character>(simbolos);
    }

}
