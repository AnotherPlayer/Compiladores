# ¿Qué necesitamos?

Vamos a requerir:

La clase AFN.

Elementos:

- Estados $\to$ Conjunto de estados.
- Alfabeto $\to$ Conjunto de elementos.
- Estado Inicial $\to$ Estado.
- Estados Aceptación $\to$ Conjunto de estados.

## Clase estado

Esta clase contiene:

- Identificador $\to$ Valor entero > 0
- Estado Aceptación $\to$ Booleano
- Token $\to$ Valor entero > 0
- Transiciones $\to$ Conjunto de transiciones

## Clase transición

- Símbolo $\to$ Carácter
- Estado destino $\to$ Estado

# AFN

- Crear AFN básico $\to$ AFN(char c);
- Crear AFN básico $\to$ AFN(char c1, char c2);

- Unión $\to$ UnionAFN(AFN F2);
- Concatenar $\to$ JoinAFN(AFN F2);
- Cerradura positiva $\to$ CerraduraPosAFN();
- Cerradura Kleein $\to$ CerraduraKleeinAFN();
- Opcional $\to$ OpcionalAFN();

#Clase AFN
Class AFN{

	Conjunto<Estado> Estados;
	Estado EdoInicial;
	Conjunto<char> alfabeto;
	Conjunto<Estados> EdosAcept;
	
	AFN(){
	
		Estados = new Conjunto<Estados>();
		Estado.clear();
		EdoInicial = null;
		Alfabeto = new Conjunto<char>();
		Alfabeto.clear();
		EdosAcept = new Conjunto<Estado>();
		EdosAcept.clear();
	
	}

	//AFN básico
	AFN doBasic( char c ){
	
		Estado e1, e2;
		e1 = new Estado();
		e2 = new Estado();
	
		this.Estados.add(e1);
		this.Estados.add(e2);
		this.EdoInicial = e1;
	
		e1.Transiciones.Add( new Transicion( c,e2 ) );
		e2.EdoAcept = true;
		this.alfabeto.add(c);
		this.EdoAcept.add(c2);
	
		return this;
	
	}
	
	//AFN básico
	AFN dobasic( char c1, char c2 ){
	
		Estado e1, e2;
		e1 = new Estado();
		e2 = new Estado();
	
		this.Estados.add(e1);
		this.Estados.add(e2);
		this.EdoInicial = e1;
		
		e1.Transiciones.add( new Transicion( c1,c2,e2 ) );
		e2.EdoAcept = true;
		this.EdoAcept.add(e2);
		
		for( int i=c1 ; i<=c2 ; i++ )
			this.alfabeto.add(i);
		
		return this;
	
	}
	
	//Union entre "this" y F2
	AFN_union( AFN F2 ){
	
		Estado e1,e2;
		e1 = new Estado();
		e2 = new Estado();
		
		e1.Transicion.add( new Transicion( t.epsilon,this.EdoInicial ) );
		e2.Transicion.add( new Transicion( t.epsilon,F2.EdoInicial ) );
	
		foreach( Estado e in this.EdoAcept ){
			e.Transiciones.add( newTransicion( t.epsilon,e2 ) );
			e.EdoAcept = false;
		}
		
		e2.EdoAcept = true;
		
		this.EdoInicial = e1;
		this.Estados.union(F2,Estados);
		this.Estados.add(e1);
		this.Estados.add(e2);
		this.EdosAcept.clear();
		this.EdosAcept.add();
		this.alfabeto.union(F2,alfabeto);
		
		return this;
		
	}
	
	//Concatenación entre "this" y F2
	AFN_join( AFN F2 ){
	
		foreach( Estado e in this.EdosAcept )
			foreach( Transicion t in F2.EdoInicial.Transiciones )
				e.EdoAcept = false;
			
		this.EdosAcept.clear();
		this.EdosAcept.union( F2.EdosAcept );
		this.alfabeto.union( F2.alfabeto );
		
		F2.Estados.quitar( F2.alfabetos );
			this.Estados.union( F2. Estados );
		
		return this;
	
	}
	
	//Cerradura positiva de "this"
	AFN_cerrPos(){
	
		Estado e1,e2;
		e1 = new Estado();
		e2 = new Estado();
		
		foreach( Estado e in this.EdosAcept ){
			e.Transiciones.add( new Transicion( t.epsilon,this.EdoInicial ) );
			e.Transiciones.add( new Transicion( t.epsilon,e2 ) );
			e.EdoAcept = false;
		}
	
		e1.Transiciones.add( new Transicion( t.epsilon,this.EdoInicial ) );
		
		this.EdoInicial = e1;
		this.EdoAcept.clear();
		this.EdosAcept.Add(e2);
		this.Estados.add(e1);
		this.Estados.add(e2);
		
		return this;
		
	}
	
	//Cerradura de Klein de "this"
	AFN_cerrKlein(){
	
		Estado e1,e2;
		e1 = new Estado();
		e2 = new Estado();
		
		foreach( Estado e in this.EdosAcept ){
			e.Transiciones.add( new Transicion( t.epsilon,this.EdoInicial ) );
			e.Transiciones.add( new Transicion( t.epsilon,e2 ) );
			e.EdoAcept = false;
		}
	
		e1.Transiciones.add( new Transicion( t.epsilon,this.EdoInicial ) );
		
		this.EdoInicial = e1;
		this.EdoAcept.clear();
		this.EdosAcept.add(e2);
		this.Estados.add(e1);
		this.Estados.add(e2);
		
		e1.Transiciones.add( new Transiciones( t.epsilon,e2 ) );
		
		return this;
		
	}
	
	//Operacion opcional de "this"
	AFN_opcional(){
	
		Estado e1,e2;
		e1 = new Estado();
		e2 = new Estado();
		
		foreach( Estado e in this.EdosAcept ){
			e.Transiciones.add( new Transicion( t.epsilon,e2 ) );
			e.EdoAcept = false;
		}
	
		e1.Transiciones.add( new Transicion( t.epsilon,this.EdoInicial ) );
		
		this.EdoInicial = e1;
		this.EdoAcept.clear();
		this.EdosAcept.add(e2);
		this.Estados.add(e1);
		this.Estados.add(e2);
		
		e1.Transiciones.add( new Transiciones( t.epsilon,e2 ) );
		
		return this;
		
	}

}

