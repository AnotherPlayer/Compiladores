//2° parcial

/*
	Para la calculadora debemos de considerar que tenemos que seguir los siguiengtes pasos:
	1.- Pasar el AFD.
	2.- Obtener cadena que represente la operacion.
	3.- Validar la cadena.
	4.- Realizar la operacion.
	5.- Pasar cadena a postfijo con analizador descendente recursivo.
*/

public class AnaDescRecCalculadora {

    int token;
    AFD afd;
    analizadorLexico Lexico;
    String sigma;

    AnaDescRecCalculadora( AFD afd,String sigma ){

        token = -1;
        this.afd = afd;
        this.sigma = sigma;
        Lexico = new analizadorLexico(afd);
		Lexico.SetSigma(sigma);

    }

	//Verifica si la cadena es valida para el AFD
	boolean analizarSigma(){

		// Verificar que la cadena sea valida para el AFD en el analizador lexico
        boolean valida = true;
        boolean errorLexico = false;
        int tk;

        // Analizamos toda la cadena
        while( true ){
            tk = Lexico.yylex();
            if( tk == -1 ){
                // yytext vacio => fin de cadena; con info => error lex
                if( !Lexico.getYytext().isEmpty() ){
                    errorLexico = true;
                }
                break;
            }
            if( tk <= 0 )
                valida = false;
        }

        if( errorLexico )
            valida = false;

        // Confirmamos que se haya consumido toda la cadena
        if( valida && sigma.length() > 0 ){
            valida = (Lexico.getFinLexema() == sigma.length() - 1);
        }

		else if( sigma.length() == 0 ){
            valida = false;
        }

        // Reposicionar el analizador para futuros usos (parsing, etc.)
        Lexico.SetSigma(sigma);
        token = -1;

        return valida;

    }

    void doOperacion(){

        try{
            double resultado = evaluarExpresion(sigma);
            System.out.println("Resultado: " + resultado);
        }catch(IllegalArgumentException ex){
            System.out.println("Error al evaluar la expresión: " + ex.getMessage());
        }

	}

	void pasarPostfijo(){

        try{
            String postfijo = convertirPostfijo(sigma);
            System.out.println("Postfijo: " + postfijo);
        }catch(IllegalArgumentException ex){
            System.out.println("Error al convertir a postfijo: " + ex.getMessage());
        }

	}

    // === Analizador recursivo descendente embebido (sin clases auxiliares) ===
    // Tokens para el parser de evaluación/postfijo
    private static final int TT_EOF   = 0;
    private static final int TT_NUM   = 1;
    private static final int TT_IDENT = 2;
    private static final int TT_MAS   = 3;
    private static final int TT_MENOS = 4;
    private static final int TT_MUL   = 5;
    private static final int TT_DIV   = 6;
    private static final int TT_POW   = 7;
    private static final int TT_LP    = 8;
    private static final int TT_RP    = 9;

    // Estado de scanner para evaluación
    private String srcEval;
    private int idxEval;
    private int tokEval;
    private String lexEval;
    private double valEval;

    // Estado de scanner para postfijo
    private String srcPost;
    private int idxPost;
    private int tokPost;
    private String lexPost;

    // ---------- Evaluación ----------
    private double evaluarExpresion(String src){
        srcEval = src;
        idxEval = 0;
        siguienteEval();
        double res = exprEval();
        if(tokEval != TT_EOF){
            throw new IllegalArgumentException("Entrada extra al final de la expresión");
        }
        return res;
    }

    private void omitirEspaciosEval(){
        while(idxEval < srcEval.length() && Character.isWhitespace(srcEval.charAt(idxEval))){
            idxEval++;
        }
    }

