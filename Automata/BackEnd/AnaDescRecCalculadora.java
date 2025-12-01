//2° parcial

public class AnaDescRecCalculadora {

    int token;
    AFN afn;
    analizadorLexico Lexico;

    AnaDescRecCalculadora(){

        token = -1;
        afn = new AFN();
        Lexico = new analizadorLexico();

    }

    AnaDescRecCalculadora( AFN afn ){

        token = -1;
        this.afn = afn;
        Lexico = new analizadorLexico();

    }

    //E
    boolean E(AFN f){

        if( T(f) )
            if( Ep(f) )
                return true;
            
        return false;

    }

    //E'
    boolean Ep(AFN f){

        int token;
        AFN f2 = new AFN();

        token = Lexico.getToken();

        if( token == 10 ){ // Token == '+'
            if( T(f) ){
				f.AFN_union(f2);
                if(Ep(f))
                    return true;
            }

            else
                return false;
        }

        Lexico.UndoToken();
        return true;
    }

    //T
    boolean T( AFN f ){

        if( C(f) )
		    if( Tp(f) )
			    return true;
			
	    return false;

    }

    //T'
    boolean Tp( AFN f ){

        int token;
	    AFN f2 = new AFN();
	
	    token = Lexico.yylex();
	
	    if( token == 20 ){
    		if( C(f) ){
			    f.AFN_join(f2);
			    if( Tp(f) )
				    return true;
				
			}

		return false;

	}

	Lexico.UndoToken();
	return true;

    }

    //C
    boolean C( AFN f ){

        if( F(f) )
		    if( Cp(f) )
    			return true;
			
    	return false;

    }
    
    //C'
    boolean Cp( AFN f ){

        int token;
    
        token = Lexico.yylex();
    
        switch(token){
            
		    case 30:
		    	f.AFN_cerrPos();
		    break;

		    case 40:
		    	f.AFN_cerrKleene();
		    break;

		    case 50:
		    	f.AFN_opcional();
		    break;
            
		    default:
		    	Lexico.UndoToken();
		    return true;
	
    	}

	    return Cp(f);

    }

    boolean F( AFN f ){

        char simb1,simb2 = 0;
	    int token;
	    
        token = Lexico.yylex();
	
	    switch( token ){
	
	        case 60:
		        if( E(f) ){
			        token = Lexico.yylex();
			        if(token == 70)
				        return true;
		        }

		    return false;
            //break;

	        case 80:
		        simb1 = Lexico.getYytext().charAt(0);
		        f.doBasic(simb1);
		    return true;
            //break;
		
	        case 90:
		        token = Lexico.yylex();
		        if( token == 80 ){
			        simb1 = Lexico.getYytext().charAt(0);
			        token = Lexico.yylex();
                    if( token == 110 ){
				        token = Lexico.yylex();
				        if( token == 60 )
					        simb2 = Lexico.getYytext().charAt(0);
					        token = Lexico.yylex();
					        if( token == 100 ){
						        f.doBasic(simb1, simb2);
						        return true;
					        }
				    }
			}
	        //break;
		}

		return false;

    }
}