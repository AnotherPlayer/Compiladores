import java.util.ArrayList;

public class Alfabeto {

    ArrayList<Character> simbolos;

    Alfabeto(){

        simbolos = new ArrayList<Character>();
        simbolos.clear();

    }
    
    void union( Alfabeto a ){

        for (Character c : a.simbolos)
            if (!this.simbolos.contains(c))
            this.simbolos.add(c);

    }
    
    void clear(){

        simbolos.clear();

    }
    
    boolean add( char c ){

        if( simbolos.add(c) )
            return true;

        return false;

    }

}