    private void siguienteEval(){
        omitirEspaciosEval();
        if(idxEval >= srcEval.length()){
            tokEval = TT_EOF;
            lexEval = "";
            valEval = 0;
            return;
        }

        char c = srcEval.charAt(idxEval);
        if(Character.isDigit(c) || c == '.'){
            int ini = idxEval;
            boolean vistoPunto = (c == '.');
            idxEval++;
            while(idxEval < srcEval.length()){
                char d = srcEval.charAt(idxEval);
                if(Character.isDigit(d)){
                    idxEval++;
                } else if(d == '.' && !vistoPunto){
                    vistoPunto = true;
                    idxEval++;
                } else {
                    break;
                }
            }
            lexEval = srcEval.substring(ini, idxEval);
            valEval = Double.parseDouble(lexEval);
            tokEval = TT_NUM;
            return;
        }

        if(Character.isLetter(c)){
            int ini = idxEval;
            idxEval++;
            while(idxEval < srcEval.length()){
                char d = srcEval.charAt(idxEval);
                if(Character.isLetterOrDigit(d) || d == '_'){
                    idxEval++;
                } else {
                    break;
                }
            }
            lexEval = srcEval.substring(ini, idxEval);
            tokEval = TT_IDENT;
            return;
        }

        idxEval++;
        switch(c){
            case '+': tokEval = TT_MAS;   lexEval = "+"; return;
            case '-': tokEval = TT_MENOS; lexEval = "-"; return;
            case '*': tokEval = TT_MUL;   lexEval = "*"; return;
            case '/': tokEval = TT_DIV;   lexEval = "/"; return;
            case '^': tokEval = TT_POW;   lexEval = "^"; return;
            case '(': tokEval = TT_LP;    lexEval = "("; return;
            case ')': tokEval = TT_RP;    lexEval = ")"; return;
            default:
                throw new IllegalArgumentException("Símbolo no reconocido: '" + c + "'");
        }
    }

    private void esperarEval(int t){
        if(tokEval != t){
            throw new IllegalArgumentException("Se esperaba otro token y se encontró '" + lexEval + "'");
        }
        siguienteEval();
    }

    private double exprEval(){
        double v = termEval();
        while(tokEval == TT_MAS || tokEval == TT_MENOS){
            int op = tokEval;
            siguienteEval();
            double rhs = termEval();
            v = (op == TT_MAS) ? v + rhs : v - rhs;
        }
        return v;
    }

    private double termEval(){
        double v = powerEval();
        while(tokEval == TT_MUL || tokEval == TT_DIV){
            int op = tokEval;
            siguienteEval();
            double rhs = powerEval();
            if(op == TT_MUL)
                v *= rhs;
            else
                v /= rhs;
        }
        return v;
    }

    private double powerEval(){
        double base = unaryEval();
        if(tokEval == TT_POW){
            siguienteEval();
            double exp = powerEval(); // asociatividad derecha
            base = Math.pow(base, exp);
        }
        return base;
    }

    private double unaryEval(){
        if(tokEval == TT_MAS){
            siguienteEval();
            return unaryEval();
        }
        if(tokEval == TT_MENOS){
            siguienteEval();
            return -unaryEval();
        }
        return primaryEval();
    }

    private double primaryEval(){
        if(tokEval == TT_NUM){
            double v = valEval;
            siguienteEval();
            return v;
        }

        if(tokEval == TT_IDENT){
            String nombre = lexEval.toLowerCase();
            siguienteEval();
            if(tokEval == TT_LP){
                siguienteEval();
                double arg = exprEval();
                esperarEval(TT_RP);
                return aplicarFuncion(nombre, arg);
            }
            if("pi".equals(nombre))
                return Math.PI;
            if("e".equals(nombre))
                return Math.E;
            throw new IllegalArgumentException("Identificador no soportado: " + nombre);
        }

        if(tokEval == TT_LP){
            siguienteEval();
            double v = exprEval();
            esperarEval(TT_RP);
            return v;
        }

        throw new IllegalArgumentException("Token inesperado: " + lexEval);
    }

    private double aplicarFuncion(String nombre, double arg){
        switch(nombre){
            case "sin": return Math.sin(arg);
            case "cos": return Math.cos(arg);
            case "tan": return Math.tan(arg);
            case "sqrt": return Math.sqrt(arg);
            case "log": return Math.log(arg);
            case "exp": return Math.exp(arg);
            case "abs": return Math.abs(arg);
            default:
                throw new IllegalArgumentException("Función no soportada: " + nombre);
        }
    }

