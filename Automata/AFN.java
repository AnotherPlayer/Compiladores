public class AFN {
    
    AFN(){

    }

    AFN doBasic( char c ){

        return this;

    }

    AFN doBasic( char c1,char c2 ){

        return this;

    }

    AFN union( AFN F2 ){

        return this;

    }

    AFN join( AFN F2 ){

        return this;

    }

    AFN cerrPos(){

        return this;

    }

    AFN cerrKleene(){

        return this;

    }

    AFN opcional(){

        return this;

    }

    //Cerradura de épsilon
    Estado[] cerrEpsilon( Estado e ){

        Estado[] C = new Estado[1];


        return C;

    }

    Estado[] mover( Estado[] e,char c ){

        Estado[] R = new Estado[1];

        return R;

    }

    Estado[] IrA( Estado[] e,char c ){

        Estado[] R = new Estado[1];

        return R;

    }

}