Cerradura épsilon

Conjunto<Estado> CerraduraEpsilon(Estado e){

	Conjunto<Estado> C = new Conjunto<Estado>();
	Stack<Estado> P = new Stack<Estado>();
	Estado e2;
	
	C.clear(); //C --> Conteo de épsilon
	P.clear(); //p --> Pila
	P.push(e);
	
	while( !P.empty() ){
	
		e2 = P.pop();
		if( !C.contiene(e2) ){
			C.add(e2);

			foreach( Transicion t in e2.Transiciones ){
				if( t.Simbolo == SimbEspecial.EPSILON)
					P.push(t.EdoDestino);
			}
		
		}
			
	}//Cierre while
	
	return C;

}//Cierre método

Conjunto<Estado> CerraduraEpsilon(Conjunto<Estado> C){

	Conjunto<Estado> R = new Conjunto<Estado>();
	
	R.clear();
	
	foreach(Estado e in C)
		R.union( CerraduraEpsilon(e) );
		
	return R;

}//Cierre método


## Mover (*Dentro de clase AFN*)

Conjunto<Estados> Mover(Estado e, char c){

		Conjunto<Estado> R = new Conjunto<Estado>();
		
		R.clear();
		
		foreach( Transicion t in e.Transiciones )
			if( t.Simbolo == c )
				R.add(t.Simbolo);
				
		return R;

}//Cierre método

//Podemos volver a llamar al método o hacer un doble foreach

Conjunto<Estados> Mover(Estado e, char c){

		Conjunto<Estado> R = new Conjunto<Estado>();
		
		R.clear();
		
		foreach( Estado e in E )
			foreach( Transicion t in e.Transiciones )
				if( t.Simbolo == c )
							R.add(t.Simbolo);
		
		return R;
		
}//Cierre método


## Ir a (*Dentro de la clase AFN*)

Conjunto<Estado> IrA(Estado e, char c){

	return CerraduraEpsilon( Mover( e,c ) );

}

Conjunto<Estado> IrA(Conjunto<Estado> E, char c){

	return CerraduraEpsilon( Mover( E,c ) );

}

# Clases

## Estado

class estado{

	int IdEdo;
	bool EdoAcept;
	int token;

	Conjunto<Transicion> Transiciones;
	static int NumEstados = 0;	
	
	Estado(){

		IdEdo = NumEstados++;
		Edoacept = false;
		Token = -1
		Transiciones = new Conjunto<Transicion>();
		Transiciones.clear();
		
	}
		
	Conjunto<Estado> Mover (Conjunto<Estado> E, char c){

		conjunto<Estado> R= new Conjunto<estado>();
		R.clear();
		foreach (Estado e in E)
			foreach (Transicion t in e.Transiciones)
				if(t.Simbolo==c)
					R.Add(t.EdoDestino);
		return R;
	}
	
	Conjunto<Estado> IrA( Estado e, char c ){

			return CerraduraEpsilon(Mover(e,c))
			
	}
	