    // ---------- Postfijo ----------
    private String convertirPostfijo(String src){
        srcPost = src;
        idxPost = 0;
        siguientePost();
        String res = exprPost().trim();
        if(tokPost != TT_EOF){
            throw new IllegalArgumentException("Entrada extra al final de la expresión");
        }
        return res;
    }

    private void omitirEspaciosPost(){
        while(idxPost < srcPost.length() && Character.isWhitespace(srcPost.charAt(idxPost))){
            idxPost++;
        }
    }

    private void siguientePost(){
        omitirEspaciosPost();
        if(idxPost >= srcPost.length()){
            tokPost = TT_EOF;
            lexPost = "";
            return;
        }

        char c = srcPost.charAt(idxPost);
        if(Character.isDigit(c) || c == '.'){
            int ini = idxPost;
            boolean vistoPunto = (c == '.');
            idxPost++;
            while(idxPost < srcPost.length()){
                char d = srcPost.charAt(idxPost);
                if(Character.isDigit(d)){
                    idxPost++;
                } else if(d == '.' && !vistoPunto){
                    vistoPunto = true;
                    idxPost++;
                } else {
                    break;
                }
            }
            lexPost = srcPost.substring(ini, idxPost);
            tokPost = TT_NUM;
            return;
        }

        if(Character.isLetter(c)){
            int ini = idxPost;
            idxPost++;
            while(idxPost < srcPost.length()){
                char d = srcPost.charAt(idxPost);
                if(Character.isLetterOrDigit(d) || d == '_'){
                    idxPost++;
                } else {
                    break;
                }
            }
            lexPost = srcPost.substring(ini, idxPost);
            tokPost = TT_IDENT;
            return;
        }

        idxPost++;
        switch(c){
            case '+': tokPost = TT_MAS;   lexPost = "+"; return;
            case '-': tokPost = TT_MENOS; lexPost = "-"; return;
            case '*': tokPost = TT_MUL;   lexPost = "*"; return;
            case '/': tokPost = TT_DIV;   lexPost = "/"; return;
            case '^': tokPost = TT_POW;   lexPost = "^"; return;
            case '(': tokPost = TT_LP;    lexPost = "("; return;
            case ')': tokPost = TT_RP;    lexPost = ")"; return;
            default:
                throw new IllegalArgumentException("Símbolo no reconocido: '" + c + "'");
        }
    }

    private void esperarPost(int t){
        if(tokPost != t){
            throw new IllegalArgumentException("Se esperaba otro token y se encontró '" + lexPost + "'");
        }
        siguientePost();
    }

    private String exprPost(){
        String izq = termPost();
        while(tokPost == TT_MAS || tokPost == TT_MENOS){
            int op = tokPost;
            siguientePost();
            String der = termPost();
            izq = izq + " " + der + " " + (op == TT_MAS ? "+" : "-");
        }
        return izq;
    }

    private String termPost(){
        String izq = powerPost();
        while(tokPost == TT_MUL || tokPost == TT_DIV){
            int op = tokPost;
            siguientePost();
            String der = powerPost();
            izq = izq + " " + der + " " + (op == TT_MUL ? "*" : "/");
        }
        return izq;
    }

    private String powerPost(){
        String base = unaryPost();
        if(tokPost == TT_POW){
            siguientePost();
            String exp = powerPost();
            base = base + " " + exp + " ^";
        }
        return base;
    }

    private String unaryPost(){
        if(tokPost == TT_MAS){
            siguientePost();
            return unaryPost();
        }
        if(tokPost == TT_MENOS){
            siguientePost();
            String val = unaryPost();
            return val + " u-";
        }
        return primaryPost();
    }

    private String primaryPost(){
        if(tokPost == TT_NUM){
            String v = lexPost;
            siguientePost();
            return v;
        }

        if(tokPost == TT_IDENT){
            String nombre = lexPost;
            siguientePost();
            if(tokPost == TT_LP){
                siguientePost();
                String arg = exprPost();
                esperarPost(TT_RP);
                return arg + " " + nombre;
            }
            return nombre;
        }

        if(tokPost == TT_LP){
            siguientePost();
            String v = exprPost();
            esperarPost(TT_RP);
            return v;
        }

        throw new IllegalArgumentException("Token inesperado: " + lexPost);
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

    //F
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