	Conjunto<Estado> IrA( Conjunto<Estado>E,char c ){

		return CerraduraEpsilon (Mover(E,C));
		
	} 
		
}

## Transición

*Se debe de hacer el cambio correspondiente para que t.Simbolo funciona con la modificación del siguiente código.*

Class Transicion{

	char Simbolo_inferior;
	char Simbolo2_superior;
	Estado EdoDestino;
	
	Transicion(){
	
		EdoDestino = null;
		
	}
	
	Transicion(char c, Estado e){
	
	Simbolo_inferior = Simbolo_superior = c;
	EdoDestino e;
	
	}
	
	Transicion(char c.inf, char c.sup, Estado e){
	
		 Simbolo_inferior=c.inf;
		 Simbolo_superior=c.sup;
		 EdoDestino e;
		 
	}
		
}

### Tiene transición (*Dentro de la clase Estado*)
Conjunto<Estado> TieneTransicion( char c ){

	Conjunto<Estado> R = new Conjunto<Estado>;
		R.clear();
		
		foreach( Transicion t in this.Transiciones )
			if( t.SimboloInf <= c && c <= t.SímboloSup )
				R.add( t.EdoDestino );
				
		return R;

}

*<aside>
📝 NOTA

Agregar un elemento a la clase AFN para identificar cada AFN creado.

Se requiere un lugar para guardar los AFN’s static Conjunto<AFN> Automatas;

</aside>

# int TransAFD[] = new int[257];AFD métodos (## Elementos Sj)

Class ElemSj{

	int Id;
	Conjunto<Estado> S;
	
	ElemSj(){	
		Id = -1;
		S = new Conjunto<Estado>();
		S.clear();
	}
	.
	.
	.

}

Estado del AFD

Class EdoAFD{

	int[257] TransAFD;
	int ID;            //--> En caso de usar conjuntos
	
	EdoAFD(){
		TransAFD = new int[257];
		Id = -1;
		for( int i=0 ; i<=256 ; i++ )
			TransAFD[i] = -1;
	}

	EdoAFD( int idEdo ){
	
	.
	.
	.
	
	}

.
.
.

}

Clase AFD

Class AFD{

	EdoAFD[] EdosAFD;
	Conjunto<char> alfabeto;
	int NumEdos;
	
	AFD(){
		NumEdos = 0;
		alfabeto = new Conjunto<char>();
	}
	
	AFD( int n ){
		EdosAFD = new EdoAFD[n];
		NumEdos = n;
		alfabeto = new Conjunto<char>();
	}

	AFD( int n,Conjunto<char>alf ){
		EdosAFD = new EdoAFD[n];
		NumEdos = n;
		alfabeto = new Conjunto<char>();
		alfabeto.clear();
		alfabeto.union(alf);
	}
	
	bool SaveAFD( string nameFile ){
		//Vas Memo, pon el código
		.
		.
		.
	}

	bool LoadAFD( string nameFile ){
		//Vas Memo, te toca el código
		.
		.
		.
	}
	
}

# Método AFN a AFD (*Método dentro del AFD*)

AFD AFNtoAFD( AFN a ){
	
	AFD afd_conv= new AFD();
	Conjunto<ElemSj> C = new Conjunto<ElemSj>();
	Queue<ElemSj> Q = new Queue<ElemSj>();
	int NumElemSj = 0;
	ElemSj SjAux = new ElemSj();
	ElemSj SjAct = new ElemSj();
	
	C.clear();
	Q.clear();
	
	SjAux.S = CerraduraEpsilon( this.EdoInicial );
	SjAux.Id = NumElemSj++;  //S_0
	
	C.add(SjAux);
	Q.add(SjAux);

	while( !Q.empty() ){
	
			SjAct = Q.DeQueue();
			foreach( char c in this.alfabeto ){
				SjAux.S = IrA( SjAct.S,c );
				if( Search( C,SjAux ) == -1 ){
					SjAux.Id = NumElemSj++;
					
					//Transicion SjAct
					
					Q.add(SjAux);
					C.add(SjAux);
				}
				//Poner la transición
				
			}//Fin "foreach"
			
	}//Fin "while"
	
	return afd_conv;
	
}//Fin conversion


Caracteres especiales
static public class SimbolosEspeciales{

	public static char EPSILON = (char)5;
	public static char FIN = (char)0:
	public static char ERROR = 20000;
	public static char OMITIR = 20001;//Lo revisa pero no notifica su revisión (Ignora)
	

}

Guardar archivo

public void SaveAFD( string NombArchivo ){

	using( StreamWriter writer ){
	
		//
		writer.WriteLine( this.NumEstado.ToString() );
	
	}
	
	
	

}
